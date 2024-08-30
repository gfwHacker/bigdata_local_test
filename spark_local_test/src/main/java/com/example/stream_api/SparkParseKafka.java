package com.example.stream_api;

import com.example.common.SparkSqlBase;
import com.example.common.SparkStreamBase;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;

public class SparkParseKafka extends SparkStreamBase implements Serializable {
    public static void main(String[] args) {
        SparkParseKafka main = new SparkParseKafka();
        main.process(args);
    }

    @Override
    public void action(JavaStreamingContext jssc) {

    }

    @Override
    public SparkConf getSparkStreamConf() {
        // 新增 Spark 配置
        SparkConf addSparkConf = SparkSqlBase.getInitSparkConf();
        addSparkConf.setMaster("local[2]").setAppName("SparkParseKafka")
                .set("spark.eventLog.dir", "spark_local_test/local_data/eventLog")
                .set("spark.sql.warehouse.dir", "spark_local_test/local_data/warehouse");
        return addSparkConf;
    }
}
