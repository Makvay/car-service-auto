package ru.car.api.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.notification.mapper.NotificationMapper;
import ru.car.api.notification.repository.NotificationRepository;
import ru.car.api.notification.service.NotificationService;
import ru.car.dto.notification.NotificationDto;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDto> getAll() {
        return notificationMapper.toDtoList(notificationRepository.findAll());
    }

    @Override
    public List<NotificationDto> getByClientId(Long clientId) {
        return notificationMapper.toDtoList(notificationRepository.findByRecipientId(clientId));
    }

}
