package JavaPracticeProjects.PP03_OrderManagementSystem;

public class OrderApplicationMain {
    public static void main(String[] args) {
        System.out.println("Order Management System");

        IOrderRepository orderRepository = new OrderRepository();
        IOrderService orderService = new OrderService(orderRepository);
        OrderController orderController = new OrderController(orderService);

        orderController.cancelOrder("ORD1");
        orderController.shipOrder("ORD1");

        orderController.createOrder("ORD1");
        orderController.createOrder("ORD1");

        orderController.cancelOrder("ORD1");
        orderController.shipOrder("ORD1");

        orderController.createOrder("ORD2");
        orderController.shipOrder("ORD2");
        orderController.cancelOrder("ORD2");
    }
}
