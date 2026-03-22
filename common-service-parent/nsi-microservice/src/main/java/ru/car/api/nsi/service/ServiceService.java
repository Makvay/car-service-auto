package ru.car.api.nsi.service;

import ru.car.dto.nsi.CreateServiceRequest;
import ru.car.dto.nsi.ServiceDto;
import ru.car.dto.nsi.UpdateServiceRequest;

import java.util.List;

public interface ServiceService {
    ServiceDto getById(Long id);
    List<ServiceDto> getByCategoryId(Long categoryId);
    List<ServiceDto> getAll();
    List<ServiceDto> getAllActive();
}
