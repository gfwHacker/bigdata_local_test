-- 创建kafka来源表
DROP TABLE IF EXISTS hive.flink.code_datagen_customer_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.code_datagen_customer_kafka (
    id INT,
    name STRING,
    details ROW<address STRING, phone STRING, emails ARRAY<STRING>>,
    orders ARRAY<ROW<order_id STRING, products ARRAY<ROW<name STRING, price DOUBLE>> >>
) WITH (
      'connector' = 'kafka',
      'topic' = 'code_datagen_customer',
      'properties.bootstrap.servers' = '192.168.2.101:9092',
      'properties.group.id' = 'customer_group1',
      'scan.startup.mode' = 'earliest-offset', -- latest-offset
      'format' = 'json',
      'json.fail-on-missing-field' = 'false',
      'json.ignore-parse-errors' = 'true'
      );

-- 创建hive输出表
DROP TABLE IF EXISTS hive.hive.code_datagen_customer_hive;
CREATE TABLE IF NOT EXISTS hive.hive.code_datagen_customer_hive (
    `id`              INT,
    `name`            STRING,
    `address`         STRING,
    `phone`           STRING,
    `email`           STRING,
    `order_id`        STRING,
    `products_name`   STRING,
    `products_price`  DOUBLE
) WITH (
     'connector' = 'hive',
     'sink.partition-commit.trigger'='process-time',
     'sink.partition-commit.delay'='1m',
     'sink.partition-commit.policy.kind'='metastore，success-file'
     );