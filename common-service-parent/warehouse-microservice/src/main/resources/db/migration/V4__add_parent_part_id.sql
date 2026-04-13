-- V4__add_parent_part_id.sql
-- Add missing parent_part_id column to warehouse.parts table
ALTER TABLE warehouse.parts ADD COLUMN IF NOT EXISTS parent_part_id BIGINT REFERENCES warehouse.parts(id);
