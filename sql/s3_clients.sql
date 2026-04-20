-- Шаг 3: Клиенты (с явными ID)
INSERT INTO client.client (id, first_name, last_name, phone, email, status, created_at, updated_at) 
VALUES (1, 'Иван', 'Иванов', '+79991111111', 'ivan@test.ru', 'ACTIVE', NOW(), NOW());

INSERT INTO client.client (id, first_name, last_name, phone, email, status, created_at, updated_at) 
VALUES (2, 'Пётр', 'Петров', '+79992222222', 'petr@test.ru', 'ACTIVE', NOW(), NOW());

INSERT INTO client.client (id, first_name, last_name, phone, email, status, created_at, updated_at) 
VALUES (3, 'Анна', 'Смирнова', '+79993333333', 'anna@test.ru', 'ACTIVE', NOW(), NOW());