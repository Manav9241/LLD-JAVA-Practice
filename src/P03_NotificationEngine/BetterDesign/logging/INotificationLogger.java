package P03_NotificationEngine.BetterDesign.logging;

import P03_NotificationEngine.BetterDesign.model.INotification;

/**
 * Abstraction for notification logging.
 * Allows swapping stdout logging for a file logger, structured logger, etc.
 */
public interface INotificationLogger {
    void log(INotification notification);
    void logError(INotification notification, String error);
}
