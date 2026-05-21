CREATE OR REPLACE VIEW annual_revenue_summary AS
SELECT
    EXTRACT(YEAR FROM r.date) AS year,
    EXTRACT(MONTH FROM r.date) AS month,
    COUNT(r.id) AS transaction_count,
    SUM(r.amount) AS total_revenue,
    COALESCE(SUM(s.amount), 0) AS total_salary
FROM revenues r
LEFT JOIN salaries s ON EXTRACT(YEAR FROM s.date) = EXTRACT(YEAR FROM r.date)
    AND EXTRACT(MONTH FROM s.date) = EXTRACT(MONTH FROM r.date)
GROUP BY
    EXTRACT(YEAR FROM r.date),
    EXTRACT(MONTH FROM r.date)
ORDER BY year DESC, month DESC;
