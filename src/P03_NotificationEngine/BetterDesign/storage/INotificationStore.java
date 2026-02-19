package P03_NotificationEngine.BetterDesign.storage;

import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.NotificationType;

import java.util.List;

/**
 * Abstraction for notification storage.
 * Decouples the service from a specific persistence implementation.
 */
public interface INotificationStore {
    void store(INotification notification);
    List<INotification> getAll();
    List<INotification> getByType(NotificationType type);
    int size();
}
