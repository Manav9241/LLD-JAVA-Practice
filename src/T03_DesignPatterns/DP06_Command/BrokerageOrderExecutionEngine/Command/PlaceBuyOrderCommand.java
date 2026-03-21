package T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command;

import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Receiver.OrderBook;

public class PlaceBuyOrderCommand implements ICommand {
    private final OrderBook domain;
    private final String ticker;
    private final String orderId;
    private final int quantity;
    private final double price;

    public PlaceBuyOrderCommand(OrderBook orderBook, String ticker, String orderId, int quantity, double price) {
        this.domain = orderBook;
        this.ticker = ticker;
        this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public void execute() {
        domain.placeBuyOrder(ticker, quantity, price);
    }

    @Override
    public void undo() {
        domain.cancelOrder(orderId);
    }

    @Override
    public String getAuditDescription() {
        return String.format("BUY: %d X %s @ Rs.%.2f", quantity, ticker, price);
    }

}
