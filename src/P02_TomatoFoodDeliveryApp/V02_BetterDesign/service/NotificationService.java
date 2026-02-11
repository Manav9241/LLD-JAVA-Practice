package P02_TomatoFoodDeliveryApp.V02_BetterDesign.service;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.Order;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.NotificationStrategy;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<NotificationStrategy> notificationStrategies;

    public NotificationService() {
        this.notificationStrategies = new ArrayList<>();
    }

    public void addNotificationStrategy(NotificationStrategy strategy) {
        if (strategy != null) {
            notificationStrategies.add(strategy);
        }
    }

    public void removeNotificationStrategy(NotificationStrategy strategy) {
        notificationStrategies.remove(strategy);
    }

    public void notifyOrderPlaced(Order order) {
        for (NotificationStrategy strategy : notificationStrategies) {
            strategy.sendNotification(order);
        }
    }
}
