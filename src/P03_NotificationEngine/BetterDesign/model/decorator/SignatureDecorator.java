package P03_NotificationEngine.BetterDesign.model.decorator;

import P03_NotificationEngine.BetterDesign.model.INotification;

public class SignatureDecorator extends NotificationDecorator {

    private final String signature;

    public SignatureDecorator(INotification notification, String signature) {
        super(notification);
        this.signature = signature;
    }

    @Override
    public String getContents() {
        return notification.getContents() + "\n-- " + signature + "\n";
    }
}
