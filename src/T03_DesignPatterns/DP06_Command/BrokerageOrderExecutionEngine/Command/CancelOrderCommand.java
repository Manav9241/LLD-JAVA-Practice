package T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command;

import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Receiver.OrderBook;

public class CancelOrderCommand implements ICommand {
    private final OrderBook domain;
    private final String orderId;

    public CancelOrderCommand(OrderBook orderBook, String orderId) {
        this.domain = orderBook;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        domain.cancelOrder(orderId);
    }

    @Override
    public void undo() {
        domain.reinstateOrder(orderId);
    }

    @Override
    public String getAuditDescription() {
        return String.format("Cancel Order: %s", orderId);
    }

}
