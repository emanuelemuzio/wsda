CREATE DATABASE IF NOT EXISTS muzio;
USE muzio;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    enabled TINYINT(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(id);

INSERT INTO users(email, first_name, last_name, password, role_id, enabled) VALUES ('admin@muzio.it','admin','test','123',1,1)