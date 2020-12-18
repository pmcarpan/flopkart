DROP TABLE discount;
DROP TABLE new_arrival;
DROP TABLE product;
DROP TABLE category;

CREATE TABLE category(
    id INTEGER PRIMARY KEY,
    category_name CHAR(128) UNIQUE
);

CREATE TABLE product(
    id INTEGER PRIMARY KEY,
    product_name CHAR(256),
    product_description CHAR(1024),
    category_id INTEGER REFERENCES category(id),
    price NUMBER(10),
    image_file_url CHAR(1024),
    footnote CHAR(1024),

    availabile_quantity INTEGER,
    inventory_quantity INTEGER
);

CREATE TABLE discount(
    id INTEGER PRIMARY KEY,
    product_id INTEGER REFERENCES product(id),
    discount_percentage NUMBER(5, 2)
);

CREATE TABLE new_arrival(
    id INTEGER PRIMARY KEY,
    product_id INTEGER REFERENCES product(id)
);