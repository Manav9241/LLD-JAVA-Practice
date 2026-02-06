package T03_DesignPatterns.DP02_Factory.Factory;

import T03_DesignPatterns.DP02_Factory.BurgerProduct.IBurger;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.IGarlicBread;

public interface IFactory {
    IBurger getBurger(String type);
    IGarlicBread getGarlicBread(String type);
}
