package T02_SOLID.OCP.Followed;

import T02_SOLID.OCP.Followed.DBPersistence.FileSystemPersistence;
import T02_SOLID.OCP.Followed.DBPersistence.MongoosePersistence;
import T02_SOLID.OCP.Followed.DBPersistence.SQLPersistence;

public class OCPFollowedMain {
    static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.AddItem(new Product("Laptop", 150000.00));
        cart.AddItem(new Product("Console", 100000.00));

        ShoppingCartPrinter invoicePrinter = new ShoppingCartPrinter(cart);
        invoicePrinter.PrintInvoice();

        IPersistence persistenceContext = new FileSystemPersistence();
        persistenceContext.Save(cart);

        persistenceContext = new MongoosePersistence();
        persistenceContext.Save(cart);

        persistenceContext = new SQLPersistence();
        persistenceContext.Save(cart);
    }
}
