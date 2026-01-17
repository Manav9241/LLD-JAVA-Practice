package JavaPracticeProjects.PP03_OrderManagementSystem;

import JavaPracticeProjects.PP03_OrderManagementSystem.CustomExceptions.OrderException;
import JavaPracticeProjects.PP03_OrderManagementSystem.CustomExceptions.OrderNotFoundException;

public class OrderController {
    private IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    public Order createOrder() {
        try {
            Order newOrder = orderService.createOrder();
            System.out.println("Order Created: " + newOrder.getId());
            return newOrder;
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
        return null;
    }

    public Order getOrder(String id) {
        try {
            Order order = orderService.getOrder(id);
            if(order == null) {
                throw new OrderNotFoundException(id);
            }
            return order;
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
        return null;
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
