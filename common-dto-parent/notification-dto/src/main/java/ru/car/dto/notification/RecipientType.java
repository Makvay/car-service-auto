package ru.car.dto.notification;

import lombok.Getter;

@Getter
public enum RecipientType {
    CLIENT("Клиент"),
    MASTER("Мастер"),
    MANAGER("Менеджер"),
    ADMIN("Администратор"),
    SYSTEM("Система");

    private final String description;

    RecipientType(String description) {
        this.description = description;
    }
}