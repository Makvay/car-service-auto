-- V3__seed_data.sql
-- Seed data for Client service only

-- Insert Clients
INSERT INTO client.client (email, first_name, last_name, phone, status) VALUES
('u.artemiy@bk.ru', 'Артемий', 'Маков', '+7(999)000-00-00', 'ACTIVE'),
('ivanov@mail.ru', 'Иван', 'Иванов', '+7(999)111-11-11', 'ACTIVE'),
('petrova@mail.ru', 'Елена', 'Петрова', '+7(999)222-22-22', 'ACTIVE'),
('sidorov@mail.ru', 'Пётр', 'Сидоров', '+7(999)333-33-33', 'ACTIVE'),
('smirnova@mail.ru', 'Анна', 'Смирнова', '+7(999)444-44-44', 'ACTIVE'),
('kuznetsov@mail.ru', 'Сергей', 'Кузнецов', '+7(999)555-55-55', 'ACTIVE');

-- Insert Client Cars
INSERT INTO client.client_car (fk_client, fk_car_stamp, model, vin, license_plate, year, mileage, color, registration_date, brand_id, brand_name, model_id) VALUES
(1, NULL, 'Toyota Camry', 'XW8AKAFG123456789', 'А777АА77', 2020, 50000, 'Чёрный', '2020-03-15', 1, 'Toyota', 1),
(2, NULL, 'Honda Civic', 'XW8AKAFG987654321', 'В555ВВ77', 2018, 75000, 'Серебристый', '2018-06-20', 2, 'Honda', 2),
(3, NULL, 'BMW X5', 'XW8AKAFG456123789', 'С333СС77', 2021, 30000, 'Белый', '2021-01-10', 3, 'BMW', 3),
(4, NULL, 'Hyundai Solaris', 'XW8AKAFG789456123', 'Е444ЕЕ77', 2019, 85000, 'Синий', '2019-09-05', 4, 'Hyundai', 4),
(5, NULL, 'Kia Rio', 'XW8AKAFG321654987', 'К666КК77', 2022, 15000, 'Красный', '2022-02-28', 5, 'Kia', 5),
(6, NULL, 'Volkswagen Polo', 'XW8AKAFG654321789', 'М888ММ77', 2017, 120000, 'Серый', '2017-11-12', 6, 'Volkswagen', 6);
