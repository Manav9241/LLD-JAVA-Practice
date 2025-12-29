package T02_SOLID.OCP.Violated;

public class ShoppingCartStorage {
    private ShoppingCart cart;

    public ShoppingCartStorage(ShoppingCart cart) {
        this.cart = cart;
    }

    public void SaveToFileStorage() {
        System.out.println("Saving following Cart Items to File Storage...");
    }

    public void SaveToPersistentDB() {
        System.out.println("Saving following Cart Items to SQL...");
    }

    public void SaveToMongoose() {
        System.out.println("Saving following Cart Items to Mongoose...");
    }
}
