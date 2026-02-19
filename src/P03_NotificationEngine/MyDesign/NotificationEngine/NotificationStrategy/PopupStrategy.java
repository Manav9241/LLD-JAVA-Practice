package P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy;

public class PopupStrategy implements INotificationStrategy{
    @Override
    public void sendNotification(String messageContent) {
        System.out.println("Sending Popup Notification : \n" + messageContent);
    }
}
