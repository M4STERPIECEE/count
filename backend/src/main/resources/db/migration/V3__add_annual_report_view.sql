CREATE OR REPLACE VIEW annual_revenue_summary AS
SELECT
    r.year AS year,
    r.month AS month,
    r.transaction_count,
    r.total_revenue,
    COALESCE(s.total_salary, 0) AS total_salary
FROM (
    SELECT
        EXTRACT(YEAR FROM date)::int AS year,
        EXTRACT(MONTH FROM date)::int AS month,
        COUNT(id) AS transaction_count,
        SUM(amount) AS total_revenue
    FROM revenues
    GROUP BY
        EXTRACT(YEAR FROM date),
        EXTRACT(MONTH FROM date)
) r
LEFT JOIN (
    SELECT
        year,
        month,
        SUM(amount) AS total_salary
    FROM salaries
    GROUP BY year, month
) s ON r.year = s.year AND r.month = s.month
ORDER BY year DESC, month DESC;
