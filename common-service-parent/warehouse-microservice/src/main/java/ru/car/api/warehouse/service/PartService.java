package ru.car.api.warehouse.service;

import org.springframework.stereotype.Service;
import ru.car.dto.warehouse.CreatePartRequest;
import ru.car.dto.warehouse.PartDto;

import java.util.List;


public interface PartService {
    PartDto create(CreatePartRequest request);
    PartDto getById(Long id);
    List<PartDto> getAll();
    PartDto update(Long id, PartDto dto);
    void delete(Long id);
}
