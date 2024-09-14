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