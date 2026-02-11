package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.Order;

public interface NotificationStrategy {
    void sendNotification(Order order);
}
