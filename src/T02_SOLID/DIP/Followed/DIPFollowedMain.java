package T02_SOLID.DIP.Followed;

public class DIPFollowedMain {
    static void main(String[] args) {
        ClientService service1 = new ClientService(new MongoDBDatabase());
        ClientService service2 = new ClientService(new SQLDatabase());

        service1.SaveToDB();
        service2.SaveToDB();
    }
}
