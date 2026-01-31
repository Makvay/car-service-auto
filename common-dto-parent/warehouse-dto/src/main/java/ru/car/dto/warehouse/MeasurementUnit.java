package ru.car.dto.warehouse;

import lombok.Getter;

@Getter
public enum MeasurementUnit {
    PCS("шт."),
    LITRE("л"),
    KG("кг"),
    METER("м"),
    SET("комплект"),
    PAIR("пара"),
    ROLL("рулон");

    private final String description;

    MeasurementUnit(String description) {
        this.description = description;
    }
}