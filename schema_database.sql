CREATE DATABASE IF NOT EXISTS rubrica;
USE rubrica;

DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `surname` VARCHAR(100) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `age` INT NOT NULL
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(100) NOT NULL,
    `password` VARCHAR(100) NOT NULL
);

INSERT INTO user (username, password) VALUES ('user', 'password');