CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS revenues (
    id UUID PRIMARY KEY,
    amount DECIMAL(12, 2) NOT NULL,
    source VARCHAR(200) NOT NULL,
    date DATE NOT NULL,
    category_id UUID NOT NULL,
    description VARCHAR(1000),
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_revenue_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT chk_revenue_amount CHECK (amount > 0)
);

CREATE INDEX idx_revenues_date ON revenues(date);
CREATE INDEX idx_revenues_type ON revenues(type);
CREATE INDEX idx_revenues_category ON revenues(category_id);
