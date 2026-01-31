package ru.car.entity.notification;

import jakarta.persistence.*;
import lombok.*;
import ru.car.dto.notification.NotificationStatus;
import ru.car.dto.notification.NotificationType;
import ru.car.dto.notification.RecipientType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", schema = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_template")
    private NotificationTemplateEntity template;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false, length = 50)
    private RecipientType recipientType;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "recipient_address", nullable = false, length = 500)
    private String recipientAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivery_confirmed_at")
    private LocalDateTime deliveryConfirmedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}