-- V2__init_tables.sql
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

CREATE TABLE IF NOT EXISTS master.work_schedule (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_master BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    day_type VARCHAR(20) DEFAULT 'WORKDAY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS master.master_work (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_master BIGINT NOT NULL,
    fk_claim BIGINT NOT NULL,
    fk_service BIGINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    hours_worked DOUBLE PRECISION,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    claim_id BIGINT,
    service_id BIGINT,
    service_name VARCHAR(200),
    updated_at TIMESTAMP
);
