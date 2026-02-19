package P03_NotificationEngine.BetterDesign.storage;

import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.NotificationType;
import P03_NotificationEngine.BetterDesign.observer.INotificationObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Thread-unsafe in-memory store — suitable for single-threaded demos/tests.
 * Implements INotificationObserver so it auto-stores every published notification.
 * Swap this for a database-backed implementation without changing any other class.
 */
public class InMemoryNotificationStore implements INotificationStore, INotificationObserver {

    private final List<INotification> store = new ArrayList<>();

    @Override
    public void store(INotification notification) {
        store.add(notification);
    }

    @Override
    public List<INotification> getAll() {
        return Collections.unmodifiableList(store);
    }

    @Override
    public List<INotification> getByType(NotificationType type) {
        return store.stream()
                .filter(n -> n.getType() == type)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public void onNotification(INotification notification) {
        store(notification);
    }
}
