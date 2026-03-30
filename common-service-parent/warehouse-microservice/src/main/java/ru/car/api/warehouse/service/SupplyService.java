package ru.car.api.warehouse.service;

import org.springframework.stereotype.Service;
import ru.car.dto.warehouse.SupplyDto;

import java.util.List;


public interface SupplyService {
    SupplyDto updateStatus(Long id, String status);
    SupplyDto getByStatus(Long id, String status);

    SupplyDto getById(Long id);
    List<SupplyDto> getAll();

}
