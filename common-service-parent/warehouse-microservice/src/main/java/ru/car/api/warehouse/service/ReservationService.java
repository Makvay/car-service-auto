package ru.car.api.warehouse.service;

import org.springframework.stereotype.Service;
import ru.car.dto.warehouse.ReservationDto;

import java.util.List;


public interface ReservationService {

    ReservationDto createReservation(ReservationDto dto);
    List<ReservationDto> getByClaimId(Long ClaimId);


}
