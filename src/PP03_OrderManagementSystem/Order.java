package PP03_OrderManagementSystem;

import PP03_OrderManagementSystem.CustomExceptions.InvalidOrderStateException;

public class Order {
    private String id;
    private OrderStatus status;

    public Order(String orderId) {
        this.id = orderId;
        this.status = OrderStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void ship() {
        if (status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Invalid Order State: Cancelled Order Cannot be Shipped");
        }
        status = OrderStatus.SHIPPED;
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException("Invalid Order State: Shipped Order Cannot be Cancelled");
        }
        status = OrderStatus.CANCELLED;
    }
}
