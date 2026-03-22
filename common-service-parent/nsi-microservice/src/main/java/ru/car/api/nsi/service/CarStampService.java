package ru.car.api.nsi.service;

import ru.car.dto.nsi.CarStampDto;
import ru.car.dto.nsi.CreateCarStampRequest;
import ru.car.dto.nsi.UpdateCarStampRequest;

import java.util.List;

public interface CarStampService {

    CarStampDto getById(Long id);
    CarStampDto getByCode(String code);
    List<CarStampDto> getAll();
    List<CarStampDto> getAllActive();
}