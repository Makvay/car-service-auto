package ru.car.api.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.notification.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
