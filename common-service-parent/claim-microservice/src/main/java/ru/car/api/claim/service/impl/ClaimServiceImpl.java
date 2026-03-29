package ru.car.api.claim.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.claim.mapper.ClaimMapper;
import ru.car.api.claim.repository.ClaimRepository;
import ru.car.api.claim.service.ClaimService;
import ru.car.api.claim.spec.ClaimSpecification;
import ru.car.dto.claim.*;
import ru.car.entity.claim.ClaimEntity;
import ru.car.dto.claim.ClaimSearchRequest;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;

    @Transactional
    @Override
    public ClaimDto createClaim(CreateClaimRequest request) {
        log.info("Создание заявки для клиента: {}, автомобиль: {}",
                request.getClientId(), request.getVehicleId());

        ClaimEntity claim = claimMapper.toEntity(request);
        ClaimEntity savedClaim = claimRepository.save(claim);

        log.info("Заявка создана: ID={}, номер={}",
                savedClaim.getId(), savedClaim.getClaimNumber());

        return claimMapper.toDto(savedClaim);
    }

    @Override
    public ClaimDto getClaim(Long id) {
        log.info("Получение заявки: {}", id);

        ClaimEntity claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена: " + id));

        return claimMapper.toDto(claim);
    }

    @Override
    public List<ClaimDto> getClientClaims(Long clientId) {
        log.info("Получение заявок клиента: {}", clientId);

        List<ClaimEntity> claims = claimRepository.findByClientId(clientId);

        return claims.stream()
                .map(claimMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimDto> getAllClaims(String status, Long masterId, Boolean isPaid) {
        log.info("Получение всех заявок: status={}, masterId={}, isPaid={}",
                status, masterId, isPaid);

        List<ClaimEntity> claims;

        if (status != null) {
            claims = claimRepository.findByStatus(status);
        } else if (masterId != null) {
            claims = claimRepository.findByMasterId(masterId);
        } else if (isPaid != null) {
            claims = claimRepository.findByIsPaid(isPaid);
        } else {
            claims = claimRepository.findAll();
        }

        return claims.stream()
                .map(claimMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClaimDto updateClaimStatus(Long claimId, ClaimStatusUpdateRequest request) {
        log.info("Обновление статуса заявки {} на {}", claimId, request.getStatus());

        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена: " + claimId));

        claim.setStatus(
                ru.car.entity.claim.ClaimStatus.valueOf(request.getStatus())
        );

        claim.setUpdatedAt(LocalDateTime.now());

        ClaimEntity updatedClaim = claimRepository.save(claim);

        return claimMapper.toDto(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto assignMaster(Long claimId, Long masterId) {
        log.info("Назначение мастера {} на заявку {}", masterId, claimId);

        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена: " + claimId));
        
        claim.setMasterId(masterId);
        claim.setUpdatedAt(LocalDateTime.now());

        ClaimEntity updatedClaim = claimRepository.save(claim);

        return claimMapper.toDto(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto addWorkItem(Long claimId, WorkItemCreateRequest request) {
        log.info("Добавление работы в заявку {}", claimId);
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public ClaimDto addPart(Long claimId, PartRequest request) {
        log.info("Добавление запчасти в заявку {}", claimId);
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public void deleteClaim(Long id) {
        log.info("Удаление заявки: {}", id);

        if (!claimRepository.existsById(id)) {
            throw new RuntimeException("Заявка не найдена: " + id);
        }

        claimRepository.deleteById(id);
        log.info("Заявка {} удалена", id);
    }

    @Override
    public List<ClaimDto> searchClaims(ClaimSearchRequest searchRequest) {
        log.info("Поиск заявок: {}", searchRequest);

        // Преобразуем DTO enum в Entity enum
        List<ru.car.entity.claim.ClaimStatus> entityStatuses = null;
        if (searchRequest.getStatuses() != null) {
            entityStatuses = searchRequest.getStatuses().stream()
                    .map(status -> ru.car.entity.claim.ClaimStatus.valueOf(status.name()))
                    .collect(Collectors.toList());
        }

        ru.car.entity.claim.ClaimPriority entityPriority = null;
        if (searchRequest.getPriority() != null) {
            entityPriority = ru.car.entity.claim.ClaimPriority.valueOf(
                    searchRequest.getPriority().name());
        }

        Specification<ClaimEntity> spec = ClaimSpecification.withFilters(
                searchRequest.getClientId(),
                searchRequest.getVehicleId(),
                searchRequest.getMasterId(),
                entityStatuses,
                entityPriority
        );

        List<ClaimEntity> claims = claimRepository.findAll(spec);
        return claims.stream()
                .map(claimMapper::toDto)
                .collect(Collectors.toList());
    }

}
















