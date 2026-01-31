package ru.car.dto.client;

import lombok.Getter;

@Getter
public enum ClientStatus {
    ACTIVE("Активен"),
    INACTIVE("Неактивен"),
    BLOCKED("Заблокирован"),
    VIP("VIP клиент");

    private final String description;

    ClientStatus(String description) {
        this.description = description;
    }
}