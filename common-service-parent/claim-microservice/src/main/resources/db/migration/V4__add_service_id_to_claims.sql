ALTER TABLE claim.claims
    ADD COLUMN IF NOT EXISTS service_id BIGINT;

UPDATE claim.claims
SET service_id = 1
WHERE service_id IS NULL;

ALTER TABLE claim.claims
    ALTER COLUMN service_id SET NOT NULL;
