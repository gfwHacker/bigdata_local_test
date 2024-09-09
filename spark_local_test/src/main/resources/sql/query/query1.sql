-- spark查询iceberg表
SELECT *
FROM iceberg.spark.code_datagen_customer_iceberg;

SELECT count(1)
FROM iceberg.spark.code_datagen_customer_iceberg;