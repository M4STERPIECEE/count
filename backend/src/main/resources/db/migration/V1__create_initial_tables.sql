CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table des catégories
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_type ON categories(type);

-- Table des revenus
CREATE TABLE revenues (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    source VARCHAR(200) NOT NULL,
    date DATE NOT NULL,
    category_id UUID NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
    description TEXT,
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index sur les revenus
CREATE INDEX idx_revenues_date ON revenues(date);
CREATE INDEX idx_revenues_category ON revenues(category_id);
CREATE INDEX idx_revenues_type ON revenues(type);
CREATE INDEX idx_revenues_year_month ON revenues(
    EXTRACT(YEAR FROM date), 
    EXTRACT(MONTH FROM date)
);

-- Table des salaires
CREATE TABLE salaries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    year INTEGER NOT NULL CHECK (year >= 2000),
    employer VARCHAR(200),
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(year, month)
);

-- Index sur les salaires
CREATE INDEX idx_salaries_year ON salaries(year);
CREATE INDEX idx_salaries_year_month ON salaries(year, month);