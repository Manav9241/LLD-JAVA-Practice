package P03_NotificationEngine.BetterDesign.logging;

import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.observer.INotificationObserver;

/**
 * Console-based logger that also acts as an INotificationObserver,
 * so it can be registered directly with the publisher without extra wiring.
 */
public class ConsoleNotificationLogger implements INotificationLogger, INotificationObserver {

    @Override
    public void log(INotification notification) {
        System.out.println("[LOG] New notification ["
                + notification.getType() + "]["
                + notification.getPriority() + "] -> "
                + notification.getContents());
    }

    @Override
    public void logError(INotification notification, String error) {
        System.err.println("[ERROR] Failed to process notification ["
                + notification.getType() + "]: "
                + error + " -> " + notification.getContents());
    }

    @Override
    public void onNotification(INotification notification) {
        log(notification);
    }
}
