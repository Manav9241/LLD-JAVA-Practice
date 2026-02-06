package T03_DesignPatterns.DP02_Factory;

import T03_DesignPatterns.DP02_Factory.BurgerProduct.BasicBurger;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.CheeseBurger;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.IBurger;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.PremiumBurger;

import java.util.Scanner;

public class SimpleFactoryMain {
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

    public static void main(String[] args) {
        System.out.println("Input string for Simple Burger choice: basic, cheese, premium");
        Scanner sc = new Scanner(System.in);
        String choice = sc.next();

        IBurger selectedBurger = new SimpleFactoryMain().getBurger(choice);
        if (selectedBurger == null) {
            System.out.println("Wrong choice");
        } else {
            selectedBurger.prepare();
        }
    }
}
