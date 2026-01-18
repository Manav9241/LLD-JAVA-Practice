package JavaPracticeProjects.PP03_OrderManagementSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository implements IOrderRepository{
    private Map<String, Order> dbStore;

    public OrderRepository() {
        dbStore = new ConcurrentHashMap<>();
    }

    @Override
    public Order findOrderById(String orderId) {
        return dbStore.get(orderId);
    }

    @Override
    public void save(Order order) {
        dbStore.putIfAbsent(order.getId(), order);
    }

    @Override
    public List<String> getAllOrders() {
        return new ArrayList<>(dbStore.keySet());
    }
}
