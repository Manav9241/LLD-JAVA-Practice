package JavaPracticeProjects.PP02_OrderManagementSystem;

public interface IOrderService {
    void createOrder(String orderId);
    void cancelOrder(String orderId);
    void shipOrder(String orderId);
}
