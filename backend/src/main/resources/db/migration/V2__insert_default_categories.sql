-- Insertion des catégories par défaut
INSERT INTO categories (name, type, description, icon) VALUES
    ('Salaire', 'SALARY', 'Revenus provenant d''un emploi salarié', '💰'),
    ('Freelance', 'FREELANCE', 'Revenus provenant de travail indépendant', '💻'),
    ('Investissement', 'INVESTMENT', 'Revenus provenant d''investissements', '📈'),
    ('Business', 'BUSINESS', 'Revenus provenant d''une activité commerciale', '🏢'),
    ('Location', 'RENTAL', 'Revenus locatifs', '🏠'),
    ('Dividendes', 'DIVIDEND', 'Revenus de dividendes', '💹'),
    ('Autre', 'OTHER', 'Autres types de revenus', '📋')
ON CONFLICT (name) DO NOTHING;