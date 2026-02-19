package P03_NotificationEngine.BetterDesign.model.decorator;

import P03_NotificationEngine.BetterDesign.model.INotification;

/**
 * New decorator that prepends the notification's priority level to the message,
 * making it immediately visible to recipients at a glance.
 */
public class PriorityTagDecorator extends NotificationDecorator {

    public PriorityTagDecorator(INotification notification) {
        super(notification);
    }

    @Override
    public String getContents() {
        return "[" + notification.getPriority() + "] " + notification.getContents();
    }
}
