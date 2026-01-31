package ru.car.dto.master;

import lombok.Getter;

@Getter
public enum MasterSpecialization {
    ENGINE("Двигатель"),
    TRANSMISSION("Трансмиссия"),
    SUSPENSION("Подвеска"),
    BRAKES("Тормоза"),
    ELECTRICAL("Электрика"),
    BODY("Кузовные работы"),
    PAINT("Покраска"),
    DIAGNOSTICS("Диагностика"),
    TIRES("Шиномонтаж"),
    GENERAL("Общее обслуживание");

    private final String description;

    MasterSpecialization(String description) {
        this.description = description;
    }
}