package P03_NotificationEngine.MyDesign.Notification;

public class Notification implements INotification{
    private final String contents;

    public Notification(String message) {
        this.contents = message;
    }

    @Override
    public String getContents() {
        return this.contents;
    }
}
