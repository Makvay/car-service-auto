package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.car.dto.warehouse.InventoryDto;
import ru.car.entity.warehouse.InventoryEntity;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryMapper {
    InventoryDto toDto(InventoryEntity entity);
    InventoryEntity toEntity(InventoryDto dto);
    List<InventoryDto> toDtoList(List<InventoryEntity> entities);
}
