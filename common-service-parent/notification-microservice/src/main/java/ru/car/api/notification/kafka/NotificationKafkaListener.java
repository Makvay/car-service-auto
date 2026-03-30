package ru.car.api.notification.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {

    @KafkaListener(topics = "client.registered", groupId = "notification-group")
    public void onClientRegistered(String message) {
        log.info(">>> SMS: Добро пожаловать! Client: {}", message);
    }

    @KafkaListener(topics = "claim.created", groupId = "notification-group")
    public void onClaimCreated(String message) {
        log.info(">>> Email: Ваша заявка создана! Claim: {}", message);
    }

    @KafkaListener(topics = "claim.status.changed", groupId = "notification-group")
    public void onClaimStatusChanged(String message) {
        log.info(">>> SMS: Статус заявки изменен! Claim: {}", message);
    }

    @KafkaListener(topics = "work.completed", groupId = "notification-group")
    public void onWorkCompleted(String message) {
        log.info(">>> SMS: Ваш автомобиль готов! Work: {}", message);
    }
}




