package P03_NotificationEngine.MyDesign.NotificationEngine.Observers;

import P03_NotificationEngine.MyDesign.NotificationEngine.NotificationObservable.NotificationObservable;
import P03_NotificationEngine.MyDesign.NotificationService;

public class Logger implements IObserver{
    private final NotificationObservable observable;

    public Logger() {
        this.observable = NotificationService.getInstance().getObservable();
        this.observable.addObserver(this);
    }

    public Logger(NotificationObservable observable) {
        this.observable = observable;
    }

    public void update() {
        System.out.println("Logging New Notification : \n" + observable.getNotificationContent());
    }
}
