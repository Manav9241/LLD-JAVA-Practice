package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

public class CardPaymentStrategy implements PaymentStrategy {
    private final String cardNumber;

    public CardPaymentStrategy(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing Card payment of ₹" + amount + " via " + maskCardNumber());
        System.out.println("Payment successful!");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }

    private String maskCardNumber() {
        if (cardNumber.length() <= 4) {
            return cardNumber;
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
