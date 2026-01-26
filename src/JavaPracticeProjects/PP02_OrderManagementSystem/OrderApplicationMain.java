package JavaPracticeProjects.PP02_OrderManagementSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderApplicationMain {
    public static void main(String[] args) throws Exception{
        System.out.println("Order Management System");

        IOrderRepository orderRepository = new OrderRepository();
        IOrderService orderService = new OrderService(orderRepository);
        OrderController orderController = new OrderController(orderService);

        //region CONCURRENT SHIP/CANCEL ON SAME ORDER

        Order newOrder = orderController.createOrder();
        String orderId = newOrder.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            orderController.cancelOrder(orderId);
        });
        executor.submit(() -> {
            orderController.shipOrder(orderId);
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n****Final Order Status****");
        System.out.println(orderController.getOrder(orderId).getStatus());

        //endregion

        //region MULTITHREADED ORDER CREATING LOGIC

//        int threadCount = 100;
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//
//        for (int i=0; i < threadCount; i++) {
//            executor.submit(() -> {
//                orderController.createOrder();
//            });
//        }
//
//        executor.shutdown();
//        executor.awaitTermination(10, TimeUnit.SECONDS);
//
//        System.out.println("\n****TOTAL ORDERS STORED****");
//        System.out.println(orderRepository.getAllOrders().size());

        //endregion

        //region WORKING SINGLE-THREADED RUN

//        orderController.cancelOrder("ORD-1");
//        orderController.shipOrder("ORD-1");
//
//        orderController.createOrder();
//        orderController.createOrder();
//
//        orderController.cancelOrder("ORD-1");
//        orderController.shipOrder("ORD-1");
//
//        orderController.createOrder();
//        orderController.shipOrder("ORD-2");
//        orderController.cancelOrder("ORD-2");

        //endregion
    }
}
