package P03_NotificationEngine.BetterDesign.channel;

import P03_NotificationEngine.BetterDesign.model.INotification;

public class SMSChannel extends AbstractNotificationChannel {

    private final String phoneNumber;

    public SMSChannel(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getChannelName() {
        return "SMS[" + phoneNumber + "]";
    }

    @Override
    public void send(INotification notification) {
        System.out.println("Sending SMS to " + phoneNumber + ":\n" + notification.getContents());
    }
}
