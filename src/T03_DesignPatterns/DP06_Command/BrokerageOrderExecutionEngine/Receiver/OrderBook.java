package T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Receiver;

public class OrderBook {
    public void placeBuyOrder(String ticker, int quantity, double price) {
        System.out.printf(" [OrderBook]\tPlace Buy Order: %d X %s @ Rs.%.2f %n", quantity, ticker, price);
    }

    public void placeSellOrder(String ticker, int quantity, double price) {
        System.out.printf(" [OrderBook]\tPlace Sell Order: %d X %s @ Rs.%.2f %n", quantity, ticker, price);
    }

    public void cancelOrder(String orderId) {
        System.out.println(" [OrderBook]\tCancel Order ID: " + orderId);
    }

    public void reinstateOrder(String orderId) {
        System.out.println(" [OrderBook]\tReinstate Order ID: " + orderId);
    }
}
