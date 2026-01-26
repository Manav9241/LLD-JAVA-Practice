package JavaPracticeProjects.PP02_OrderManagementSystem.CustomExceptions;

public class DuplicateOrderException extends OrderException {
    public DuplicateOrderException(String orderID) {
        super("Duplicate Order: Order Already Exists: " + orderID);
    }
}
