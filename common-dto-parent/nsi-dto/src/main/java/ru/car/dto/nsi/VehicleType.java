package ru.car.dto.nsi;

import lombok.Getter;

@Getter
public enum VehicleType {
    SEDAN("Седан"),
    HATCHBACK("Хэтчбек"),
    COUPE("Купе"),
    CONVERTIBLE("Кабриолет"),
    WAGON("Универсал"),
    SUV("Внедорожник"),
    CROSSOVER("Кроссовер"),
    MINIVAN("Минивэн"),
    PICKUP("Пикап"),
    VAN("Фургон"),
    TRUCK("Грузовик"),
    BUS("Автобус");

    private final String description;

    VehicleType(String description) {
        this.description = description;
    }
}