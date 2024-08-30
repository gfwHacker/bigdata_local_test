package com.example.common;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.until.TimeStampTransformUtils.getFormattedTime;

/**
 * Spark SQL 任务的基础抽象类
 */
public abstract class SparkSqlBase {
    // 日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkSqlBase.class);

    /**
     * 处理函数，负责启动和停止 Spark 会话，并调用具体的操作
     * @param ignoredArgs 命令行参数
     */
    public void process(String[] ignoredArgs) {
        LOGGER.info("Spark task started.");

        // 创建SparkSqlSession
        SparkSession spark = SparkSession.builder()
                .config(getSparkSqlConf())
                .enableHiveSupport()
                .getOrCreate();

        // 执行具体的操作
        action(spark, getFormattedTime());

        //停止Spark会话
        spark.stop();

        LOGGER.info("Spark job Succeed.");
    }

    public abstract void action(SparkSession spark, String cycle);

    public abstract SparkConf getSparkSqlConf();

    /**
     * 获取默认的 Spark 配置
     * @return SparkConf 对象
     */
    public static SparkConf getInitSparkConf() {
        SparkConf sparkConf = new SparkConf();
        // 启用 historyServer
        sparkConf.set("spark.eventLog.enabled", "true");
        sparkConf.set("spark.eventLog.dir", "hdfs://master:9000/spark/eventLog");
        sparkConf.set("spark.history.fs.logDirectory", "hdfs://master:9000/spark/eventLog");
        sparkConf.set("spark.yarn.historyServer.address", "http://master:18080");
        // 启用 iceberg
        sparkConf.set("spark.sql.catalog.iceberg", "org.apache.iceberg.spark.SparkCatalog");
        sparkConf.set("spark.sql.catalog.iceberg.type", "hive");
        sparkConf.set("spark.sql.catalog.iceberg.uri", "thrift://master:9083");
        sparkConf.set("spark.sql.catalog.iceberg.warehouse", "hdfs://master:9000/iceberg/warehouse");
        return sparkConf;
    }
}
