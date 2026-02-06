package T03_DesignPatterns.DP02_Factory.BurgerProduct;

public class BasicBurger implements IBurger{
    public void prepare() {
        System.out.println("Basic Burger in preparation...");
    }
}
