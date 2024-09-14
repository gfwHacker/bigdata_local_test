DROP TABLE IF EXISTS hive.flink.open_clash_log_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.open_clash_log_kafka (
    `@timestamp` STRING,
    `@metadata`  STRING,
    `fields`     STRING,
    `input`      STRING,
    `agent`      STRING,
    `ecs`        STRING,
    `host`       STRING,
    `log`        STRING,
    `message`    STRING
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
    `access_minutes` STRING COMMENT '请求时间，格式为 yyyyMMddHHmm',
    `access_hour`    STRING COMMENT '请求时间，格式为 yyyyMMddHH',
    `access_day`     STRING COMMENT '请求时间，格式为 yyyyMMdd',
    `log_level`      STRING COMMENT '日志级别',
    `proto`          STRING COMMENT '协议类型',
    `src_address`    STRING COMMENT '源IP地址',
    `src_port`       STRING COMMENT '源端口',
    `dst_address`    STRING COMMENT '目标IP地址',
    `dst_port`       STRING COMMENT '目标端口',
    `rule_set`       STRING COMMENT '使用的规则集',
    `node_name`      STRING COMMENT '使用的节点名称',
    `access_status`  INT    COMMENT '访问状态：1 表示成功，0 表示失败'
) WITH (
      'connector' = 'jdbc',
      'url' = 'jdbc:mysql://192.168.2.6:3306/hive_sync',
      'table-name' = 'open_clash_log_mysql',
      'driver' = 'com.mysql.cj.jdbc.Driver',
      'username' = 'root',
      'password' = 'mysql_xTwQXK'
      );