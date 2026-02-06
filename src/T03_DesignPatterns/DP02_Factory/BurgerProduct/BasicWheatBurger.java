package T03_DesignPatterns.DP02_Factory.BurgerProduct;

public class BasicWheatBurger implements IBurger{
    public void prepare() {
        System.out.println("Basic Wheat Burger preparing...");
    }
}
