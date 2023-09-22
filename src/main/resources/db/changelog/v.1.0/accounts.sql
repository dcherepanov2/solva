CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL ,
    account_number CHAR(10) NOT NULL UNIQUE,
    currency_id SERIAL,
    balance DECIMAL(18, 2) DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (currency_id) REFERENCES currency(id)
);