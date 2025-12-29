package T02_SOLID.OCP.Violated;

public class OCPViolatedMain {
    static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.AddItem(new Product("Laptop", 150000.00));
        cart.AddItem(new Product("Console", 100000.00));

        ShoppingCartPrinter invoicePrinter = new ShoppingCartPrinter(cart);
        invoicePrinter.PrintInvoice();

        ShoppingCartStorage dbStorage = new ShoppingCartStorage(cart);
        dbStorage.SaveToFileStorage();
        dbStorage.SaveToMongoose();
        dbStorage.SaveToPersistentDB();
    }
}
