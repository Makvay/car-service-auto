package ru.car.api.nsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.car.dto.nsi.CarStampDto;
import ru.car.entity.nsi.CarStampEntity;

@Mapper(componentModel = "spring")
public interface CarStampMapper {

    CarStampDto toDto(CarStampEntity entity);
}