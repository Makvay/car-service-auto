package ru.car.entity.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates", schema = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_type", nullable = false, length = 50)
    private String templateType;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "body_html")
    private String bodyHtml;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}