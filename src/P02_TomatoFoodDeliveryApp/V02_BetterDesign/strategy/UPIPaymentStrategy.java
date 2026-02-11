package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

public class UPIPaymentStrategy implements PaymentStrategy {
    private final String upiId;

    public UPIPaymentStrategy(String upiId) {
        if (upiId == null || upiId.isEmpty()) {
            throw new IllegalArgumentException("UPI ID cannot be null or empty");
        }
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing UPI payment of ₹" + amount + " via " + upiId);
        System.out.println("Payment successful!");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
}
