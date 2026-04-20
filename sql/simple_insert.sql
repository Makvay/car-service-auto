-- Сначала сбрасываем последовательности
ALTER SEQUENCE client.client_id_seq RESTART WITH 1;
ALTER SEQUENCE client.client_car_id_seq RESTART WITH 1;
ALTER SEQUENCE master.masters_id_seq RESTART WITH 1;
ALTER SEQUENCE claim.claims_id_seq RESTART WITH 1;
ALTER SEQUENCE nsi.car_stamp_id_seq RESTART WITH 1;
ALTER SEQUENCE nsi.services_id_seq RESTART WITH 1;
ALTER SEQUENCE warehouse.parts_id_seq RESTART WITH 1;
ALTER SEQUENCE warehouse.inventory_id_seq RESTART WITH 1;

-- Очищаем таблицы
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;
DELETE FROM claim.claims;
DELETE FROM client.client_car;
DELETE FROM client.client;
DELETE FROM master.masters;
DELETE FROM nsi.services;
DELETE FROM nsi.car_stamp;

-- 1. Клиенты
INSERT INTO client.client (id, first_name, last_name, phone, email, status, created_at, updated_at) 
SELECT 1, 'Иван', 'Иванов', '+79991111111', 'ivan@test.ru', 'ACTIVE', NOW(), NOW()
UNION ALL SELECT 2, 'Пётр', 'Петров', '+79992222222', 'petr@test.ru', 'ACTIVE', NOW(), NOW()
UNION ALL SELECT 3, 'Анна', 'Смирнова', '+79993333333', 'anna@test.ru', 'ACTIVE', NOW(), NOW();

-- 2. Машины
INSERT INTO client.client_car (id, fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
SELECT 1, 1, 'Toyota Camry', 'VIN11111111111111', 'А777АА', 2020, 50000, 'Чёрный', 'Toyota', NOW()
UNION ALL SELECT 2, 2, 'BMW X5', 'VIN22222222222222', 'В888ВВ', 2021, 30000, 'Белый', 'BMW', NOW()
UNION ALL SELECT 3, 3, 'Kia Rio', 'VIN33333333333333', 'К999КК', 2022, 15000, 'Красный', 'Kia', NOW();

-- 3. Мастера
INSERT INTO master.masters (id, employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
SELECT 1, 'M001', 'Александр', 'Сергеев', '+79001112233', 'alex@test.ru', 'DIAGNOSTICIAN', 'SENIOR', 2500.00, TRUE, DATE '2018-01-15', NOW(), NOW()
UNION ALL SELECT 2, 'M002', 'Дмитрий', 'Козлов', '+79002223344', 'dmitry@test.ru', 'MOTOR_SPECIALIST', 'MIDDLE', 2000.00, TRUE, DATE '2019-06-01', NOW(), NOW()
UNION ALL SELECT 3, 'M003', 'Мария', 'Волкова', '+79003334455', 'maria@test.ru', 'BODY_REPAIRER', 'SENIOR', 2800.00, TRUE, DATE '2020-03-20', NOW(), NOW();

-- 4. Заявки
INSERT INTO claim.claims (id, claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) 
SELECT 1, 'CLM-001', 1, 1, 'IN_PROGRESS', 'Замена масла', 50000, 'NORMAL', TRUE, FALSE, NOW(), NOW()
UNION ALL SELECT 2, 'CLM-002', 2, 2, 'CREATED', 'Диагностика', 30000, 'NORMAL', FALSE, FALSE, NOW(), NOW();

-- 5. NSI
INSERT INTO nsi.car_stamp (id, code, name, country, is_active, created_at) 
SELECT 1, 'TOYOTA', 'Toyota', 'Япония', TRUE, NOW()
UNION ALL SELECT 2, 'BMW', 'BMW', 'Германия', TRUE, NOW()
UNION ALL SELECT 3, 'KIA', 'Kia', 'Южная Корея', TRUE, NOW();

INSERT INTO nsi.services (id, code, name, description, standard_price, standard_duration_min, is_active, created_at) 
SELECT 1, 'OIL_CHANGE', 'Замена масла', 'Замена моторного масла', 1500.00, 30, TRUE, NOW()
UNION ALL SELECT 2, 'BRAKE_SERVICE', 'Обслуживание тормозов', 'Проверка тормозной системы', 2000.00, 45, TRUE, NOW();

-- 6. Склад
INSERT INTO warehouse.parts (id, sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) 
SELECT 1, 'OIL-5W30', 'Масло 5W-30', 1, 'Mobil', 2500.00, 1800.00, TRUE, NOW(), NOW()
UNION ALL SELECT 2, 'FILTER-OIL', 'Фильтр масляный', 6, 'Mann', 450.00, 300.00, TRUE, NOW(), NOW();

INSERT INTO warehouse.inventory (id, fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) 
SELECT 1, 1, 50, 5, 10, 100, NOW(), NOW()
UNION ALL SELECT 2, 2, 100, 10, 20, 200, NOW(), NOW();