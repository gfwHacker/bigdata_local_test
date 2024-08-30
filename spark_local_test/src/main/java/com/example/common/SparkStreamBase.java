package com.example.common;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spark Streaming 任务的基础抽象类
 */
public abstract class SparkStreamBase {
    // 日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkStreamBase.class);

    /**
     * 处理函数，负责启动和停止 Spark 会话，并调用具体的操作
     * @param ignoredArgs 命令行参数
     */
    public void process(String[] ignoredArgs) {
        LOGGER.info("Spark task started.");

        // 创建SparkStreamSession
        try(JavaStreamingContext jssc =
                    new JavaStreamingContext(getSparkStreamConf(), Durations.seconds(10))) {

            // 执行具体的操作
            action(jssc);

            // 启动流处理
//            jssc.start();

            // 等待流处理完成
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Spark job Succeed.");
    }

    public abstract void action(JavaStreamingContext jssc);

    public abstract SparkConf getSparkStreamConf();

}
