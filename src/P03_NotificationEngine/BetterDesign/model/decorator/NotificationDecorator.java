package P03_NotificationEngine.BetterDesign.model.decorator;

import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.NotificationPriority;
import P03_NotificationEngine.BetterDesign.model.NotificationType;

/**
 * Base decorator that delegates type, priority and recipient to the wrapped notification.
 * Subclasses only need to override getContents() to enrich the message.
 */
public abstract class NotificationDecorator implements INotification {

    protected final INotification notification;

    protected NotificationDecorator(INotification notification) {
        this.notification = notification;
    }

    @Override
    public NotificationType getType() {
        return notification.getType();
    }

    @Override
    public NotificationPriority getPriority() {
        return notification.getPriority();
    }

    @Override
    public String getRecipient() {
        return notification.getRecipient();
    }
}
