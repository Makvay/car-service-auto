package ru.car.api.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.notification.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByRecipientId(Long id);
    List<NotificationEntity> findByStatus(String status);
}
