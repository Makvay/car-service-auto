package ru.car.api.notification.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.car.api.notification.service.EmailService;
import ru.car.api.notification.service.ProcessedEventService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {

    private final EmailService emailService;
    private final ProcessedEventService processedEventService;

    @KafkaListener(topics = "client.registered", groupId = "notification-group")
    public void onClientRegistered(ClientRegisteredEvent event) {
        String subject = "Welcome to Car Service";
        String body = String.format(
                "Hello, %s %s!\n\nYour account has been successfully created.",
                valueOrDefault(event.getFirstName(), "Client"),
                valueOrDefault(event.getLastName(), "")
        );
        emailService.sendEmailToClient(event.getEmail(), subject, body);
    }

    @KafkaListener(topics = "claim.created", groupId = "notification-group")
    public void onClaimCreated(ClaimEvent event) {
        if (isDuplicate("claim.created", event)) {
            return;
        }
        String subject = "Claim #" + event.getClaimNumber() + " created";
        String body = String.format(
                "Hello, %s!\n\nYour claim #%s was created.\nDescription: %s",
                valueOrDefault(event.getClientName(), "Client"),
                valueOrDefault(event.getClaimNumber(), "-"),
                valueOrDefault(event.getDescription(), "No description")
        );
        emailService.sendEmailToClient(event.getClientEmail(), subject, body);
        markProcessed("claim.created", event);
    }

    @KafkaListener(topics = "claim.status.changed", groupId = "notification-group")
    public void onClaimStatusChanged(ClaimEvent event) {
        if (isDuplicate("claim.status.changed", event)) {
            return;
        }
        String status = valueOrDefault(event.getStatus(), "UPDATED");
        String subject = "Claim #" + event.getClaimNumber() + ": " + status;
        String body = String.format(
                "Hello, %s!\n\nStatus of claim #%s changed to %s.",
                valueOrDefault(event.getClientName(), "Client"),
                valueOrDefault(event.getClaimNumber(), "-"),
                status
        );
        emailService.sendEmailToClient(event.getClientEmail(), subject, body);
        markProcessed("claim.status.changed", event);
    }

    @KafkaListener(topics = "work.completed", groupId = "notification-group")
    public void onWorkCompleted(ClaimEvent event) {
        if (isDuplicate("work.completed", event)) {
            return;
        }
        String subject = "Your car is ready";
        String body = String.format(
                "Hello, %s!\n\nWork for claim #%s has been completed.",
                valueOrDefault(event.getClientName(), "Client"),
                valueOrDefault(event.getClaimNumber(), "-")
        );
        emailService.sendEmailToClient(event.getClientEmail(), subject, body);
        markProcessed("work.completed", event);
    }

    private boolean isDuplicate(String topic, ClaimEvent event) {
        if (event == null || event.getEventId() == null || event.getEventId().isBlank()) {
            return false;
        }
        boolean duplicate = processedEventService.isProcessed(topic, event.getEventId());
        if (duplicate) {
            log.info("Skip duplicate event topic={}, eventId={}", topic, event.getEventId());
        }
        return duplicate;
    }

    private void markProcessed(String topic, ClaimEvent event) {
        if (event != null && event.getEventId() != null && !event.getEventId().isBlank()) {
            processedEventService.markProcessed(topic, event.getEventId());
        }
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
