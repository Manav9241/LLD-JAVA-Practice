package PP03_OrderManagementSystem;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private OrderService orderService;

    public OrderManager() {
        this.orderService = new OrderService();
    }

    public void CreateOrder(String id) {
        orderService.CreateOrder(id);
    }

    public void CancelOrder(String id) {
        orderService.CancelOrder(id);
    }

    public void ShipOrder(String id) {
        orderService.ShipOrder(id);
    }
}
