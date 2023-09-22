CREATE TABLE currency_exchange_rate (
        id SERIAL PRIMARY KEY,
        currency_from_id SERIAL NOT NULL,
        currency_to_id SERIAL NOT NULL,
        rate DECIMAL(20, 6) NOT NULL,
        date DATE NOT NULL,
        FOREIGN KEY (currency_from_id) REFERENCES currency(id),
        FOREIGN KEY (currency_to_id) REFERENCES currency(id)
);
