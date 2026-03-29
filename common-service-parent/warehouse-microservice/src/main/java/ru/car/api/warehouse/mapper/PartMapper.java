package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.car.dto.warehouse.PartDto;
import ru.car.entity.warehouse.PartEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartMapper {
    PartDto toDto(PartEntity entity);
    PartEntity toEntity(PartDto dto);
}
