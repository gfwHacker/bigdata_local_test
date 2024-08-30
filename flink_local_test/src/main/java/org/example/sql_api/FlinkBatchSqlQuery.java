package org.example.sql_api;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.example.catalog.InitHiveCatalog;
import org.example.common.FlinkBatchBase;

import java.io.Serializable;

public class FlinkBatchSqlQuery extends FlinkBatchBase implements Serializable {
    public static void main(String[] args) {
        FlinkBatchSqlQuery main = new FlinkBatchSqlQuery();
        main.process(args);
    }

    @Override
    public void action(StreamTableEnvironment tableEnv) {

        // 输出Flink WebUI地址，便于用户监控任务
        System.out.println("======================================================");
        System.out.println("请打开Flink WebUI：http://localhost:8081 以查看任务运行状况");
        System.out.println("======================================================");

        // 初始化HiveCatalog
        InitHiveCatalog.getHiveCatalog(tableEnv);

        // 执行SQL查询Hive表数据
        tableEnv.executeSql("select * from hive.hive.sink_datagen_table").print();
    }

    @Override
    public void addFlinkConf(StreamTableEnvironment tableEnv) {
        Configuration flinkConf = new Configuration();
        flinkConf.setString("yarn.application.name", "FlinkBatchSqlQuery");
        flinkConf.setString("pipeline.name", "FlinkBatchSqlQuery");
        flinkConf.setString("pipeline.operator-chaining", "false");
        //增加更多参数
        tableEnv.getConfig().addConfiguration(flinkConf);
    }
}
