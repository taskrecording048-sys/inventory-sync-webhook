package com.aiclaude05.inventorysync;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventorySyncWebhookTest {

    @Test
    void acceptsValidPost() {
        String body = new InventorySyncEvent("SKU-1001", 42, "west-1").toJson();
        int status = InventorySyncWebhook.process("POST", body);
        assertEquals(200, status);
    }

    @Test
    void rejectsGet() {
        int status = InventorySyncWebhook.process("GET", "");
        assertEquals(405, status);
    }
}
