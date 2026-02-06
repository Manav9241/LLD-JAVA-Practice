package T03_DesignPatterns.DP02_Factory.BurgerProduct;

public class PremiumWheatBurger implements IBurger{
    public void prepare() {
        System.out.println("Preparing Premium Wheat burger with cheese and lettuce...");
    }
}
