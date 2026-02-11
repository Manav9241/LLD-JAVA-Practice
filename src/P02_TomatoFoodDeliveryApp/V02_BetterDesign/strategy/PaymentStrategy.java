package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethod();
}
