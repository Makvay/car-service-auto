ALTER TABLE claim.claims
    ALTER COLUMN fk_client DROP NOT NULL;

ALTER TABLE claim.claims
    ALTER COLUMN fk_vehicle DROP NOT NULL;

UPDATE claim.claims
SET fk_client = client_id
WHERE fk_client IS NULL AND client_id IS NOT NULL;

UPDATE claim.claims
SET fk_vehicle = vehicle_id
WHERE fk_vehicle IS NULL AND vehicle_id IS NOT NULL;

UPDATE claim.claims
SET fk_master = master_id
WHERE fk_master IS NULL AND master_id IS NOT NULL;
