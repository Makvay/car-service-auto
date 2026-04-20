package ru.car.api.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.notification.mapper.NotificationMapper;
import ru.car.api.notification.repository.NotificationRepository;
import ru.car.api.notification.service.NotificationService;
import ru.car.dto.notification.NotificationDto;
import ru.car.dto.notification.NotificationStatus;
import ru.car.entity.notification.NotificationEntity;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public void markAsRead(Long id) {
        NotificationEntity entity = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));

        entity.setStatus(NotificationStatus.READ);
        if (entity.getDeliveryConfirmedAt() == null) {
            entity.setDeliveryConfirmedAt(LocalDateTime.now());
        }
        notificationRepository.save(entity);
    }

}
