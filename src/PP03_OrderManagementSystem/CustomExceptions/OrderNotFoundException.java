package PP03_OrderManagementSystem.CustomExceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String orderId) {
        super("Invalid Id: Order Not Found: " + orderId);
    }
}

