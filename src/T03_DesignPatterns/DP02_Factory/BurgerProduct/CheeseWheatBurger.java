package T03_DesignPatterns.DP02_Factory.BurgerProduct;

public class CheeseWheatBurger implements IBurger{
    public void prepare() {
        System.out.println("Preparing Cheese Wheat Burger...");
    }
}
