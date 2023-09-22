CREATE TABLE expense_limit (
    id SERIAL PRIMARY KEY,
    account_id SERIAL NOT NULL,
    category_id SERIAL NOT NULL,
    limit_amount DECIMAL(18, 2) NOT NULL DEFAULT 1000.00,
    used_amount DECIMAL(18, 2) NOT NULL,
    currency_id SERIAL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (category_id) REFERENCES expense_category(id),
    FOREIGN KEY (currency_id) REFERENCES currency(id)
);