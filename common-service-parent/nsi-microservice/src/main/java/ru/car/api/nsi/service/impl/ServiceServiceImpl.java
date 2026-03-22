package ru.car.api.nsi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.nsi.mapper.ServiceMapper;
import ru.car.api.nsi.repository.ServiceRepository;
import ru.car.api.nsi.service.ServiceService;
import ru.car.dto.nsi.ServiceDto;
import ru.car.entity.nsi.ServiceEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public ServiceDto getById(Long id) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return serviceMapper.toDto(entity);
    }

    @Override
    public List<ServiceDto> getByCategoryId(Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId).stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getAll() {
        return serviceRepository.findAll().stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getAllActive() {
        return serviceRepository.findByIsActiveTrue().stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }
}