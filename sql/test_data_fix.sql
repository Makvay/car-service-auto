-- Fix: убираем явные ID, используем DEFAULT для автогенерации

-- Сначала удаляем в правильном порядке
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;
DELETE FROM claim.claims;
DELETE FROM client.client_car;
DELETE FROM client.client;
DELETE FROM master.masters;
DELETE FROM nsi.services;
DELETE FROM nsi.car_stamp;

-- Вставляем клиентов БЕЗ явных ID
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) 
VALUES 
(NOW(), 'ivanov@mail.ru', 'Иван', 'Иванов', '+7(999)111-11-11', 'ACTIVE', NOW()),
(NOW(), 'petrova@mail.ru', 'Елена', 'Петрова', '+7(999)222-22-22', 'ACTIVE', NOW()),
(NOW(), 'sidorov@mail.ru', 'Пётр', 'Сидоров', '+7(999)333-33-33', 'ACTIVE', NOW()),
(NOW(), 'smirnova@mail.ru', 'Анна', 'Смирнова', '+7(999)444-44-44', 'ACTIVE', NOW()),
(NOW(), 'kuznetsov@mail.ru', 'Сергей', 'Кузнецов', '+7(999)555-55-55', 'ACTIVE', NOW())
RETURNING id, first_name;

-- После этого нужно получить ID клиентов и использовать их для машин
-- Но так как мы не знаем ID заранее, делаем two-step:
