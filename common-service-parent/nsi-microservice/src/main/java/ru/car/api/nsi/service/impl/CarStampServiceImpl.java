package ru.car.api.nsi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.nsi.mapper.CarStampMapper;
import ru.car.api.nsi.repository.CarStampRepository;
import ru.car.api.nsi.service.CarStampService;
import ru.car.dto.nsi.CarStampDto;
import ru.car.entity.nsi.CarStampEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarStampServiceImpl implements CarStampService {

    private final CarStampRepository carStampRepository;
    private final CarStampMapper carStampMapper;

    @Override
    public CarStampDto getById(Long id) {
        CarStampEntity entity = carStampRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car stamp not found with id: " + id));
        return carStampMapper.toDto(entity);
    }

    @Override
    public CarStampDto getByCode(String code) {
        CarStampEntity entity = carStampRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Car stamp not found with code: " + code));
        return carStampMapper.toDto(entity);
    }

    @Override
    public List<CarStampDto> getAll() {
        return carStampRepository.findAll().stream()
                .map(carStampMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarStampDto> getAllActive() {
        return carStampRepository.findByIsActiveTrue().stream()
                .map(carStampMapper::toDto)
                .collect(Collectors.toList());
    }
}