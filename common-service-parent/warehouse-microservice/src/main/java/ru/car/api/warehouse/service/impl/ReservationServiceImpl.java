package ru.car.api.warehouse.service.impl;

import ru.car.api.warehouse.service.ReservationService;
import ru.car.dto.warehouse.ReservationDto;

import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    @Override
    public ReservationDto createReservation(ReservationDto dto) {
        return null;
    }

    @Override
    public List<ReservationDto> getByClaimId(Long ClaimId) {
        return List.of();
    }
}
