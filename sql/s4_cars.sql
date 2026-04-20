-- Шаг 4: Машины
INSERT INTO client.client_car (id, fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
VALUES (1, 1, 'Toyota Camry', 'VIN11111111111111', 'А777АА', 2020, 50000, 'Чёрный', 'Toyota', NOW());

INSERT INTO client.client_car (id, fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
VALUES (2, 2, 'BMW X5', 'VIN22222222222222', 'В888ВВ', 2021, 30000, 'Белый', 'BMW', NOW());

INSERT INTO client.client_car (id, fk_client, model, vin, license_plate, year, mileage, color, brand_name, created_at) 
VALUES (3, 3, 'Kia Rio', 'VIN33333333333333', 'К999КК', 2022, 15000, 'Красный', 'Kia', NOW());