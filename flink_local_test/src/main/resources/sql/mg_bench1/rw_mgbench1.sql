-- 定义来源表
DROP TABLE IF EXISTS hive.flink.mg_bench1_source_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.mg_bench1_source_kafka (
    log_time      STRING,
    machine_name  STRING,
    machine_group STRING,
    cpu_idle      FLOAT,
    cpu_nice      FLOAT,
    cpu_system    FLOAT,
    cpu_user      FLOAT,
    cpu_wio       FLOAT,
    disk_free     FLOAT,
    disk_total    FLOAT,
    part_max_used FLOAT,
    load_fifteen  FLOAT,
    load_five     FLOAT,
    load_one      FLOAT,
    mem_buffers   FLOAT,
    mem_cached    FLOAT,
    mem_free      FLOAT,
    mem_shared    FLOAT,
    swap_free     FLOAT,
    bytes_in      FLOAT,
    bytes_out     FLOAT
) WITH (
      'connector' = 'kafka',
      'topic' = 'mg_bench1',
      'properties.bootstrap.servers' = '192.168.2.101:9092',
      'properties.group.id' = 'mg_bench1_group1',
      'scan.startup.mode' = 'earliest-offset', -- latest-offset
      'format' = 'csv',
      'csv.field-delimiter' = ',',
      'csv.ignore-parse-errors' = 'true',
      'csv.allow-comments' = 'true'
      );

-- 定义输出表
DROP TABLE IF EXISTS hive.hive.mg_bench1_sink_hive;
CREATE TABLE IF NOT EXISTS hive.hive.mg_bench1_sink_hive
    WITH (
      'connector' = 'hive',
      'sink.partition-commit.trigger'='process-time',
      'sink.partition-commit.delay'='1m',
      'sink.partition-commit.policy.kind'='metastore，success-file')
    LIKE hive.flink.mg_bench1_source_kafka (EXCLUDING ALL);

-- 全表复写插入
INSERT INTO hive.hive.mg_bench1_sink_hive
SELECT *
FROM hive.flink.mg_bench1_source_kafka;