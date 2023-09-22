INSERT INTO currency (code, name)VALUES ('KZT', 'Kazakhstani Tenge');

INSERT INTO currency (code, name)VALUES ('USD', 'US Dollar');

INSERT INTO currency (code, name)VALUES ('RUB', 'Russian Ruble');

INSERT INTO users (username, first_name, last_name, timezone) VALUES ('john_doe', 'John', 'Doe', 'America/New_York');
INSERT INTO users (username, first_name, last_name, timezone) VALUES ('john_doe', 'John', 'Doe', 'America/New_York');
INSERT INTO users (username, first_name, last_name, timezone) VALUES ('john_doe', 'John', 'Doe', 'America/New_York');

INSERT INTO accounts (user_id, account_number, currency_id, balance) VALUES (1, '1234567890', 1, 10000000.00);
INSERT INTO accounts (user_id, account_number, currency_id, balance) VALUES (2, '1234567891', 3, 100000.00);
INSERT INTO accounts (user_id, account_number, currency_id, balance) VALUES (3, '1234567892', 1, 10000000.00);
INSERT INTO accounts (user_id, account_number, currency_id, balance) VALUES (3, '1234567893', 3, 100000.00);

INSERT INTO expense_category (name, user_id) VALUES ('PRODUCT', 1);
INSERT INTO expense_category (name, user_id) VALUES ('SERVICE', 2);

INSERT INTO currency_exchange_rate (currency_from_id, currency_to_id, rate, date)
VALUES (2, 1, 473.56000, '2023-09-20');

INSERT INTO currency_exchange_rate (currency_from_id, currency_to_id, rate, date)
VALUES (3, 1, 4.92522, '2023-09-20');

INSERT INTO expense_limit (account_id, category_id, limit_amount, used_amount, currency_id, date)
VALUES (1, 1, 2000.00, 0.00, 2, '2023-09-19');

INSERT INTO expense_limit (account_id, category_id, limit_amount, used_amount, currency_id, date)
VALUES (1, 2, 2000.00, 0.00, 2, '2023-09-19');

INSERT INTO expense_limit (account_id, category_id, limit_amount, used_amount, currency_id, date)
VALUES (3, 2, 1000.00, 0.00, 2, '2023-09-19');

INSERT INTO limit_exceeded (exchange_rate_id_at_limit, limit_exceeded, account_limit_id)
VALUES (1, false, 1);
INSERT INTO limit_exceeded (exchange_rate_id_at_limit, limit_exceeded, account_limit_id)
VALUES (2, true, 2);

INSERT INTO transaction (id, account_from_id, account_to_id, amount, limit_exceeded_id, datetime, expense_category_id)
VALUES (100000, 3, 4, 100.00, 2, '2023-09-19 14:30:00', 1);

INSERT INTO currency_conversion (currency_id, currency_exchange_rate_id, transaction_id, conversion_sum)
VALUES (2, 2, 100000, 500.00);
INSERT INTO currency_conversion (currency_id, currency_exchange_rate_id, transaction_id, conversion_sum)
VALUES (1, 2, 100000, 500.00);

INSERT INTO expense_limit (account_id, category_id, limit_amount, used_amount, currency_id, date)
VALUES (1, 1, 1000.00, 0.00, 2, '2023-09-19 12:00:00'),
       (2, 1, 1000.00, 0.00, 2, '2023-09-20 14:30:00');