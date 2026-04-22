package ru.car.api.claim.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.claim.feign.ClientFeignClient;
import ru.car.api.claim.feign.MasterFeignClient;
import ru.car.api.claim.feign.NotificationFeignClient;
import ru.car.api.claim.feign.NsiFeignClient;
import ru.car.api.claim.feign.WarehouseFeignClient;
import ru.car.api.claim.mapper.ClaimMapper;
import ru.car.api.claim.repository.ClaimPartRepository;
import ru.car.api.claim.repository.ClaimRepository;
import ru.car.api.claim.repository.ClaimStatusHistoryRepository;
import ru.car.api.claim.repository.ClaimWorkItemRepository;
import ru.car.api.claim.service.ClaimService;
import ru.car.api.claim.spec.ClaimSpecification;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.ClaimPriority;
import ru.car.dto.claim.ClaimSearchRequest;
import ru.car.dto.claim.ClaimStatusHistoryDto;
import ru.car.dto.claim.ClaimStatusUpdateRequest;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.dto.claim.PartRequest;
import ru.car.dto.claim.WorkItemCreateRequest;
import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.ClientDto;
import ru.car.dto.master.MasterDto;
import ru.car.dto.notification.EmailRequestDto;
import ru.car.dto.nsi.ServiceDto;
import ru.car.dto.warehouse.DeductRequest;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.claim.ClaimPartEntity;
import ru.car.entity.claim.ClaimStatus;
import ru.car.entity.claim.ClaimStatusHistoryEntity;
import ru.car.entity.claim.ClaimWorkItemEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private static final Map<ClaimStatus, Set<ClaimStatus>> ALLOWED_TRANSITIONS = Map.ofEntries(
            Map.entry(ClaimStatus.CREATED, Set.of(ClaimStatus.WAITING_DIAGNOSIS, ClaimStatus.WAITING_APPROVAL, ClaimStatus.WAITING_PARTS, ClaimStatus.IN_PROGRESS, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.WAITING_DIAGNOSIS, Set.of(ClaimStatus.DIAGNOSIS_IN_PROGRESS, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.DIAGNOSIS_IN_PROGRESS, Set.of(ClaimStatus.WAITING_APPROVAL, ClaimStatus.WAITING_PARTS, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.WAITING_APPROVAL, Set.of(ClaimStatus.APPROVED, ClaimStatus.REJECTED, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.APPROVED, Set.of(ClaimStatus.WAITING_PARTS, ClaimStatus.IN_PROGRESS, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.REJECTED, Set.of(ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.WAITING_PARTS, Set.of(ClaimStatus.IN_PROGRESS, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.IN_PROGRESS, Set.of(ClaimStatus.WAITING_PAYMENT, ClaimStatus.COMPLETED, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.WAITING_PAYMENT, Set.of(ClaimStatus.PAID, ClaimStatus.CANCELLED)),
            Map.entry(ClaimStatus.PAID, Set.of(ClaimStatus.COMPLETED)),
            Map.entry(ClaimStatus.COMPLETED, Set.of()),
            Map.entry(ClaimStatus.CANCELLED, Set.of())
    );

    private final ClaimRepository claimRepository;
    private final ClaimPartRepository claimPartRepository;
    private final ClaimWorkItemRepository claimWorkItemRepository;
    private final ClaimStatusHistoryRepository claimStatusHistoryRepository;
    private final ClaimMapper claimMapper;
    private final ClientFeignClient clientFeignClient;
    private final MasterFeignClient masterFeignClient;
    private final NsiFeignClient nsiFeignClient;
    private final NotificationFeignClient notificationFeignClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WarehouseFeignClient warehouseFeignClient;

    @Value("${notifications.kafka.enabled:false}")
    private boolean kafkaNotificationsEnabled;

    @Transactional
    @Override
    public ClaimDto createClaim(CreateClaimRequest request) {
        validateClientAndVehicle(request.getClientId(), request.getVehicleId());
        validateServiceExists(request.getServiceId());

        ClaimEntity claim = claimMapper.toEntity(request);
        claim.setStatus(ClaimStatus.CREATED);
        claim.setClaimNumber("TMP-" + UUID.randomUUID());
        claim.setCreatedAt(LocalDateTime.now());
        claim.setUpdatedAt(LocalDateTime.now());

        ClaimEntity savedClaim = claimRepository.save(claim);
        savedClaim.setClaimNumber("CL-" + savedClaim.getId());
        savedClaim = claimRepository.save(savedClaim);

        writeStatusHistory(savedClaim, null, ClaimStatus.CREATED, "Claim created");

        ClientContact clientContact = resolveClientContact(savedClaim.getClientId(), request.getClientEmail(), request.getClientName());
        notifyClaimCreated(savedClaim, clientContact);

        return toDtoWithNumber(savedClaim);
    }

    @Override
    public ClaimDto getClaim(Long id) {
        ClaimEntity claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + id));
        return toDtoWithNumber(claim);
    }

    @Override
    public Page<ClaimDto> getClientClaims(Long clientId, Pageable pageable) {
        return claimRepository.findByClientId(clientId, pageable).map(this::toDtoWithNumber);
    }

    @Override
    public Page<ClaimDto> getAllClaims(String status, Long masterId, Boolean isPaid, Pageable pageable) {
        Page<ClaimEntity> claims;
        if (status != null) {
            claims = claimRepository.findByStatus(parseFilterStatus(status), pageable);
        } else if (masterId != null) {
            claims = claimRepository.findByMasterId(masterId, pageable);
        } else if (isPaid != null) {
            claims = claimRepository.findByIsPaid(isPaid, pageable);
        } else {
            claims = claimRepository.findAll(pageable);
        }
        return claims.map(this::toDtoWithNumber);
    }

    @Override
    @Transactional
    public ClaimDto updateClaimStatus(Long claimId, ClaimStatusUpdateRequest request) {
        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        ClaimStatus oldStatus = claim.getStatus();
        ClaimStatus newStatus = ClaimStatus.valueOf(request.getStatus().name());
        if (newStatus == null) {
            throw new RuntimeException("Status is required");
        }
        validateTransition(oldStatus, newStatus);

        if (oldStatus == newStatus) {
            return toDtoWithNumber(claim);
        }

        if (newStatus == ClaimStatus.COMPLETED) {
            deductPartsFromWarehouse(claimId);
        }

        claim.setStatus(newStatus);
        claim.setUpdatedAt(LocalDateTime.now());
        if (newStatus == ClaimStatus.IN_PROGRESS && claim.getStartDate() == null) {
            claim.setStartDate(LocalDateTime.now());
        }
        if (newStatus == ClaimStatus.COMPLETED && claim.getCompletionDate() == null) {
            claim.setCompletionDate(LocalDateTime.now());
        }

        ClaimEntity updatedClaim = claimRepository.save(claim);
        writeStatusHistory(updatedClaim, oldStatus, newStatus, request.getComment());

        ClientContact clientContact = resolveClientContact(updatedClaim.getClientId(), null, null);
        String masterName = resolveMasterName(updatedClaim.getMasterId());

        notifyClaimStatusChanged(updatedClaim, clientContact, newStatus, masterName);

        return toDtoWithNumber(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto assignMaster(Long claimId, Long masterId) {
        validateMasterExists(masterId);

        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        claim.setMasterId(masterId);
        claim.setUpdatedAt(LocalDateTime.now());
        ClaimEntity updatedClaim = claimRepository.save(claim);

        if (kafkaNotificationsEnabled) {
            ClaimEvent event = new ClaimEvent();
            event.setClaimId(updatedClaim.getId());
            event.setClaimNumber(updatedClaim.getClaimNumber());
            event.setStatus("ASSIGNED_TO_MASTER");
            event.setDescription(updatedClaim.getProblemDescription());
            kafkaTemplate.send("claim.assigned.to.master", event);
        }

        return toDtoWithNumber(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto addWorkItem(Long claimId, WorkItemCreateRequest request) {
        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));
        validateServiceExists(request.getServiceId());

        ClaimWorkItemEntity entity = new ClaimWorkItemEntity();
        entity.setClaim(claim);
        entity.setServiceId(request.getServiceId());
        entity.setDescription(request.getComment());
        entity.setEstimatedHours(request.getEstimatedHours());
        entity.setStatus("PLANNED");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        claimWorkItemRepository.save(entity);

        return getClaim(claimId);
    }

    @Override
    @Transactional
    public ClaimDto addPart(Long claimId, PartRequest request) {
        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        ClaimPartEntity entity = new ClaimPartEntity();
        entity.setClaim(claim);
        entity.setPartId(request.getPartId());
        entity.setQuantity(request.getQuantity());
        entity.setNotes(request.getComment());
        entity.setStatus("ADDED");
        entity.setCreatedAt(LocalDateTime.now());
        claimPartRepository.save(entity);

        return getClaim(claimId);
    }

    @Override
    @Transactional
    public void deleteClaim(Long id) {
        ClaimEntity claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + id));
        claimRepository.delete(claim);
    }

    @Override
    @Transactional
    public ClaimDto deletePart(Long claimId, Long claimPartId) {
        claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        ClaimPartEntity part = claimPartRepository.findById(claimPartId)
                .orElseThrow(() -> new RuntimeException("Claim part not found: " + claimPartId));

        if (!part.getClaim().getId().equals(claimId)) {
            throw new RuntimeException("Claim part " + claimPartId + " does not belong to claim " + claimId);
        }

        claimPartRepository.delete(part);
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public ClaimDto deleteWorkItem(Long claimId, Long workItemId) {
        claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        ClaimWorkItemEntity workItem = claimWorkItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("Work item not found: " + workItemId));
        if (!workItem.getClaim().getId().equals(claimId)) {
            throw new RuntimeException("Work item " + workItemId + " does not belong to claim " + claimId);
        }

        claimWorkItemRepository.delete(workItem);
        return getClaim(claimId);
    }

    @Override
    public List<ClaimStatusHistoryDto> getStatusHistory(Long claimId) {
        claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        return claimStatusHistoryRepository.findByClaimIdOrderByCreatedAtDesc(claimId).stream()
                .map(history -> {
                    ClaimStatusHistoryDto dto = new ClaimStatusHistoryDto();
                    dto.setId(history.getId());
                    dto.setClaimId(history.getClaim().getId());
                    dto.setOldStatus(history.getOldStatus());
                    dto.setNewStatus(history.getNewStatus());
                    dto.setChangedBy(history.getChangedBy());
                    dto.setNotes(history.getNotes());
                    dto.setCreatedAt(history.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClaimDto> searchClaims(ClaimSearchRequest searchRequest, Pageable pageable) {
        List<ClaimStatus> entityStatuses = null;
        if (searchRequest.getStatuses() != null) {
            entityStatuses = searchRequest.getStatuses().stream()
                    .map(status -> ClaimStatus.valueOf(status.name()))
                    .collect(Collectors.toList());
        }

        ru.car.entity.claim.ClaimPriority entityPriority = null;
        if (searchRequest.getPriority() != null) {
            entityPriority = ru.car.entity.claim.ClaimPriority.valueOf(searchRequest.getPriority().name());
        }

        Specification<ClaimEntity> specification = ClaimSpecification.withFilters(
                searchRequest.getClientId(),
                searchRequest.getVehicleId(),
                searchRequest.getMasterId(),
                entityStatuses,
                entityPriority
        );
        return claimRepository.findAll(specification, pageable).map(this::toDtoWithNumber);
    }

    private void validateClientAndVehicle(Long clientId, Long vehicleId) {
        ClientDto client = clientFeignClient.getClientById(clientId);
        if (client == null || client.getId() == null) {
            throw new RuntimeException("Client not found: " + clientId);
        }

        List<ClientCarDto> vehicles = clientFeignClient.getClientVehicles(clientId);
        boolean vehicleExists = vehicles != null && vehicles.stream().anyMatch(v -> vehicleId.equals(v.getId()));
        if (!vehicleExists) {
            throw new RuntimeException("Vehicle " + vehicleId + " not found for client " + clientId);
        }
    }

    private void validateMasterExists(Long masterId) {
        MasterDto master = masterFeignClient.getMasterById(masterId);
        if (master == null || master.getId() == null) {
            throw new RuntimeException("Master not found: " + masterId);
        }
    }

    private void validateServiceExists(Long serviceId) {
        List<ServiceDto> services = nsiFeignClient.getServices();
        boolean exists = services != null && services.stream().anyMatch(service -> serviceId.equals(service.getId()));
        if (!exists) {
            throw new RuntimeException("Service not found in NSI: " + serviceId);
        }
    }

    private void deductPartsFromWarehouse(Long claimId) {
        List<ClaimPartEntity> usedParts = claimPartRepository.findByClaimId(claimId);
        if (usedParts.isEmpty()) {
            return;
        }

        DeductRequest deductRequest = new DeductRequest();
        List<DeductRequest.DeductItem> items = usedParts.stream()
                .map(part -> {
                    DeductRequest.DeductItem item = new DeductRequest.DeductItem();
                    item.setPartId(part.getPartId());
                    item.setQuantity(part.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());
        deductRequest.setItems(items);

        warehouseFeignClient.deductInventory(deductRequest);
    }

    private void writeStatusHistory(ClaimEntity claim, ClaimStatus oldStatus, ClaimStatus newStatus, String notes) {
        ClaimStatusHistoryEntity history = ClaimStatusHistoryEntity.builder()
                .claim(claim)
                .oldStatus(oldStatus == null ? null : oldStatus.name())
                .newStatus(newStatus.name())
                .notes(notes)
                .createdAt(LocalDateTime.now())
                .build();
        claimStatusHistoryRepository.save(history);
    }

    private ClientContact resolveClientContact(Long clientId, String fallbackEmail, String fallbackName) {
        try {
            ClientDto client = clientFeignClient.getClientById(clientId);
            return new ClientContact(
                    firstNonBlank(client.getEmail(), fallbackEmail),
                    buildName(client.getFirstName(), client.getLastName(), fallbackName)
            );
        } catch (Exception e) {
            return new ClientContact(fallbackEmail, firstNonBlank(fallbackName, "Client"));
        }
    }

    private String resolveMasterName(Long masterId) {
        if (masterId == null) {
            return null;
        }
        try {
            MasterDto master = masterFeignClient.getMasterById(masterId);
            return master == null ? null : buildName(master.getFirstName(), master.getLastName(), null);
        } catch (Exception e) {
            return null;
        }
    }

    private void notifyClaimCreated(ClaimEntity claim, ClientContact clientContact) {
        if (kafkaNotificationsEnabled) {
            publishEvent("claim.created", claim, clientContact, claim.getStatus().name());
            return;
        }

        sendNotificationEmail(
                clientContact.email(),
                "Заявка #" + claim.getClaimNumber() + " принята",
                "Здравствуйте, " + clientContact.name() + "!\n\nВаша заявка #" + claim.getClaimNumber() + " принята сервисом."
        );
    }

    private void notifyClaimStatusChanged(ClaimEntity claim, ClientContact clientContact, ClaimStatus status, String masterName) {
        if (kafkaNotificationsEnabled) {
            publishEvent("claim.status.changed", claim, clientContact, status.name());
            return;
        }

        String statusText = switch (status) {
            case CREATED -> "Принята";
            case IN_PROGRESS -> "В работе";
            case WAITING_PARTS -> "Ожидает запчасти";
            case COMPLETED -> "Завершена";
            case CANCELLED -> "Отменена";
            default -> status.name();
        };
        String body = "Здравствуйте, " + clientContact.name() + "!\n\n"
                + "Статус вашей заявки #" + claim.getClaimNumber() + ": " + statusText + ".";
        if (status == ClaimStatus.IN_PROGRESS && masterName != null) {
            body += "\nМастер: " + masterName + ".";
        }
        sendNotificationEmail(clientContact.email(), "Заявка #" + claim.getClaimNumber() + ": " + statusText, body);
    }

    private void sendNotificationEmail(String email, String subject, String body) {
        if (email == null || email.isBlank()) {
            return;
        }
        EmailRequestDto request = new EmailRequestDto();
        request.setTo(email);
        request.setSubject(subject);
        request.setBody(body);
        notificationFeignClient.sendEmail(request);
    }

    private void publishEvent(String topic, ClaimEntity claim, ClientContact clientContact, String status) {
        ClaimEvent event = new ClaimEvent();
        event.setClaimId(claim.getId());
        event.setClaimNumber(claim.getClaimNumber());
        event.setClientEmail(clientContact.email());
        event.setClientName(clientContact.name());
        event.setStatus(status);
        event.setDescription(claim.getProblemDescription());
        kafkaTemplate.send(topic, event);
    }

    private void validateTransition(ClaimStatus oldStatus, ClaimStatus newStatus) {
        Set<ClaimStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(oldStatus, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new RuntimeException("Invalid status transition: " + oldStatus + " -> " + newStatus);
        }
    }

    private ClaimStatus parseFilterStatus(String status) {
        try {
            return ClaimStatus.valueOf(status);
        } catch (Exception e) {
            throw new RuntimeException("Unsupported claim status filter: " + status);
        }
    }

    private ClaimDto toDtoWithNumber(ClaimEntity claim) {
        ClaimDto dto = claimMapper.toDto(claim);
        if (dto.getClaimNumber() == null) {
            dto.setClaimNumber("CL-" + dto.getId());
        }
        return enrichClaimDto(dto, claim);
    }

    private ClaimDto enrichClaimDto(ClaimDto dto, ClaimEntity claim) {
        if (dto == null || claim == null) {
            return dto;
        }

        try {
            if (claim.getClientId() != null && (dto.getClientFirstName() == null || dto.getClientLastName() == null)) {
                ClientDto client = clientFeignClient.getClientById(claim.getClientId());
                if (client != null) {
                    dto.setClientFirstName(client.getFirstName());
                    dto.setClientLastName(client.getLastName());
                    dto.setClientPhone(client.getPhone());
                }
            }
        } catch (Exception ignored) {
        }

        try {
            if (claim.getMasterId() != null && (dto.getMasterFirstName() == null || dto.getMasterLastName() == null)) {
                MasterDto master = masterFeignClient.getMasterById(claim.getMasterId());
                if (master != null) {
                    dto.setMasterFirstName(master.getFirstName());
                    dto.setMasterLastName(master.getLastName());
                }
            }
        } catch (Exception ignored) {
        }

        return dto;
    }

    private String buildName(String firstName, String lastName, String fallback) {
        String fullName = ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
        return firstNonBlank(fullName, fallback, "Client");
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private record ClientContact(String email, String name) {
    }
}
