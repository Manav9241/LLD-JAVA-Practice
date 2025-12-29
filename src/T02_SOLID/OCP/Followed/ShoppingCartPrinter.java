package T02_SOLID.OCP.Followed;

public class ShoppingCartPrinter {
    private ShoppingCart cart;

    public ShoppingCartPrinter(ShoppingCart cart) {
        this.cart = cart;
    }

    public void PrintInvoice(){
        System.out.println("Shopping Cart Invoice");
        System.out.println("Product\t\tPrice");
        for(Product product: cart.GetCartItems()) {
            System.out.println(product.name + "\t\t Rs." + product.price);
        }
        System.out.println("Total Bill: Rs." + cart.CalculateTotalPrice());
    }
}
