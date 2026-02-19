package P03_NotificationEngine.BetterDesign.channel;

import P03_NotificationEngine.BetterDesign.model.INotification;

public class PushNotificationChannel extends AbstractNotificationChannel {

    @Override
    public String getChannelName() {
        return "PushNotification";
    }

    @Override
    public void send(INotification notification) {
        System.out.println("Sending Push Notification:\n" + notification.getContents());
    }
}
