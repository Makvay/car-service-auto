package ru.car.dto.claim;

import lombok.Getter;

@Getter
public enum ClaimPriority {
    URGENT("Срочный", 1),
    HIGH("Высокий", 2),
    NORMAL("Обычный", 3),
    LOW("Низкий", 4);

    private final String description;
    private final int level;

    ClaimPriority(String description, int level) {
        this.description = description;
        this.level = level;
    }
}