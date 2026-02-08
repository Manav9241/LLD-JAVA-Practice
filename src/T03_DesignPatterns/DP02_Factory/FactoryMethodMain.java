package T03_DesignPatterns.DP02_Factory;

import T03_DesignPatterns.DP02_Factory.Factory.IFactory;
import T03_DesignPatterns.DP02_Factory.Factory.KingBurgers;
import T03_DesignPatterns.DP02_Factory.BurgerProduct.*;
import T03_DesignPatterns.DP02_Factory.Factory.SinghBurgers;

import java.util.Scanner;

public class FactoryMethodMain {
    public static void main(String[] args) {
        System.out.println("Input string for Simple Burger choice: basic, cheese, premium");
        Scanner sc = new Scanner(System.in);
        String choice = sc.next();

        System.out.println("select want wheat burger: y or n");
        String isWheat = String.valueOf(sc.next().charAt(0));

        IFactory burgerFactory = isWheat.equalsIgnoreCase("y") ?
                new SinghBurgers()
                : new KingBurgers();

        IBurger selectedBurger = burgerFactory.getBurger(choice);

        if (selectedBurger == null) {
            System.out.println("Wrong choice");
        } else {
            selectedBurger.prepare();
        }
    }
}
