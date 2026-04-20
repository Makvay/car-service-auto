-- Шаг 2: Очистка таблиц
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;
DELETE FROM claim.claims;
DELETE FROM client.client_car;
DELETE FROM client.client;
DELETE FROM master.masters;
DELETE FROM nsi.services;
DELETE FROM nsi.car_stamp;