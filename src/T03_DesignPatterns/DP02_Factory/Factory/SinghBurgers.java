package T03_DesignPatterns.DP02_Factory.Factory;

import T03_DesignPatterns.DP02_Factory.BurgerProduct.BasicWheatBurger;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.CheeseWheatBurger;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.IBurger;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.PremiumWheatBurger;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.CheeseWheatGarlicBread;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.IGarlicBread;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.SimpleWheatGarlicBread;

public class SinghBurgers implements IFactory {
    public IBurger getBurger(String type) {
        if (type.equalsIgnoreCase("basic")) {
            return new BasicWheatBurger();
        } else if (type.equalsIgnoreCase("cheese")) {
            return new CheeseWheatBurger();
        } else if (type.equalsIgnoreCase("premium")) {
            return new PremiumWheatBurger();
        }
        return null;
    }

    @Override
    public IGarlicBread getGarlicBread(String type) {
        if (type.equalsIgnoreCase("simple")) {
            return new SimpleWheatGarlicBread();
        } else if (type.equalsIgnoreCase("cheese")) {
            return new CheeseWheatGarlicBread();
        }
        return null;
    }
}

