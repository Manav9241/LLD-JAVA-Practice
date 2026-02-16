package T03_DesignPatterns.DP04_Observer;

public class Subscriber implements ISubscriber{
    private final String name;
    private final Channel channel;

    public Subscriber(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    @Override
    public void update() {
        System.out.println("Hey " + this.name + "!!\nNew Video Uploaded in " + channel.getName() + ":\n" + channel.getLatestVideo());
    }
}
