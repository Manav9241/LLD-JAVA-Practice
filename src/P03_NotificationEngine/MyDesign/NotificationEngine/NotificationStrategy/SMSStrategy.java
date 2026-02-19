package P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy;

public class SMSStrategy implements INotificationStrategy{
    private final String phoneNumber;

    public SMSStrategy(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void sendNotification(String messageContent) {
        System.out.println("Sending SMS Notification to " + this.phoneNumber + " : \n" + messageContent);
    }
}
