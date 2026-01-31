package ru.car.dto.master;

import lombok.Getter;

@Getter
public enum MasterQualification {
    TRAINEE("Стажер"),
    JUNIOR("Младший специалист"),
    MIDDLE("Специалист"),
    SENIOR("Старший специалист"),
    LEAD("Ведущий специалист"),
    EXPERT("Эксперт");

    private final String description;

    MasterQualification(String description) {
        this.description = description;
    }
}