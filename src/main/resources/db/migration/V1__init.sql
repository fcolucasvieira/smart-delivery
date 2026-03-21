CREATE TABLE users (
    id UUID PRIMARY KEY,

    username VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT uk_users_username UNIQUE (username)
);

CREATE TABLE customers (
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150) NOT NULL,
    address VARCHAR(255),
    zip_code VARCHAR(20),

    user_id UUID,

    CONSTRAINT uk_customers_email UNIQUE (email),
    CONSTRAINT fk_customers_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_customers_user_id ON customers(user_id);

CREATE TABLE products (
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price NUMERIC(10,2) NOT NULL
);

CREATE TABLE delivery_men (
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    document VARCHAR(20) NOT NULL,
    phone VARCHAR(20),

    is_available BOOLEAN NOT NULL DEFAULT true,

    CONSTRAINT uk_delivery_men_document UNIQUE (document)
);

CREATE INDEX idx_delivery_men_available ON delivery_men(is_available);

CREATE TABLE orders (
    id UUID PRIMARY KEY,

    customer_id UUID NOT NULL,
    delivery_man_id UUID,

    status VARCHAR(50) NOT NULL,

    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id),

    CONSTRAINT fk_orders_delivery_man
        FOREIGN KEY (delivery_man_id) REFERENCES delivery_men(id)
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_delivery_man_id ON orders(delivery_man_id);

CREATE TABLE order_items (
    id UUID PRIMARY KEY,

    order_id UUID NOT NULL,
    product_id UUID NOT NULL,

    quantity INTEGER NOT NULL,
    price NUMERIC(10,2) NOT NULL,

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders(id),

    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);