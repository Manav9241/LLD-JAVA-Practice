package JavaPracticeProjects.PP03_OrderManagementSystem;

public class OrderApplicationMain {
    public static void main(String[] args) {
        System.out.println("Order Management System");

        IOrderRepository orderRepository = new OrderRepository();
        IOrderService orderService = new OrderService(orderRepository);
        OrderController orderController = new OrderController(orderService);

        orderController.cancelOrder("ORD-1");
        orderController.shipOrder("ORD-1");

        orderController.createOrder();
        orderController.createOrder();

        orderController.cancelOrder("ORD-1");
        orderController.shipOrder("ORD-1");

        orderController.createOrder();
        orderController.shipOrder("ORD-2");
        orderController.cancelOrder("ORD-2");
    }
}
