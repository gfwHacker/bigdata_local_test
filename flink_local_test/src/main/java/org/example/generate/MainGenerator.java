package org.example.generate;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.sql_api.FlinkSqlExcScript;
import org.json.JSONObject;

import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainGenerator {

    private static final Logger LOGGER = Logger.getLogger(FlinkSqlExcScript.class.getName());

    private static final String KAFKA_TOPIC = "code_datagen_customer"; // 替换为实际的 Kafka topic 名称

    public static void main(String[] args) {
        // 从用户获取生成数据的时间和速率
        int durationInSeconds = askUserForDuration();
        int messagesPerSecond = askUserForRate();

        // 计算总记录数
        int totalRecords = calculateTotalRecords(durationInSeconds, messagesPerSecond);

        // 生成数据
        List<JSONObject> entries = CustomerGenerator.generateEntries(totalRecords);

        // Kafka 生产者配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.2.101:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 创建 Kafka 生产者
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // 发送每条数据到 Kafka topic
            for (JSONObject entry : entries) {
                ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, entry.toString());
                producer.send(record);
                Thread.sleep(1000 / messagesPerSecond); // 控制发送速率
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "创建Kafka生产者失败，请检查！", e);
        }
    }

    // 询问用户需要生成数据的时间（秒数）
    private static int askUserForDuration() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入需要生成数据的时间（秒数）：");
        return scanner.nextInt();
    }

    // 询问用户每秒需要生成的消息数量
    private static int askUserForRate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入每秒需要生成的消息数量：");
        return scanner.nextInt();
    }

    // 根据用户输入的时间和速率计算需要生成的总记录数
    private static int calculateTotalRecords(int durationInSeconds, int messagesPerSecond) {
        return messagesPerSecond * durationInSeconds;
    }
}
