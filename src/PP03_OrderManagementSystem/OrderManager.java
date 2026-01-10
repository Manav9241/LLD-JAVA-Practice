package PP03_OrderManagementSystem;

public class OrderManager {
    private OrderService orderService;

    public OrderManager() {
        this.orderService = new OrderService();
    }

    public void CreateOrder(String id) {
        try {
            orderService.createOrder(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void CancelOrder(String id) {
        try {
            orderService.cancelOrder(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void ShipOrder(String id) {
        try {
            orderService.shipOrder(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
