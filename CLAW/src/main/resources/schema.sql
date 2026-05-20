-- CLAW Streetwear Database Schema
DROP DATABASE IF EXISTS claw_db;
CREATE DATABASE claw_db;
USE claw_db;

-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_picture_url VARCHAR(255) DEFAULT 'images/profiles/default.png',
    points INT DEFAULT 0,
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PRODUCTS TABLE
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

-- ORDERS TABLE
CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    street_address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'PROCESSING',
    items_summary TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ORDER ITEMS JUNCTION TABLE (PHYSICALLY CONNECTS ORDERS AND PRODUCTS)
CREATE TABLE IF NOT EXISTS order_products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(50),
    product_id VARCHAR(50),
    quantity INT DEFAULT 1,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- SYSTEM LOGS TABLE
CREATE TABLE IF NOT EXISTS system_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Initial Admin
INSERT INTO users (username, email, password, is_admin)
VALUES ('Admin', 'admin@claw.com', '$2a$10$CwrNCO5ukNFjJvQ80GF3LOwgyOKEFW87daDJlBGoi2gEJgT9mjOIe', TRUE)
ON DUPLICATE KEY UPDATE is_admin = TRUE;

