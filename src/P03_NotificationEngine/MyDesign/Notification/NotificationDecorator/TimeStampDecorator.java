package P03_NotificationEngine.MyDesign.Notification.NotificationDecorator;

import P03_NotificationEngine.MyDesign.Notification.INotification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampDecorator extends INotificationDecorator{
    public TimeStampDecorator(INotification notif) {
        super(notif);
    }

    public String getContents() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimeStamp = now.format(formatter);
        StringBuilder builder = new StringBuilder();
        builder.append('[').append(formattedTimeStamp).append(']');
        return builder.toString() + "  " + this.notification.getContents();
    }
}
