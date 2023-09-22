CREATE TABLE currency_conversion (
        id SERIAL PRIMARY KEY,
        currency_id SERIAL NOT NULL,
        currency_exchange_rate_id SERIAL NOT NULL,
        transaction_id SERIAL NOT NULL,
        conversion_sum DECIMAL(18, 2) NOT NULL,
        FOREIGN KEY (currency_id) REFERENCES currency(id),
        FOREIGN KEY (currency_exchange_rate_id) REFERENCES currency_exchange_rate(id),
        FOREIGN KEY (transaction_id) REFERENCES transaction(id)
);