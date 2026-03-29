package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.car.dto.warehouse.ReservationDto;
import ru.car.dto.warehouse.SupplyDto;
import ru.car.entity.warehouse.SupplyEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SupplyMapper {
    SupplyDto toDto(SupplyEntity entity);
    SupplyEntity toEntity(SupplyDto dto);
}
