-- Vue pour les rapports mensuels
CREATE OR REPLACE VIEW monthly_revenue_report AS
SELECT 
    EXTRACT(YEAR FROM date) AS year,
    EXTRACT(MONTH FROM date) AS month,
    COUNT(*) AS transaction_count,
    SUM(amount) AS total_amount,
    AVG(amount) AS average_amount,
    MIN(amount) AS min_amount,
    MAX(amount) AS max_amount
FROM revenues
GROUP BY EXTRACT(YEAR FROM date), EXTRACT(MONTH FROM date)
ORDER BY year DESC, month DESC;

-- Vue pour les rapports annuels
CREATE OR REPLACE VIEW annual_revenue_report AS
SELECT 
    EXTRACT(YEAR FROM date) AS year,
    COUNT(*) AS transaction_count,
    SUM(amount) AS total_amount,
    AVG(amount) AS average_amount,
    SUM(amount) / 12 AS monthly_average
FROM revenues
GROUP BY EXTRACT(YEAR FROM date)
ORDER BY year DESC;

-- Vue pour le rapport complet
CREATE OR REPLACE VIEW full_financial_report AS
SELECT 
    r.year,
    r.month,
    r.total_amount AS total_revenue,
    COALESCE(s.amount, 0) AS salary_amount,
    r.total_amount - COALESCE(s.amount, 0) AS net_amount,
    r.transaction_count AS revenue_count
FROM monthly_revenue_report r
LEFT JOIN salaries s ON r.year = s.year AND r.month = s.month
ORDER BY r.year DESC, r.month DESC;

-- Fonction pour calculer le taux de croissance
CREATE OR REPLACE FUNCTION calculate_growth_rate(
    current_amount DECIMAL,
    previous_amount DECIMAL
) RETURNS DECIMAL AS $$
BEGIN
    IF previous_amount = 0 THEN
        RETURN 0;
    END IF;
    RETURN ROUND(((current_amount - previous_amount) / previous_amount) * 100, 2);
END;
$$ LANGUAGE plpgsql;