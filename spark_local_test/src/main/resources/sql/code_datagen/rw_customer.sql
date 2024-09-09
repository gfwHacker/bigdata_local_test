-- hive创建hive表语法
-- CREATE TABLE IF NOT EXISTS hive.code_datagen_customer_hive (
--     `id`              INT,
--     `name`            STRING,
--     `address`         STRING,
--     `phone`           STRING,
--     `email`           STRING,
--     `order_id`        STRING,
--     `products_name`   STRING,
--     `products_price`  DOUBLE
-- ) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
-- LINES TERMINATED BY '\n'
-- STORED AS TEXTFILE

-- spark创建hive表语法
-- DROP TABLE IF EXISTS spark.code_datagen_customer_hive;
-- CREATE TABLE IF NOT EXISTS spark.code_datagen_customer_hive (
--     `id`              INT,
--     `name`            STRING,
--     `address`         STRING,
--     `phone`           STRING,
--     `email`           STRING,
--     `order_id`        STRING,
--     `products_name`   STRING,
--     `products_price`  DOUBLE
-- )

-- spark创建iceberg表语法
DROP TABLE IF EXISTS iceberg.spark.code_datagen_customer_iceberg;
CREATE TABLE IF NOT EXISTS iceberg.spark.code_datagen_customer_iceberg (
    `id`              INT,
    `name`            STRING,
    `address`         STRING,
    `phone`           STRING,
    `email`           STRING,
    `order_id`        STRING,
    `products_name`   STRING,
    `products_price`  DOUBLE
) USING iceberg;

-- spark插入iceberg表
INSERT INTO iceberg.spark.code_datagen_customer_iceberg
SELECT * FROM hive.code_datagen_customer_hive;