package P03_NotificationEngine.MyDesign.NotificationEngine.NotificationObservable;

import P03_NotificationEngine.MyDesign.NotificationEngine.Observers.IObserver;

public interface IObservable {
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers();
}
