package com.example.stream_api;

import com.example.common.SparkSqlBase;
import com.example.common.SparkStreamBase;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;

public class SparkSocketExample extends SparkStreamBase implements Serializable {

    public static void main(String[] args) {
        SparkSocketExample main = new SparkSocketExample();
        main.process(args);
    }

    @Override
    public void action(JavaStreamingContext jssc) {
        // 从 localhost 的 9999 端口接收数据流
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);

        // 将接收到的文本行分割成单词
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());

        // 将单词映射为键值对（word,num）
        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));

        // 根据单词进行分组，并计算每个单词的频次
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(Integer::sum);

        // 打印结果
        wordCounts.print();
    }

    @Override
    public SparkConf getSparkStreamConf() {
        // 新增 SparkStream 配置
        SparkConf addSparkConf = SparkSqlBase.getInitSparkConf();
        addSparkConf.setMaster("local[2]").setAppName("SparkStreamExample");
        return addSparkConf;
    }
}
