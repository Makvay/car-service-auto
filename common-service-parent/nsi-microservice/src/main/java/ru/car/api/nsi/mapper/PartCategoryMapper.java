package ru.car.api.nsi.mapper;

import org.mapstruct.Mapper;
import ru.car.dto.nsi.PartCategoryDto;
import ru.car.entity.nsi.PartCategoryEntity;

@Mapper(componentModel = "spring")
public interface PartCategoryMapper {

    PartCategoryDto toDto(PartCategoryEntity entity);
}