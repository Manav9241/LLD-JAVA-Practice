package T02_SOLID.OCP.Followed.DBPersistence;

import T02_SOLID.OCP.Followed.IPersistence;
import T02_SOLID.OCP.Followed.ShoppingCart;

public class MongoosePersistence implements IPersistence {
    @Override
    public void Save(ShoppingCart cart) {
        System.out.println("Starting to save cart items to Mongoose...");
        System.out.println(cart.GetCartItems());
        System.out.println("Successful Mongoose Cloud!!");
    }
}
