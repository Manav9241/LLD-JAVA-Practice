package T02_SOLID.DIP.Followed;

public class MongoDBDatabase implements DBPersistence{
    @Override
    public void Save() {
        System.out.println("Saving to MongoDB Database...\nSuccessful");
    }
}
