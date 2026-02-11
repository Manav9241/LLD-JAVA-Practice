package P02_TomatoFoodDeliveryApp.V02_BetterDesign.service;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.OrderStatus;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final List<Order> orders;

    public OrderService() {
        this.orders = new ArrayList<>();
    }

    public void placeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        orders.add(order);
        order.setStatus(OrderStatus.CONFIRMED);
    }

    public Order getOrderById(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orders.stream()
                .filter(o -> o.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
        }
    }
}
