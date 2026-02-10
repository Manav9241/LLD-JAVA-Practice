package P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy.CardPaymentStrategy;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy.IPaymentStrategy;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy.UPIPaymentStrategy;

public class FactoryPaymentStrategy {
    private static FactoryPaymentStrategy instance = null;

    private FactoryPaymentStrategy(){}

    public static FactoryPaymentStrategy getInstance() {
        if (instance == null) {
            instance = new FactoryPaymentStrategy();
        }
        return instance;
    }

    public IPaymentStrategy createPaymentStrategyObject(String paymentMethod, String accountDetails) {
        if (paymentMethod.equalsIgnoreCase("upi")) {
            return new UPIPaymentStrategy(accountDetails);
        } else if (paymentMethod.equalsIgnoreCase("card")) {
            return new CardPaymentStrategy(accountDetails);
        } else {
            System.out.println("Wrong payment choice!!!");
            return null;
        }
    }
}
