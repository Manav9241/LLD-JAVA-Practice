package JavaPracticeProjects.PP03_OrderManagementSystem;

public interface IOrderService {
    Order createOrder();
    void cancelOrder(String orderId);
    void shipOrder(String orderId);
}
