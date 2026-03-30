package ru.car.api.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.warehouse.mapper.InventoryMapper;
import ru.car.api.warehouse.mapper.ReservationMapper;
import ru.car.api.warehouse.repository.InventoryRepository;
import ru.car.api.warehouse.repository.ReservationRepository;
import ru.car.api.warehouse.service.ReservationService;
import ru.car.dto.warehouse.ReservationDto;
import ru.car.entity.warehouse.ReservationEntity;
    // Reservation = связь между "запчасть" + "количество" + "заявка на ремонт"
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationDto createReservation(ReservationDto dto) {
        log.info("Бронь: {}", dto);
        ReservationEntity entity = reservationMapper.toEntity(dto);
        ReservationEntity saved = reservationRepository.save(entity);
        return reservationMapper.toDto(saved);
    }

    @Override
    public List<ReservationDto> getByClaimId(Long ClaimId) {
        List<ReservationEntity> reservations = reservationRepository.findByClaimId(ClaimId);
        return reservationMapper.toDtoList(reservations);
    }
}
