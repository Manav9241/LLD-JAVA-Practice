package P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy;

public class CardPaymentStrategy implements IPaymentStrategy {
    private final String cardNumber;

    public CardPaymentStrategy(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    private String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return "****";
        }
        int length = cardNumber.length();
        if (length <= 4) {
            return "****" + cardNumber;
        }
        String lastFour = cardNumber.substring(length - 4);
        return "****" + lastFour;
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Payment for Rs." + amount + " from " + getMaskedCardNumber() + " via Card Payment mode, Processed Successfully...");
    }
}
