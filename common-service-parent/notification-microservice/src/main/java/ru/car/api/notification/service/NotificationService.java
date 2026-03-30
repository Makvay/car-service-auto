package ru.car.api.notification.service;

import ru.car.dto.notification.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getAll();
    List<NotificationDto> getByClientId(Long clientId);
}
