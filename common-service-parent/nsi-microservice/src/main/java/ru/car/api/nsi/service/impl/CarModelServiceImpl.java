package ru.car.api.nsi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.nsi.mapper.CarModelMapper;
import ru.car.api.nsi.repository.CarModelRepository;
import ru.car.api.nsi.repository.CarStampRepository;
import ru.car.api.nsi.service.CarModelService;
import ru.car.dto.nsi.CarModelDto;
import ru.car.dto.nsi.CreateCarModelRequest;
import ru.car.dto.nsi.UpdateCarModelRequest;
import ru.car.entity.nsi.CarModelEntity;
import ru.car.entity.nsi.CarStampEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;
    private final CarStampRepository carStampRepository;
    private final CarModelMapper carModelMapper;

    @Override
    public CarModelDto getById(Long id) {
        CarModelEntity entity = carModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car model not found with id: " + id));
        return carModelMapper.toDto(entity);
    }

    @Override
    public List<CarModelDto> getByStampId(Long stampId) {
        return carModelRepository.findByStampId(stampId).stream()
                .map(carModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarModelDto> getAll() {
        return carModelRepository.findAll().stream()
                .map(carModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarModelDto> getAllActive() {
        return carModelRepository.findByIsActiveTrue().stream()
                .map(carModelMapper::toDto)
                .collect(Collectors.toList());
    }

}