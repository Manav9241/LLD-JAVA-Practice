package P03_NotificationEngine.BetterDesign.routing;

import P03_NotificationEngine.BetterDesign.channel.INotificationChannel;
import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.NotificationType;

public interface INotificationRouter {
    /** Register a channel for all notification types. */
    void registerChannel(INotificationChannel channel);

    /** Register a channel for a specific notification type only. */
    void registerChannel(NotificationType type, INotificationChannel channel);

    /** Route the notification to all eligible channels. */
    void route(INotification notification);
}
