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
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method must not be null");
        }

        if (paymentMethod.equalsIgnoreCase("upi")) {
            return new UPIPaymentStrategy(accountDetails);
        } else if (paymentMethod.equalsIgnoreCase("card")) {
            return new CardPaymentStrategy(accountDetails);
        } else {
            throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
    }
}
