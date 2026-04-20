-- Шаг 6: NSI - марки авто
INSERT INTO nsi.car_stamp (id, code, name, country, is_active, created_at) 
VALUES (1, 'TOYOTA', 'Toyota', 'Япония', TRUE, NOW());

INSERT INTO nsi.car_stamp (id, code, name, country, is_active, created_at) 
VALUES (2, 'BMW', 'BMW', 'Германия', TRUE, NOW());

INSERT INTO nsi.car_stamp (id, code, name, country, is_active, created_at) 
VALUES (3, 'KIA', 'Kia', 'Южная Корея', TRUE, NOW());

-- Шаг 7: NSI - услуги
INSERT INTO nsi.services (id, code, name, description, standard_price, standard_duration_min, is_active, created_at) 
VALUES (1, 'OIL_CHANGE', 'Замена масла', 'Замена моторного масла', 1500.00, 30, TRUE, NOW());

INSERT INTO nsi.services (id, code, name, description, standard_price, standard_duration_min, is_active, created_at) 
VALUES (2, 'BRAKE_SERVICE', 'Обслуживание тормозов', 'Проверка тормозной системы', 2000.00, 45, TRUE, NOW());