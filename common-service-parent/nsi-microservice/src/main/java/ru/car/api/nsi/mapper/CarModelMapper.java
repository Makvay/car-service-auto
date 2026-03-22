package ru.car.api.nsi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.car.dto.nsi.CarModelDto;
import ru.car.entity.nsi.CarModelEntity;

@Mapper(componentModel = "spring")
public interface CarModelMapper {

    @Mapping(source = "stamp.id", target = "stampId")
    @Mapping(source = "stamp.name", target = "stampName")
    CarModelDto toDto(CarModelEntity entity);
}