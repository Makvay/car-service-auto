package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.car.dto.warehouse.PartDto;
import ru.car.dto.warehouse.ReservationDto;
import ru.car.entity.warehouse.PartEntity;
import ru.car.entity.warehouse.ReservationEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    ReservationDto toDto(ReservationEntity entity);
    ReservationEntity toEntity(ReservationDto dto);
    List<ReservationDto> toDtoList(List<ReservationEntity> entities);

}
