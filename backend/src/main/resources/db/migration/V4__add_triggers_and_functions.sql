CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_revenues_updated_at 
    BEFORE UPDATE ON revenues 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_salaries_updated_at 
    BEFORE UPDATE ON salaries 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE OR REPLACE FUNCTION check_duplicate_salary()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM salaries 
        WHERE year = NEW.year 
        AND month = NEW.month 
        AND id != NEW.id
    ) THEN
        RAISE EXCEPTION 'Un salaire existe déjà pour cette période (mois: %, année: %)', 
            NEW.month, NEW.year;
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER prevent_duplicate_salary
    BEFORE INSERT OR UPDATE ON salaries
    FOR EACH ROW
    EXECUTE FUNCTION check_duplicate_salary();

CREATE OR REPLACE FUNCTION validate_positive_amount()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.amount <= 0 THEN
        RAISE EXCEPTION 'Le montant doit être positif';
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER validate_revenue_amount
    BEFORE INSERT OR UPDATE ON revenues
    FOR EACH ROW
    EXECUTE FUNCTION validate_positive_amount();

CREATE TRIGGER validate_salary_amount
    BEFORE INSERT OR UPDATE ON salaries
    FOR EACH ROW
    EXECUTE FUNCTION validate_positive_amount();