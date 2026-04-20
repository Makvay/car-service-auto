CREATE TABLE IF NOT EXISTS warehouse.supplies (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    supply_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_name VARCHAR(255),
    supply_date DATE NOT NULL,
    total_cost DOUBLE PRECISION,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    received_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse.supply_items (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    supply_id BIGINT NOT NULL,
    part_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_cost DOUBLE PRECISION,
    total_cost DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_schema = 'warehouse'
          AND table_name = 'supply_items'
          AND constraint_name = 'fk_supply_items_supply'
    ) THEN
        ALTER TABLE warehouse.supply_items
            ADD CONSTRAINT fk_supply_items_supply
            FOREIGN KEY (supply_id) REFERENCES warehouse.supplies(id) ON DELETE CASCADE;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_schema = 'warehouse'
          AND table_name = 'supply_items'
          AND constraint_name = 'fk_supply_items_part'
    ) THEN
        ALTER TABLE warehouse.supply_items
            ADD CONSTRAINT fk_supply_items_part
            FOREIGN KEY (part_id) REFERENCES warehouse.parts(id);
    END IF;
END $$;

