use atm_database;
-- Table to store user details
CREATE TABLE users (
    user_id VARCHAR(20) PRIMARY KEY,
    user_pin VARCHAR(4)
);

-- Table to store account details
CREATE TABLE accounts (
    user_id VARCHAR(20) PRIMARY KEY,
    balance DECIMAL(10, 2) DEFAULT 0.0
);

-- Table to store transaction history
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20),
    type VARCHAR(20),
    amount DECIMAL(10, 2),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


-- Inserting sample data for users
INSERT INTO users (user_id, user_pin)
VALUES
    ('Aditya', '1234'),
    ('Poli', '5678');

-- Inserting sample data for accounts
INSERT INTO accounts (user_id, balance)
VALUES
    ('Aditya', 0),
    ('Poli', 0);
    
Select * from transactions

