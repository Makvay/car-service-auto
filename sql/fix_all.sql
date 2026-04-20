-- Fix all database issues for car-service

-- 1. Fix warehouse.parts - add missing column
ALTER TABLE warehouse.parts ADD COLUMN IF NOT EXISTS parent_part_id BIGINT;

-- 2. Fix claim.claims - make nullable columns optional
ALTER TABLE claim.claims ALTER COLUMN client_id DROP NOT NULL;
ALTER TABLE claim.claims ALTER COLUMN vehicle_id DROP NOT NULL;

-- 3. Fix claim status values (map DONE -> COMPLETED)
UPDATE claim.claims SET status = 'COMPLETED' WHERE status = 'DONE';
UPDATE claim.claims SET status = 'IN_PROGRESS' WHERE status = 'INPROGRESS';

-- 4. Clear and reseed claims with correct statuses
DELETE FROM claim.claims;

INSERT INTO claim.claims (claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) VALUES 
('CLM-001', 1, 1, 'CREATED', 'Стук в подвеске', 50000, 'NORMAL', FALSE, FALSE, NOW(), NOW()),
('CLM-002', 2, 2, 'IN_PROGRESS', 'Замена масла', 30000, 'NORMAL', TRUE, FALSE, NOW(), NOW()),
('CLM-003', 3, 3, 'WAITING_PARTS', 'Не работает фонарь', 85000, 'NORMAL', TRUE, FALSE, NOW(), NOW()),
('CLM-004', 4, 4, 'COMPLETED', 'Техническое обслуживание', 15000, 'LOW', TRUE, TRUE, NOW(), NOW()),
('CLM-005', 5, 5, 'CANCELLED', 'Вмятина на крыле', 120000, 'HIGH', FALSE, FALSE, NOW(), NOW());

-- 5. Clear and reseed warehouse parts
DELETE FROM warehouse.inventory;
DELETE FROM warehouse.parts;

INSERT INTO warehouse.parts (sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) VALUES 
('OIL-5W30', 'Масло моторное 5W-30', 1, 'Mobil', 2500.00, 1800.00, TRUE, NOW(), NOW()),
('OIL-FILTER', 'Фильтр масляный', 6, 'Mann', 450.00, 300.00, TRUE, NOW(), NOW()),
('AIR-FILTER', 'Фильтр воздушный', 6, 'Bosch', 380.00, 250.00, TRUE, NOW(), NOW()),
('BRAKE-PAD-F', 'Колодки передние', 5, 'Brembo', 4500.00, 3200.00, TRUE, NOW(), NOW()),
('BRAKE-PAD-R', 'Колодки задние', 5, 'Brembo', 3800.00, 2700.00, TRUE, NOW(), NOW());

INSERT INTO warehouse.inventory (fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) VALUES 
(1, 50, 5, 10, 100, NOW(), NOW()),
(2, 100, 10, 20, 200, NOW(), NOW()),
(3, 80, 8, 20, 150, NOW(), NOW()),
(4, 30, 2, 10, 50, NOW(), NOW()),
(5, 25, 3, 10, 50, NOW(), NOW());
