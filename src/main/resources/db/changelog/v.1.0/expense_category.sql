CREATE TABLE expense_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id SERIAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);