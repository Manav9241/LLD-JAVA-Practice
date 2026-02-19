package P03_NotificationEngine.MyDesign.NotificationEngine;

import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationObservable.NotificationObservable;
import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationStrategy.INotificationStrategy;
import P03_NotificationEngine.MyDesign.NotificationEngine.Observers.IObserver;
import P03_NotificationEngine.MyDesign.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class NotificationEngine implements IObserver {
    private NotificationObservable observable;
    private List<INotificationStrategy> notificationStrategies;

    public NotificationEngine() {
        this.observable = NotificationService.getInstance().getObservable();
        this.observable.addObserver(this);
        this.notificationStrategies = new ArrayList<>();
    }

    public NotificationEngine(NotificationObservable observable) {
        this.observable = observable;
        this.notificationStrategies = new ArrayList<>();
    }

    @Override
    public void update() {
        String messageContents = observable.getNotificationContent();

        for (INotificationStrategy notificationStrategy: notificationStrategies) {
            notificationStrategy.sendNotification(messageContents);
        }
    }

    public void addNotificationStrategy(INotificationStrategy strategy) {
        notificationStrategies.add(strategy);
    }
}
