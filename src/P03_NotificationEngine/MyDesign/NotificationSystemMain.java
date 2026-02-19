package P03_NotificationEngine.MyDesign;

import P03_NotificationEngine.MyDesign.Notification.INotification;
import P03_NotificationEngine.MyDesign.Notification.Notification;
import P03_NotificationEngine.MyDesign.Notification.NotificationDecorator.SignatureDecorator;
import P03_NotificationEngine.MyDesign.Notification.NotificationDecorator.TimeStampDecorator;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationEngine;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationObservable.NotificationObservable;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy.EmailStrategy;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy.PopupStrategy;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy.SMSStrategy;
import P03_NotificationEngine.MyDesign.NotificationEngine.Observers.Logger;

public class NotificationSystemMain {
    public static void main(String[] args) {

        // Create NotificationService.
        NotificationService notificationService = NotificationService.getInstance();

        // Create Logger Observer
        Logger logger = new Logger();

        // Create NotificationEngine observers.
        NotificationEngine notificationEngine = new NotificationEngine();

        notificationEngine.addNotificationStrategy(new EmailStrategy("random.person@gmail.com"));
        notificationEngine.addNotificationStrategy(new SMSStrategy("+91 9876543210"));
        notificationEngine.addNotificationStrategy(new PopupStrategy());

        // Create a notification with decorators.
        INotification notification = new Notification("Your order has been shipped!");
        notification = new TimeStampDecorator(notification);
        notification = new SignatureDecorator(notification, "Customer Care");

        notificationService.sendNotification(notification);
    }
}
