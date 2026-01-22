CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0.01),
    sku VARCHAR(20) NOT NULL UNIQUE,
    category VARCHAR(20) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_products_sku ON products(sku);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);

-- Insert sample data
INSERT INTO products (name, description, price, sku, category, stock_quantity) VALUES
('Laptop Pro 15', 'High-performance laptop with 15-inch display', 1299.99, 'LP-15-PRO', 'ELECTRONICS', 50),
('Wireless Mouse', 'Ergonomic wireless mouse with precision tracking', 29.99, 'WM-001', 'ELECTRONICS', 200),
('Office Chair', 'Comfortable office chair with lumbar support', 199.99, 'OC-DELUXE', 'FURNITURE', 30),
('Coffee Maker', 'Automatic coffee maker with timer function', 79.99, 'CM-AUTO', 'KITCHEN', 75),
('Desk Lamp', 'LED desk lamp with adjustable brightness', 34.99, 'DL-LED-01', 'FURNITURE', 100);