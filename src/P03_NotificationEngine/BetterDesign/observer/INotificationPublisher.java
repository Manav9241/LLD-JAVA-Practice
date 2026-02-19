package P03_NotificationEngine.BetterDesign.observer;

import P03_NotificationEngine.BetterDesign.model.INotification;

public interface INotificationPublisher {
    void addObserver(INotificationObserver observer);
    void removeObserver(INotificationObserver observer);
    void publish(INotification notification);
}
