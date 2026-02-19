package P03_NotificationEngine.MyDesign.Notification.NotificationDecorator;

import P03_NotificationEngine.MyDesign.Notification.INotification;

public class TimeStampDecorator extends INotificationDecorator{
    public TimeStampDecorator(INotification notif) {
        super(notif);
    }

    public String getContents() {
        return "[2026-02-19 22:02:00]  " + this.notification.getContents();
    }
}
