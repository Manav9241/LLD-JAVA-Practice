package P03_NotificationEngine.BetterDesign.observer;

import P03_NotificationEngine.BetterDesign.model.INotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete publisher that maintains a list of observers and notifies them
 * in registration order by pushing the notification directly.
 */
public class NotificationPublisher implements INotificationPublisher {

    private final List<INotificationObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(INotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(INotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void publish(INotification notification) {
        for (INotificationObserver observer : observers) {
            observer.onNotification(notification);
        }
    }
}
