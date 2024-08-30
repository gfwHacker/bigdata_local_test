package org.example.generate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CustomerGenerator {

    private static final Random random = new Random();

    public static List<JSONObject> generateEntries(int count) {
        List<JSONObject> entries = new ArrayList<>();
        Set<String> generatedEntries = new HashSet<>();

        while (entries.size() < count) {
            JSONObject entry = CustomerGenerator.generateEntry(entries.size() + 1);
            String entryAsString = entry.toString();

            // Check if the generated entry already exists
            if (!generatedEntries.contains(entryAsString)) {
                entries.add(entry);
                generatedEntries.add(entryAsString);
            }
        }

        return entries;
    }

    public static JSONObject generateEntry(int id) {
        JSONObject entry = new JSONObject();

        // Generate details
        JSONObject details = DetailsGenerator.generateDetails(id);
        entry.put("id", id);
        entry.put("name", DetailsGenerator.generateName(id));
        entry.put("details", details);

        // Optionally generate orders
        if (random.nextBoolean()) {
            JSONArray orders = OrderGenerator.generateOrders(id);
            entry.put("orders", orders);
        }

        return entry;
    }
}
