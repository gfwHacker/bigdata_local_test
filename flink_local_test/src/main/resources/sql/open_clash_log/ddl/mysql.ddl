CREATE DATABASE IF NOT EXISTS hive_sync;
DROP TABLE IF EXISTS hive_sync.open_clash_log_mysql;
CREATE TABLE hive_sync.open_clash_log_mysql (
    `id`             INT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一标识符',
    `access_minutes` VARCHAR(255)                   COMMENT '请求时间，格式为 yyyyMMddHHmm',
    `access_hour`    VARCHAR(255)                   COMMENT '请求时间，格式为 yyyyMMddHH',
    `access_day`     VARCHAR(255)                   COMMENT '请求时间，格式为 yyyyMMdd',
    `req_time`       VARCHAR(255)                   COMMENT '原始请求时间，格式为 yyyy-MM-dd HH:mm:ss',
    `log_level`      VARCHAR(255)                   COMMENT '日志级别',
    `proto`          VARCHAR(255)                   COMMENT '协议类型',
    `src_address`    VARCHAR(255)                   COMMENT '发起IP地址',
    `src_port`       VARCHAR(255)                   COMMENT '发起端口',
    `dst_address`    VARCHAR(255)                   COMMENT '请求IP地址',
    `dst_port`       VARCHAR(255)                   COMMENT '请求端口',
    `rule_set`       VARCHAR(255)                   COMMENT '使用的规则集',
    `node_name`      VARCHAR(255)                   COMMENT '使用的节点名称',
    `access_status`  INT                            COMMENT '访问状态：1 表示成功，0 表示失败',
    INDEX idx_access_minutes (`access_minutes`)
) ENGINE=InnoDB;