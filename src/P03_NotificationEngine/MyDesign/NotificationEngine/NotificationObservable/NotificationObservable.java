package P03_NotificationEngine.MyDesign.NotificationEngine.NotificationObservable;

import P03_NotificationEngine.MyDesign.Notification.INotification;
import P03_NotificationEngine.MyDesign.NotificationEngine.Observers.IObserver;

import java.util.ArrayList;
import java.util.List;

public class NotificationObservable implements IObservable{
    protected List<IObserver> observers;
    private INotification notification;

    public NotificationObservable() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(IObserver observer) {
        if (!observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(IObserver observer) {
        if (!observers.isEmpty()) {
            this.observers.remove(observer);
        }
    }

    public void notifyObservers() {
        for (IObserver observer: observers) {
            observer.update();
        }
    }

    public String getNotificationContent() {
        return notification.getContents();
    }

    public void setNotification(INotification notification) {
        this.notification = notification;
        notifyObservers();
    }
}
