-- flink端执行语句
DROP TABLE IF EXISTS hive.flink.rn_ufw_log_mysql;
CREATE TABLE hive.flink.rn_ufw_log_mysql (
    `ufw_minutes` STRING COMMENT '日志的时间戳',
    `ufw_hour`    STRING COMMENT '日志的时间戳',
    `ufw_day`     STRING COMMENT '日志的时间戳',
    `src_ip`      STRING COMMENT '源IP地址',
    `len`         INT    COMMENT '数据包长度',
    `prec`        STRING COMMENT '优先级',
    `ttl`         INT    COMMENT '存活时间',
    `proto`       STRING COMMENT '协议',
    `src_port`    STRING COMMENT '源端口',
    `dst_port`    STRING COMMENT '目标端口',
    `window`      INT    COMMENT '窗口大小'
) WITH (
      'connector' = 'jdbc',
      'url' = 'jdbc:mysql://192.168.2.6:3306/hive_sync',
      'table-name' = 'rn_ufw_log_mysql',
      'driver' = 'com.mysql.cj.jdbc.Driver',
      'username' = 'root',
      'password' = 'mysql_xTwQXK'
      );