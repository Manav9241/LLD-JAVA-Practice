package T02_SOLID.SRP.Violated;

public class SRPViolatedMain {
    static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.AddItem(new Product("laptop", 50000.00));
        cart.AddItem(new Product("Mouse", 1500.00));
        cart.ViewCart();
        cart.SaveToDatabase();
        cart.PrintInvoice();
    }
}
