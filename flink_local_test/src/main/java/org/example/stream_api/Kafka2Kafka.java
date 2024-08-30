package org.example.stream_api;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 示例程序，实现从一个Kafka主题读取数据并将其写入另一个Kafka主题。
 */
public class Kafka2Kafka {
    public static void main(String[] args) throws Exception {
        // 初始化Flink流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 配置Kafka Source，从指定Kafka集群、主题读取数据
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.2.101:9092") // Kafka broker的地址
                .setTopics("input") // 设置要消费的主题
                .setGroupId("group1") // 消费组ID
                .setStartingOffsets(OffsetsInitializer.earliest()) // 从主题的最早偏移开始消费
                .setValueOnlyDeserializer(new SimpleStringSchema()) // 使用SimpleStringSchema进行消息反序列化
                .build();

        // 从Kafka Source创建一个数据流
        DataStream<String> ds = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        // 配置Kafka Sink，用于将数据流写入到另一个Kafka主题
        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers("192.168.2.101:9092") // Kafka broker的地址
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("output") // 设置写入的Kafka主题
                        .setValueSerializationSchema(new SimpleStringSchema()) // 设置数据序列化方式
                        .build()
                )
                .build();

        // 将数据流ds sink到Kafka Sink
        ds.sinkTo(sink);

        // 执行Flink作业
        env.execute("kafka2kafka");
    }
}
