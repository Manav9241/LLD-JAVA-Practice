package P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy;

public class CardPaymentStrategy implements IPaymentStrategy {
    private final String cardNumber;

    public CardPaymentStrategy(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Payment for Rs." + amount + " from " + cardNumber + " via Card Payment mode, Processed Successfully...");
    }
}
