package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.MenuItem;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.Order;

public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(Order order) {
        System.out.println("\n=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + order.getUser().getName() + "@email.com");
        System.out.println("Subject: Order Confirmation - " + order.getOrderId());
        System.out.println("----------------------------------");
        System.out.println("Dear " + order.getUser().getName() + ",");
        System.out.println("Your order has been placed successfully!");
        System.out.println("\nOrder Details:");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Restaurant: " + order.getRestaurant().getName());
        System.out.println("Order Type: " + order.getOrderType());
        System.out.println("\nItems:");
        for (MenuItem item : order.getItems()) {
            System.out.println("  - " + item.getName() + " (₹" + item.getPrice() + ")");
        }
        System.out.println("\nTotal Amount: ₹" + order.getTotalAmount());
        System.out.println("Status: " + order.getStatus());
        System.out.println("==========================\n");
    }
}
