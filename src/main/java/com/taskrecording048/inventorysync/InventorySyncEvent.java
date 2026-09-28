package com.taskrecording048.inventorysync;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record InventorySyncEvent(String sku, int quantity, String warehouse) {

    private static final Pattern SKU_PATTERN = Pattern.compile("\"sku\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern QUANTITY_PATTERN = Pattern.compile("\"quantity\"\\s*:\\s*(-?\\d+)");
    private static final Pattern WAREHOUSE_PATTERN = Pattern.compile("\"warehouse\"\\s*:\\s*\"([^\"]*)\"");

    public static InventorySyncEvent fromJson(String json) {
        Matcher skuMatcher = SKU_PATTERN.matcher(json);
        Matcher quantityMatcher = QUANTITY_PATTERN.matcher(json);
        Matcher warehouseMatcher = WAREHOUSE_PATTERN.matcher(json);

        if (!skuMatcher.find() || !quantityMatcher.find() || !warehouseMatcher.find()) {
            return null;
        }

        return new InventorySyncEvent(skuMatcher.group(1), Integer.parseInt(quantityMatcher.group(1)), warehouseMatcher.group(1));
    }

    public String toJson() {
        return "{\"sku\":\"" + sku + "\",\"quantity\":" + quantity + ",\"warehouse\":\"" + warehouse + "\"}";
    }
}
