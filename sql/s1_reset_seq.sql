-- Шаг 1: Сброс последовательностей (чтобы id начинались с 1)
ALTER SEQUENCE client.client_id_seq RESTART WITH 1;
ALTER SEQUENCE client.client_car_id_seq RESTART WITH 1;
ALTER SEQUENCE master.masters_id_seq RESTART WITH 1;
ALTER SEQUENCE claim.claims_id_seq RESTART WITH 1;
ALTER SEQUENCE nsi.car_stamp_id_seq RESTART WITH 1;
ALTER SEQUENCE nsi.services_id_seq RESTART WITH 1;
ALTER SEQUENCE warehouse.parts_id_seq RESTART WITH 1;
ALTER SEQUENCE warehouse.inventory_id_seq RESTART WITH 1;

-- Если не найдены, попробуйте такие имена:
-- ALTER SEQUENCE "client.client_id_seq" RESTART WITH 1;
-- и т.д.