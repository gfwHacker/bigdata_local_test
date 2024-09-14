CREATE DATABASE IF NOT EXISTS hive_sync;
DROP TABLE IF EXISTS hive_sync.rn_ufw_log_mysql;
CREATE TABLE hive_sync.rn_ufw_log_mysql (
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一标识符',
    `ufw_minutes` VARCHAR(255)                   COMMENT '请求时间，格式为 yyyyMMddHHmm',
    `ufw_hour`    VARCHAR(255)                   COMMENT '请求时间，格式为 yyyyMMddHH',
    `ufw_day`     VARCHAR(255)                   COMMENT '请求时间，格式为 yyyyMMdd',
    `src_ip`      VARCHAR(255)                   COMMENT '源IP地址',
    `prec`        VARCHAR(255)                   COMMENT '优先级',
    `proto`       VARCHAR(255)                   COMMENT '协议',
    `src_port`    VARCHAR(255)                   COMMENT '源端口',
    `dst_port`    VARCHAR(255)                   COMMENT '目标端口',
    `len`         INT                            COMMENT '数据包长度',
    `ttl`         INT                            COMMENT '存活时间',
    `window`      INT                            COMMENT '窗口大小',
    INDEX idx_ufw_minutes (`ufw_minutes` ASC)
) ENGINE=InnoDB;