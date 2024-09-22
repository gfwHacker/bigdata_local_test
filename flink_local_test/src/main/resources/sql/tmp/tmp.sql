EXECUTE STATEMENT SET
BEGIN

INSERT INTO hive.hive.rn_ufw_log_hive
SELECT DATE_FORMAT(ufw_time_str, 'yyyyMMddHHmm')                       AS `ufw_minutes` --ufw时间
     , DATE_FORMAT(ufw_time_str, 'yyyyMMddHH')                         AS `ufw_hour`    --ufw时间
     , DATE_FORMAT(ufw_time_str, 'yyyyMMdd')                           AS `ufw_day`     --ufw时间
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 1), '=', 1)               AS `src_ip`      --源IP地址
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 5), '=', 1)               AS `prec`        --优先级
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 8), '=', 1)               AS `proto`       --协议
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 9), '=', 1)               AS `src_port`    --源端口
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 10), '=', 1)              AS `dst_port`    --目标端口
     , CAST(SPLIT_INDEX(SPLIT_INDEX(message, ' ', 3), '=', 1) AS INT)  AS `len`         --数据包长度
     , CAST(SPLIT_INDEX(SPLIT_INDEX(message, ' ', 6), '=', 1) AS INT)  AS `ttl`         --存活时间
     , CAST(SPLIT_INDEX(SPLIT_INDEX(message, ' ', 11), '=', 1) AS INT) AS `window`      --窗口大小
     , DATE_FORMAT(LOCALTIMESTAMP, 'yyyyMMddHH')                       AS `pt_h`
FROM (
         SELECT CONCAT('2024-', CASE SUBSTRING(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), 1, 3)
                                    WHEN 'Jan' THEN '01'
                                    WHEN 'Feb' THEN '02'
                                    WHEN 'Mar' THEN '03'
                                    WHEN 'Apr' THEN '04'
                                    WHEN 'May' THEN '05'
                                    WHEN 'Jun' THEN '06'
                                    WHEN 'Jul' THEN '07'
                                    WHEN 'Aug' THEN '08'
                                    WHEN 'Sep' THEN '09'
                                    WHEN 'Oct' THEN '10'
                                    WHEN 'Nov' THEN '11'
                                    WHEN 'Dec' THEN '12' END, '-',
                       CASE SUBSTRING(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), 5, 1)
                           WHEN ' ' THEN LPAD(SUBSTRING(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), 6, 1), 2, '0')
                           ELSE SUBSTRING(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), 5, 2) END, ' ',
                       CASE SUBSTRING(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), 5, 1)
                           WHEN ' ' THEN SPLIT_INDEX(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), ' ', 3)
                           ELSE SPLIT_INDEX(TRIM(SPLIT_INDEX(message, 'rnvps', 0)), ' ', 2) END) AS `ufw_time_str`
              , TRIM(SPLIT_INDEX(message, 'OUT=', 1))                                            AS `message`
         FROM hive.flink.rn_ufw_log_kafka
     );

INSERT INTO hive.flink.rn_ufw_log_mysql
SELECT `ufw_minutes`
     , `ufw_hour`
     , `ufw_day`
     , `src_ip`
     , `prec`
     , `proto`
     , `src_port`
     , `dst_port`
     , `len`
     , `ttl`
     , `window`
FROM hive.hive.rn_ufw_log_hive /*+ OPTIONS('streaming-source.enable'='true',
                                 'streaming-source.partition.include'='all',
                                 'streaming-source.monitor-interval'='1s',
                                 'streaming-source.partition-order'='create-time') */;

END;