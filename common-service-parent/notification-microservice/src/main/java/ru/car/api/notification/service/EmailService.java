package ru.car.api.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serial;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Async
    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000)
    )
    public void sendEmail(String to, String subject, String body) {
        if (!mailEnabled) {
            log.info(">>> MOCK EMAIL to={}, subject={}", to, subject);
            log.info(">>> BODY: {}", body);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        if (mailUsername != null && !mailUsername.isBlank()) {
            message.setFrom(mailUsername);
        }

        mailSender.send(message);
        log.info("Email отправлен на {}: {}", to, subject);
    }

    @Recover
    public void recoverEmail(Exception e, String to, String subject, String body) {
        log.error("Failed to send email after retries to {}: {}. Fallback to mock.", to, e.getMessage());
        log.info(">>> MOCK EMAIL (fallback) to={}, subject={}", to, subject);
        log.info(">>> BODY: {}", body);
    }

    public void sendEmailToClient(String clientEmail, String subject, String body) {
        if (clientEmail != null && !clientEmail.isBlank()) {
            sendEmail(clientEmail, subject, body);
        } else {
            log.warn("Email клиента пустой, отправка пропущена");
        }
    }
}
