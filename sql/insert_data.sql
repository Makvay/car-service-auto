-- Заполнение данных (БЕЗ явных ID)

-- Клиенты
INSERT INTO client.client (first_name, last_name, phone, email, status, created_at, updated_at) 
VALUES ('Иван', 'Иванов', '+79991111111', 'ivan@test.ru', 'ACTIVE', NOW(), NOW());

INSERT INTO client.client (first_name, last_name, phone, email, status, created_at, updated_at) 
VALUES ('Пётр', 'Петров', '+79992222222', 'petr@test.ru', 'ACTIVE', NOW(), NOW());

INSERT INTO client.client (first_name, last_name, phone, email, status, created_at, updated_at) 
VALUES ('Анна', 'Смирнова', '+79993333333', 'anna@test.ru', 'ACTIVE', NOW(), NOW());

-- Машины
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
VALUES (1, 'Toyota Camry', 'VIN11111111111111', 'А777АА', 2020, 50000, 'Чёрный', 'Toyota', NOW());

INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
VALUES (2, 'BMW X5', 'VIN22222222222222', 'В888ВВ', 2021, 30000, 'Белый', 'BMW', NOW());

INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
VALUES (3, 'Kia Rio', 'VIN33333333333333', 'К999КК', 2022, 15000, 'Красный', 'Kia', NOW());

-- Мастера
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES ('M001', 'Александр', 'Сергеев', '+79001112233', 'alex@test.ru', 'DIAGNOSTICIAN', 'SENIOR', 2500.00, TRUE, DATE '2018-01-15', NOW(), NOW());

INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES ('M002', 'Дмитрий', 'Козлов', '+79002223344', 'dmitry@test.ru', 'MOTOR_SPECIALIST', 'MIDDLE', 2000.00, TRUE, DATE '2019-06-01', NOW(), NOW());

INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES ('M003', 'Мария', 'Волкова', '+79003334455', 'maria@test.ru', 'BODY_REPAIRER', 'SENIOR', 2800.00, TRUE, DATE '2020-03-20', NOW(), NOW());

-- NSI
INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) 
VALUES ('TOYOTA', 'Toyota', 'Япония', TRUE, NOW());

INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) 
VALUES ('BMW', 'BMW', 'Германия', TRUE, NOW());

INSERT INTO nsi.services (code, name, description, standard_price, standard_duration_min, is_active, created_at) 
VALUES ('OIL_CHANGE', 'Замена масла', 'Замена моторного масла', 1500.00, 30, TRUE, NOW());

-- Заявки
INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) 
VALUES ('CLM-001', 1, 1, 'IN_PROGRESS', 'Замена масла', 50000, 'NORMAL', TRUE, FALSE, NOW(), NOW());

-- Склад
INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) 
VALUES ('OIL-5W30', 'Масло 5W-30', 1, 'Mobil', 2500.00, 1800.00, TRUE, NOW(), NOW());

INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) 
VALUES (1, 50, 5, 10, 100, NOW(), NOW());