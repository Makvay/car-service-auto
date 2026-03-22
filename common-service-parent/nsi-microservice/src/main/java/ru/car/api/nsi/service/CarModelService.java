package ru.car.api.nsi.service;

import ru.car.dto.nsi.CarModelDto;
import ru.car.dto.nsi.CreateCarModelRequest;
import ru.car.dto.nsi.UpdateCarModelRequest;

import java.util.List;

public interface CarModelService {
    CarModelDto getById(Long id);

    List<CarModelDto> getByStampId(Long stampId);

    List<CarModelDto> getAll();

    List<CarModelDto> getAllActive();




}
