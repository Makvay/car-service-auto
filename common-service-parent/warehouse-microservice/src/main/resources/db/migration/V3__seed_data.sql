-- V3__seed_data.sql
-- Seed data for Warehouse

-- Insert Parts
INSERT INTO warehouse.parts (id, sku, name, description, fk_category, brand, unit_price, cost_price, measurement_unit, is_active, created_at, updated_at) VALUES
(1, 'OIL-5W30', 'Масло моторное 5W-30', 'Синтетическое моторное масло 5 литров', 1, 'Mobil', 2500.00, 1800.00, 'PCS', TRUE, NOW(), NOW()),
(2, 'OIL-FILTER-001', 'Фильтр масляный', 'Масляный фильтр универсальный', 6, 'Mann', 450.00, 300.00, 'PCS', TRUE, NOW(), NOW()),
(3, 'AIR-FILTER-001', 'Фильтр воздушный', 'Воздушный фильтр двигателя', 6, 'Bosch', 380.00, 250.00, 'PCS', TRUE, NOW(), NOW()),
(4, 'BRAKE-PAD-F', 'Колодки передние', 'Тормозные колодки передние', 5, 'Brembo', 4500.00, 3200.00, 'PCS', TRUE, NOW(), NOW()),
(5, 'BRAKE-PAD-R', 'Колодки задние', 'Тормозные колодки задние', 5, 'Brembo', 3800.00, 2700.00, 'PCS', TRUE, NOW(), NOW()),
(6, 'SPARK-PLUG', 'Свечи зажигания', 'Свечи зажигания комплект 4 шт', 3, 'NGK', 1200.00, 800.00, 'PCS', TRUE, NOW(), NOW()),
(7, 'BATTERY-75AH', 'Аккумулятор 75Ач', 'Автомобильный аккумулятор 75Ач', 3, 'Varta', 6500.00, 5000.00, 'PCS', TRUE, NOW(), NOW()),
(8, 'TIRE-205-55-R16', 'Шина 205/55R16', 'Зимняя шина 205/55R16', 2, 'Nokian', 8000.00, 6000.00, 'PCS', TRUE, NOW(), NOW()),
(9, 'COOLANT-5L', 'Антифриз 5л', 'Антифриз G12 красный 5 литров', 1, 'Liqui Moly', 1500.00, 1000.00, 'PCS', TRUE, NOW(), NOW()),
(10, 'WIPER-BLADE', 'Дворники', 'Щётки стеклоочистителя комплект', 3, 'Bosch', 1200.00, 800.00, 'PCS', TRUE, NOW(), NOW());

-- Insert Inventory
INSERT INTO warehouse.inventory (id, fk_part, location_id, quantity, reserved_quantity, min_stock_level, max_stock_level, batch_number, expiration_date, last_restocked, created_at, updated_at) VALUES
(1, 1, 1, 50, 5, 10, 100, 'BATCH-001', NULL, NOW(), NOW(), NOW()),
(2, 2, 1, 100, 10, 20, 200, 'BATCH-002', NULL, NOW(), NOW(), NOW()),
(3, 3, 1, 80, 8, 20, 150, 'BATCH-003', NULL, NOW(), NOW(), NOW()),
(4, 4, 2, 30, 2, 10, 50, 'BATCH-004', NULL, NOW(), NOW(), NOW()),
(5, 5, 2, 25, 3, 10, 50, 'BATCH-005', NULL, NOW(), NOW(), NOW()),
(6, 6, 3, 60, 6, 15, 100, 'BATCH-006', NULL, NOW(), NOW(), NOW()),
(7, 7, 3, 15, 1, 5, 30, 'BATCH-007', NULL, NOW(), NOW(), NOW()),
(8, 8, 4, 40, 4, 10, 80, 'BATCH-008', NULL, NOW(), NOW(), NOW()),
(9, 9, 1, 35, 0, 10, 60, 'BATCH-009', NULL, NOW(), NOW(), NOW()),
(10, 10, 3, 50, 5, 15, 80, 'BATCH-010', NULL, NOW(), NOW(), NOW());

-- Insert Reservations
INSERT INTO warehouse.reservations (id, reservation_number, fk_claim, fk_part, quantity, status, reserved_by, reserved_at, used_at, cancelled_at, notes) VALUES
(1, 'RES-001', 1, 1, 1, 'RESERVED', 1, NOW(), NULL, NULL, 'Резерв масла для заявки CLM-2024-0001'),
(2, 'RES-002', 1, 2, 1, 'RESERVED', 1, NOW(), NULL, NULL, 'Резерв масляного фильтра'),
(3, 'RES-003', 3, 4, 1, 'RESERVED', 4, NOW(), NULL, NULL, 'Резерв передних колодок');
