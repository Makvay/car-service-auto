package ru.car.dto.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push-уведомление"),
    TELEGRAM("Telegram"),
    WHATSAPP("WhatsApp");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
