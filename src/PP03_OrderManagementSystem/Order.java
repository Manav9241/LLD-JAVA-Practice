package PP03_OrderManagementSystem;

public class Order {
    public String id;
    public String status;

    public Order(String orderId) {
        this.id = orderId;
        this.status = "CREATED";
    }
}
