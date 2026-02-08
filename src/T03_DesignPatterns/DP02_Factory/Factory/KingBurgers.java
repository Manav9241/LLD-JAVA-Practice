package T03_DesignPatterns.DP02_Factory.Factory;

import T03_DesignPatterns.DP02_Factory.BurgerProduct.*;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.CheeseGarlicBread;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.IGarlicBread;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.SimpleGarlicBread;

public class KingBurgers implements IFactory {
    public IBurger getBurger(String burgerType) {
        if (burgerType.equalsIgnoreCase("basic")) {
            return new BasicBurger();
        } else if (burgerType.equalsIgnoreCase("cheese")) {
            return new CheeseBurger();
        } else if (burgerType.equalsIgnoreCase("premium")) {
            return new PremiumBurger();
        }
        return null;
    }

    @Override
    public IGarlicBread getGarlicBread(String type) {
        if (type.equalsIgnoreCase("simple")) {
            return new SimpleGarlicBread();
        } else if (type.equalsIgnoreCase("cheese")) {
            return new CheeseGarlicBread();
        }
        return null;
    }
}
