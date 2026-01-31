package ru.car.dto.notification;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationTemplateDto {
    private Long id;
    private String templateType;
    private String code;
    private String name;
    private String subject;
    private String body;
    private String bodyHtml;
    private Boolean isActive;
    private Integer priority;
    private LocalDateTime createdAt;
}