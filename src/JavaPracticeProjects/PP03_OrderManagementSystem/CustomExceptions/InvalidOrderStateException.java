package JavaPracticeProjects.PP03_OrderManagementSystem.CustomExceptions;

public class InvalidOrderStateException extends OrderException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
