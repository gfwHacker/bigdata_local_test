SELECT
    message[1] AS session_id,
    message[2] AS username,
    message[3] AS service_or_terminal,
    message[4] AS source_ip
FROM (
    SELECT
        split(message,',') AS message
    FROM hive.login_logs_hive
    WHERE DATE_FORMAT(trans_time,'yyyyMMddHHmm') >= '202408201200'
    );