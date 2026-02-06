package T03_DesignPatterns.DP02_Factory.BurgerProduct;

public class CheeseBurger implements IBurger{
    public void prepare() {
        System.out.println("Cheese Burger in preparation...");
    }
}
