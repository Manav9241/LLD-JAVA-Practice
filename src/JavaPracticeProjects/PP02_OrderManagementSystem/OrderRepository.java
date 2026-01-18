package JavaPracticeProjects.PP02_OrderManagementSystem;

import java.util.HashMap;
import java.util.Map;

public class OrderRepository implements IOrderRepository{
    private Map<String, Order> dbStore;

    public OrderRepository() {
        dbStore = new HashMap<>();
    }

    @Override
    public Order findOrderById(String orderId) {
        return dbStore.get(orderId);
    }

    @Override
    public void save(Order order) {
        dbStore.put(order.getId(), order);
    }
}
