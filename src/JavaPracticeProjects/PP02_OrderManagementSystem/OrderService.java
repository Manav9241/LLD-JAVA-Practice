package JavaPracticeProjects.PP02_OrderManagementSystem;

import JavaPracticeProjects.PP02_OrderManagementSystem.CustomExceptions.DuplicateOrderException;
import JavaPracticeProjects.PP02_OrderManagementSystem.CustomExceptions.OrderNotFoundException;

public class OrderService implements IOrderService{
    private IOrderRepository repository;
    private int orderCount = 0;

    private final Object idlock = new Object();

    public OrderService(IOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order createOrder() {
        String orderId = generateOrderId();

        if (repository.findOrderById(orderId) != null) {
            throw new DuplicateOrderException(orderId);
        }

        Order newOrder = new Order(orderId);
        repository.save(newOrder);

        return newOrder;
    }

    @Override
    public Order getOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
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

    private String generateOrderId() {
        synchronized (idlock) {
            orderCount += 1;
            return "ORD-" + orderCount;
        }
    }
}
