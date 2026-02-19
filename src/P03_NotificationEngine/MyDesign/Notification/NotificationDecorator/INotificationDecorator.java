package P03_NotificationEngine.MyDesign.Notification.NotificationDecorator;

import P03_NotificationEngine.MyDesign.Notification.INotification;

public abstract class INotificationDecorator implements INotification {
    protected INotification notification;

    public INotificationDecorator(INotification notif) {
        this.notification = notif;
    }
}
