package ru.car.dto.notification;

import lombok.Getter;

@Getter
public enum TemplateType {
    EMAIL("Email шаблон"),
    SMS("SMS шаблон"),
    PUSH("Push шаблон");

    private final String description;

    TemplateType(String description) {
        this.description = description;
    }
}