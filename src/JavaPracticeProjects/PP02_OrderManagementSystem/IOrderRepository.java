package JavaPracticeProjects.PP02_OrderManagementSystem;

public interface IOrderRepository {
    Order findOrderById(String id);
    void save(Order order);
}
