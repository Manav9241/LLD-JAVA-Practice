package P03_NotificationEngine.MyDesign;

import P03_NotificationEngine.MyDesign.Notification.INotification;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationObservable.NotificationObservable;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private static volatile NotificationService INSTANCE = null;
    private NotificationObservable observable;
    private List<INotification> notificatonStorage;

    private NotificationService() {
        observable = new NotificationObservable();
        notificatonStorage = new ArrayList<>();
    }

    public static NotificationService getInstance() {
        if (INSTANCE == null) {
            synchronized (NotificationService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotificationService();
                }
            }
        }
        return INSTANCE;
    }

    public void sendNotification(INotification notificaton) {
        observable.setNotification(notificaton);

        notificatonStorage.add(notificaton);
    }

    public NotificationObservable getObservable() {
        return this.observable;
    }
}
