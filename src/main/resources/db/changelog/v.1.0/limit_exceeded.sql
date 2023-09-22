CREATE TABLE limit_exceeded (
    id SERIAL PRIMARY KEY,
    exchange_rate_id_at_limit INT,
    limit_exceeded BOOLEAN,
    account_limit_id SERIAL,
    FOREIGN KEY (account_limit_id) REFERENCES expense_limit(id),
    FOREIGN KEY (exchange_rate_id_at_limit) REFERENCES currency_exchange_rate(id)
);