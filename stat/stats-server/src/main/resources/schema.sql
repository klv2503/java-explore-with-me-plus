CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    app       VARCHAR(255) NOT NULL,
    uri       VARCHAR(500) NOT NULL,
    ip        VARCHAR(45)  NOT NULL,
    timestamp TIMESTAMP    NOT NULL
);