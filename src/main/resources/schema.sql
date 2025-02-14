CREATE TABLE account
(
    account_number BIGINT AUTO_INCREMENT PRIMARY KEY,
    balance        BIGINT NOT NULL CHECK (balance >= 0)
);