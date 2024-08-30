package com.example.sql_api;

import com.example.common.SparkSqlBase;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class SparkSql_CRUD extends SparkSqlBase implements Serializable {

    public static void main(String[] args) {
        SparkSql_CRUD main = new SparkSql_CRUD();
        main.process(args);
    }

    @Override
    public void action(SparkSession spark, String cycle) {

        // 设置库名为spark库，如果不存在则创建
        spark.sql("CREATE DATABASE IF NOT EXISTS spark");
        spark.sql("USE spark");

        // 创建people表
        spark.sql("DROP TABLE IF EXISTS spark.spark_crud_tset_hive");
        spark.sql("CREATE TABLE IF NOT EXISTS spark.spark_crud_tset_hive (" +
                "id INT, " +
                "name STRING, " +
                "age INT, " +
                "gender STRING, " +
                "address STRING, " +
                "phone STRING, " +
                "email STRING, " +
                "job STRING, " +
                "salary FLOAT, " +
                "department STRING)");

        // 插入数据
        spark.sql("INSERT OVERWRITE spark.spark_crud_tset_hive VALUES " +
                "(1, '张三', 28, '男', '北京市海淀区', '13800138000', 'zhangsan@example.com', '产品经理', 12000.00, '产品部')," +
                "(2, '李四', 32, '男', '上海市浦东新区', '13900139000', 'lisi@example.com', '数据分析师', 15000.00, '开发部')," +
                "(3, '王五', 25, '女', '广州市天河区', '13700137000', 'wangwu@example.com', '人事专员', 9000.00, '人事部')," +
                "(4, '赵六', 29, '女', '深圳市南山区', '13600136000', 'zhaoliu@example.com', '产品经理', 13000.00, '产品部')," +
                "(5, '孙七', 35, '男', '杭州市西湖区', '13500135000', 'sunqi@example.com', '后端开发', 11000.00, '开发部')," +
                "(6, '周八', 40, '女', '南京市玄武区', '13400134000', 'zhouba@example.com', '实习生', 6000.00, '实习生')," +
                "(7, '吴九', 22, '男', '武汉市洪山区', '13300133000', 'wujiu@example.com', '实习生', 5000.00, '实习生')," +
                "(8, '郑十', 26, '女', '成都市武侯区', '13200132000', 'zhengshi@example.com', '数据分析师', 14000.00, '开发部')," +
                "(9, '王二', 31, '男', '重庆市渝中区', '13100131000', 'wanger@example.com', '项目经理', 17000.00, '项目部')," +
                "(10, '李一', 24, '女', '西安市雁塔区', '13000130000', 'liyi@example.com', '前端开发', 10000.00, '开发部')");

        // 查询示例
        Dataset<Row> result1 = spark.sql("SELECT * FROM spark.spark_crud_tset_hive");
        result1.show();

        // 输出Spark HistoryServer地址，便于用户在任务执行完成后查看任务执行过程
        System.out.println("===================================================================");
        System.out.println("请打开Spark HistoryServer：http://192.168.2.101:18080 以查看任务历史记录");
        System.out.println("===================================================================");
    }

    @Override
    public SparkConf getSparkSqlConf() {
        // 添加更多SparkSql配置
        SparkConf addSparkConf = SparkSqlBase.getInitSparkConf();
        addSparkConf.setMaster("local[2]").setAppName("SparkSql_CRUD")
                .set("spark.eventLog.dir", "spark_local_test/local_data/eventLog")
                .set("spark.sql.warehouse.dir", "spark_local_test/local_data/warehouse");
        return addSparkConf;
    }
}

