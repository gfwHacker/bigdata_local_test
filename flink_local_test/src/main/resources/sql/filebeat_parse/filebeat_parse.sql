INSERT INTO hive.hive.filebeat_parse_hive
SELECT `@timestamp`                              AS message_ts
     , `@metadata`.`beat`                        AS metadata_beat
     , `@metadata`.`type`                        AS metadata_type
     , `@metadata`.`version`                     AS metadata_version
     , fields.`log_topic`                        AS fields_log_topic
     , input.`type`                              AS input_type
     , agent.`id`                                AS agent_id
     , agent.`name`                              AS agent_name
     , agent.`type`                              AS agent_type
     , agent.`version`                           AS agent_version
     , agent.`ephemeral_id`                      AS agent_ephemeral_id
     , ecs.`version`                             AS ecs_version
     , host.`id`                                 AS host_id
     , host.`containerized`                      AS host_containerized
     , host.`name`                               AS host_hostname
     , host.`ip`                                 AS host_ip
     , host.`mac`                                AS host_mac
     , host.`hostname`                           AS host_hostname
     , host.`architecture`                       AS host_architecture
     , host.`os`.`version`                       AS host_os_version
     , host.`os`.`family`                        AS host_os_family
     , host.`os`.`name`                          AS host_os_name
     , host.`os`.`kernel`                        AS host_os_kernel
     , host.`os`.`codename`                      AS host_os_codename
     , host.`os`.`type`                          AS host_os_type
     , host.`os`.`platform`                      AS host_os_platform
     , log.`offset`                              AS log_offset
     , log.`file`.`device_id`                    AS log_file_device_id
     , log.`file`.`inode`                        AS log_file_inode
     , log.`file`.`path`                         AS log_file_path
     , `message`                                 AS message
     , DATE_FORMAT(LOCALTIMESTAMP, 'yyyyMMddHH') AS pt_h
FROM hive.flink.filebeat_parse_kafka;