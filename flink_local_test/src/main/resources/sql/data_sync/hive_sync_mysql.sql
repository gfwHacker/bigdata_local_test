INSERT INTO hive.flink.rn_ufw_log_mysql
SELECT
    DATE_FORMAT(
            TO_TIMESTAMP(ufw_time_str, 'yyyyMMdd-HH:mm:ss'), 'yyyyMMddHHmm') AS `ufw_minutes` --ufw时间
   , DATE_FORMAT(
        TO_TIMESTAMP(ufw_time_str, 'yyyyMMdd-HH:mm:ss'), 'yyyyMMddHH')       AS `ufw_hour` --ufw时间
   , DATE_FORMAT(
        TO_TIMESTAMP(ufw_time_str, 'yyyyMMdd-HH:mm:ss'), 'yyyyMMdd')         AS `ufw_day` --ufw时间
   , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 1), '=', 1)                       AS `src_ip` --源IP地址
   , CAST(SPLIT_INDEX(SPLIT_INDEX(message, ' ', 3), '=', 1) AS INT)          AS `len` --数据包长度
   , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 5), '=', 1)                       AS `prec` --优先级
   , CAST(SPLIT_INDEX(SPLIT_INDEX(message, ' ', 6), '=', 1) AS INT)          AS `ttl` --存活时间
   , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 8), '=', 1)                       AS `proto` --协议
   , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 9), '=', 1)                       AS `src_port` --源端口
   , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 10), '=', 1)                      AS `dst_port` --目标端口
   , CAST(SPLIT_INDEX(SPLIT_INDEX(message, ' ', 11), '=', 1) AS INT)         AS `window`  --窗口大小
FROM (
        SELECT
            CONCAT(DATE_FORMAT(CURRENT_DATE, 'yyyyMM'),
                   SPLIT_INDEX(message, ' ', 1),'-',
                   SPLIT_INDEX(message, ' ', 2))    AS `ufw_time_str`
            , TRIM(SPLIT_INDEX(message, 'OUT=', 1)) AS `message`
        FROM hive.hive.rn_ufw_log_hive
           /*+ OPTIONS('streaming-source.enable'='true','streaming-source.partition.include'='all',
            'streaming-source.monitor-interval'='1s','streaming-source.partition-order'='create-time') */
    )
WHERE SPLIT_INDEX(message, ' ', 8) <> 'DF'
AND SPLIT_INDEX(SPLIT_INDEX(message, ' ', 11), '=', 0) <> 'LEN';