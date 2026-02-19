package P03_NotificationEngine.BetterDesign.channel;

import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.NotificationPriority;
import P03_NotificationEngine.BetterDesign.model.NotificationType;

import java.util.EnumSet;
import java.util.Set;

/**
 * Base channel that adds optional type and priority filters.
 * Concrete channels extend this and only need to implement getChannelName() and send().
 * Filtering is configured fluently via supportingTypes() / withMinimumPriority().
 */
public abstract class AbstractNotificationChannel implements INotificationChannel {

    private Set<NotificationType> supportedTypes = EnumSet.allOf(NotificationType.class);
    private NotificationPriority minimumPriority = NotificationPriority.LOW;

    /** Restrict this channel to the specified notification types only. */
    public AbstractNotificationChannel supportingTypes(NotificationType... types) {
        this.supportedTypes = EnumSet.copyOf(Set.of(types));
        return this;
    }

    /** Only deliver notifications at or above the given priority. */
    public AbstractNotificationChannel withMinimumPriority(NotificationPriority priority) {
        this.minimumPriority = priority;
        return this;
    }

    @Override
    public boolean canHandle(INotification notification) {
        return supportedTypes.contains(notification.getType())
                && notification.getPriority().ordinal() >= minimumPriority.ordinal();
    }
}
