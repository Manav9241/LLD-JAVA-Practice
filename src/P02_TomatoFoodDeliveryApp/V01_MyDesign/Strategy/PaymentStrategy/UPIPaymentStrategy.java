package P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy;

public class UPIPaymentStrategy implements IPaymentStrategy {
    private final String mobileNumber;

    public UPIPaymentStrategy(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public void processPayment(double amount){
        System.out.println("Payment for Rs." + amount + " from " + mobileNumber + " via UPI Payment mode, Processed Successfully...");
    }
}
