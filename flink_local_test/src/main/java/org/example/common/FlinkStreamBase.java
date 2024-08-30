package org.example.common;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FlinkStreamBase {
    // 日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkStreamBase.class);

    public void process(String[] ignoredArgs){
        LOGGER.info("Flink task started.");

        // 创建StreamTableEnvironment
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(
                getStreamEnv()
                        .setParallelism(2)
                        .setRuntimeMode(RuntimeExecutionMode.STREAMING));

        // 添加Flink配置
        addFlinkConf(tableEnv);

        // 执行具体操作
        action(tableEnv);
    }

    public abstract void action(StreamTableEnvironment tableEnv);

    public abstract void addFlinkConf(StreamTableEnvironment tableEnv);

    public static StreamExecutionEnvironment getStreamEnv(){
        return StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(getInitFlinkConf());
    }

    /**
     * 获取默认的 Flink 配置
     * @return Configuration 对象
     */
    public static Configuration getInitFlinkConf() {
        Configuration initFlinkConf = new Configuration();
        //开启检查点
        initFlinkConf.setString("execution.checkpointing.interval", "60s");
        //设置任务管理器的参数
        initFlinkConf.setString("taskmanager.numberOfTaskSlots", "2");
        initFlinkConf.setString("taskmanager.memory.task.heap.size", "512M");
        initFlinkConf.setString("taskmanager.memory.task.off-heap.size", "0M");
        return initFlinkConf;
    }
}
