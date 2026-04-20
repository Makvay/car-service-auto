-- V4__add_master_specializations.sql
-- Добавляем таблицу для множественных специализаций мастера

CREATE TABLE IF NOT EXISTS master.master_specializations (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    master_id BIGINT NOT NULL,
    specialization VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(master_id, specialization)
);

-- Добавляем внешний ключ
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'fk_master_specializations_master'
    ) THEN
        ALTER TABLE master.master_specializations 
        ADD CONSTRAINT fk_master_specializations_master 
        FOREIGN KEY (master_id) REFERENCES master.masters(id) ON DELETE CASCADE;
    END IF;
END $$;

-- Переносим существующие специализации из таблицы masters
INSERT INTO master.master_specializations (master_id, specialization)
SELECT id, specialization 
FROM master.masters 
WHERE specialization IS NOT NULL
ON CONFLICT DO NOTHING;

-- Делаем колонку specialization в таблице masters nullable (она больше не нужна)
ALTER TABLE master.masters ALTER COLUMN specialization DROP NOT NULL;