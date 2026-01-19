package JavaPracticeProjects.Learning.PlainHttpServer;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception{
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/health", exchange -> {
            System.out.println(exchange.getRequestMethod());
            System.out.println(exchange.getRequestURI());
            System.out.println(exchange.getRequestBody());
            System.out.println(exchange.getRequestHeaders().getFirst("Content-Type"));

            System.out.println();


            exchange.getResponseHeaders().set("Content-type", "text/plain");
            exchange.getResponseHeaders().set("X-Custom-Headers", "My-Server");

            String responseBodyText = "Up and Running";

            exchange.sendResponseHeaders(200, responseBodyText.length());

            byte[] responseBodyBytes = responseBodyText.getBytes();
            exchange.getResponseBody().write(responseBodyBytes);

            System.out.println(responseBodyBytes);

            exchange.close();
        });

        httpServer.start();
        System.out.println("Server started on port 8080");
    }
}
