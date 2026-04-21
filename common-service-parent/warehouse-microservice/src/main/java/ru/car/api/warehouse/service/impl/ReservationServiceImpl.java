package ru.car.api.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.warehouse.mapper.ReservationMapper;
import ru.car.api.warehouse.repository.InventoryRepository;
import ru.car.api.warehouse.repository.PartRepository;
import ru.car.api.warehouse.repository.ReservationRepository;
import ru.car.api.warehouse.service.ReservationService;
import ru.car.dto.warehouse.ReservationDto;
import ru.car.entity.warehouse.InventoryEntity;
import ru.car.entity.warehouse.PartEntity;
import ru.car.entity.warehouse.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final InventoryRepository inventoryRepository;
    private final PartRepository partRepository;

    @Override
    @Transactional
    public ReservationDto createReservation(ReservationDto dto) {
        if (dto.getPartId() == null || dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new RuntimeException("partId and positive quantity are required");
        }
        if (dto.getClaimId() == null) {
            throw new RuntimeException("claimId is required");
        }

        PartEntity part = partRepository.findById(dto.getPartId())
                .orElseThrow(() -> new RuntimeException("Part not found: " + dto.getPartId()));

        List<InventoryEntity> inventories = inventoryRepository.findByPartIdOrderByIdAsc(dto.getPartId());
        if (inventories.isEmpty()) {
            throw new RuntimeException("Part not found in inventory: " + dto.getPartId());
        }

        int available = inventories.stream()
                .mapToInt(i -> Math.max(0, safe(i.getQuantity()) - safe(i.getReservedQuantity())))
                .sum();
        if (available < dto.getQuantity()) {
            throw new RuntimeException("Insufficient stock for reservation: required="
                    + dto.getQuantity() + ", available=" + available);
        }

        int toReserve = dto.getQuantity();
        for (InventoryEntity inventory : inventories) {
            if (toReserve == 0) {
                break;
            }
            int quantity = safe(inventory.getQuantity());
            int reserved = safe(inventory.getReservedQuantity());
            int free = Math.max(0, quantity - reserved);
            if (free == 0) {
                continue;
            }
            int reserve = Math.min(free, toReserve);
            inventory.setReservedQuantity(reserved + reserve);
            inventoryRepository.save(inventory);
            toReserve -= reserve;
        }

        if (toReserve != 0) {
            throw new RuntimeException("Reservation invariant failed for part " + dto.getPartId());
        }

        ReservationEntity entity = new ReservationEntity();
        entity.setReservationNumber(dto.getReservationNumber() == null || dto.getReservationNumber().isBlank()
                ? "RSV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                : dto.getReservationNumber());
        entity.setClaimId(dto.getClaimId());
        entity.setPart(part);
        entity.setQuantity(dto.getQuantity());
        entity.setStatus(dto.getStatus() == null || dto.getStatus().isBlank() ? "RESERVED" : dto.getStatus());
        entity.setReservedBy(dto.getReservedBy());
        entity.setReservedAt(dto.getReservedAt() == null ? LocalDateTime.now() : dto.getReservedAt());
        entity.setNotes(dto.getNotes());

        return reservationMapper.toDto(reservationRepository.save(entity));
    }

    @Override
    public List<ReservationDto> getByClaimId(Long claimId) {
        List<ReservationEntity> reservations = reservationRepository.findByClaimId(claimId);
        return reservationMapper.toDtoList(reservations);
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }
}
