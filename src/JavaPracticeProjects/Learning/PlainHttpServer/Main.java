package JavaPracticeProjects.Learning.PlainHttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static Map<Integer, String> orders = new ConcurrentHashMap<>();
    private static AtomicInteger orderIdGenerator = new AtomicInteger(100);

    public static void main(String[] args) throws Exception{
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/health", exchange -> {
            String response = "Up and Running";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        httpServer.createContext("/orders", exchange -> {
            String httpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try{
                if (httpMethod.equalsIgnoreCase("get") && path.matches("/orders/\\d+")) {
                    handleGetOrderById(exchange);
                } else if (httpMethod.equalsIgnoreCase("get")) {
                    handleGetOrders(exchange);
                }
                else if (httpMethod.equalsIgnoreCase("post")) {
                    handleCreateOrders(exchange);
                }
                else {
                    exchange.sendResponseHeaders(405, -1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                exchange.close();
            }
        });

        //region FIRST ROUTING IN JAVA
//        httpServer.createContext("/health", exchange -> {
//            System.out.println(exchange.getRequestMethod());
//            System.out.println(exchange.getRequestURI());
//            System.out.println(exchange.getRequestBody());
//            System.out.println(exchange.getRequestHeaders().getFirst("Content-Type"));
//
//            System.out.println();
//
//            exchange.getResponseHeaders().set("Content-type", "text/plain");
//            exchange.getResponseHeaders().set("X-Custom-Headers", "My-Server");
//
//            String responseBodyText = "Up and Running";
//
//            exchange.sendResponseHeaders(200, responseBodyText.length());
//
//            byte[] responseBodyBytes = responseBodyText.getBytes();
//            exchange.getResponseBody().write(responseBodyBytes);
//
//            System.out.println(responseBodyBytes);
//
//            exchange.close();
//        });

        //endregion

        httpServer.start();
        System.out.println("Server started on port 8080");
    }

    public static void handleGetOrderById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        int orderId = Integer.parseInt(parts[2]);

        String order = orders.get(orderId);

        if (order == null) {
            String response = "Invalid Order Id: OrderId not found";
            exchange.sendResponseHeaders(404, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
            return;
        }

        exchange.sendResponseHeaders(200, order.length());
        exchange.getResponseBody().write(order.getBytes());
        exchange.close();
    }

    public static void handleGetOrders(HttpExchange exchange) throws IOException {
        if(orders.isEmpty()) {
            String response = "No Orders found";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
            return;
        }

        StringBuilder responseBody = new StringBuilder();
        for(Map.Entry<Integer, String> order: orders.entrySet()) {
            responseBody.append("orderId: " + order.getKey());
            responseBody.append("\nSummary: " + order.getValue());
            responseBody.append("\n\n");
        }

        String response = responseBody.toString();

        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    public static void handleCreateOrders(HttpExchange exchange) throws IOException{
        String requestBody = new String(exchange.getRequestBody().readAllBytes());

        if (requestBody.length() == 0) {
            String response = "Empty Request Body";
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
            return;
        }

        int orderId = orderIdGenerator.incrementAndGet();
        orders.put(orderId, requestBody);

        String response = "Order details received:\nOrderID: " + orderId + "\nOrder: " + requestBody;

        exchange.sendResponseHeaders(201, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
