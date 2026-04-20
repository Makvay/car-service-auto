-- Test data for frontend
-- Execute in PostgreSQL

-- 1. Clear and insert clients
DELETE FROM client.client_car;
DELETE FROM client.client;

INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES 
(NOW(), 'ivan@test.ru', 'Иван', 'Иванов', '+79991234567', 'ACTIVE', NOW()),
(NOW(), 'petr@test.ru', 'Пётр', 'Петров', '+79992345678', 'ACTIVE', NOW()),
(NOW(), 'anna@test.ru', 'Анна', 'Смирнова', '+79993456789', 'ACTIVE', NOW());

-- 2. Insert cars (client_id will be 1,2,3)
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES 
(1, 'Toyota Camry', 'VIN11111111111111', 'А777АА', 2020, 50000, 'Чёрный', NOW(), 'Toyota'),
(2, 'BMW X5', 'VIN22222222222222', 'В888ВВ', 2021, 30000, 'Белый', NOW(), 'BMW'),
(3, 'Kia Rio', 'VIN33333333333333', 'К999КК', 2022, 15000, 'Красный', NOW(), 'Kia');

-- 3. Clear and insert masters
DELETE FROM master.masters;

INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) VALUES 
('M001', 'Александр', 'Сергеев', '+79001112233', 'alex@test.ru', 'DIAGNOSTICIAN', 'SENIOR', 2500.00, TRUE, '2018-01-15', NOW(), NOW()),
('M002', 'Дмитрий', 'Козлов', '+79002223344', 'dmitry@test.ru', 'MOTOR_SPECIALIST', 'MIDDLE', 2000.00, TRUE, '2019-06-01', NOW(), NOW()),
('M003', 'Мария', 'Волкова', '+79003334455', 'maria@test.ru', 'BODY_REPAIRER', 'SENIOR', 2800.00, TRUE, '2020-03-20', NOW(), NOW());

-- 4. Clear and insert claims (use correct client/car IDs)
DELETE FROM claim.claims;

INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) VALUES 
('CLM-001', 1, 1, 'CREATED', 'Замена масла', 50000, 'NORMAL', FALSE, FALSE, NOW(), NOW()),
('CLM-002', 2, 2, 'IN_PROGRESS', 'Диагностика', 30000, 'NORMAL', TRUE, FALSE, NOW(), NOW()),
('CLM-003', 3, 3, 'COMPLETED', 'Техобслуживание', 15000, 'LOW', TRUE, TRUE, NOW(), NOW());

-- 5. Clear and insert parts
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;

INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) VALUES 
('OIL-5W30', 'Масло 5W-30', 1, 'Mobil', 2500, 1800, TRUE, NOW(), NOW()),
('FILTER-OIL', 'Фильтр масляный', 6, 'Mann', 450, 300, TRUE, NOW(), NOW()),
('FILTER-AIR', 'Фильтр воздушный', 6, 'Bosch', 380, 250, TRUE, NOW(), NOW()),
('BRAKE-F', 'Колодки передние', 5, 'Brembo', 4500, 3200, TRUE, NOW(), NOW()),
('BRAKE-R', 'Колодки задние', 5, 'Brembo', 3800, 2700, TRUE, NOW(), NOW());

INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) VALUES 
(1, 50, 5, 10, 100, NOW(), NOW()),
(2, 100, 10, 20, 200, NOW(), NOW()),
(3, 80, 8, 20, 150, NOW(), NOW()),
(4, 30, 2, 10, 50, NOW(), NOW()),
(5, 25, 3, 10, 50, NOW(), NOW());
