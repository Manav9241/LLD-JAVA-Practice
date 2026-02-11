package T03_DesignPatterns.DP02_Factory;

import T03_DesignPatterns.DP02_Factory.BurgerProduct.IBurger;
import T03_DesignPatterns.DP02_Factory.Factory.IFactory;
import T03_DesignPatterns.DP02_Factory.Factory.KingBurgers;
import T03_DesignPatterns.DP02_Factory.Factory.SinghBurgers;
import T03_DesignPatterns.DP02_Factory.GarlicBreadProduct.IGarlicBread;

import java.util.Scanner;

public class AbstractFactoryMethodMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select product: burger, garlicbread");
        String product = sc.nextLine();

        System.out.println("Wheat bread? y or n");
        boolean isWheat = String.valueOf(sc.nextLine().charAt(0)).equalsIgnoreCase("y");

        IFactory factoryInstance;

        if(isWheat) {
            factoryInstance = new SinghBurgers();
        } else {
            factoryInstance = new KingBurgers();
        }

        if (product.equalsIgnoreCase("burger")) {
            System.out.println("Select product type: basic, cheese, premium");
            String productType = sc.nextLine();
            IBurger finalProduct = factoryInstance.getBurger(productType);
            finalProduct.prepare();
        } else if (product.equalsIgnoreCase("garlicbread")) {
            System.out.println("Select product type: simple, cheese");
            String productType = sc.nextLine();
            IGarlicBread finalProduct = factoryInstance.getGarlicBread(productType);
            finalProduct.prepare();
        } else {
            System.out.println("Wrong Product");
        }
    }
}
