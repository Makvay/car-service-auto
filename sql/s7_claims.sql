-- Шаг 8: Заявки
INSERT INTO claim.claims (id, claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) 
VALUES (1, 'CLM-001', 1, 1, 'IN_PROGRESS', 'Замена масла', 50000, 'NORMAL', TRUE, FALSE, NOW(), NOW());

INSERT INTO claim.claims (id, claim_number, fk_client, fk_vehicle, status, problem_description, mileage_at_entry, priority, is_approved, is_paid, created_at, updated_at) 
VALUES (2, 'CLM-002', 2, 2, 'CREATED', 'Диагностика', 30000, 'NORMAL', FALSE, FALSE, NOW(), NOW());