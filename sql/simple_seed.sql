-- FIX
ALTER TABLE claim.claims ALTER COLUMN client_id DROP NOT NULL;
ALTER TABLE claim.claims ALTER COLUMN vehicle_id DROP NOT NULL;

-- CLEAR
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;
DELETE FROM claim.claims;
DELETE FROM client.client_car;
DELETE FROM client.client;
DELETE FROM master.masters;
DELETE FROM nsi.services;
DELETE FROM nsi.car_stamp;
DELETE FROM nsi.part_categories;
DELETE FROM notification.notification_templates;

-- CLIENTS
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES (NOW(), 'ivanov@mail.ru', 'Иван', 'Иванов', '+7(999)111-11-11', 'ACTIVE', NOW());
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES (NOW(), 'petrova@mail.ru', 'Елена', 'Петрова', '+7(999)222-22-22', 'ACTIVE', NOW());
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES (NOW(), 'sidorov@mail.ru', 'Пётр', 'Сидоров', '+7(999)333-33-33', 'ACTIVE', NOW());
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES (NOW(), 'smirnova@mail.ru', 'Анна', 'Смирнова', '+7(999)444-44-44', 'ACTIVE', NOW());
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES (NOW(), 'kuznetsov@mail.ru', 'Сергей', 'Кузнецов', '+7(999)555-55-55', 'ACTIVE', NOW());

-- CARS
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES (1, 'Toyota Camry', 'VIN001', 'А777АА77', 2020, 50000, 'Чёрный', NOW(), 'Toyota');
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES (2, 'BMW X5', 'VIN002', 'С333СС77', 2021, 30000, 'Белый', NOW(), 'BMW');
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES (3, 'Hyundai Solaris', 'VIN003', 'Е444ЕЕ77', 2019, 85000, 'Синий', NOW(), 'Hyundai');
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES (4, 'Kia Rio', 'VIN004', 'К666КК77', 2022, 15000, 'Красный', NOW(), 'Kia');
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES (5, 'Volkswagen Polo', 'VIN005', 'М888ММ77', 2017, 120000, 'Серый', NOW(), 'Volkswagen');

-- MASTERS
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, hourly_rate, is_active, hire_date, created_at, updated_at) VALUES ('MSTR001', 'Александр', 'Сергеев', '+7(900)111-11-11', 'alex@mail.ru', 'DIAGNOSTIC', 2500.00, TRUE, '2018-01-15', NOW(), NOW());
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, hourly_rate, is_active, hire_date, created_at, updated_at) VALUES ('MSTR002', 'Дмитрий', 'Козлов', '+7(900)222-22-22', 'dmitry@mail.ru', 'ENGINE', 3000.00, TRUE, '2017-06-01', NOW(), NOW());
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, hourly_rate, is_active, hire_date, created_at, updated_at) VALUES ('MSTR003', 'Мария', 'Волкова', '+7(900)333-33-33', 'maria@mail.ru', 'BODY', 2800.00, TRUE, '2019-03-20', NOW(), NOW());

-- NSI
INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) VALUES ('TOYOTA', 'Toyota', 'Япония', TRUE, NOW());
INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) VALUES ('BMW', 'BMW', 'Германия', TRUE, NOW());
INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) VALUES ('HYUNDAI', 'Hyundai', 'Южная Корея', TRUE, NOW());
INSERT INTO nsi.services (code, name, description, standard_price, standard_duration_min, is_active, created_at) VALUES ('OIL_CHANGE', 'Замена масла', 'Замена моторного масла', 1500.00, 30, TRUE, NOW());
INSERT INTO nsi.services (code, name, description, standard_price, standard_duration_min, is_active, created_at) VALUES ('DIAGNOSTICS', 'Компьютерная диагностика', 'Полная диагностика', 3000.00, 60, TRUE, NOW());
INSERT INTO nsi.part_categories (code, name, is_active, created_at) VALUES ('ENGINE', 'Двигатель', TRUE, NOW());
INSERT INTO nsi.part_categories (code, name, is_active, created_at) VALUES ('BRAKES', 'Тормоза', TRUE, NOW());
INSERT INTO nsi.part_categories (code, name, is_active, created_at) VALUES ('FILTERS', 'Фильтры', TRUE, NOW());

-- CLAIMS
INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) VALUES ('CLM-001', 1, 1, 'IN_PROGRESS', 'Стук в подвеске', 50000, 'NORMAL', TRUE, FALSE, NOW(), NOW());
INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) VALUES ('CLM-002', 2, 2, 'DONE', 'Замена масла', 30000, 'NORMAL', TRUE, TRUE, NOW(), NOW());
INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) VALUES ('CLM-003', 3, 3, 'CREATED', 'Техобслуживание', 85000, 'LOW', FALSE, FALSE, NOW(), NOW());

-- WAREHOUSE
INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) VALUES ('OIL-5W30', 'Масло 5W-30', 1, 'Mobil', 2500.00, 1800.00, TRUE, NOW(), NOW());
INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) VALUES ('OIL-FILTER', 'Фильтр масляный', 8, 'Mann', 450.00, 300.00, TRUE, NOW(), NOW());
INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) VALUES ('BRAKE-PAD-F', 'Колодки передние', 7, 'Brembo', 4500.00, 3200.00, TRUE, NOW(), NOW());
INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) VALUES (1, 50, 5, 10, 100, NOW(), NOW());
INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) VALUES (2, 100, 10, 20, 200, NOW(), NOW());
INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) VALUES (3, 30, 2, 10, 50, NOW(), NOW());
