package ru.car.api.warehouse.mapper;

import org.mapstruct.Mapper;
import ru.car.dto.client.ClientDto;
import ru.car.dto.client.CreateClientRequest;
import ru.car.entity.client.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    // to dto
    ClientDto toDto(ClientEntity client);

    // to entity
    ClientEntity toEntity(CreateClientRequest request);
}
