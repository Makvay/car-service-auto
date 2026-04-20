
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;
DELETE FROM claim.claims;
DELETE FROM claim.claim_work_items;
DELETE FROM claim.claim_parts;
DELETE FROM claim.claim_status_history;
DELETE FROM client.client_car;
DELETE FROM client.client_history;
DELETE FROM client.client;
DELETE FROM master.master_work;
DELETE FROM master.work_schedule;
DELETE FROM master.masters;
DELETE FROM notification.notifications;
DELETE FROM notification.notification_templates;
DELETE FROM nsi.services;
DELETE FROM nsi.part_categories;
DELETE FROM nsi.car_model;
DELETE FROM nsi.car_stamp;

-- 2. Исправляем дублирующиеся колонки в claims (убираем NOT NULL)
ALTER TABLE claim.claims ALTER COLUMN client_id DROP NOT NULL;
ALTER TABLE claim.claims ALTER COLUMN vehicle_id DROP NOT NULL;

-- 3. Вставляем клиентов
INSERT INTO client.client (created_at, email, first_name, last_name, phone, status, updated_at) VALUES 
(NOW(), 'ivanov@mail.ru', 'Иван', 'Иванов', '+7(999)111-11-11', 'ACTIVE', NOW()),
(NOW(), 'petrova@mail.ru', 'Елена', 'Петрова', '+7(999)222-22-22', 'ACTIVE', NOW()),
(NOW(), 'sidorov@mail.ru', 'Пётр', 'Сидоров', '+7(999)333-33-33', 'ACTIVE', NOW()),
(NOW(), 'smirnova@mail.ru', 'Анна', 'Смирнова', '+7(999)444-44-44', 'ACTIVE', NOW()),
(NOW(), 'kuznetsov@mail.ru', 'Сергей', 'Кузнецов', '+7(999)555-55-55', 'ACTIVE', NOW());

-- 4. Вставляем машины клиентов
INSERT INTO client.client_car (fk_client, model, vin, license_plate, year, mileage, color, created_at, brand_name) VALUES 
(1, 'Toyota Camry', 'VIN001', 'А777АА77', 2020, 50000, 'Чёрный', NOW(), 'Toyota'),
(1, 'Honda Civic', 'VIN002', 'В555ВВ77', 2018, 75000, 'Серебристый', NOW(), 'Honda'),
(2, 'BMW X5', 'VIN003', 'С333СС77', 2021, 30000, 'Белый', NOW(), 'BMW'),
(3, 'Hyundai Solaris', 'VIN004', 'Е444ЕЕ77', 2019, 85000, 'Синий', NOW(), 'Hyundai'),
(4, 'Kia Rio', 'VIN005', 'К666КК77', 2022, 15000, 'Красный', NOW(), 'Kia'),
(5, 'Volkswagen Polo', 'VIN006', 'М888ММ77', 2017, 120000, 'Серый', NOW(), 'Volkswagen');

-- 5. Вставляем мастеров
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) VALUES 
('MSTR001', 'Александр', 'Сергеев', '+7(900)111-11-11', 'alex@mail.ru', 'DIAGNOSTIC', 'SENIOR', 2500.00, TRUE, '2018-01-15', NOW(), NOW()),
('MSTR002', 'Дмитрий', 'Козлов', '+7(900)222-22-22', 'dmitry@mail.ru', 'ENGINE', 'MASTER', 3000.00, TRUE, '2017-06-01', NOW(), NOW()),
('MSTR003', 'Мария', 'Волкова', '+7(900)333-33-33', 'maria@mail.ru', 'BODY', 'SENIOR', 2800.00, TRUE, '2019-03-20', NOW(), NOW()),
('MSTR004', 'Николай', 'Морозов', '+7(900)444-44-44', 'nikolay@mail.ru', 'ELECTRIC', 'MASTER', 3200.00, TRUE, '2016-09-10', NOW(), NOW()),
('MSTR005', 'Екатерина', 'Соколова', '+7(900)555-55-55', 'ekaterina@mail.ru', 'GENERAL', 'JUNIOR', 1500.00, TRUE, '2022-05-01', NOW(), NOW());

-- 6. NSI - бренды авто
INSERT INTO nsi.car_stamp (code, name, country, is_active, created_at) VALUES 
('TOYOTA', 'Toyota', 'Япония', TRUE, NOW()),
('HONDA', 'Honda', 'Япония', TRUE, NOW()),
('BMW', 'BMW', 'Германия', TRUE, NOW()),
('HYUNDAI', 'Hyundai', 'Южная Корея', TRUE, NOW()),
('KIA', 'Kia', 'Южная Корея', TRUE, NOW()),
('VOLKSWAGEN', 'Volkswagen', 'Германия', TRUE, NOW()),
('MERCEDES', 'Mercedes-Benz', 'Германия', TRUE, NOW()),
('AUDI', 'Audi', 'Германия', TRUE, NOW()),
('FORD', 'Ford', 'США', TRUE, NOW()),
('NISSAN', 'Nissan', 'Япония', TRUE, NOW());

-- 7. NSI - услуги
INSERT INTO nsi.services (code, name, description, standard_price, standard_duration_min, is_active, created_at, category_id) VALUES 
('OIL_CHANGE', 'Замена масла', 'Замена моторного масла и масляного фильтра', 1500.00, 30, TRUE, NOW(), 1),
('BRAKE_SERVICE', 'Обслуживание тормозов', 'Проверка и обслуживание тормозной системы', 2000.00, 45, TRUE, NOW(), 1),
('TIRE_SERVICE', 'Шиномонтаж', 'Балансировка и замена шин', 1800.00, 40, TRUE, NOW(), 2),
('AIR_FILTER', 'Замена воздушного фильтра', 'Замена воздушного фильтра двигателя', 500.00, 15, TRUE, NOW(), 1),
('COOLANT', 'Замена антифриза', 'Замена охлаждающей жидкости', 2500.00, 60, TRUE, NOW(), 1),
('BATTERY', 'Обслуживание аккумулятора', 'Диагностика и обслуживание АКБ', 800.00, 20, TRUE, NOW(), 3),
('DIAGNOSTICS', 'Компьютерная диагностика', 'Полная диагностика автомобиля', 3000.00, 60, TRUE, NOW(), 3),
('TO', 'Техническое обслуживание', 'Плановое ТО по регламенту', 5000.00, 90, TRUE, NOW(), 1),
('BODY_REPAIR', 'Кузовной ремонт', 'Ремонт кузовных элементов', 10000.00, 240, TRUE, NOW(), 4),
('PAINT', 'Покраска', 'Покраска кузовных элементов', 15000.00, 480, TRUE, NOW(), 4);

-- 8. NSI - категории запчастей
INSERT INTO nsi.part_categories (code, name, is_active, created_at) VALUES 
('ENGINE', 'Двигатель', TRUE, NOW()),
('WHEELS', 'Колёса', TRUE, NOW()),
('ELECTRICAL', 'Электрика', TRUE, NOW()),
('BODY', 'Кузов', TRUE, NOW()),
('BRAKES', 'Тормоза', TRUE, NOW()),
('FILTERS', 'Фильтры', TRUE, NOW());

-- 9. Заявки (только нужные поля - без client_id, vehicle_id)
INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) VALUES 
('CLM-2024-0001', 1, 1, 'IN_PROGRESS', 'Стук в подвеске при движении по неровностям', 50000, 'NORMAL', TRUE, FALSE, NOW(), NOW()),
('CLM-2024-0002', 2, 3, 'DONE', 'Замена масла и фильтров', 30000, 'NORMAL', TRUE, TRUE, NOW() - INTERVAL '7 days', NOW()),
('CLM-2024-0003', 3, 4, 'WAITING_PARTS', 'Не работает фонарь заднего хода', 85000, 'NORMAL', TRUE, FALSE, NOW() - INTERVAL '2 days', NOW()),
('CLM-2024-0004', 4, 5, 'CREATED', 'Техническое обслуживание', 15000, 'LOW', FALSE, FALSE, NOW(), NOW()),
('CLM-2024-0005', 5, 6, 'IN_PROGRESS', 'Вмятина на крыле', 120000, 'HIGH', TRUE, TRUE, NOW() - INTERVAL '5 days', NOW());

-- 10. Склад - запчасти
INSERT INTO warehouse.parts (sku, name, description, fk_category, brand, unit_price, cost_price, measurement_unit, is_active, created_at, updated_at) VALUES 
('OIL-5W30', 'Масло моторное 5W-30', 'Синтетическое моторное масло 5 литров', 1, 'Mobil', 2500.00, 1800.00, 'PCS', TRUE, NOW(), NOW()),
('OIL-FILTER-001', 'Фильтр масляный', 'Масляный фильтр универсальный', 6, 'Mann', 450.00, 300.00, 'PCS', TRUE, NOW(), NOW()),
('AIR-FILTER-001', 'Фильтр воздушный', 'Воздушный фильтр двигателя', 6, 'Bosch', 380.00, 250.00, 'PCS', TRUE, NOW(), NOW()),
('BRAKE-PAD-F', 'Колодки передние', 'Тормозные колодки передние', 5, 'Brembo', 4500.00, 3200.00, 'PCS', TRUE, NOW(), NOW()),
('BRAKE-PAD-R', 'Колодки задние', 'Тормозные колодки задние', 5, 'Brembo', 3800.00, 2700.00, 'PCS', TRUE, NOW(), NOW()),
('SPARK-PLUG', 'Свечи зажигания', 'Свечи зажигания комплект 4 шт', 3, 'NGK', 1200.00, 800.00, 'PCS', TRUE, NOW(), NOW()),
('BATTERY-75AH', 'Аккумулятор 75Ач', 'Автомобильный аккумулятор 75Ач', 3, 'Varta', 6500.00, 5000.00, 'PCS', TRUE, NOW(), NOW()),
('TIRE-205-55-R16', 'Шина 205/55R16', 'Зимняя шина 205/55R16', 2, 'Nokian', 8000.00, 6000.00, 'PCS', TRUE, NOW(), NOW()),
('COOLANT-5L', 'Антифриз 5л', 'Антифриз G12 красный 5 литров', 1, 'Liqui Moly', 1500.00, 1000.00, 'PCS', TRUE, NOW(), NOW()),
('WIPER-BLADE', 'Дворники', 'Щётки стеклоочистителя комплект', 3, 'Bosch', 1200.00, 800.00, 'PCS', TRUE, NOW(), NOW());

-- 11. Склад - остатки
INSERT INTO warehouse.inventory (fk_part, location_id, quantity, reserved_quantity, min_stock_level, max_stock_level, batch_number, last_restocked, created_at, updated_at) VALUES 
(1, 1, 50, 5, 10, 100, 'BATCH-001', NOW(), NOW(), NOW()),
(2, 1, 100, 10, 20, 200, 'BATCH-002', NOW(), NOW(), NOW()),
(3, 1, 80, 8, 20, 150, 'BATCH-003', NOW(), NOW(), NOW()),
(4, 2, 30, 2, 10, 50, 'BATCH-004', NOW(), NOW(), NOW()),
(5, 2, 25, 3, 10, 50, 'BATCH-005', NOW(), NOW(), NOW()),
(6, 3, 60, 6, 15, 100, 'BATCH-006', NOW(), NOW(), NOW()),
(7, 3, 15, 1, 5, 30, 'BATCH-007', NOW(), NOW(), NOW()),
(8, 4, 40, 4, 10, 80, 'BATCH-008', NOW(), NOW(), NOW()),
(9, 1, 35, 0, 10, 60, 'BATCH-009', NOW(), NOW(), NOW()),
(10, 3, 50, 5, 15, 80, 'BATCH-010', NOW(), NOW(), NOW());

-- 12. Уведомления
INSERT INTO notification.notification_templates (template_type, code, name, subject, body, is_active, priority, created_at) VALUES 
('EMAIL', 'CLAIM_CREATED', 'Заявка создана', 'Ваша заявка #{{claimNumber}} создана', 'Уважаемый клиент! Ваша заявка на обслуживание #{{claimNumber}} успешно создана.', TRUE, 1, NOW()),
('EMAIL', 'CLAIM_STATUS', 'Изменение статуса заявки', 'Статус вашей заявки #{{claimNumber}} изменён', 'Уважаемый клиент! Статус вашей заявки #{{claimNumber}} изменён на: {{newStatus}}', TRUE, 1, NOW()),
('EMAIL', 'CLAIM_DONE', 'Заявка выполнена', 'Ваша заявка #{{claimNumber}} выполнена', 'Уважаемый клиент! Ваша заявка #{{claimNumber}} выполнена. Вы можете забрать автомобиль.', TRUE, 1, NOW()),
('SMS', 'REMINDER', 'Напоминание о ТО', 'Напоминание о ТО', 'Напоминаем, что подошло время технического обслуживания вашего автомобиля.', TRUE, 2, NOW());
