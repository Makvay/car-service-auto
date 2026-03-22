package ru.car.api.nsi.mapper;

import org.mapstruct.Mapper;
import ru.car.dto.nsi.ServiceDto;
import ru.car.entity.nsi.ServiceEntity;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDto toDto(ServiceEntity entity);
}