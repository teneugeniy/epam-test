CREATE TABLE items
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    price DECIMAL      NOT NULL,
    UNIQUE KEY uq_items_name(name)
);

INSERT INTO items(name, price)
VALUES ('apples', 0.60),
       ('oranges', 0.25);

CREATE TABLE orders
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    time_stamp         DATETIME NOT NULL,
    total_price        DECIMAL  NOT NULL,
    total_price_to_pay DECIMAL  NOT NULL
);

CREATE TABLE order_items
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id         BIGINT  NOT NULL,
    item_id          BIGINT  NOT NULL,
    price            DECIMAL NOT NULL,
    amount           INT     NOT NULL,
    calculated_total DECIMAL NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE TABLE applied_offers
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT       NOT NULL,
    offer_uid  UUID         NOT NULL,
    offer_code VARCHAR(255) NOT NULL,
    item_id    BIGINT       NULL,
    amount     INT          NULL,
    discount   DECIMAL      NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (item_id) REFERENCES items (id)
);
