package ru.car.api.claim.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import ru.car.api.claim.mapper.ClaimMapper;
import ru.car.api.claim.repository.ClaimRepository;
import ru.car.api.claim.service.ClaimService;
import ru.car.dto.claim.*;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.client.ClientCarEntity;
import ru.car.entity.client.ClientEntity;
import ru.car.entity.master.MasterEntity;

import java.time.LocalDateTime;
import java.util.List;

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

        // 1. Создаем заглушки
        ClientEntity client = new ClientEntity();
        client.setId(request.getClientId());
        client.setFirstName("Клиент " + request.getClientId());

        ClientCarEntity vehicle = new ClientCarEntity();
        vehicle.setId(request.getVehicleId());
        vehicle.setLicensePlate("A" + request.getVehicleId() + "BC");

        // 2. Используем MapStruct для создания Entity
        ClaimEntity claim = claimMapper.toEntity(request, client, vehicle);

        // 3. Сохраняем
        ClaimEntity savedClaim = claimRepository.save(claim);

        log.info("Заявка создана: ID={}", savedClaim.getId());

        // 4. Возвращаем DTO
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
                .toList();
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
                .toList();
    }

    @Override
    @Transactional
    public ClaimDto updateClaimStatus(Long claimId, ClaimStatusUpdateRequest request) {
        log.info("Обновление статуса заявки {} на {}", claimId, request.getStatus());

        ClaimEntity claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена: " + claimId));

        claim.setStatus(ClaimStatus.valueOf(request.getStatus().toUpperCase()));
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

        MasterEntity master = new MasterEntity();
        master.setId(masterId);
        master.setFirstName("Мастер " + masterId);

        claim.setMaster(master);
        claim.setUpdatedAt(LocalDateTime.now());

        ClaimEntity updatedClaim = claimRepository.save(claim);

        return claimMapper.toDto(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDto addWorkItem(Long claimId, WorkItemCreateRequest request) {
        log.info("Добавление работы в заявку {}", claimId);

        // Пока просто возвращаем заявку
        return getClaim(claimId);
    }

    @Override
    @Transactional
    public ClaimDto addPart(Long claimId, PartRequest request) {
        log.info("Добавление запчасти в заявку {}", claimId);

        // Пока просто возвращаем заявку
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
}