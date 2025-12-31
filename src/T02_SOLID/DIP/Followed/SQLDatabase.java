package T02_SOLID.DIP.Followed;

public class SQLDatabase implements DBPersistence{
    @Override
    public void Save() {
        System.out.println("Saving to SQl Database...\nSuccessful");
    }
}
