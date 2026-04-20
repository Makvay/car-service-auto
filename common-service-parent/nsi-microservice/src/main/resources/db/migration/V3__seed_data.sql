-- V3__seed_data.sql
-- Seed data for NSI (reference tables)

-- Insert Car Stamps (brands)
INSERT INTO nsi.car_stamp (code, name, country, is_active) VALUES
('TOYOTA', 'Toyota', 'Япония', TRUE),
('HONDA', 'Honda', 'Япония', TRUE),
('BMW', 'BMW', 'Германия', TRUE),
('HYUNDAI', 'Hyundai', 'Южная Корея', TRUE),
('KIA', 'Kia', 'Южная Корея', TRUE),
('VOLKSWAGEN', 'Volkswagen', 'Германия', TRUE),
('MERCEDES', 'Mercedes-Benz', 'Германия', TRUE),
('AUDI', 'Audi', 'Германия', TRUE),
('FORD', 'Ford', 'США', TRUE),
('NISSAN', 'Nissan', 'Япония', TRUE);

-- Insert Car Models
INSERT INTO nsi.car_model (fk_car_stamp, code, name, production_years, vehicle_type, is_active) VALUES
(1, 'CAMRY', 'Camry', '2018-2023', 'SEDAN', TRUE),
(2, 'CIVIC', 'Civic', '2016-2021', 'SEDAN', TRUE),
(3, 'X5', 'X5', '2019-2024', 'SUV', TRUE),
(4, 'SOLARIS', 'Solaris', '2017-2022', 'SEDAN', TRUE),
(5, 'RIO', 'Rio', '2017-2022', 'SEDAN', TRUE),
(6, 'POLO', 'Polo', '2015-2020', 'SEDAN', TRUE),
(1, 'RAV4', 'RAV4', '2019-2024', 'SUV', TRUE),
(3, '3SERIES', '3 Series', '2019-2024', 'SEDAN', TRUE),
(7, 'C_CLASS', 'C-Class', '2018-2023', 'SEDAN', TRUE),
(8, 'A4', 'A4', '2019-2024', 'SEDAN', TRUE);

-- Insert Services
INSERT INTO nsi.services (code, name, description, standard_price, standard_duration_min, is_active, category_id) VALUES
('OIL_CHANGE', 'Замена масла', 'Замена моторного масла и масляного фильтра', 1500.00, 30, TRUE, 1),
('BRAKE_SERVICE', 'Обслуживание тормозов', 'Проверка и обслуживание тормозной системы', 2000.00, 45, TRUE, 1),
('TIRE_SERVICE', 'Шиномонтаж', 'Балансировка и замена шин', 1800.00, 40, TRUE, 2),
('AIR_FILTER', 'Замена воздушного фильтра', 'Замена воздушного фильтра двигателя', 500.00, 15, TRUE, 1),
('COOLANT', 'Замена антифриза', 'Замена охлаждающей жидкости', 2500.00, 60, TRUE, 1),
('BATTERY', 'Обслуживание аккумулятора', 'Диагностика и обслуживание АКБ', 800.00, 20, TRUE, 3),
('DIAGNOSTICS', 'Компьютерная диагностика', 'Полная диагностика автомобиля', 3000.00, 60, TRUE, 3),
('TO', 'Техническое обслуживание', 'Плановое ТО по регламенту', 5000.00, 90, TRUE, 1),
('BODY_REPAIR', 'Кузовной ремонт', 'Ремонт кузовных элементов', 10000.00, 240, TRUE, 4),
('PAINT', 'Покраска', 'Покраска кузовных элементов', 15000.00, 480, TRUE, 4);

-- Insert Part Categories
INSERT INTO nsi.part_categories (code, name, fk_parent_category, is_active, parent_id) VALUES
('ENGINE', 'Двигатель', NULL, TRUE, NULL),
('WHEELS', 'Колёса', NULL, TRUE, NULL),
('ELECTRICAL', 'Электрика', NULL, TRUE, NULL),
('BODY', 'Кузов', NULL, TRUE, NULL),
('BRAKES', 'Тормоза', NULL, TRUE, NULL),
('FILTERS', 'Фильтры', NULL, TRUE, NULL);
