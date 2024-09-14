DROP TABLE IF EXISTS hive.flink.rn_ufw_log_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.rn_ufw_log_kafka (
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
      'topic' = 'rn_ufw_log',
      'properties.bootstrap.servers' = '192.168.2.101:9092',
      'scan.startup.mode' = 'earliest-offset', -- latest-offset
      'properties.group.id' = 'rn_ufw_log_group',
      'format' = 'json',
      'json.fail-on-missing-field' = 'false',
      'json.ignore-parse-errors' = 'true'
      );

DROP TABLE IF EXISTS hive.hive.rn_ufw_log_hive;
CREATE TABLE IF NOT EXISTS hive.hive.rn_ufw_log_hive (
    `ufw_minutes` STRING COMMENT '日志的时间戳，格式为 yyyyMMddHHmm',
    `ufw_hour`    STRING COMMENT '日志的时间戳，格式为 yyyyMMddHH',
    `ufw_day`     STRING COMMENT '日志的时间戳，格式为 yyyyMMdd',
    `src_ip`      STRING COMMENT '源IP地址',
    `prec`        STRING COMMENT '优先级',
    `proto`       STRING COMMENT '协议',
    `src_port`    STRING COMMENT '源端口',
    `dst_port`    STRING COMMENT '目标端口',
    `len`         INT    COMMENT '数据包长度',
    `ttl`         INT    COMMENT '存活时间',
    `window`      INT    COMMENT '窗口大小',
    `pt_h`            STRING
) PARTITIONED BY (`pt_h`)
  WITH (
      'connector' = 'hive',
      -- 开启小文件合并
      'auto-compaction' = 'true',
      'compaction.file-size' = '1MB',
      -- 数据完整时才感知到分区，但是没有watermark，或者无法从分区字段的值中提取时间
      'sink.partition-commit.trigger' = 'process-time',
      'sink.partition-commit.delay' = '60s',
      'sink.partition-commit.policy.kind' = 'metastore,success-file'
      );