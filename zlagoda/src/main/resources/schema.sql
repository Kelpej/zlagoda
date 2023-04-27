CREATE TABLE IF NOT EXISTS categories
(
    id   bigserial              NOT NULL,
    name character varying(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS products
(
    id          bigserial              NOT NULL,
    category_id bigserial              NOT NULL,
    name        varchar(255)           NOT NULL,
    description character varying(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories (id) ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS products_in_store
(
    upc        varchar(12) NOT NULL,
    product_id bigserial   NOT NULL,
    price      decimal     NOT NULL,
    quantity   int         NOT NULL,
    on_sale    bool        NOT NULL,
    PRIMARY KEY (upc),
    FOREIGN KEY (product_id) REFERENCES products (id) ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS employees
(
    id_employee     bigserial    NOT NULL,
    empl_name       varchar(255) NOT NULL,
    empl_surname    varchar(255) NOT NULL,
    empl_patronymic varchar(255),
    empl_role       varchar(50)  NOT NULL,
    salary          decimal      NOT NULL,
    date_of_birth   date         NOT NULL,
    date_of_start   date         NOT NULL,
    phone_number    varchar(255) NOT NULL,
    password        varchar(255) NOT NULL,
    city            varchar(255) NOT NULL,
    street          varchar(255) NOT NULL,
    zip_code        varchar(255) NOT NULL,
    PRIMARY KEY (id_employee)
);

CREATE TABLE IF NOT EXISTS customer_cards
(
    card_number     VARCHAR(16)  NOT NULL PRIMARY KEY,
    cust_name       VARCHAR(100) NOT NULL,
    cust_surname    VARCHAR(100) NOT NULL,
    cust_patronymic VARCHAR(100),
    phone_number    VARCHAR(20)  NOT NULL,
    city            VARCHAR(100) NOT NULL,
    street          VARCHAR(100) NOT NULL,
    zip_code        VARCHAR(10)  NOT NULL,
    percent         INTEGER      NOT NULL
);

CREATE TABLE IF NOT EXISTS receipts
(
    check_number BIGSERIAL NOT NULL,
    id_employee  BIGSERIAL NOT NULL,
    card_number  VARCHAR(255),
    print_date   DATE      NOT NULL,
    sum_total    decimal   NOT NULL,
    vat          decimal   NOT NULL,
    PRIMARY KEY (check_number),
    FOREIGN KEY (id_employee) REFERENCES employees (id_employee) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (card_number) REFERENCES customer_cards (card_number) ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS sales
(
    upc        varchar(12)      NOT NULL,
    receipt_id BIGSERIAL        NOT NULL,
    quantity   INT              NOT NULL,
    price      double precision NOT NULL,
    PRIMARY KEY (upc, receipt_id),
    FOREIGN KEY (upc) REFERENCES products_in_store (upc) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (receipt_id) REFERENCES receipts (check_number) ON UPDATE CASCADE ON DELETE CASCADE
);