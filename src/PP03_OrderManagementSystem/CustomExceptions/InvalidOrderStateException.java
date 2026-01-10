package PP03_OrderManagementSystem.CustomExceptions;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
