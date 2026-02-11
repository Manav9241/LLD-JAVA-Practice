package P02_TomatoFoodDeliveryApp.V02_BetterDesign.model;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.OrderStatus;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.OrderType;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final User user;
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private final double totalAmount;
    private final OrderType orderType;
    private final LocalDateTime orderTime;
    private final LocalDateTime scheduledTime;
    private OrderStatus status;
    private PaymentStrategy paymentStrategy;

    private Order(Builder builder) {
        this.orderId = UUID.randomUUID().toString();
        this.user = builder.user;
        this.restaurant = builder.restaurant;
        this.items = new ArrayList<>(builder.items);
        this.totalAmount = builder.totalAmount;
        this.orderType = builder.orderType;
        this.orderTime = LocalDateTime.now();
        this.scheduledTime = builder.scheduledTime;
        this.status = OrderStatus.PENDING;
        this.paymentStrategy = builder.paymentStrategy;
    }

    public String getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean processPayment() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        return paymentStrategy.pay(totalAmount);
    }

    public static class Builder {
        private User user;
        private Restaurant restaurant;
        private List<MenuItem> items;
        private double totalAmount;
        private OrderType orderType;
        private LocalDateTime scheduledTime;
        private PaymentStrategy paymentStrategy;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder restaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public Builder items(List<MenuItem> items) {
            this.items = items;
            this.totalAmount = items.stream().mapToDouble(MenuItem::getPrice).sum();
            return this;
        }

        public Builder orderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder scheduledTime(LocalDateTime scheduledTime) {
            this.scheduledTime = scheduledTime;
            return this;
        }

        public Builder paymentStrategy(PaymentStrategy paymentStrategy) {
            this.paymentStrategy = paymentStrategy;
            return this;
        }

        public Order build() {
            if (user == null) {
                throw new IllegalStateException("User is required");
            }
            if (restaurant == null) {
                throw new IllegalStateException("Restaurant is required");
            }
            if (items == null || items.isEmpty()) {
                throw new IllegalStateException("Items cannot be empty");
            }
            if (orderType == null) {
                throw new IllegalStateException("Order type is required");
            }
            return new Order(this);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", user=" + user.getName() +
                ", restaurant=" + restaurant.getName() +
                ", totalAmount=" + totalAmount +
                ", orderType=" + orderType +
                ", status=" + status +
                ", scheduledTime=" + (scheduledTime != null ? scheduledTime : "Instant") +
                '}';
    }
}
