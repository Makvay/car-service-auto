package ru.car.dto.notification;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    PENDING("Ожидает отправки"),
    SENT("Отправлено"),
    DELIVERED("Доставлено"),
    READ("Прочитано"),
    FAILED("Ошибка отправки"),
    RETRY("Повторная отправка");

    private final String description;

    NotificationStatus(String description) {
        this.description = description;
    }
}