package T03_DesignPatterns.DP04_Observer;

import java.util.ArrayList;
import java.util.List;

public class Channel implements IChannel{
    private final List<ISubscriber> subscribers;
    private final String name;
    private String latestVideo;

    public Channel(String name) {
        this.subscribers = new ArrayList<>();
        this.name = name;
    }

    @Override
    public void subscribe(ISubscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (ISubscriber subscriber: subscribers) {
            subscriber.update();
        }
    }

    public String getLatestVideo() {
        return latestVideo;
    }

    private void setLatestVideo(String latestVideo) {
        this.latestVideo = latestVideo;
    }

    public String getName() {
        return this.name;
    }

    public void uploadVideo(String videoTitle) {
        this.setLatestVideo(videoTitle);
        notifySubscribers();
    }
}
