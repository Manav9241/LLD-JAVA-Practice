package JavaPracticeProjects.PP02_OrderManagementSystem.CustomExceptions;

public abstract class OrderException extends RuntimeException{
    public OrderException(String message) {
        super(message);
    }
}
