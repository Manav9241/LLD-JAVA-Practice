package JavaPracticeProjects.PP02_OrderManagementSystem;

import java.util.List;

public interface IOrderRepository {
    Order findOrderById(String id);
    List<String> getAllOrders();
    void save(Order order);
}
