package org.example.generate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class OrderGenerator {

    private static final Random random = new Random();

    public static JSONArray generateOrders(int id) {
        JSONArray orders = new JSONArray();
        int numOrders = 1 + random.nextInt(3); // Generate 1 to 3 orders

        for (int i = 1; i <= numOrders; i++) {
            JSONObject order = new JSONObject();
            order.put("order_id", OrderGenerator.generateOrderId(id, i));
            order.put("products", OrderGenerator.generateProducts(id));
            orders.put(order);
        }

        return orders;
    }

    public static String generateOrderId(int id, int orderNumber) {
        return String.format("%09d", orderNumber);
    }

    public static JSONArray generateProducts(int id) {
        JSONArray products = new JSONArray();
        int numProducts = 1 + random.nextInt(4); // Generate 1 to 4 products per order

        for (int i = 1; i <= numProducts; i++) {
            JSONObject product = new JSONObject();
            product.put("name", "Product" + (i + random.nextInt(20)));
            product.put("price", 10.0 + random.nextDouble() * 100);
            products.put(product);
        }

        return products;
    }
}
