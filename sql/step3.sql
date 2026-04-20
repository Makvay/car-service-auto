-- БЛОК 3: Вставляем клиентов С ЯВНЫМИ ID
INSERT INTO client.client (id, created_at, email, first_name, last_name, phone, status, updated_at) 
VALUES 
(1, NOW(), 'ivanov@mail.ru', 'Иван', 'Иванов', '+7(999)111-11-11', 'ACTIVE', NOW()),
(2, NOW(), 'petrova@mail.ru', 'Елена', 'Петрова', '+7(999)222-22-22', 'ACTIVE', NOW()),
(3, NOW(), 'sidorov@mail.ru', 'Пётр', 'Сидоров', '+7(999)333-33-33', 'ACTIVE', NOW()),
(4, NOW(), 'smirnova@mail.ru', 'Анна', 'Смирнова', '+7(999)444-44-44', 'ACTIVE', NOW()),
(5, NOW(), 'kuznetsov@mail.ru', 'Сергей', 'Кузнецов', '+7(999)555-55-55', 'ACTIVE', NOW());

-- БЛОК 4: Вставляем машины
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) 
VALUES 
(1, 'Toyota Camry', 'VIN001', 'А777АА77', 2020, 50000, 'Чёрный', NOW(), 'Toyota'),
(2, 'BMW X5', 'VIN002', 'С333СС77', 2021, 30000, 'Белый', NOW(), 'BMW'),
(3, 'Hyundai Solaris', 'VIN003', 'Е444ЕЕ77', 2019, 85000, 'Синий', NOW(), 'Hyundai'),
(4, 'Kia Rio', 'VIN004', 'К666КК77', 2022, 15000, 'Красный', NOW(), 'Kia'),
(5, 'Volkswagen Polo', 'VIN005', 'М888ММ77', 2017, 120000, 'Серый', NOW(), 'Volkswagen');

-- БЛОК 5: Мастера
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES 
('MSTR001', 'Александр', 'Сергеев', '+7(900)111-11-11', 'alex@mail.ru', 'DIAGNOSTICIAN', 'SENIOR', 2500.00, TRUE, '2018-01-15', NOW(), NOW()),
('MSTR002', 'Дмитрий', 'Козлов', '+7(900)222-22-22', 'dmitry@mail.ru', 'MOTOR_SPECIALIST', 'MIDDLE', 3000.00, TRUE, '2017-06-01', NOW(), NOW()),
('MSTR003', 'Мария', 'Волкова', '+7(900)333-33-33', 'maria@mail.ru', 'BODY_REPAIRER', 'SENIOR', 2800.00, TRUE, '2019-03-20', NOW(), NOW()),
('MSTR004', 'Николай', 'Морозов', '+7(900)444-44-44', 'nikolay@mail.ru', 'ELECTRICIAN', 'LEAD', 3200.00, TRUE, '2016-09-10', NOW(), NOW()),
('MSTR005', 'Екатерина', 'Соколова', '+7(900)555-55-55', 'ekaterina@mail.ru', 'UNIVERSAL', 'JUNIOR', 1500.00, TRUE, '2022-05-01', NOW(), NOW());

-- БЛОК 6: Заявки
INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) 
VALUES 
('CLM-2024-0001', 1, 1, 'IN_PROGRESS', 'Стук в подвеске', 50000, 'NORMAL', TRUE, FALSE, NOW(), NOW()),
('CLM-2024-0002', 2, 2, 'COMPLETED', 'Замена масла', 30000, 'NORMAL', TRUE, TRUE, NOW() - INTERVAL '7 days', NOW()),
('CLM-2024-0003', 3, 3, 'WAITING_PARTS', 'Не работает фонарь', 85000, 'NORMAL', TRUE, FALSE, NOW() - INTERVAL '2 days', NOW()),
('CLM-2024-0004', 4, 4, 'CREATED', 'Техническое обслуживание', 15000, 'LOW', FALSE, FALSE, NOW(), NOW()),
('CLM-2024-0005', 5, 5, 'IN_PROGRESS', 'Вмятина на крыле', 120000, 'HIGH', TRUE, TRUE, NOW() - INTERVAL '5 days', NOW());

-- БЛОК 7: NSI
INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) 
VALUES 
('TOYOTA', 'Toyota', 'Япония', TRUE, NOW()),
('BMW', 'BMW', 'Германия', TRUE, NOW()),
('HYUNDAI', 'Hyundai', 'Южная Корея', TRUE, NOW()),
('KIA', 'Kia', 'Южная Корея', TRUE, NOW()),
('VOLKSWAGEN', 'Volkswagen', 'Германия', TRUE, NOW());

INSERT INTO nsi.services (code, name, description, standard_price, standard_duration_min, is_active, created_at) 
VALUES 
('OIL_CHANGE', 'Замена масла', 'Замена моторного масла', 1500.00, 30, TRUE, NOW()),
('BRAKE_SERVICE', 'Обслуживание тормозов', 'Проверка тормозной системы', 2000.00, 45, TRUE, NOW()),
('DIAGNOSTICS', 'Компьютерная диагностика', 'Полная диагностика', 3000.00, 60, TRUE, NOW());

-- БЛОК 8: Склад
INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) 
VALUES 
('OIL-5W30', 'Масло моторное 5W-30', 1, 'Mobil', 2500.00, 1800.00, TRUE, NOW(), NOW()),
('OIL-FILTER', 'Фильтр масляный', 6, 'Mann', 450.00, 300.00, TRUE, NOW(), NOW()),
('AIR-FILTER', 'Фильтр воздушный', 6, 'Bosch', 380.00, 250.00, TRUE, NOW(), NOW()),
('BRAKE-PAD-F', 'Колодки передние', 5, 'Brembo', 4500.00, 3200.00, TRUE, NOW(), NOW()),
('BRAKE-PAD-R', 'Колодки задние', 5, 'Brembo', 3800.00, 2700.00, TRUE, NOW(), NOW());

INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) 
VALUES 
(1, 50, 5, 10, 100, NOW(), NOW()),
(2, 100, 10, 20, 200, NOW(), NOW()),
(3, 80, 8, 20, 150, NOW(), NOW()),
(4, 30, 2, 10, 50, NOW(), NOW()),
(5, 25, 3, 10, 50, NOW(), NOW());
