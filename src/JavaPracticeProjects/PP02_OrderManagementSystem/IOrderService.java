package JavaPracticeProjects.PP02_OrderManagementSystem;

public interface IOrderService {
    Order createOrder();
    Order getOrder(String id);
    void cancelOrder(String orderId);
    void shipOrder(String orderId);
}
