-- 初始化 Hive Catalog
CREATE CATALOG hive WITH (
  'type' = 'hive',
  'default-database' = 'default',
  'hive-conf-dir' = 'D:\IDEA\bigdata_local_test\flink_local_test\src\main\resources'
);
USE CATALOG hive;
CREATE DATABASE IF NOT EXISTS hive;
CREATE DATABASE IF NOT EXISTS flink;

-- 初始化 Iceberg Catalog
CREATE CATALOG iceberg WITH (
  'type' = 'iceberg',
  'catalog-type'='hive',
  'uri'='thrift://master:9083',
  'warehouse' = 'hdfs://master:9000/iceberg/warehouse'
);
USE CATALOG iceberg;
CREATE DATABASE IF NOT EXISTS iceberg;

-- 加载Hive模块并提示当前使用的模块
LOAD MODULE hive;