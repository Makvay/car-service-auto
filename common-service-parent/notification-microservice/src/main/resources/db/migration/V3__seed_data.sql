-- V3__seed_data.sql
-- Seed data for Notification service

-- Insert Notification Templates
INSERT INTO notification.notification_templates (template_type, code, name, subject, body, body_html, is_active, priority) VALUES
('EMAIL', 'CLAIM_CREATED', 'Заявка создана', 'Ваша заявка #{{claimNumber}} создана', 'Уважаемый клиент! Ваша заявка на обслуживание #{{claimNumber}} успешно создана. Мы свяжемся с вами в ближайшее время.', '<h1>Заявка создана</h1><p>Номер: {{claimNumber}}</p>', TRUE, 1),
('EMAIL', 'CLAIM_STATUS', 'Изменение статуса заявки', 'Статус вашей заявки #{{claimNumber}} изменён', 'Уважаемый клиент! Статус вашей заявки #{{claimNumber}} изменён на: {{newStatus}}', '<h1>Статус изменён</h1><p>Заявка: {{claimNumber}}<br/>Новый статус: {{newStatus}}</p>', TRUE, 1),
('EMAIL', 'CLAIM_DONE', 'Заявка выполнена', 'Ваша заявка #{{claimNumber}} выполнена', 'Уважаемый клиент! Ваша заявка #{{claimNumber}} выполнена. Вы можете забрать автомобиль.', '<h1>Заявка выполнена</h1><p>Заявка #{{claimNumber}} готова к выдаче.</p>', TRUE, 1),
('SMS', 'REMINDER', 'Напоминание о ТО', 'Напоминание о ТО', 'Напоминаем, что подошло время технического обслуживания вашего автомобиля. Запишитесь на ТО по телефону.', NULL, TRUE, 2);

-- Insert Notifications
INSERT INTO notification.notifications (fk_template, recipient_type, recipient_id, recipient_address, notification_type, subject, message, metadata, status, entity_type, entity_id, retry_count) VALUES
(1, 'CLIENT', 1, 'ivanov@mail.ru', 'EMAIL', 'Ваша заявка #CLM-2024-0001 создана', 'Уважаемый Иван! Ваша заявка на обслуживание #CLM-2024-0001 успешно создана.', '{"claimId": 1, "claimNumber": "CLM-2024-0001"}', 'SENT', 'CLAIM', 1, 0),
(2, 'CLIENT', 2, 'petrova@mail.ru', 'EMAIL', 'Статус вашей заявки #CLM-2024-0002 изменён', 'Уважаемая Елена! Статус вашей заявки #CLM-2024-0002 изменён на: DONE', '{"claimId": 2, "claimNumber": "CLM-2024-0002", "newStatus": "DONE"}', 'SENT', 'CLAIM', 2, 0),
(3, 'CLIENT', 5, 'kuznetsov@mail.ru', 'EMAIL', 'Ваша заявка #CLM-2024-0005 выполнена', 'Уважаемый Сергей! Ваша заявка #CLM-2024-0005 выполнена. Вы можете забрать автомобиль.', '{"claimId": 5, "claimNumber": "CLM-2024-0005"}', 'SENT', 'CLAIM', 5, 0);
