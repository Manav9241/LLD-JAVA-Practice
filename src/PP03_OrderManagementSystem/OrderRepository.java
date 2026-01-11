package PP03_OrderManagementSystem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository {
    private Map<String, Order> dbStore;

    public OrderRepository() {
        dbStore = new ConcurrentHashMap<>();
    }

    public Order findOrderById(String orderId) {
        return dbStore.get(orderId);
    }

    public void saveToDB(Order order) {
        dbStore.put(order.getId(), order);
    }
}
