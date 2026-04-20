-- БЛОК 2: Вставляем клиентов
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) 
VALUES 
(NOW(), 'ivanov@mail.ru', 'Иван', 'Иванов', '+7(999)111-11-11', 'ACTIVE', NOW()),
(NOW(), 'petrova@mail.ru', 'Елена', 'Петрова', '+7(999)222-22-22', 'ACTIVE', NOW()),
(NOW(), 'sidorov@mail.ru', 'Пётр', 'Сидоров', '+7(999)333-33-33', 'ACTIVE', NOW()),
(NOW(), 'smirnova@mail.ru', 'Анна', 'Смирнова', '+7(999)444-44-44', 'ACTIVE', NOW()),
(NOW(), 'kuznetsov@mail.ru', 'Сергей', 'Кузнецов', '+7(999)555-55-55', 'ACTIVE', NOW());
