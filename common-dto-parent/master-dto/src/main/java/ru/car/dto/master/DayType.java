package ru.car.dto.master;


import lombok.Getter;

@Getter
public enum DayType {
    WORKDAY("Рабочий день"),
    VACATION("Отпуск"),
    SICK_LEAVE("Больничный"),
    DAY_OFF("Выходной"),
    HOLIDAY("Праздничный"),
    BUSINESS_TRIP("Командировка");

    private final String description;

    DayType(String description) {
        this.description = description;
    }
}