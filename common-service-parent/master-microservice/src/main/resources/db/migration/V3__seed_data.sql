-- V3__seed_data.sql
-- Seed data for Master service

-- Insert Masters
INSERT INTO master.masters (employee_code, first_name, last_name, phone, email, specialization, qualification_level, hourly_rate, is_active, hire_date) VALUES
('MSTR001', 'Александр', 'Сергеев', '+7(900)111-11-11', 'alex@mail.ru', 'DIAGNOSTICIAN', 'SENIOR', 2500.00, TRUE, '2018-01-15'),
('MSTR002', 'Дмитрий', 'Козлов', '+7(900)222-22-22', 'dmitry@mail.ru', 'MOTOR_SPECIALIST', 'MASTER', 3000.00, TRUE, '2017-06-01'),
('MSTR003', 'Мария', 'Волкова', '+7(900)333-33-33', 'maria@mail.ru', 'BODY_REPAIRER', 'SENIOR', 2800.00, TRUE, '2019-03-20'),
('MSTR004', 'Николай', 'Морозов', '+7(900)444-44-44', 'nikolay@mail.ru', 'ELECTRICIAN', 'MASTER', 3200.00, TRUE, '2016-09-10'),
('MSTR005', 'Екатерина', 'Соколова', '+7(900)555-55-55', 'ekaterina@mail.ru', 'UNIVERSAL', 'JUNIOR', 1500.00, TRUE, '2022-05-01');

-- Work Schedule (sample schedules for masters)
INSERT INTO master.work_schedule (fk_master, date, start_time, end_time, day_type) VALUES
(1, CURRENT_DATE, '09:00:00', '18:00:00', 'WORKDAY'),
(2, CURRENT_DATE, '09:00:00', '18:00:00', 'WORKDAY'),
(3, CURRENT_DATE, '09:00:00', '18:00:00', 'WORKDAY'),
(4, CURRENT_DATE, '09:00:00', '18:00:00', 'WORKDAY'),
(5, CURRENT_DATE, '09:00:00', '15:00:00', 'WORKDAY');

-- Master Work (work done on claims)
INSERT INTO master.master_work (fk_master, fk_claim, fk_service, start_time, end_time, hours_worked, notes, claim_id, service_id, service_name) VALUES
(2, 2, 1, NOW() - INTERVAL '6 days', NOW() - INTERVAL '5 days 2 hours', 8, 'Замена масла и фильтров', 2, 1, 'Замена масла'),
(1, 1, 7, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day' + INTERVAL '2 hours', 2, 'Диагностика подвески', 1, 7, 'Компьютерная диагностика'),
(3, 5, 9, NOW() - INTERVAL '4 days', NOW() - INTERVAL '3 days 6 hours', 12, 'Осмотр вмятины, подготовка к покраске', 5, 9, 'Кузовной ремонт');
