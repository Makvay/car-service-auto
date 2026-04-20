-- Шаг 5: Мастера
INSERT INTO master.masters (id, employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES (1, 'M001', 'Александр', 'Сергеев', '+79001112233', 'alex@test.ru', 'DIAGNOSTICIAN', 'SENIOR', 2500.00, TRUE, DATE '2018-01-15', NOW(), NOW());

INSERT INTO master.masters (id, employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES (2, 'M002', 'Дмитрий', 'Козлов', '+79002223344', 'dmitry@test.ru', 'MOTOR_SPECIALIST', 'MIDDLE', 2000.00, TRUE, DATE '2019-06-01', NOW(), NOW());

INSERT INTO master.masters (id, employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date, created_at, updated_at) 
VALUES (3, 'M003', 'Мария', 'Волкова', '+79003334455', 'maria@test.ru', 'BODY_REPAIRER', 'SENIOR', 2800.00, TRUE, DATE '2020-03-20', NOW(), NOW());