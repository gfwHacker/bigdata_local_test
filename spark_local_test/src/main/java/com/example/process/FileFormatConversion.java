package com.example.process;

import com.example.common.SparkSqlBase;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class FileFormatConversion extends SparkSqlBase implements Serializable {

    public static void main(String[] args) {
        FileFormatConversion main = new FileFormatConversion();
        main.process(args);
    }

    @Override
    public void action(SparkSession spark, String cycle) {
        //设置模块根路径
        String rootPath = "file:///D:\\IDEA\\bigdata_local_test\\spark_local_test\\";

        Dataset<Row> csvData = spark.read().format("csv")
                .option("seq", ",")
                .option("header", "true")
                .option("charset", "UTF-8")
                .option("inferSchema", "true")
                .load(rootPath+"src\\main\\resources\\data\\data.csv");
        System.out.println("csv文件读取成功");

        csvData.write().format("parquet")
                .option("compression", "snappy")
                .partitionBy("gender")
                .mode("overwrite")
                .save(rootPath+"local_data\\spark_warehouse\\users.parquet");
//                .save("spark_warehouse/users.parquet");
        System.out.println("parquet文件写入成功");

        Dataset<Row> sqlDF =
                spark.sql("SELECT * FROM parquet.`spark_warehouse/users.parquet`");
        sqlDF.show();
        System.out.println("sql读取成功");
    }

    @Override
    public SparkConf getSparkSqlConf() {
        // 添加更多SparkSql配置
        SparkConf addSparkConf = SparkSqlBase.getInitSparkConf();
        addSparkConf.setMaster("local[2]").setAppName("FileFormatConversion")
                .set("spark.eventLog.dir", "spark_local_test/local_data/eventLog");
        return addSparkConf;
    }
}
