package P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy;

public class EmailStrategy implements INotificationStrategy{
    private final String emailID;

    public EmailStrategy(String email) {
        this.emailID = email;
    }

    @Override
    public void sendNotification(String messageContent) {
        System.out.println("Sending Email Notification to " + this.emailID + " : \n" + messageContent);
    }
}
