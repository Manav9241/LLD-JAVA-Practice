package PP03_OrderManagementSystem;

/**
 * Interface for Order Service operations.
 * Provides contract for order management business logic.
 */
public interface IOrderService {
    /**
     * Creates a new order with the given ID
     * @param orderId unique identifier for the order
     */
    void createOrder(String orderId);

    /**
     * Cancels an existing order
     * @param orderId unique identifier for the order to cancel
     */
    void cancelOrder(String orderId);

    /**
     * Ships an existing order
     * @param orderId unique identifier for the order to ship
     */
    void shipOrder(String orderId);
}
