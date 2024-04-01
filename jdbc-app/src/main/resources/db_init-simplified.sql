USE shopping_jdbc;

CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS sales (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    customer_id VARCHAR(36) NULL,
    `when` DATETIME NOT NULL,
    total_price_minor_units INT NOT NULL,
    total_price_ccy VARCHAR(3) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE IF NOT EXISTS stock (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    price_minor_units INT NOT NULL,
    price_ccy VARCHAR(3) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS sold_items (
    sale_id VARCHAR(36) NOT NULL,
    stock_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (sale_id, stock_id),
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (stock_id) REFERENCES stock(id)
);
