package JavaPracticeProjects.Learning.PlainHttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception{
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/health", exchange -> {
            String response = "Up and Running";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
        });

        httpServer.createContext("/orders", exchange -> {
            String httpMethod = exchange.getRequestMethod();
            if (httpMethod.equalsIgnoreCase("get")) {
                handleGetOrders(exchange);
            }
            else if (httpMethod.equalsIgnoreCase("post")) {
                handleCreateOrders(exchange);
            }
            else {
                exchange.sendResponseHeaders(405, -1);
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

    public static void handleGetOrders(HttpExchange exchange) throws IOException {
        String response = "No Orders yet";

        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    public static void handleCreateOrders(HttpExchange exchange) throws IOException{
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        System.out.println("RequestBody received: " + requestBody);

        String response = "Order details received:\n" + requestBody;
        exchange.sendResponseHeaders(201, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
