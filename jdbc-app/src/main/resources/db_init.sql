USE shopping_jdbc;

CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    -- verify we don't store invalid email addresses
    CONSTRAINT email_valid CHECK (`email` REGEXP "^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]*?[a-zA-Z0-9._-]?@[a-zA-Z0-9][a-zA-Z0-9._-]*?[a-zA-Z0-9]?\\.[a-zA-Z]{2,63}$")
);

CREATE TABLE IF NOT EXISTS sales (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    customer_id VARCHAR(36) NULL,
    `when` DATETIME NOT NULL,
    total_price_minor_units INT NOT NULL,
    total_price_ccy VARCHAR(3) NOT NULL,
    -- define fk on customer table
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    -- verify we don't store negative prices
    CONSTRAINT price_not_negative CHECK (total_price_minor_units >= 0)
);

CREATE TABLE IF NOT EXISTS stock (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    price_minor_units INT NOT NULL,
    price_ccy VARCHAR(3) NOT NULL,
    quantity INT NOT NULL,
    -- verify we don't store negative prices
    CONSTRAINT price_not_negative CHECK (price_minor_units >= 0),
    -- verify that quantity is not negative
    CONSTRAINT quantity_greater_than_one CHECK (quantity >= 0)
);

CREATE TABLE IF NOT EXISTS sold_items (
    sale_id VARCHAR(36) NOT NULL,
    stock_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    -- add our composite primary key
    PRIMARY KEY (sale_id, stock_id),
    -- define foreign keys on sales & stock tables
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (stock_id) REFERENCES stock(id),
    -- verify that we have bought at least 1 item of something
    CONSTRAINT quantity_greater_than_one CHECK (quantity > 0)
);
