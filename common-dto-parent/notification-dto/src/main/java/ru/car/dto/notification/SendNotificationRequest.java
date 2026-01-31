package ru.car.dto.notification;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class SendNotificationRequest {
    @NotBlank(message = "Тип получателя обязателен")
    private String recipientType;

    @NotNull(message = "ID получателя обязательно")
    private Long recipientId;

    @NotBlank(message = "Адрес получателя обязателен")
    private String recipientAddress;

    @NotBlank(message = "Тип уведомления обязателен")
    private String notificationType;

    @NotBlank(message = "Сообщение обязательно")
    private String message;

    private String subject;
    private Long templateId;
    private String entityType;
    private Long entityId;
    private String metadata;
}