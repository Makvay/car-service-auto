package ru.car.api.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.car.dto.notification.NotificationDto;
import ru.car.entity.notification.NotificationEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    NotificationDto toDto(NotificationEntity entity);
    List<NotificationDto> toDtoList(List<NotificationEntity> entities);
}