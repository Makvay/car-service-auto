-- V2__init_tables.sql
CREATE TABLE IF NOT EXISTS notification.notifications (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fk_template BIGINT,
    recipient_type VARCHAR(50) NOT NULL,
    recipient_id BIGINT,
    recipient_address VARCHAR(500) NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    subject VARCHAR(500),
    message TEXT NOT NULL,
    metadata JSONB,
    status VARCHAR(20) DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    delivery_confirmed_at TIMESTAMP,
    failure_reason TEXT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notification.notification_templates (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    template_type VARCHAR(50) NOT NULL,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    subject VARCHAR(500),
    body TEXT NOT NULL,
    body_html TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    priority INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
