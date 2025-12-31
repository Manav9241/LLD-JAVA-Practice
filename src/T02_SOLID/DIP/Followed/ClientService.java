package T02_SOLID.DIP.Followed;

public class ClientService {
    private final DBPersistence instance;

    public ClientService(DBPersistence instance) {
        this.instance = instance;
    }

    public void SaveToDB(){
        instance.Save();
    }
}
