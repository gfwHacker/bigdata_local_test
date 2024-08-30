package com.example.until;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampTransformUtils {

    /**
     * 获取当前时间并格式化为字符串。
     * @return 格式化后的当前时间字符串
     */
    public static String getFormattedTime() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        // 格式化当前时间为字符串
        return now.format(formatter);
    }
}



