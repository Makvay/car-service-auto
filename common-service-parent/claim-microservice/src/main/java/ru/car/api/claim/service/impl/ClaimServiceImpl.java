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
import ru.car.api.claim.feign.WarehouseFeignClient;
import ru.car.api.claim.mapper.ClaimMapper;
import ru.car.api.claim.repository.ClaimPartRepository;
import ru.car.api.claim.repository.ClaimRepository;
import ru.car.api.claim.service.ClaimService;
import ru.car.api.claim.spec.ClaimSpecification;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.ClaimPriority;
import ru.car.dto.claim.ClaimSearchRequest;
import ru.car.dto.claim.ClaimStatusUpdateRequest;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.dto.claim.PartRequest;
import ru.car.dto.claim.WorkItemCreateRequest;
import ru.car.dto.client.ClientDto;
import ru.car.dto.master.MasterDto;
import ru.car.dto.notification.EmailRequestDto;
import ru.car.dto.warehouse.DeductRequest;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.claim.ClaimPartEntity;
import ru.car.entity.claim.ClaimStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimPartRepository claimPartRepository;
    private final ClaimMapper claimMapper;
    private final ClientFeignClient clientFeignClient;
    private final MasterFeignClient masterFeignClient;
    private final NotificationFeignClient notificationFeignClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WarehouseFeignClient warehouseFeignClient;
    @Value("${notifications.kafka.enabled:false}")
    private boolean kafkaNotificationsEnabled;

    @Transactional
    @Override
    public ClaimDto createClaim(CreateClaimRequest request) {
        log.info("Создание заявки для клиента: {}, автомобиль: {}", request.getClientId(), request.getVehicleId());

        ClaimEntity claim = claimMapper.toEntity(request);
        claim.setStatus(ClaimStatus.CREATED);
        claim.setClaimNumber("TMP-" + UUID.randomUUID());

        ClaimEntity savedClaim = claimRepository.save(claim);
        savedClaim.setClaimNumber("CL-" + savedClaim.getId());
        savedClaim = claimRepository.save(savedClaim);

        ClientContact clientContact = resolveClientContact(savedClaim.getClientId(), request.getClientEmail(), request.getClientName());

        log.info("Заявка создана: ID={}, номер={}", savedClaim.getId(), savedClaim.getClaimNumber());

        sendClaimCreatedEmail(savedClaim, clientContact);
        publishClaimCreatedEvent(savedClaim, clientContact);

        return toDtoWithNumber(savedClaim);
    }

    @Override
    public ClaimDto getClaim(Long id) {
        log.info("РџРѕР»СѓС‡РµРЅРёРµ Р·Р°СЏРІРєРё: {}", id);

        ClaimEntity claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Р—Р°СЏРІРєР° РЅРµ РЅР°Р№РґРµРЅР°: " + id));

        return toDtoWithNumber(claim);
    }

    @Override
    public Page<ClaimDto> getClientClaims(Long clientId, Pageable pageable) {
        log.info("РџРѕР»СѓС‡РµРЅРёРµ Р·Р°СЏРІРѕРє РєР»РёРµРЅС‚Р°: {}", clientId);

        return claimRepository.findByClientId(clientId, pageable).map(this::toDtoWithNumber);
    }

    @Override
    public Page<ClaimDto> getAllClaims(String status, Long masterId, Boolean isPaid, Pageable pageable) {
        log.info("РџРѕР»СѓС‡РµРЅРёРµ РІСЃРµС… Р·Р°СЏРІРѕРє: status={}, masterId={}, isPaid={}, page={}",
                status, masterId, isPaid, pageable.getPageNumber());

        Page<ClaimEntity> claims;
        if (status != null) {
            claims = claimRepository.findByStatus(status, pageable);
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
        log.info("Обновление статуса заявки {} на {}", claimId, request.getStatus());

        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Р—Р°СЏРІРєР° РЅРµ РЅР°Р№РґРµРЅР°: " + claimId));

        ClaimStatus newStatus = ClaimStatus.valueOf(request.getStatus());
        claim.setStatus(newStatus);
        claim.setUpdatedAt(LocalDateTime.now());

        if (newStatus == ClaimStatus.COMPLETED) {
            deductPartsFromWarehouse(claimId);
        }

        ClaimEntity updatedClaim = claimRepository.save(claim);
        ClientContact clientContact = resolveClientContact(updatedClaim.getClientId(), null, null);
        String masterName = resolveMasterName(updatedClaim.getMasterId());

        sendClaimStatusEmail(updatedClaim, clientContact, newStatus.name(), masterName);
        publishClaimStatusChangedEvent(updatedClaim, clientContact, newStatus.name());

        return toDtoWithNumber(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto assignMaster(Long claimId, Long masterId) {
        log.info("РќР°Р·РЅР°С‡РµРЅРёРµ РјР°СЃС‚РµСЂР° {} РЅР° Р·Р°СЏРІРєСѓ {}", masterId, claimId);

        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Р—Р°СЏРІРєР° РЅРµ РЅР°Р№РґРµРЅР°: " + claimId));

        claim.setMasterId(masterId);
        claim.setUpdatedAt(LocalDateTime.now());
        ClaimEntity updatedClaim = claimRepository.save(claim);
        publishClaimAssignedToMasterEvent(updatedClaim);
        return toDtoWithNumber(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto addWorkItem(Long claimId, WorkItemCreateRequest request) {
        log.info("Р”РѕР±Р°РІР»РµРЅРёРµ СЂР°Р±РѕС‚С‹ РІ Р·Р°СЏРІРєСѓ {}", claimId);
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public ClaimDto addPart(Long claimId, PartRequest request) {
        log.info("Р”РѕР±Р°РІР»РµРЅРёРµ Р·Р°РїС‡Р°СЃС‚Рё РІ Р·Р°СЏРІРєСѓ {}", claimId);
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public void deleteClaim(Long id) {
        log.info("РЈРґР°Р»РµРЅРёРµ Р·Р°СЏРІРєРё: {}", id);

        if (!claimRepository.existsById(id)) {
            throw new RuntimeException("Р—Р°СЏРІРєР° РЅРµ РЅР°Р№РґРµРЅР°: " + id);
        }

        claimRepository.deleteById(id);
        log.info("Р—Р°СЏРІРєР° {} СѓРґР°Р»РµРЅР°", id);
    }

    @Override
    @Transactional
    public ClaimDto deletePart(Long claimId, Long partId) {
        log.info("РЈРґР°Р»РµРЅРёРµ Р·Р°РїС‡Р°СЃС‚Рё {} РёР· Р·Р°СЏРІРєРё {}", partId, claimId);

        claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Р—Р°СЏРІРєР° РЅРµ РЅР°Р№РґРµРЅР°: " + claimId));

        List<ClaimPartEntity> parts = claimPartRepository.findByClaimId(claimId);
        ClaimPartEntity partToDelete = parts.stream()
                .filter(part -> part.getId().equals(partId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Р—Р°РїС‡Р°СЃС‚СЊ РЅРµ РЅР°Р№РґРµРЅР°: " + partId));

        claimPartRepository.delete(partToDelete);
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public ClaimDto deleteWorkItem(Long claimId, Long workItemId) {
        log.info("РЈРґР°Р»РµРЅРёРµ СЂР°Р±РѕС‚С‹ {} РёР· Р·Р°СЏРІРєРё {} (TODO)", workItemId, claimId);

        claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Р—Р°СЏРІРєР° РЅРµ РЅР°Р№РґРµРЅР°: " + claimId));

        return getClaim(claimId);
    }

    @Override
    public Page<ClaimDto> searchClaims(ClaimSearchRequest searchRequest, Pageable pageable) {
        log.info("РџРѕРёСЃРє Р·Р°СЏРІРѕРє: {}, page={}", searchRequest, pageable.getPageNumber());

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

    private void deductPartsFromWarehouse(Long claimId) {
        try {
            log.info("РЎРїРёСЃР°РЅРёРµ Р·Р°РїС‡Р°СЃС‚РµР№ СЃРѕ СЃРєР»Р°РґР° РґР»СЏ Р·Р°СЏРІРєРё {}", claimId);

            List<ClaimPartEntity> usedParts = claimPartRepository.findByClaimId(claimId);
            if (usedParts.isEmpty()) {
                log.info("РќРµС‚ Р·Р°РїС‡Р°СЃС‚РµР№ РґР»СЏ СЃРїРёСЃР°РЅРёСЏ РІ Р·Р°СЏРІРєРµ {}", claimId);
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
            log.info("Р—Р°РїС‡Р°СЃС‚Рё СѓСЃРїРµС€РЅРѕ СЃРїРёСЃР°РЅС‹ РґР»СЏ Р·Р°СЏРІРєРё {}", claimId);
        } catch (Exception e) {
            log.error("РћС€РёР±РєР° РїСЂРё СЃРїРёСЃР°РЅРёРё Р·Р°РїС‡Р°СЃС‚РµР№ РґР»СЏ Р·Р°СЏРІРєРё {}: {}", claimId, e.getMessage());
        }
    }

    private ClientContact resolveClientContact(Long clientId, String fallbackEmail, String fallbackName) {
        try {
            ClientDto client = clientFeignClient.getClientById(clientId);
            return new ClientContact(
                    firstNonBlank(client.getEmail(), fallbackEmail),
                    buildClientName(client.getFirstName(), client.getLastName(), fallbackName)
            );
        } catch (Exception e) {
            log.warn("Не удалось получить профиль клиента {}: {}", clientId, e.getMessage());
            return new ClientContact(fallbackEmail, firstNonBlank(fallbackName, "Клиент"));
        }
    }

        private void sendClaimCreatedEmail(ClaimEntity claim, ClientContact clientContact) {
        String subject = "Заявка #" + claim.getClaimNumber() + " принята";
        String body = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Ваша заявка #%s принята сервисом.\n\n" +
                        "Описание проблемы: %s\n" +
                        "Приоритет: %s\n\n" +
                        "Мы уведомим вас о каждом следующем этапе: когда мастер приступит к работе и когда заявка будет завершена.",
                clientContact.name(),
                claim.getClaimNumber(),
                claim.getProblemDescription(),
                claim.getPriority() != null ? claim.getPriority().name() : "NORMAL"
        );
        sendNotificationEmail(clientContact.email(), subject, body);
    }
    private void sendClaimStatusEmail(ClaimEntity claim, ClientContact clientContact, String status, String masterName) {
        String statusText = switch (status) {
            case "CREATED" -> "Принята";
            case "IN_PROGRESS" -> "В работе";
            case "WAITING_PARTS" -> "Ожидает запчасти";
            case "COMPLETED" -> "Завершена";
            case "CANCELLED" -> "Отменена";
            default -> status;
        };

        String subject = "Заявка #" + claim.getClaimNumber() + ": " + statusText;
        String body = switch (status) {
            case "CREATED" -> String.format(
                    "Здравствуйте, %s!\n\n" +
                            "Ваша заявка #%s принята.\n\n" +
                            "Описание проблемы: %s",
                    clientContact.name(),
                    claim.getClaimNumber(),
                    claim.getProblemDescription()
            );
            case "IN_PROGRESS" -> String.format(
                    "Здравствуйте, %s!\n\n" +
                            "По заявке #%s мастер %s приступил к работе.\n\n" +
                            "Описание проблемы: %s",
                    clientContact.name(),
                    claim.getClaimNumber(),
                    masterName != null && !masterName.isBlank() ? masterName : "назначенный",
                    claim.getProblemDescription()
            );
            case "COMPLETED" -> String.format(
                    "Здравствуйте, %s!\n\n" +
                            "Работы по заявке #%s завершены. Автомобиль готов к выдаче.\n\n" +
                            "Описание проблемы: %s",
                    clientContact.name(),
                    claim.getClaimNumber(),
                    claim.getProblemDescription()
            );
            default -> String.format(
                    "Здравствуйте, %s!\n\n" +
                            "Статус вашей заявки #%s изменен на: %s.\n\n" +
                            "Описание проблемы: %s",
                    clientContact.name(),
                    claim.getClaimNumber(),
                    statusText,
                    claim.getProblemDescription()
            );
        };

        sendNotificationEmail(clientContact.email(), subject, body);
    }
        private void sendNotificationEmail(String email, String subject, String body) {
        if (email == null || email.isBlank()) {
            log.warn("Email клиента пустой, письмо не отправлено");
            return;
        }

        try {
            EmailRequestDto request = new EmailRequestDto();
            request.setTo(email);
            request.setSubject(subject);
            request.setBody(body);
            notificationFeignClient.sendEmail(request);
            log.info("Email-запрос отправлен в notification-service для {}", email);
        } catch (Exception e) {
            log.error("Не удалось отправить email через notification-service: {}", e.getMessage());
        }
    }
    private void publishClaimCreatedEvent(ClaimEntity claim, ClientContact clientContact) {
        if (!kafkaNotificationsEnabled) {
            return;
        }
        try {
            ClaimEvent event = new ClaimEvent();
            event.setClaimId(claim.getId());
            event.setClaimNumber(claim.getClaimNumber());
            event.setClientEmail(clientContact.email());
            event.setClientName(clientContact.name());
            event.setDescription(claim.getProblemDescription());
            event.setStatus(claim.getStatus().name());
            kafkaTemplate.send("claim.created", event);
            log.info("РЎРѕР±С‹С‚РёРµ claim.created РѕС‚РїСЂР°РІР»РµРЅРѕ РІ Kafka");
        } catch (Exception e) {
            log.error("РћС€РёР±РєР° РѕС‚РїСЂР°РІРєРё СЃРѕР±С‹С‚РёСЏ РІ Kafka: {}", e.getMessage());
        }
    }

    private void publishClaimStatusChangedEvent(ClaimEntity claim, ClientContact clientContact, String status) {
        if (!kafkaNotificationsEnabled) {
            return;
        }
        try {
            ClaimEvent event = new ClaimEvent();
            event.setClaimId(claim.getId());
            event.setClaimNumber(claim.getClaimNumber());
            event.setClientEmail(clientContact.email());
            event.setClientName(clientContact.name());
            event.setDescription(claim.getProblemDescription());
            event.setStatus(status);
            kafkaTemplate.send("claim.status.changed", event);
            log.info("РЎРѕР±С‹С‚РёРµ claim.status.changed РѕС‚РїСЂР°РІР»РµРЅРѕ РІ Kafka");
        } catch (Exception e) {
            log.error("РћС€РёР±РєР° РѕС‚РїСЂР°РІРєРё СЃРѕР±С‹С‚РёСЏ РІ Kafka: {}", e.getMessage());
        }
    }

    private void publishClaimAssignedToMasterEvent(ClaimEntity claim) {
        if (!kafkaNotificationsEnabled) {
            return;
        }
        try {
            ClaimEvent event = new ClaimEvent();
            event.setClaimId(claim.getId());
            event.setClaimNumber(claim.getClaimNumber());
            event.setStatus("ASSIGNED_TO_MASTER");
            event.setDescription(claim.getProblemDescription());
            kafkaTemplate.send("claim.assigned.to.master", event);
            log.info("Событие claim.assigned.to.master отправлено в Kafka");
        } catch (Exception e) {
            log.error("Ошибка отправки события claim.assigned.to.master в Kafka: {}", e.getMessage());
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

        if (dto.getClientFirstName() == null || dto.getClientLastName() == null || dto.getClientPhone() == null) {
            try {
                if (claim.getClientId() != null) {
                    ClientDto client = clientFeignClient.getClientById(claim.getClientId());
                    if (client != null) {
                        dto.setClientFirstName(client.getFirstName());
                        dto.setClientLastName(client.getLastName());
                        dto.setClientPhone(client.getPhone());
                    }
                }
            } catch (Exception e) {
                log.debug("Не удалось получить данные клиента {} для заявки {}: {}",
                        claim.getClientId(), claim.getId(), e.getMessage());
            }
        }

        if (dto.getMasterFirstName() == null || dto.getMasterLastName() == null) {
            try {
                if (claim.getMasterId() != null) {
                    MasterDto master = masterFeignClient.getMasterById(claim.getMasterId());
                    if (master != null) {
                        dto.setMasterFirstName(master.getFirstName());
                        dto.setMasterLastName(master.getLastName());
                    }
                }
            } catch (Exception e) {
                log.debug("Не удалось получить данные мастера {} для заявки {}: {}",
                        claim.getMasterId(), claim.getId(), e.getMessage());
            }
        }

        return dto;
    }

    private String resolveMasterName(Long masterId) {
        if (masterId == null) {
            return null;
        }
        try {
            MasterDto master = masterFeignClient.getMasterById(masterId);
            return master == null ? null : buildClientName(master.getFirstName(), master.getLastName(), null);
        } catch (Exception e) {
            log.warn("Не удалось получить профиль мастера {}: {}", masterId, e.getMessage());
            return null;
        }
    }

    private String buildClientName(String firstName, String lastName, String fallbackName) {
        String fullName = ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
        return firstNonBlank(fullName, fallbackName, "Клиент");
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

