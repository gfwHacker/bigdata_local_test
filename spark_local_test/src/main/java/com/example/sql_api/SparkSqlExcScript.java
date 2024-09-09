package com.example.sql_api;

import com.example.common.SparkSqlBase;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SparkSqlExcScript extends SparkSqlBase implements Serializable {

    public static void main(String[] args) {
        SparkSqlExcScript main = new SparkSqlExcScript();
        main.process(args);
    }

    @Override
    public void action(SparkSession spark, String cycle) {

        // 指定目录路径数组
        String[] directoryPaths = {"sql/code_datagen", "sql/query"};

        // 使用类加载器获取目录下的所有文件
        String[] filePathArray = findSQLFiles(directoryPaths);

        // 打印 filePathArray 的内容
        System.out.println("待执行脚本文件路径列表:");
        for (String filePath : filePathArray) {
            System.out.println(filePath);
        }

        // 循环执行 SQL 脚本
        for (String sqlFilePath : filePathArray) {
            executeSqlScript(spark, sqlFilePath);
        }

        // 输出Spark HistoryServer地址，便于用户在任务执行完成后查看任务执行过程
        System.out.println("===================================================================");
        System.out.println("请打开Spark HistoryServer：http://192.168.2.101:18080 以查看任务历史记录");
        System.out.println("===================================================================");
    }

    @Override
    public SparkConf getSparkSqlConf() {
        // 添加更多SparkSql配置
        SparkConf addSparkConf = SparkSqlBase.getInitSparkConf();
        addSparkConf.setMaster("local[2]").setAppName("SparkSqlExcScript");
        return addSparkConf;
    }

    /**
     * 从给定的目录数组中查找所有的 .sql 文件并返回它们的相对路径。
     *
     * @param directoryPaths 指定的目录路径数组
     * @return 所有找到的 .sql 文件的相对路径数组
     */
    private static String[] findSQLFiles(String[] directoryPaths) {
        // 初始化 sqlFilePaths 列表并添加默认路径
        List<String> sqlFilePaths = new ArrayList<>();

        // 使用类加载器获取目录下的所有文件
        for (String directoryPath : directoryPaths) {
            URL dirURL = SparkSqlExcScript.class.getClassLoader().getResource(directoryPath);
            try {
                File directory = new File(Objects.requireNonNull(dirURL).toURI());
                if (directory.exists() && directory.isDirectory()) {
                    File[] files = directory.listFiles((dir, name) -> name.endsWith(".sql")); // 只筛选 .sql 文件
                    if (files != null) {
                        for (File file : files) {
                            // 使用 directoryPath 的父目录作为基准来构建相对路径
                            String relativePath = directoryPath + "/" + file.getName();
                            sqlFilePaths.add(relativePath);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error processing directory: " + directoryPath, e);
            }
        }

        // 转换为 String 数组
        return sqlFilePaths.toArray(new String[0]);
    }

    /**
     * 执行给定路径的SQL脚本
     *
     * @param sparkSession 会话执行环境，用于执行SQL查询
     * @param sqlFilePath  SQL脚本的文件路径
     */
    private static void executeSqlScript(SparkSession sparkSession, String sqlFilePath) {
        // 读取 SQL 文件
        InputStream inputStream = SparkSqlExcScript.class.getClassLoader().getResourceAsStream(sqlFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Unable to find SQL script: " + sqlFilePath);
        }

        // 将InputStream转换为字符串
        String sqlContent;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            sqlContent = reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read SQL script: " + sqlFilePath, e);
        }

        // 拆分SQL语句
        String[] sqlStatements = sqlContent.split(";");

        // 循环执行SQL语句并提示执行语句的序号
        int scriptIndex = 1;
        for (String sql : sqlStatements) {
            if (!sql.trim().isEmpty()) { // 跳过空语句
                System.out.println("Executing SQL " + scriptIndex + ":");
                // 执行SQL查询并打印结果
                sparkSession.sql(sql.trim()).show();
                scriptIndex++;
            }
        }
    }

}
