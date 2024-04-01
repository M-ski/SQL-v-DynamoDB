USE shopping_jdbc;

CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    -- verify we don't store invalid email addresses
    CONSTRAINT email_valid CHECK (`email` REGEXP "^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]*?[a-zA-Z0-9._-]?@[a-zA-Z0-9][a-zA-Z0-9._-]*?[a-zA-Z0-9]?\\.[a-zA-Z]{2,63}$")
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS sales (
    customer_id VARCHAR(36) NOT NULL,
    `when` DATETIME NOT NULL,
    total_price_minor_units INT NOT NULL,
    total_price_ccy VARCHAR(3) NOT NULL,
    -- add our composite primary key
    CONSTRAINT pk PRIMARY KEY (customer_id, `when`),
    -- define fk on customer table
    CONSTRAINT customer_fk FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=INNODB;
-- verify we don't store negative prices
ALTER TABLE sales ADD CONSTRAINT IF NOT EXISTS price_not_negative CHECK (total_price_minor_units >= 0);

CREATE TABLE IF NOT EXISTS stock (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    price_minor_units INT NOT NULL,
    price_ccy VARCHAR(3) NOT NULL,
    quantity INT NOT NULL
) ENGINE=INNODB;
-- verify we don't store negative prices
ALTER TABLE stock ADD CONSTRAINT IF NOT EXISTS price_not_negative CHECK (price_minor_units >= 0);
-- verify that quantity is not negative
ALTER TABLE stock ADD CONSTRAINT IF NOT EXISTS quantity_greater_than_one CHECK (quantity >= 0);

CREATE TABLE IF NOT EXISTS sold_items (
    customer_id VARCHAR(36) NOT NULL,
    `when` DATETIME NOT NULL,
    stock_id VARCHAR(36) NOT NULL,
    quantity INT,
    -- add our composite primary key
    CONSTRAINT pk PRIMARY KEY (customer_id, `when`, stock_id),
    -- define foreign keys on sales & stock tables
    CONSTRAINT sales_fk FOREIGN KEY (customer_id, `when`) REFERENCES sales(customer_id, `when`),
    CONSTRAINT stock_fk FOREIGN KEY (stock_id) REFERENCES stock(id)
) ENGINE=INNODB;
-- verify that we have bought at least 1 item of something
ALTER TABLE sold_items ADD CONSTRAINT IF NOT EXISTS quantity_greater_than_one CHECK (quantity > 0);