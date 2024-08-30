package org.example.common;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FlinkBatchBase {
    // 日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkBatchBase.class);

    public void process(String[] ignoredArgs){
        LOGGER.info("Flink task started.");

        // 创建StreamTableEnvironment
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(
                FlinkStreamBase.getStreamEnv()
                        .setParallelism(2)
                        .setRuntimeMode(RuntimeExecutionMode.BATCH));

        // 添加Flink配置
        addFlinkConf(tableEnv);

        // 执行具体操作
        action(tableEnv);
    }

    public abstract void action(StreamTableEnvironment tableEnv);

    public abstract void addFlinkConf(StreamTableEnvironment tableEnv);
}
