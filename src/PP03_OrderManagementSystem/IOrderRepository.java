package PP03_OrderManagementSystem;

/**
 * Interface for Order Repository operations.
 * Provides contract for order persistence operations.
 */
public interface IOrderRepository {
    /**
     * Finds an order by its ID
     * @param orderId unique identifier for the order
     * @return Order object if found, null otherwise
     */
    Order findOrderById(String orderId);

    /**
     * Saves or updates an order in the repository
     * @param order the order to save or update
     */
    void save(Order order);
}
