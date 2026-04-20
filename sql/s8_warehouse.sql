-- Шаг 9: Склад - запчасти
INSERT INTO warehouse.parts (id, sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) 
VALUES (1, 'OIL-5W30', 'Масло 5W-30', 1, 'Mobil', 2500.00, 1800.00, TRUE, NOW(), NOW());

INSERT INTO warehouse.parts (id, sku, name, fk_category, brand, unit_price, cost_price, is_active, created_at, updated_at) 
VALUES (2, 'FILTER-OIL', 'Фильтр масляный', 6, 'Mann', 450.00, 300.00, TRUE, NOW(), NOW());

-- Шаг 10: Склад - инвентарь
INSERT INTO warehouse.inventory (id, fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) 
VALUES (1, 1, 50, 5, 10, 100, NOW(), NOW());

INSERT INTO warehouse.inventory (id, fk_part, quantity, reserved_quantity, min_stock_level, max_stock_level, created_at, updated_at) 
VALUES (2, 2, 100, 10, 20, 200, NOW(), NOW());