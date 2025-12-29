package T02_SOLID.SRP.Violated;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> cart;

    public ShoppingCart() {
        cart = new ArrayList<Product>();
    }

    public void AddItem(Product item){
        cart.add(item);
        System.out.println(item + " Added to ShoppingCart");
    }

    public void ViewCart() {
        System.out.println(cart);
    }

    public double CalculateTotalPrice() {
        double total = 0;
        for(Product product: cart) {
            total += product.price;
        }
        return total;
    }

    public void PrintInvoice(){
        System.out.println("Shopping Cart Invoice");
        System.out.println("Product\t\tPrice");
        for(Product product: cart) {
            System.out.println(product.name + "\t\t Rs." + product.price);
        }
        System.out.println("Total Bill: Rs." + CalculateTotalPrice());
    }

    public void SaveToDatabase(){
        System.out.println("Saving Cart Details to Database.....");
        System.out.println("Successfull");
    }
}
