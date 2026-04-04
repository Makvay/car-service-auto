-- V2__init_tables.sql
CREATE TABLE IF NOT EXISTS warehouse.parts (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    sku VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    fk_category BIGINT NOT NULL,
    brand VARCHAR(100),
    unit_price NUMERIC(10,2) NOT NULL,
    cost_price NUMERIC(10,2) NOT NULL,
    measurement_unit VARCHAR(20) DEFAULT 'PCS',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse.inventory (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_part BIGINT NOT NULL,
    location_id BIGINT,
    quantity INTEGER DEFAULT 0 NOT NULL,
    reserved_quantity INTEGER DEFAULT 0 NOT NULL,
    min_stock_level INTEGER DEFAULT 5,
    max_stock_level INTEGER DEFAULT 100,
    batch_number VARCHAR(100),
    expiration_date DATE,
    last_restocked TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse.reservations (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    reservation_number VARCHAR(50) NOT NULL,
    fk_claim BIGINT NOT NULL,
    fk_part BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'RESERVED',
    reserved_by BIGINT,
    reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    used_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    notes TEXT
);
