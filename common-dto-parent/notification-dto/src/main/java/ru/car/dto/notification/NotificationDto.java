package ru.car.dto.notification;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private Long templateId;
    private String recipientType;
    private Long recipientId;
    private String recipientAddress;
    private String notificationType;
    private String subject;
    private String message;
    private String metadata;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime deliveryConfirmedAt;
    private String failureReason;
    private String entityType;
    private Long entityId;
    private Integer retryCount;
    private LocalDateTime createdAt;
}