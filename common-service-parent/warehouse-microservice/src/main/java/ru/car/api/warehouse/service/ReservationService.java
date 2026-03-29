package ru.car.api.warehouse.service;

import org.springframework.stereotype.Service;
import ru.car.dto.warehouse.InventoryDto;
import ru.car.dto.warehouse.PartDto;
import ru.car.dto.warehouse.ReservationDto;
import ru.car.entity.warehouse.InventoryEntity;
import ru.car.entity.warehouse.PartEntity;

import java.util.List;


public interface ReservationService {


        ReservationDto createReservation(ReservationDto dto);
        List<ReservationDto> getByClaimId(Long claimId);




}
