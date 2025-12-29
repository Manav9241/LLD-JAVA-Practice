package T02_SOLID.OCP.Violated;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> cart;

    public ShoppingCart() {
        cart = new ArrayList<Product>();
    }

    public void AddItem(Product item) {
        cart.add(item);
    }

    public List<Product> GetCartItems() {
        return cart;
    }

    public double CalculateTotalPrice() {
        double total = 0.0;
        for(Product product: cart) {
            total += product.price;
        }

        return total;
    }
}
