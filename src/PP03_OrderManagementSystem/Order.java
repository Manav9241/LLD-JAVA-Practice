package PP03_OrderManagementSystem;

public class Order {
    private String id;
    private String status;

    public Order(String orderId) {
        this.id = orderId;
        this.status = "CREATED";
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void ship() {
        status = "SHIPPED";
    }

    public void cancel() {
        status = "CANCELLED";
    }
}
