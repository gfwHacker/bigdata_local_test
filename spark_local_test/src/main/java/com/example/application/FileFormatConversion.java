package com.example.application;

import com.example.common.SparkSqlBase;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class FileFormatConversion extends SparkSqlBase implements Serializable {

    public static void main(String[] args) {
        FileFormatConversion main = new FileFormatConversion();
        main.process(args);
    }

    @Override
    public void action(SparkSession spark, String cycle) {
        //业务代码实现
    }

    @Override
    public SparkConf getSparkSqlConf() {
        // 添加更多SparkSql配置
        SparkConf addSparkConf = SparkSqlBase.getInitSparkConf();
        addSparkConf.setMaster("local[2]").setAppName("SparkSqlSQL_CRUD")
                .set("spark.eventLog.dir", "local_data/eventLog")
                .set("spark.sql.warehouse.dir", "local_data/warehouse");
        return addSparkConf;
    }
}
