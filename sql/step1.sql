-- Тестовая загрузка данных
-- Выполняйте по очереди, блоками

-- БЛОК 1: Удаляем всё
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;
DELETE FROM claim.claims;
DELETE FROM client.client_car;
DELETE FROM client.client;
DELETE FROM master.masters;
DELETE FROM nsi.services;
DELETE FROM nsi.car_stamp;

-- Проверка что удалилось
SELECT 'Удалено' as status;
