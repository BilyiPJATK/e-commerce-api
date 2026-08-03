CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    display_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE equipment (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    size VARCHAR(20),
    sku VARCHAR(100) UNIQUE,
    purchase_date DATE,
    type VARCHAR(50) NOT NULL,
    condition VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE members (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    join_date DATE NOT NULL,
    membership_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_member_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE maintenance_logs (
    id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT NOT NULL,
    date_sent DATE,
    date_returned DATE,
    cost DECIMAL(10, 2),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_maintenance_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (id)
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE rental_transactions (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    check_out_time TIMESTAMP,
    expected_return_time TIMESTAMP,
    actual_return_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_rental_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_rental_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (id)
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_equipment_type ON equipment(type);
CREATE INDEX idx_equipment_condition ON equipment(condition);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_rental_transactions_member_id ON rental_transactions(member_id);
CREATE INDEX idx_rental_transactions_equipment_id ON rental_transactions(equipment_id);