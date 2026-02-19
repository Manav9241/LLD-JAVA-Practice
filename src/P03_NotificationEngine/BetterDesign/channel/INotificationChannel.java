package P03_NotificationEngine.BetterDesign.channel;

import P03_NotificationEngine.BetterDesign.model.INotification;

/**
 * A self-contained delivery channel.
 * Replaces the two-step engine + strategy combo from MyDesign with a single, cohesive abstraction.
 */
public interface INotificationChannel {
    /** Human-readable identifier for this channel (e.g. "Email[user@x.com]"). */
    String getChannelName();

    /** Returns true if this channel is willing to handle the given notification. */
    boolean canHandle(INotification notification);

    /** Delivers the notification through this channel. */
    void send(INotification notification);
}
