-- Создание всех таблиц вручную

-- 1. master.masters
CREATE TABLE IF NOT EXISTS master.masters (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    employee_code VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    qualification_level VARCHAR(50),
    hourly_rate DOUBLE PRECISION NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    hire_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. client.client
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

-- 3. client.client_car
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

-- 4. claim.claims
CREATE TABLE IF NOT EXISTS claim.claims (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    claim_number VARCHAR(50) NOT NULL,
    fk_client BIGINT,
    fk_vehicle BIGINT,
    fk_master BIGINT,
    status VARCHAR(50) NOT NULL,
    problem_description TEXT,
    mileage_at_entry INTEGER,
    priority VARCHAR(20),
    is_approved BOOLEAN DEFAULT FALSE,
    is_paid BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. warehouse.parts
CREATE TABLE IF NOT EXISTS warehouse.parts (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    sku VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    fk_category BIGINT,
    brand VARCHAR(100),
    unit_price DOUBLE PRECISION NOT NULL,
    cost_price DOUBLE PRECISION NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    parent_part_id BIGINT
);

-- 6. warehouse.inventory
CREATE TABLE IF NOT EXISTS warehouse.inventory (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_part BIGINT NOT NULL,
    quantity INTEGER DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    min_stock_level INTEGER DEFAULT 0,
    max_stock_level INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. nsi.car_stamp
CREATE TABLE IF NOT EXISTS nsi.car_stamp (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. nsi.services
CREATE TABLE IF NOT EXISTS nsi.services (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    standard_price DOUBLE PRECISION NOT NULL,
    standard_duration_min INTEGER NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. notification.notifications (если нужна)
CREATE TABLE IF NOT EXISTS notification.notifications (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    recipient_type VARCHAR(50) NOT NULL,
    recipient_id BIGINT,
    recipient_address VARCHAR(255) NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);