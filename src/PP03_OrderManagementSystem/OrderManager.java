package PP03_OrderManagementSystem;

import PP03_OrderManagementSystem.CustomExceptions.OrderException;

public class OrderManager {
    private OrderService orderService;

    public OrderManager() {
        this.orderService = new OrderService();
    }

    public void CreateOrder(String id) {
        try {
            orderService.createOrder(id);
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
    }

    public void CancelOrder(String id) {
        try {
            orderService.cancelOrder(id);
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
    }

    public void ShipOrder(String id) {
        try {
            orderService.shipOrder(id);
        } catch (OrderException e) {
            System.out.println("Business Exception -> " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System Error -> " + e.getMessage());
        }
    }
}
