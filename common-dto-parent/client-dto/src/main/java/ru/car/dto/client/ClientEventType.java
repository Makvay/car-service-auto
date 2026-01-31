package ru.car.dto.client;

import lombok.Getter;

@Getter
public enum ClientEventType {
    REGISTRATION("Регистрация"),
    UPDATE_PROFILE("Изменение профиля"),
    VEHICLE_ADDED("Добавлен автомобиль"),
    VEHICLE_UPDATED("Обновлен автомобиль"),
    VEHICLE_REMOVED("Удален автомобиль"),
    STATUS_CHANGED("Изменен статус"),
    CONTACT_UPDATED("Обновлены контакты");

    private final String description;

    ClientEventType(String description) {
        this.description = description;
    }
}