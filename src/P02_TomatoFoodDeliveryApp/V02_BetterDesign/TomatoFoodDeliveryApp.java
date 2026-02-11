package P02_TomatoFoodDeliveryApp.V02_BetterDesign;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.OrderType;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.PaymentMethod;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.factory.PaymentStrategyFactory;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.*;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.service.NotificationService;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.service.OrderService;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.service.RestaurantService;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.List;

public class TomatoFoodDeliveryApp {
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final PaymentStrategyFactory paymentStrategyFactory;

    public TomatoFoodDeliveryApp() {
        this.restaurantService = new RestaurantService();
        this.orderService = new OrderService();
        this.notificationService = new NotificationService();
        this.paymentStrategyFactory = new PaymentStrategyFactory();
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public List<Restaurant> searchRestaurantsByLocation(String location) {
        return restaurantService.searchByLocation(location);
    }

    public void addItemToCart(User user, Restaurant restaurant, String itemId) {
        Cart cart = user.getCart();
        
        if (cart.getRestaurant() == null) {
            cart.setRestaurant(restaurant);
        } else if (!cart.getRestaurant().equals(restaurant)) {
            throw new IllegalStateException("Cart already contains items from a different restaurant");
        }

        MenuItem item = restaurant.getMenuItem(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found in restaurant menu");
        }

        cart.addItem(item);
    }

    public Order createOrder(User user, OrderType orderType, LocalDateTime scheduledTime) {
        Cart cart = user.getCart();
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order.Builder()
                .user(user)
                .restaurant(cart.getRestaurant())
                .items(cart.getItems())
                .orderType(orderType)
                .scheduledTime(scheduledTime)
                .build();

        return order;
    }

    public boolean processOrderPayment(Order order, PaymentMethod paymentMethod, String paymentDetails) {
        PaymentStrategy paymentStrategy = paymentStrategyFactory.createPaymentStrategy(paymentMethod, paymentDetails);
        order.setPaymentStrategy(paymentStrategy);
        
        boolean paymentSuccess = order.processPayment();
        
        if (paymentSuccess) {
            orderService.placeOrder(order);
            notificationService.notifyOrderPlaced(order);
            return true;
        }
        
        return false;
    }

    public void displayCart(User user) {
        Cart cart = user.getCart();
        if (cart.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        System.out.println("\n=== Shopping Cart ===");
        System.out.println("Restaurant: " + cart.getRestaurant().getName());
        System.out.println("Items:");
        
        cart.getItemsWithQuantity().forEach((item, quantity) -> {
            System.out.println("  " + quantity + "x " + item.getName() + 
                    " @ ₹" + item.getPrice() + " = ₹" + (item.getPrice() * quantity));
        });
        
        System.out.println("--------------------");
        System.out.println("Total: ₹" + cart.getTotalCost());
        System.out.println("====================\n");
    }

    public void displayOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found");
            return;
        }

        System.out.println("\n=== All Orders ===");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("  Customer: " + order.getUser().getName());
            System.out.println("  Restaurant: " + order.getRestaurant().getName());
            System.out.println("  Type: " + order.getOrderType());
            System.out.println("  Total: ₹" + order.getTotalAmount());
            System.out.println("  Status: " + order.getStatus());
            System.out.println("  Scheduled: " + (order.getScheduledTime() != null ? 
                    order.getScheduledTime() : "Instant"));
            System.out.println("--------------------");
        }
        System.out.println("==================\n");
    }
}
