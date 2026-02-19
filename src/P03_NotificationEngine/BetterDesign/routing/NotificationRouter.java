package P03_NotificationEngine.BetterDesign.routing;

import P03_NotificationEngine.BetterDesign.channel.INotificationChannel;
import P03_NotificationEngine.BetterDesign.model.INotification;
import P03_NotificationEngine.BetterDesign.model.NotificationType;
import P03_NotificationEngine.BetterDesign.observer.INotificationObserver;

import java.util.*;

/**
 * Routes incoming notifications to the appropriate channels.
 * Implements INotificationObserver so it can be plugged directly into the publisher.
 *
 * Routing logic:
 *   1. Global channels (registered without a type) receive every notification they canHandle().
 *   2. Type-specific channels receive only notifications of the matching type they canHandle().
 */
public class NotificationRouter implements INotificationRouter, INotificationObserver {

    private final List<INotificationChannel> globalChannels = new ArrayList<>();
    private final Map<NotificationType, List<INotificationChannel>> typedChannels =
            new EnumMap<>(NotificationType.class);

    @Override
    public void registerChannel(INotificationChannel channel) {
        globalChannels.add(channel);
    }

    @Override
    public void registerChannel(NotificationType type, INotificationChannel channel) {
        typedChannels.computeIfAbsent(type, k -> new ArrayList<>()).add(channel);
    }

    @Override
    public void route(INotification notification) {
        for (INotificationChannel channel : globalChannels) {
            if (channel.canHandle(notification)) {
                channel.send(notification);
            }
        }

        List<INotificationChannel> specific =
                typedChannels.getOrDefault(notification.getType(), Collections.emptyList());
        for (INotificationChannel channel : specific) {
            if (channel.canHandle(notification)) {
                channel.send(notification);
            }
        }
    }

    @Override
    public void onNotification(INotification notification) {
        route(notification);
    }
}
