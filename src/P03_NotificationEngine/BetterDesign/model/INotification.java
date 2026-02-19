package P03_NotificationEngine.BetterDesign.model;

public interface INotification {
    String getContents();
    NotificationType getType();
    NotificationPriority getPriority();
    String getRecipient();
}
