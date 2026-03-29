package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.car.dto.warehouse.CreatePartRequest;
import ru.car.dto.warehouse.PartDto;
import ru.car.entity.warehouse.PartEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartMapper {
    PartDto toDto(PartEntity entity);
    PartEntity toEntity(PartDto dto);
    PartEntity toEntity(CreatePartRequest request);
    List<PartDto> toDtoList(List<PartEntity> entities);
}
