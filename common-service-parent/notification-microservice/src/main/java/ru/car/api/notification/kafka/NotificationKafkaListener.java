package ru.car.api.notification.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.car.api.notification.service.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {

    private final EmailService emailService;

    @KafkaListener(topics = "client.registered", groupId = "notification-group")
    public void onClientRegistered(ClientRegisteredEvent event) {
        log.info("Регистрация клиента: {}", event.getEmail());

        String subject = "Добро пожаловать в Автосервис";
        String body = String.format(
                "Уважаемый(ая) %s %s!\n\n" +
                        "Ваш аккаунт в системе автосервиса успешно создан.\n" +
                        "Теперь вы можете создавать заявки и отслеживать их статус.\n\n" +
                        "С уважением,\nКоманда автосервиса",
                event.getFirstName(),
                event.getLastName()
        );

        emailService.sendEmailToClient(event.getEmail(), subject, body);
    }

    @KafkaListener(topics = "claim.created", groupId = "notification-group")
    public void onClaimCreated(ClaimEvent event) {
        log.info("Создана заявка: {}", event.getClaimNumber());

        String subject = "Заявка #" + event.getClaimNumber() + " создана";
        String body = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Ваша заявка #%s успешно создана.\n\n" +
                        "Описание: %s\n\n" +
                        "С уважением,\nКоманда автосервиса",
                valueOrDefault(event.getClientName(), "клиент"),
                event.getClaimNumber(),
                valueOrDefault(event.getDescription(), "Без описания")
        );

        emailService.sendEmailToClient(event.getClientEmail(), subject, body);
    }

    @KafkaListener(topics = "claim.status.changed", groupId = "notification-group")
    public void onClaimStatusChanged(ClaimEvent event) {
        log.info("Изменен статус заявки: {}", event.getClaimNumber());

        String statusText = getStatusText(event.getStatus());
        String subject = "Заявка #" + event.getClaimNumber() + ": " + statusText;
        String body = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Статус вашей заявки #%s изменен на: %s.\n\n" +
                        "%s\n\n" +
                        "С уважением,\nКоманда автосервиса",
                valueOrDefault(event.getClientName(), "клиент"),
                event.getClaimNumber(),
                statusText,
                getStatusMessage(event.getStatus())
        );

        emailService.sendEmailToClient(event.getClientEmail(), subject, body);
    }

    @KafkaListener(topics = "work.completed", groupId = "notification-group")
    public void onWorkCompleted(ClaimEvent event) {
        log.info("Работа завершена: {}", event.getClaimNumber());

        String subject = "Ваш автомобиль готов";
        String body = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Работа по заявке #%s завершена.\n" +
                        "Вы можете забрать автомобиль в удобное время.\n\n" +
                        "С уважением,\nКоманда автосервиса",
                valueOrDefault(event.getClientName(), "клиент"),
                event.getClaimNumber()
        );

        emailService.sendEmailToClient(event.getClientEmail(), subject, body);
    }

    private String getStatusText(String status) {
        return switch (status) {
            case "CREATED" -> "Новая";
            case "IN_PROGRESS" -> "В работе";
            case "WAITING_PARTS" -> "Ожидает запчасти";
            case "COMPLETED" -> "Готова";
            case "CANCELLED" -> "Отменена";
            default -> valueOrDefault(status, "Обновлена");
        };
    }

    private String getStatusMessage(String status) {
        return switch (status) {
            case "CREATED" -> "Ваша заявка принята и ожидает назначения мастера.";
            case "IN_PROGRESS" -> "Мастер приступил к работе по вашей заявке.";
            case "WAITING_PARTS" -> "Мы ожидаем необходимые запчасти.";
            case "COMPLETED" -> "Все работы выполнены. Автомобиль готов к выдаче.";
            case "CANCELLED" -> "Заявка отменена. Если есть вопросы, свяжитесь с нами.";
            default -> "Статус заявки был обновлен.";
        };
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
