CREATE TABLE IF NOT EXISTS salaries (
    id UUID PRIMARY KEY,
    amount DECIMAL(12, 2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    employer VARCHAR(200),
    description VARCHAR(500),
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_salary_month CHECK (month BETWEEN 1 AND 12),
    CONSTRAINT chk_salary_year CHECK (year >= 2000),
    CONSTRAINT chk_salary_amount CHECK (amount > 0)
);

CREATE INDEX idx_salaries_year ON salaries(year);
CREATE INDEX idx_salaries_year_month ON salaries(year, month);
CREATE INDEX idx_salaries_employer ON salaries(employer);
