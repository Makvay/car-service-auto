-- V2__init_tables.sql
CREATE TABLE IF NOT EXISTS client.client (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    created_at TIMESTAMP,
    email VARCHAR(255),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS client.client_car (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_client BIGINT NOT NULL,
    fk_car_stamp BIGINT,
    model VARCHAR(100),
    vin VARCHAR(17) NOT NULL,
    license_plate VARCHAR(20),
    year INTEGER,
    mileage INTEGER DEFAULT 0,
    color VARCHAR(50),
    registration_date DATE,
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    brand_id BIGINT,
    brand_name VARCHAR(100),
    model_id BIGINT
);

CREATE TABLE IF NOT EXISTS client.client_history (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_client BIGINT NOT NULL,
    fk_vehicle BIGINT,
    event_type VARCHAR(50) NOT NULL,
    description TEXT,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT,
    vehicle_id BIGINT
);
