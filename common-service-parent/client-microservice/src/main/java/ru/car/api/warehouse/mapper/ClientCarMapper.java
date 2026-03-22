package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import ru.car.dto.client.ClientCarDto;
import ru.car.entity.client.ClientCarEntity;

@Mapper(componentModel = "spring")
public interface ClientCarMapper {

    ClientCarDto toDto(ClientCarEntity entity);

}
