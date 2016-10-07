CREATE TABLE orders
(
id BIGINT NOT NULL,
converse_id BIGINT,
pair VARCHAR(50) NOT NULL,
operation VARCHAR(50) NOT NULL,
amount FLOAT NOT NULL,
price DECIMAL NOT NULL,
converse_price DECIMAL,
create_date DATE,
done_date DATE,
close_date DATE,
closed BOOL NOT NULL,
spread DECIMAL,
profit DECIMAL,
CONSTRAINT pk_orders PRIMARY KEY (id)
);