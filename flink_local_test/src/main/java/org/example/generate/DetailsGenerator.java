package org.example.generate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class DetailsGenerator {

    private static final String[] NAMES = {"张三", "李四", "王五", "赵六", "小明", "小红", "刘强", "陈娟", "周伟", "钱芳"};
    private static final String[] CITIES = {"北京市", "上海市", "广州市", "深圳市", "成都市", "重庆市", "天津市", "南京市", "武汉市", "西安市"};
    private static final Random random = new Random();

    public static JSONObject generateDetails(int id) {
        JSONObject details = new JSONObject();
        details.put("address", DetailsGenerator.generateAddress(id));
        details.put("phone", DetailsGenerator.generatePhone());
        details.put("emails", DetailsGenerator.generateEmails(id));
        return details;
    }

    public static String generateName(int id) {
        return NAMES[id % NAMES.length];
    }

    public static String generateAddress(int id) {
        return CITIES[id % CITIES.length] + " " + (1000 + random.nextInt(9000)) + "号";
    }

    public static String generatePhone() {
        return "138" + String.format("%08d", random.nextInt(100000000));
    }

    public static JSONArray generateEmails(int id) {
        JSONArray emails = new JSONArray();
        emails.put("test" + id + "@example.com");
        emails.put("user" + id + "@gmail.com");
        return emails;
    }
}
