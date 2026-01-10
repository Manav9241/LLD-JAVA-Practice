package PP03_OrderManagementSystem;

import java.util.HashMap;
import java.util.Map;

public class OrderRepository {
    private Map<String, Order> dbStore;

    public OrderRepository() {
        dbStore = new HashMap<>();
    }

    public Order findOrderById(String orderId) {
        return dbStore.get(orderId);
    }

    public void saveToDB(Order order) {
        dbStore.put(order.getId(), order);
    }
}
