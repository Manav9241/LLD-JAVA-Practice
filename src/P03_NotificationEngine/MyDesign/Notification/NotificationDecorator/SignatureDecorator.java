package P03_NotificationEngine.MyDesign.Notification.NotificationDecorator;

import P03_NotificationEngine.MyDesign.Notification.INotification;

public class SignatureDecorator extends INotificationDecorator{
    private final String signature;

    public SignatureDecorator(INotification notif, String signature) {
        super(notif);
        this.signature = signature;
    }

    public String getContents() {
        return (this.notification.getContents() + "\n-- " + this.signature + "\n\n");
    }
}
