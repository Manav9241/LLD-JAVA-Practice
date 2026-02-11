package T03_DesignPatterns.DP02_Factory.BurgerProduct;

public class PremiumBurger implements IBurger{
    public void prepare() {
        System.out.println("Premium Burger with cheese and lettuce in preparation...");
    }
}
