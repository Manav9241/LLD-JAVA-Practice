package T03_DesignPatterns.DP04_Observer;

public class ObserverMain {
    public static void main(String[] args) {
        Channel channel = new Channel("Coder Army");

        Subscriber subscriber1 = new Subscriber("Manav", channel);
        Subscriber subscriber2 = new Subscriber("Rahul", channel);
        Subscriber subscriber3 = new Subscriber("Ashish", channel);

        channel.subscribe(subscriber1);
        channel.subscribe(subscriber2);
        channel.subscribe(subscriber3);

        channel.uploadVideo("Observer Pattern - Part 1 | LLD System Design Series | Coder Army");

        channel.unsubscribe(subscriber2);

        System.out.println("\n\n");
        channel.uploadVideo("Observer Pattern - Part 1 | LLD System Design Series | Coder Army");
    }
}
