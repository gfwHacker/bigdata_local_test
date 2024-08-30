package org.example.sql_api;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.example.catalog.InitHiveCatalog;
import org.example.common.FlinkStreamBase;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkSql_CRUD extends FlinkStreamBase implements Serializable {

    public static void main(String[] args) {
        FlinkSql_CRUD main = new FlinkSql_CRUD();
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

        // 创建IcebergCatalog
        tableEnv.executeSql("CREATE CATALOG iceberg WITH (\n" +
                "  'type' = 'iceberg',\n" +
                "  'catalog-type'='hive',\n" +
                "  'uri'='thrift://master:9083',\n" +
                "  'warehouse' = 'hdfs://master:9000/iceberg/warehouse'\n" +
                ")");
        tableEnv.executeSql("USE CATALOG iceberg");

        // 设置库名为spark库，如果不存在则创建
        tableEnv.executeSql("CREATE DATABASE IF NOT EXISTS iceberg");
        tableEnv.executeSql("USE iceberg");

        System.out.println("1、--------------------建表阶段--------------------");
        tableEnv.executeSql("DROP TABLE IF EXISTS hive.flink.flink_crud_test_flink");
        tableEnv.executeSql("CREATE TABLE IF NOT EXISTS hive.flink.flink_crud_test_flink (\n" +
                " sequence INT,\n" +
                " random INT,\n" +
                " random_str STRING,\n" +
                " ts AS localtimestamp,\n" +
                " WATERMARK FOR ts AS ts\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second'='5',\n" +
                " 'fields.sequence.kind'='sequence',\n" +
                " 'fields.sequence.start'='1',\n" +
                " 'fields.sequence.end'='100',\n" +
                " 'fields.random.min'='1',\n" +
                " 'fields.random.max'='100',\n" +
                " 'fields.random_str.length'='10'\n" +
                ")").print();

        tableEnv.executeSql("DROP TABLE IF EXISTS iceberg.iceberg.flink_crud_test_iceberg");
        tableEnv.executeSql("CREATE TABLE IF NOT EXISTS iceberg.iceberg.flink_crud_test_iceberg (\n" +
                " sequence INT,\n" +
                " random INT,\n" +
                " random_str STRING,\n" +
                " ts STRING\n" +
                ")").print();

        System.out.println("2、--------------------插入数据--------------------");
        // 使用Table API 操作表数据
        Table table = tableEnv.from("hive.flink.flink_crud_test_flink")
                .filter($("sequence")
                        .isNotEqual(100))
                .select($("sequence"), $("random"), $("random_str"), $("ts")
                        .cast(DataTypes.STRING()));

        TableResult tableResult = table.executeInsert("iceberg.iceberg.flink_crud_test_iceberg");

        // 等待插入数据完成
        try {
            tableResult.await();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("3、--------------------查询数据--------------------");
        tableEnv.executeSql("SELECT * FROM iceberg.iceberg.flink_crud_test_iceberg").print();
    }

    @Override
    public void addFlinkConf(StreamTableEnvironment tableEnv){
        Configuration flinkConf = new Configuration();
        flinkConf.setString("yarn.application.name", "FlinkSql_CRUD");
        flinkConf.setString("pipeline.name", "FlinkSql_CRUD");
        flinkConf.setString("pipeline.operator-chaining", "false");
        //增加更多参数
        tableEnv.getConfig().addConfiguration(flinkConf);
    }
}
