package PP03_OrderManagementSystem.CustomExceptions;

public class DuplicateOrderException extends OrderException {
    public DuplicateOrderException(String orderID) {
        super("Duplicate Order: Order Already Exists: " + orderID);
    }
}

