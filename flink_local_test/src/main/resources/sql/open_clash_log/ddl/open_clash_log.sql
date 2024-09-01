DROP TABLE IF EXISTS hive.flink.open_clash_log_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.open_clash_log_kafka (
    `@timestamp` STRING,
    `@metadata` ROW<beat STRING, type STRING, version STRING>,
    fields ROW<log_topic STRING>,
    input ROW<type STRING>,
    agent ROW<id STRING, name STRING, type STRING, version STRING, ephemeral_id STRING>,
    ecs ROW<version STRING>,
    host ROW<id STRING, containerized BOOLEAN, name STRING, ip ARRAY<STRING>, mac ARRAY<STRING>, hostname STRING, architecture STRING,
            os ROW<version STRING, family STRING, name STRING, kernel STRING, codename STRING, type STRING, platform STRING>
            >,
    log ROW<`offset` INT, file ROW<device_id STRING, inode STRING, path STRING>>,
    message STRING
) WITH (
      'connector' = 'kafka',
      'topic' = 'open_clash_log',
      'properties.bootstrap.servers' = '192.168.2.101:9092',
      'scan.startup.mode' = 'earliest-offset', -- latest-offset
      'properties.group.id' = 'open_clash_log_group',
      'format' = 'json',
      'json.fail-on-missing-field' = 'false',
      'json.ignore-parse-errors' = 'true'
      );


DROP TABLE IF EXISTS hive.flink.open_clash_log_mysql;
CREATE TABLE hive.flink.open_clash_log_mysql (
    access_minutes STRING COMMENT '请求时间，格式为 yyyyMMddHHmm',
    access_hour    STRING COMMENT '请求时间，格式为 yyyyMMddHH',
    access_day     STRING COMMENT '请求时间，格式为 yyyyMMdd',
    req_time       STRING COMMENT '原始请求时间，格式为 yyyy-MM-dd HH:mm:ss',
    log_level      STRING COMMENT '日志级别',
    proto          STRING COMMENT '协议类型从消息中提取',
    src_address    STRING COMMENT '发起IP地址',
    src_port       STRING COMMENT '发起端口',
    dst_address    STRING COMMENT '请求IP地址',
    dst_port       STRING COMMENT '请求端口',
    rule_set       STRING COMMENT '使用的规则集',
    node_name      STRING COMMENT '节点名称或采取的操作类',
    access_status  INT    COMMENT '访问状态：1 表示成功，0 表示失败'
) WITH (
      'connector' = 'jdbc',
      'url' = 'jdbc:mysql://192.168.2.6:3306/hive_sync',
      'table-name' = 'open_clash_log_mysql',
      'driver' = 'com.mysql.cj.jdbc.Driver',
      'username' = 'root',
      'password' = 'mysql_xTwQXK'
      );