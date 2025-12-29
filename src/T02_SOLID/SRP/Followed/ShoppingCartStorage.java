package T02_SOLID.SRP.Followed;

public class ShoppingCartStorage {
    ShoppingCart cart;

    public ShoppingCartStorage(ShoppingCart cart) {
        this.cart = cart;
    }

    public void SaveToDatabase() {
        System.out.println("Saving following Cart Items to Database...");
        System.out.println(cart.GetCartItems());
        System.out.println("Save to DB Successfull!!");
    }
}
