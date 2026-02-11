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
        String maskedUpiId = maskUpiId();
        System.out.println("Processing UPI payment of ₹" + amount + " via " + maskedUpiId);
        System.out.println("Payment successful!");
        return true;
    }

    private String maskUpiId() {
        if (upiId.contains("@")) {
            int atIndex = upiId.indexOf("@");
            String prefix = upiId.substring(0, Math.min(3, atIndex));
            String suffix = upiId.substring(atIndex);
            return prefix + "***" + suffix;
        }
        return "***" + upiId.substring(Math.max(0, upiId.length() - 4));
    }

    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
}
