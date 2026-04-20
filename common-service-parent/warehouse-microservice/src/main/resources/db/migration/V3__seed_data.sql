-- V3__seed_data.sql
-- Seed data for Warehouse

-- Insert Parts
INSERT INTO warehouse.parts (sku, name, description, fk_category, brand, unit_price, cost_price, measurement_unit, is_active) VALUES
('OIL-5W30', 'Масло моторное 5W-30', 'Синтетическое моторное масло 5 литров', 1, 'Mobil', 2500.00, 1800.00, 'PCS', TRUE),
('OIL-FILTER-001', 'Фильтр масляный', 'Масляный фильтр универсальный', 6, 'Mann', 450.00, 300.00, 'PCS', TRUE),
('AIR-FILTER-001', 'Фильтр воздушный', 'Воздушный фильтр двигателя', 6, 'Bosch', 380.00, 250.00, 'PCS', TRUE),
('BRAKE-PAD-F', 'Колодки передние', 'Тормозные колодки передние', 5, 'Brembo', 4500.00, 3200.00, 'PCS', TRUE),
('BRAKE-PAD-R', 'Колодки задние', 'Тормозные колодки задние', 5, 'Brembo', 3800.00, 2700.00, 'PCS', TRUE),
('SPARK-PLUG', 'Свечи зажигания', 'Свечи зажигания комплект 4 шт', 3, 'NGK', 1200.00, 800.00, 'PCS', TRUE),
('BATTERY-75AH', 'Аккумулятор 75Ач', 'Автомобильный аккумулятор 75Ач', 3, 'Varta', 6500.00, 5000.00, 'PCS', TRUE),
('TIRE-205-55-R16', 'Шина 205/55R16', 'Зимняя шина 205/55R16', 2, 'Nokian', 8000.00, 6000.00, 'PCS', TRUE),
('COOLANT-5L', 'Антифриз 5л', 'Антифриз G12 красный 5 литров', 1, 'Liqui Moly', 1500.00, 1000.00, 'PCS', TRUE),
('WIPER-BLADE', 'Дворники', 'Щётки стеклоочистителя комплект', 3, 'Bosch', 1200.00, 800.00, 'PCS', TRUE);

-- Insert Inventory
INSERT INTO warehouse.inventory (fk_part, location_id, quantity, reserved_quantity, min_stock_level, max_stock_level, batch_number) VALUES
(1, 1, 50, 5, 10, 100, 'BATCH-001'),
(2, 1, 100, 10, 20, 200, 'BATCH-002'),
(3, 1, 80, 8, 20, 150, 'BATCH-003'),
(4, 2, 30, 2, 10, 50, 'BATCH-004'),
(5, 2, 25, 3, 10, 50, 'BATCH-005'),
(6, 3, 60, 6, 15, 100, 'BATCH-006'),
(7, 3, 15, 1, 5, 30, 'BATCH-007'),
(8, 4, 40, 4, 10, 80, 'BATCH-008'),
(9, 1, 35, 0, 10, 60, 'BATCH-009'),
(10, 3, 50, 5, 15, 80, 'BATCH-010');

-- Insert Reservations
INSERT INTO warehouse.reservations (reservation_number, fk_claim, fk_part, quantity, status, reserved_by, notes) VALUES
('RES-001', 1, 1, 1, 'RESERVED', 1, 'Резерв масла для заявки CLM-2024-0001'),
('RES-002', 1, 2, 1, 'RESERVED', 1, 'Резерв масляного фильтра'),
('RES-003', 3, 4, 1, 'RESERVED', 4, 'Резерв передних колодок');
