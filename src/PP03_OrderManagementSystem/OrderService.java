package PP03_OrderManagementSystem;

import PP03_OrderManagementSystem.CustomExceptions.DuplicateOrderException;
import PP03_OrderManagementSystem.CustomExceptions.OrderNotFoundException;

public class OrderService {
    private OrderRepository repository;
    private final Object lock = new Object();

    public OrderService() {
        repository = new OrderRepository();
    }

    public void createOrder(String orderId) {
        synchronized (lock) {
            if (repository.findOrderById(orderId) != null) {
                throw new DuplicateOrderException(orderId);
            }

            Order newOrder = new Order(orderId);
            repository.saveToDB(newOrder);
            System.out.println("Order Created");
        }
    }

    public void cancelOrder(String orderId) {
        synchronized (lock) {
            Order order = repository.findOrderById(orderId);
            if (order == null) {
                throw new OrderNotFoundException(orderId);
            }
            order.cancel();
            repository.saveToDB(order);
            System.out.println("Order Cancelled");
        }
    }

    public void shipOrder(String orderId) {
        synchronized (lock) {
            Order order = repository.findOrderById(orderId);
            if (order == null) {
                throw new OrderNotFoundException(orderId);
            }
            order.ship();
            repository.saveToDB(order);
            System.out.println("Order Shipped");
        }
    }
}
