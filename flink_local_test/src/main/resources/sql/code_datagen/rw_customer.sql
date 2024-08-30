-- 创建一层视图，解析部分字段
CREATE TEMPORARY VIEW IF NOT EXISTS hive.flink.view1 AS
SELECT
    id,
    name,
    -- 解析details ROW<address STRING, phone STRING, emails ARRAY<STRING>>
    details.address    AS address,
    details.phone      AS phone,
    details.emails     AS emails,
    -- 解析orders ARRAY<ROW<order_id STRING, products ARRAY<ROW<name STRING, price DOUBLE>> >>
    order_id,
    products
FROM hive.flink.code_datagen_customer_kafka
    CROSS JOIN UNNEST(orders) AS t5 (order_id,products);

-- 查询视图继续解析全部字段并插入hive表
INSERT INTO hive.hive.code_datagen_customer_hive
SELECT
    id,
    name,
    address,
    phone,
    -- 解析details.emails ARRAY<STRING>
    email,
    order_id,
    -- 解析products ARRAY<ROW<name STRING, price DOUBLE>>
    products_name,
    products_price
FROM hive.flink.view1
    CROSS JOIN UNNEST(emails)     AS t1 (email)
    CROSS JOIN UNNEST(products)   AS t6 (products_name,products_price);