package P03_NotificationEngine.BetterDesign.service;

import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.observer.INotificationObserver;
import P03_NotificationEngine.BetterDesign.observer.INotificationPublisher;
import P03_NotificationEngine.BetterDesign.observer.NotificationPublisher;

/**
 * Facade for the notification subsystem.
 *
 * Key improvements over MyDesign.NotificationService:
 *  - Not a Singleton: no global mutable state; fully testable via constructor injection.
 *  - Accepts an INotificationPublisher so the publisher can be swapped in tests.
 *  - Observer registration is explicit (no hidden side-effects in constructors elsewhere).
 */
public class NotificationService {

    private final INotificationPublisher publisher;

    public NotificationService() {
        this.publisher = new NotificationPublisher();
    }

    public NotificationService(INotificationPublisher publisher) {
        this.publisher = publisher;
    }

    public void registerObserver(INotificationObserver observer) {
        publisher.addObserver(observer);
    }

    public void unregisterObserver(INotificationObserver observer) {
        publisher.removeObserver(observer);
    }

    public void sendNotification(INotification notification) {
        publisher.publish(notification);
    }
}
