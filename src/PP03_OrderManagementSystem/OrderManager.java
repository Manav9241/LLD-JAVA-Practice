package PP03_OrderManagementSystem;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private Map<String, String> orders;

    public OrderManager() {
        this.orders = new HashMap<>();
    }

    public void CreateOrder(String id) {
        if(orders.containsKey(id)) {
            System.out.println("Duplicate Order: Order Already Exists");
            return;
        }
        orders.put(id, "CREATED");
        System.out.println("Order Created");
    }

    public void CancelOrder(String id) {
        if (!orders.containsKey(id)) {
            System.out.println("Invalid Id: Order Not Found");
            return;
        }
        if (orders.get(id) == "SHIPPED") {
            System.out.println("Invalid State: Shipped Order Cannot be Cancelled");
            return;
        }
        orders.put(id, "CANCELLED");
        System.out.println("Order Cancelled");
    }

    public void ShipOrder(String id) {
        if (!orders.containsKey(id)) {
            System.out.println("Invalid Id: Order Not Found");
            return;
        }
        if (orders.get(id) == "CANCELLED") {
            System.out.println("Invalid State: Cancelled Order Cannot be Shipped");
            return;
        }
        orders.put(id, "SHIPPED");
        System.out.println("Order Shipped");
    }
}
