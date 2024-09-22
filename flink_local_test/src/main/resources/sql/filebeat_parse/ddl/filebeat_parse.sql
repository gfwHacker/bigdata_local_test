DROP TABLE IF EXISTS hive.flink.filebeat_parse_kafka;
CREATE TABLE IF NOT EXISTS hive.flink.filebeat_parse_kafka (
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
      'topic' = 'rn_ufw_log',
      'properties.bootstrap.servers' = '192.168.2.101:9092',
      'scan.startup.mode' = 'earliest-offset', -- latest-offset
      'properties.group.id' = 'filebeat_parse_group',
      'format' = 'json',
      'json.fail-on-missing-field' = 'false',
      'json.ignore-parse-errors' = 'true'
      );

DROP TABLE IF EXISTS hive.hive.filebeat_parse_hive;
CREATE TABLE IF NOT EXISTS hive.hive.filebeat_parse_hive (
    `message_ts`          STRING       COMMENT '原始时间戳',
    `metadata_beat`      STRING        COMMENT '元数据的beat信息',
    `metadata_type`      STRING        COMMENT '元数据类型',
    `metadata_version`   STRING        COMMENT '元数据版本',
    `fields_log_topic`   STRING        COMMENT '目标topic',
    `input_type`         STRING        COMMENT '输入类型',
    `agent_id`           STRING        COMMENT '代理的ID',
    `agent_name`         STRING        COMMENT '代理的名称',
    `agent_type`         STRING        COMMENT '生成日志的代理类型',
    `agent_version`      STRING        COMMENT '生成日志的代理版本',
    `agent_ephemeral_id` STRING        COMMENT '代理的短暂ID',
    `ecs_version`        STRING        COMMENT 'ECS版本',
    `host_id`            STRING        COMMENT '主机ID',
    `host_containerized` BOOLEAN       COMMENT '是否容器化',
    `host_name`          STRING        COMMENT '主机名称',
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
    `message`            STRING        COMMENT '日志消息',
    `pt_h`               STRING        COMMENT '分区字段小时'
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