CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    account_from_id SERIAL NOT NULL,
    account_to_id SERIAL NOT NULL,
    amount DECIMAL(18, 2) NOT NULL,
    limit_exceeded_id SERIAL,
    datetime TIMESTAMP NOT NULL,
    expense_category_id SERIAL NOT NULL,
    FOREIGN KEY (account_from_id) REFERENCES accounts(id),
    FOREIGN KEY (limit_exceeded_id) REFERENCES limit_exceeded(id),
    FOREIGN KEY (expense_category_id) REFERENCES expense_category(id),
    FOREIGN KEY (account_to_id) REFERENCES accounts(id)
);