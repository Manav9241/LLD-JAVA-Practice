package T02_SOLID.DIP.Violated;

public class UserService {
    private MongoDB mongoDB;
    private SQLDB sql;

    public UserService(MongoDB mongoDB, SQLDB sql) {
        this.mongoDB = mongoDB;
        this.sql = sql;
    }

    public void SaveToMongoDB() {
        mongoDB.Save();
    }

    public void SaveToSQLDB() {
        sql.Save();
    }
}
