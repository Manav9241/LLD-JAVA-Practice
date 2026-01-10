package PP03_OrderManagementSystem;

import PP03_OrderManagementSystem.CustomExceptions.DuplicateOrderException;
import PP03_OrderManagementSystem.CustomExceptions.InvalidOrderStateException;
import PP03_OrderManagementSystem.CustomExceptions.OrderNotFoundException;

public class OrderService {
    private OrderRepository repository;

    public OrderService() {
        repository = new OrderRepository();
    }

    public void createOrder(String orderId) {
        if (repository.findOrderById(orderId) != null) {
            throw new DuplicateOrderException(orderId);
        }

        Order newOrder = new Order(orderId);
        repository.saveToDB(newOrder);
        System.out.println("Order Created");
    }

    public void cancelOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        order.cancel();
        repository.saveToDB(order);
        System.out.println("Order Cancelled");
    }

    public void shipOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        order.ship();
        repository.saveToDB(order);
        System.out.println("Order Shipped");
    }
}
