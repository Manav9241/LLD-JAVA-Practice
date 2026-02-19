package P03_NotificationEngine.BetterDesign.observer;

import P03_NotificationEngine.BetterDesign.model.INotification;

/**
 * Push-based observer: the notification is passed directly into the callback,
 * eliminating the need for observers to know about (and pull from) the observable.
 */
public interface INotificationObserver {
    void onNotification(INotification notification);
}
