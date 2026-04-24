package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.car.dto.warehouse.ReservationDto;
import ru.car.entity.warehouse.ReservationEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    @Mapping(target = "partId", source = "part.id")
    ReservationDto toDto(ReservationEntity entity);

    @Mapping(target = "part", ignore = true)
    ReservationEntity toEntity(ReservationDto dto);

    List<ReservationDto> toDtoList(List<ReservationEntity> entities);

}
