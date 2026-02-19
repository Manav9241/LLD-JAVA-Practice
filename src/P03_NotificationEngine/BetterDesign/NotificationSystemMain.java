package P03_NotificationEngine.BetterDesign;

import P03_NotificationEngine.BetterDesign.channel.EmailChannel;
import P03_NotificationEngine.BetterDesign.channel.PushNotificationChannel;
import P03_NotificationEngine.BetterDesign.channel.SMSChannel;
import P03_NotificationEngine.BetterDesign.logging.ConsoleNotificationLogger;
import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.Notification;
import P03_NotificationEngine.BetterDesign.model.NotificationPriority;
import P03_NotificationEngine.BetterDesign.model.NotificationType;
import P03_NotificationEngine.BetterDesign.model.decorator.PriorityTagDecorator;
import P03_NotificationEngine.BetterDesign.model.decorator.SignatureDecorator;
import P03_NotificationEngine.BetterDesign.model.decorator.TimestampDecorator;
import P03_NotificationEngine.BetterDesign.routing.NotificationRouter;
import P03_NotificationEngine.BetterDesign.service.NotificationService;
import P03_NotificationEngine.BetterDesign.storage.InMemoryNotificationStore;

public class NotificationSystemMain {

    public static void main(String[] args) {

        // ── 1. Setup (performed once at application startup via DI) ────────────

        // Service: no Singleton, fully injectable
        NotificationService notificationService = new NotificationService();

        // Logger observer — registers itself only when explicitly asked
        ConsoleNotificationLogger logger = new ConsoleNotificationLogger();
        notificationService.registerObserver(logger);

        // Storage observer — persists every published notification
        InMemoryNotificationStore store = new InMemoryNotificationStore();
        notificationService.registerObserver(store);

        // Router: routes notifications to appropriate delivery channels
        NotificationRouter router = new NotificationRouter();

        // Global channels: accept all notification types
        router.registerChannel(new EmailChannel("customer@example.com"));
        router.registerChannel(new PushNotificationChannel());

        // Type-specific: ALERT notifications at HIGH or above also go via SMS
        router.registerChannel(
                NotificationType.ALERT,
                new SMSChannel("+91 9876543210")
                        .withMinimumPriority(NotificationPriority.HIGH));

        notificationService.registerObserver(router);

        // ── 2. Usage: send notifications ──────────────────────────────────────

        // Notification 1 — INFO, order shipped
        INotification orderUpdate = new Notification.Builder("Your order has been shipped!")
                .type(NotificationType.INFO)
                .priority(NotificationPriority.MEDIUM)
                .recipient("customer@example.com")
                .build();
        orderUpdate = new TimestampDecorator(orderUpdate);
        orderUpdate = new SignatureDecorator(orderUpdate, "Customer Care");

        System.out.println("=== Sending INFO notification ===");
        notificationService.sendNotification(orderUpdate);

        System.out.println();

        // Notification 2 — ALERT, CRITICAL priority → triggers all channels including SMS
        INotification securityAlert = new Notification.Builder("Suspicious login detected on your account!")
                .type(NotificationType.ALERT)
                .priority(NotificationPriority.CRITICAL)
                .recipient("customer@example.com")
                .build();
        securityAlert = new PriorityTagDecorator(securityAlert);
        securityAlert = new TimestampDecorator(securityAlert);

        System.out.println("=== Sending ALERT notification ===");
        notificationService.sendNotification(securityAlert);

        System.out.println();

        // Notification 3 — PROMOTION, LOW priority → only channels that accept LOW
        INotification promo = new Notification.Builder("Get 20% off on your next order! Use code SAVE20.")
                .type(NotificationType.PROMOTION)
                .priority(NotificationPriority.LOW)
                .recipient("customer@example.com")
                .build();
        promo = new TimestampDecorator(promo);

        System.out.println("=== Sending PROMOTION notification ===");
        notificationService.sendNotification(promo);

        System.out.println();

        // ── 3. Query stored notifications ─────────────────────────────────────
        System.out.println("Total notifications stored : " + store.size());
        System.out.println("ALERT notifications stored : " + store.getByType(NotificationType.ALERT).size());
    }
}
