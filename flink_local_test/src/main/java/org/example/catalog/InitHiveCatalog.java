package org.example.catalog;

import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

public class InitHiveCatalog {
    public static void getHiveCatalog(StreamTableEnvironment tableEnv) {
        // 创建HiveCatalog实例，指定目录位置和版本
        HiveCatalog hive = new HiveCatalog("hive", "default",
                "flink_local_test/src/main/resources", "3.1.2");

        // 将HiveCatalog注册到TableEnvironment，并设置为当前使用的Catalog
        tableEnv.registerCatalog("hive", hive);
        tableEnv.useCatalog("hive");

        // 创建默认数据库，如果不存在则创建
        tableEnv.executeSql("CREATE DATABASE IF NOT EXISTS flink");
        tableEnv.useDatabase("flink");

        // 加载Hive模块，显示所有加载的模块
        tableEnv.executeSql("LOAD MODULE hive");
        tableEnv.executeSql("SHOW FULL MODULES");

        // 设置TableEnvironment的SQL方言为DEFAULT
        tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);
    }
}
