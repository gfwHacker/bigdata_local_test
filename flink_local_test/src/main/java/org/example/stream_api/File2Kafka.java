package org.example.stream_api;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineFormat;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 将文件数据流持续地传输到Kafka主题的示例程序。
 */
public class File2Kafka {
    /**
     * 主函数，负责执行文件到Kafka的数据导入流程。
     * @param args 命令行参数，目前未使用。
     * @throws Exception 如果作业执行过程中遇到异常。
     */
    public static void main(String[] args) throws Exception {

        // 创建配置对象，设置WebUI绑定的本地端口
        Configuration conf = new Configuration();
        conf.setString(RestOptions.BIND_PORT,"8081");

        // 创建本地执行环境，并应用配置
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        // 定义文件路径数组，这些文件将被导入到Kafka
        String[] filePaths = {"D:\\Install\\DataImport\\kafka_data\\mg_bench1.csv"};

        // 遍历文件路径数组，为每个文件创建一个数据流，并将其导入到对应的Kafka主题
        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];

            // 配置文件源，读取指定路径的文本文件
            final FileSource<String> source =
                    FileSource.forRecordStreamFormat(new TextLineFormat(), new Path(filePath))
                            .build();

            // 从文件源创建一个有界数据流
            final DataStream<String> dataStream =
                    env.fromSource(source, WatermarkStrategy.noWatermarks(), "file-source" + i);

            // 配置KafkaSink，指定Kafka broker地址和主题，以及数据序列化方式，用于将数据流写入到不同的 Kafka 主题
            KafkaSink<String> sink = KafkaSink.<String>builder()
                    .setBootstrapServers("192.168.2.101:9092") // Kafka broker的地址
                    .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                            .setTopic("mg_bench" + (i + 1)) // 设置写入的 Kafka 主题
                            .setValueSerializationSchema(new SimpleStringSchema()) // 设置数据序列化方式
                            .build()
                    )
                    .build();

            // 将有界数据流sink到Kafka主题
            dataStream.sinkTo(sink);

            // 打印提示信息，告知当前文件正在导入到哪个Kafka主题
            System.out.println("文件mg_bench" + (i + 1) + ".csv正在导入主题mg_bench" + (i + 1) + "中，请稍后查看kafka。");
        }

        // 提交Flink作业并执行
        env.execute("file2kafka");

        // 打印提示信息，表明文件导入作业已完成
        System.out.println("文件导入完毕，请查看kafka。");
    }

}
