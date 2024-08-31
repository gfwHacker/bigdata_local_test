CREATE DATABASE IF NOT EXISTS hive_sync;
DROP TABLE IF EXISTS hive_sync.rn_ufw_log_mysql;
CREATE TABLE hive_sync.rn_ufw_log_mysql (
    `ufw_minutes` VARCHAR(255) COMMENT '日志的时间戳',
    `ufw_hour`    VARCHAR(255) COMMENT '日志的时间戳',
    `ufw_day`     VARCHAR(255) COMMENT '日志的时间戳',
    `src_ip`      VARCHAR(255) COMMENT '源IP地址',
    `len`         INT          COMMENT '数据包长度',
    `prec`        VARCHAR(255) COMMENT '优先级',
    `ttl`         INT          COMMENT '存活时间',
    `proto`       VARCHAR(255) COMMENT '协议',
    `src_port`    VARCHAR(255) COMMENT '源端口',
    `dst_port`    VARCHAR(255) COMMENT '目标端口',
    `window`      INT          COMMENT '窗口大小'
);