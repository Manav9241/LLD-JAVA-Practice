package P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.NotificationService;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.MenuItem;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.Order;

import java.util.List;

public class NotificationService {
    public static void notify(Order order) {
        System.out.println("\nNotification: New " + order.getType() + " order placed!");
        System.out.println("---------------------------------------------");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getUser().getName());
        System.out.println("CustomerID: " + order.getUser().getUserId());
        System.out.println("Restaurant: " + order.getRestaurant().getName());
        System.out.println("RestaurantID: " + order.getRestaurant().getID());
        System.out.println("Items Ordered:");

        List<MenuItem> items = order.getItems();
        for (MenuItem item : items) {
            System.out.println("   - " + item.getName() + " (₹" + item.getPrice() + ")");
        }

        System.out.println("Total: ₹" + order.getTotalAmount());
        System.out.println("Scheduled For: " + order.getScheduledTime());
        System.out.println("Payment: Done");
        System.out.println("---------------------------------------------");
    }
}
