DROP TABLE IF EXISTS hive.flink.rn_ufw_log_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.rn_ufw_log_kafka (
    `@timestamp` STRING,
    `@metadata` ROW<beat STRING, type STRING, version STRING>,
    input ROW<type STRING>,
    agent ROW<id STRING, name STRING, type STRING, version STRING, ephemeral_id STRING>,
    ecs ROW<version STRING>,
    host ROW<id STRING, containerized BOOLEAN, ip ARRAY<STRING>, mac ARRAY<STRING>, hostname STRING, architecture STRING,
            os ROW<version STRING, family STRING, name STRING, kernel STRING, codename STRING, type STRING, platform STRING>
            >,
    log ROW<`offset` INT, file ROW<device_id STRING, inode STRING, path STRING>>,
    message STRING
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
    `timestamp`          STRING        COMMENT '原始时间戳',
    `trans_time`         STRING        COMMENT '格式化时间',
    `metadata_beat`      STRING        COMMENT '元数据的beat信息',
    `metadata_type`      STRING        COMMENT '元数据类型',
    `metadata_version`   STRING        COMMENT '元数据版本',
    `input_type`         STRING        COMMENT '输入类型',
    `agent_id`           STRING        COMMENT '代理的ID',
    `agent_name`         STRING        COMMENT '代理的名称',
    `agent_type`         STRING        COMMENT '生成日志的代理类型',
    `agent_version`      STRING        COMMENT '生成日志的代理版本',
    `agent_ephemeral_id` STRING        COMMENT '代理的短暂ID',
    `ecs_version`        STRING        COMMENT 'ECS版本',
    `host_id`            STRING        COMMENT '主机ID',
    `host_containerized` BOOLEAN       COMMENT '是否容器化',
    `host_ip`            ARRAY<STRING> COMMENT '主机IP地址',
    `host_mac`           ARRAY<STRING> COMMENT '主机MAC地址',
    `host_hostname`      STRING        COMMENT '主机名称',
    `host_architecture`  STRING        COMMENT '主机架构',
    `host_os_version`    STRING        COMMENT '操作系统版本',
    `host_os_family`     STRING        COMMENT '操作系统家族',
    `host_os_name`       STRING        COMMENT '操作系统名称',
    `host_os_kernel`     STRING        COMMENT '操作系统内核',
    `host_os_codename`   STRING        COMMENT '操作系统代号',
    `host_os_type`       STRING        COMMENT '操作系统类型',
    `host_os_platform`   STRING        COMMENT '操作系统平台',
    `log_offset`         INT           COMMENT '日志偏移量',
    `log_file_device_id` STRING        COMMENT '日志文件设备ID',
    `log_file_inode`     STRING        COMMENT '日志文件inode',
    `log_file_path`      STRING        COMMENT '日志文件路径',
    `message`            STRING        COMMENT '日志消息'
) WITH (
      'connector' = 'hive',
      'sink.partition-commit.trigger' = 'process-time',
      'sink.partition-commit.delay' = '60s',
      'sink.partition-commit.policy.kind' = 'metastore, success-file'
      );