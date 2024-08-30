package org.example.environment;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class DataStream2Table {
    public static void main(String[] args) throws Exception {
        // 创建两个 API 的环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 创建 DataStream
        DataStream<String> dataStream = env.fromElements("Alice", "Bob", "John");

        // 将仅插入的 DataStream 解释为Table
        Table inputTable = tableEnv.fromDataStream(dataStream);

        // 将 Table 对象注册为视图并对其进行查询
        tableEnv.createTemporaryView("InputTable", inputTable);
        Table resultTable = tableEnv.sqlQuery("SELECT UPPER(f0) FROM InputTable");

        // 再次将结果表解释为仅插入数据流
        DataStream<Row> resultStream = tableEnv.toDataStream(resultTable);

        // 添加打印接收器并在 DataStream API 中执行
        resultStream.print();
        env.execute();
    }
}
