-- V2__init_tables.sql
CREATE TABLE IF NOT EXISTS claim.claims (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    claim_number VARCHAR(255) NOT NULL,
    fk_client BIGINT NOT NULL,
    fk_vehicle BIGINT NOT NULL,
    fk_master BIGINT,
    status VARCHAR(255) DEFAULT 'CREATED',
    problem_description TEXT NOT NULL,
    customer_notes TEXT,
    internal_notes TEXT,
    mileage_at_entry INTEGER NOT NULL,
    priority VARCHAR(255) DEFAULT 'NORMAL',
    is_approved BOOLEAN DEFAULT FALSE,
    is_paid BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    scheduled_date DATE,
    diagnosis_date TIMESTAMP,
    start_date TIMESTAMP,
    completion_date TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT NOT NULL,
    master_id BIGINT,
    vehicle_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS claim.claim_status_history (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_claim BIGINT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    changed_by BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS claim.claim_work_items (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_claim BIGINT NOT NULL,
    fk_service BIGINT,
    description TEXT,
    quantity INTEGER DEFAULT 1,
    unit_price NUMERIC(10,2),
    total_price NUMERIC(10,2),
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actual_hours INTEGER,
    completed_at TIMESTAMP,
    completed_by BIGINT,
    estimated_hours INTEGER,
    hourly_rate DOUBLE PRECISION,
    service_id BIGINT,
    service_name VARCHAR(200),
    status VARCHAR(50),
    total_cost DOUBLE PRECISION,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS claim.claim_parts (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_claim BIGINT NOT NULL,
    fk_part BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    part_code VARCHAR(100),
    part_id BIGINT,
    part_name VARCHAR(200),
    reserved_at TIMESTAMP,
    returned_at TIMESTAMP,
    status VARCHAR(50),
    used_at TIMESTAMP
);
