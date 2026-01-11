package PP03_OrderManagementSystem;

import PP03_OrderManagementSystem.CustomExceptions.DuplicateOrderException;
import PP03_OrderManagementSystem.CustomExceptions.OrderNotFoundException;

public class OrderService implements IOrderService {
    private IOrderRepository repository;

    // Dependency injection through constructor
    public OrderService(IOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createOrder(String orderId) {
        if (repository.findOrderById(orderId) != null) {
            throw new DuplicateOrderException(orderId);
        }

        Order newOrder = new Order(orderId);
        repository.save(newOrder);
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        order.cancel();
        repository.save(order);
    }

    @Override
    public void shipOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        order.ship();
        repository.save(order);
    }
}
