package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.Order;

public class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(Order order) {
        System.out.println("\n=== SMS NOTIFICATION ===");
        System.out.println("To: User Mobile Number");
        System.out.println("Message: Your order #" + order.getOrderId() + 
                " from " + order.getRestaurant().getName() + 
                " has been confirmed. Total: ₹" + order.getTotalAmount());
        System.out.println("========================\n");
    }
}
