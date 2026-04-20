package ru.car.api.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public void sendEmail(String to, String subject, String body) {
        if (!mailEnabled) {
            log.info(">>> MOCK EMAIL to={}, subject={}", to, subject);
            log.info(">>> BODY: {}", body);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            if (mailUsername != null && !mailUsername.isBlank()) {
                message.setFrom(mailUsername);
            }

            mailSender.send(message);
            log.info("Email отправлен на {}: {}", to, subject);
        } catch (Exception e) {
            log.error("Ошибка отправки email на {}: {}", to, e.getMessage());
            log.info(">>> MOCK EMAIL (fallback) to={}, subject={}", to, subject);
            log.info(">>> BODY: {}", body);
        }
    }

    public void sendEmailToClient(String clientEmail, String subject, String body) {
        if (clientEmail != null && !clientEmail.isBlank()) {
            sendEmail(clientEmail, subject, body);
        } else {
            log.warn("Email клиента пустой, отправка пропущена");
        }
    }
}
