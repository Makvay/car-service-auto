package ru.car.api.master.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ru.car.dto.master.MasterDto;
import ru.car.entity.master.MasterEntity;
import ru.car.dto.master.CreateMasterRequest;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MasterMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    MasterEntity toEntity(CreateMasterRequest request);

    MasterDto toDto(MasterEntity entity);

    List<MasterDto> toDtoList(List<MasterEntity> entities);
}
