package P03_NotificationEngine.BetterDesign.channel;

import P03_NotificationEngine.BetterDesign.model.INotification;

public class EmailChannel extends AbstractNotificationChannel {

    private final String emailAddress;

    public EmailChannel(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String getChannelName() {
        return "Email[" + emailAddress + "]";
    }

    @Override
    public void send(INotification notification) {
        System.out.println("Sending Email to " + emailAddress + ":\n" + notification.getContents());
    }
}
