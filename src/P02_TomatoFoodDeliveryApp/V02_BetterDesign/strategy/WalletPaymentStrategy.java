package P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy;

public class WalletPaymentStrategy implements PaymentStrategy {
    private final String walletId;

    public WalletPaymentStrategy(String walletId) {
        if (walletId == null || walletId.isEmpty()) {
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }
        this.walletId = walletId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing Wallet payment of ₹" + amount + " via " + walletId);
        System.out.println("Payment successful!");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "WALLET";
    }
}
