package ru.car.dto.warehouse;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    RESERVED("Зарезервировано"),
    USED("Использовано"),
    CANCELLED("Отменено"),
    EXPIRED("Просрочено");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}