-- This file allow to write SQL commands that will be emitted in test and dev.

INSERT INTO product (maxmeasure, minmeasure, created, id, name) VALUES (25, 0, '2024-01-18 18:49:39.444568', 1, 'Bananas');
INSERT INTO product (maxmeasure, minmeasure, created, id, name) VALUES (15, 5, '2024-01-18 18:49:39.444568', 2, 'Snickers');
INSERT INTO measurement (isok, measvalue, created, id, product_id) VALUES (false, -1, '2024-01-20 18:49:39.445568', 1, 1);
INSERT INTO measurement (isok, measvalue, created, id, product_id) VALUES (false, 20, '2024-01-20 18:49:39.445568', 2, 2);

alter sequence Measurement_SEQ restart with 3;
alter sequence Product_SEQ restart with 3;
