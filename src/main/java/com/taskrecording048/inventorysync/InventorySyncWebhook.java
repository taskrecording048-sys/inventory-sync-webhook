package com.aiclaude05.inventorysync;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class InventorySyncWebhook implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(InventorySyncWebhook.class.getName());

    static int process(String method, String body) {
        if (!"POST".equals(method)) {
            return 405;
        }

        InventorySyncEvent event = InventorySyncEvent.fromJson(body);
        if (event == null) {
            return 400;
        }

        LOGGER.info(() -> "synced sku=" + event.sku() + " quantity=" + event.quantity() + " warehouse=" + event.warehouse());
        return 200;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        int status = process(exchange.getRequestMethod(), body);
        exchange.sendResponseHeaders(status, -1);
        exchange.close();
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/webhook/inventory-sync", new InventorySyncWebhook());
        server.start();
        LOGGER.info("inventory-sync-webhook listening on :8080");
    }
}
