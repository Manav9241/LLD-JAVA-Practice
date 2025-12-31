package T02_SOLID.DIP.Violated;

public class DIPViolatedMain {
    static void main(String[] args) {
        UserService userService = new UserService(
                new MongoDB(),
                new SQLDB()
        );

        userService.SaveToMongoDB();
        userService.SaveToSQLDB();
    }
}
