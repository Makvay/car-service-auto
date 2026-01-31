package ru.car.dto.warehouse;

import lombok.Getter;

@Getter
public enum SupplyStatus {
    PENDING("Ожидает"),
    ORDERED("Заказано"),
    IN_TRANSIT("В пути"),
    PARTIALLY_RECEIVED("Частично получено"),
    RECEIVED("Получено"),
    CANCELLED("Отменено"),
    RETURNED("Возвращено");

    private final String description;

    SupplyStatus(String description) {
        this.description = description;
    }
}