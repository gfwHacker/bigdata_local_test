CREATE VIEW hive.flink.open_clash_log_view AS
SELECT CONVERT_TZ(
        DATE_FORMAT(TO_TIMESTAMP(SUBSTRING(message, 7, 19), 'yyyy-MM-dd''T''HH:mm:ss'), 'yyyy-MM-dd HH:mm:ss'), 'UTC',
        'Asia/Shanghai')                                 AS req_time
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 1), '=', 1) AS log_level
     , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 2), '=', 1) AS msg
     , `message`                                         AS message
FROM hive.flink.open_clash_log_kafka
;

INSERT INTO hive.flink.open_clash_log_mysql
SELECT DATE_FORMAT(req_time, 'yyyyMMddHHmm') AS access_minutes
     , DATE_FORMAT(req_time, 'yyyyMMddHH')   AS access_hour
     , DATE_FORMAT(req_time, 'yyyyMMdd')     AS access_day
     , log_level
     , proto
     , src_address
     , src_port
     , dst_address
     , dst_port
     , rule_set
     , node_name
     , access_status
FROM (
         SELECT req_time                                          AS req_time
              , log_level                                         AS log_level
              , SUBSTRING(msg, 3, 3)                              AS proto
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 3), ':', 0) AS src_address
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 3), ':', 1) AS src_port
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 5), ':', 0) AS dst_address
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 5), ':', 1) AS dst_port
              , CASE
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 7), 1, 7) = 'RuleSet'
                        THEN REGEXP_EXTRACT(SPLIT_INDEX(message, ' ', 7), '\((.*?)\)', 1)
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 7), 1, 6) = 'Domain' THEN 'DIRECT'
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 7), 1, 11) = 'ProcessName' THEN 'DIRECT'
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 7), 1, 12) = 'DomainSuffix' THEN 'DIRECT'
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 7), 1, 13) = 'DomainKeyword' THEN 'Optimize'
                    ELSE 'Match' END                              AS rule_set
              , CASE
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 9), 1, 5) = 'PROXY'
                        THEN REGEXP_EXTRACT(SPLIT_INDEX(message, ' ', 9), '\[(.*?)\]', 1)
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 9), 1, 8) = 'Optimize'
                        THEN REGEXP_EXTRACT(SPLIT_INDEX(message, ' ', 9), '\[(.*?)\]', 1)
                    WHEN SUBSTRING(SPLIT_INDEX(message, ' ', 9), 1, 6) = 'REJECT' THEN 'REJECT'
                    ELSE 'DIRECT' END                             AS node_name
              , 1                                                 AS access_status
         FROM hive.flink.open_clash_log_view
         WHERE SPLIT_INDEX(message, ' ', 4) = '-->'

         UNION ALL

         SELECT req_time                                          AS req_time
              , log_level                                         AS log_level
              , SUBSTRING(msg, 3, 3)                              AS proto
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 7), ':', 0) AS src_address
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 7), ':', 1) AS src_port
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 9), ':', 0) AS dst_address
              , SPLIT_INDEX(SPLIT_INDEX(message, ' ', 9), ':', 1) AS dst_port
              , SPLIT_INDEX(message, ' ', 4)                      AS rule_set
              , SPLIT_INDEX(message, ' ', 4)                      AS node_name
              , 0                                                 AS access_status
         FROM hive.flink.open_clash_log_view
         WHERE SPLIT_INDEX(message, ' ', 3) = 'dial'
     );