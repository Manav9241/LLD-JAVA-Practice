package JavaPracticeProjects.PP03_OrderManagementSystem;

import JavaPracticeProjects.PP03_OrderManagementSystem.CustomExceptions.OrderException;

public class OrderController {
    private IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder() {
        try {
            String newOrderId = orderService.createOrder().getId();
            System.out.println("Order Created: " + newOrderId);
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
    }

    public void cancelOrder(String id) {
        try {
            orderService.cancelOrder(id);
            System.out.println("Order Cancelled");
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
    }

    public void shipOrder(String id) {
        try {
            orderService.shipOrder(id);
            System.out.println("Order Shipped");
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
    }
}
