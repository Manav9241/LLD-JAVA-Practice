package JavaPracticeProjects.PP02_OrderManagementSystem.CustomExceptions;

public class InvalidOrderStateException extends OrderException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
