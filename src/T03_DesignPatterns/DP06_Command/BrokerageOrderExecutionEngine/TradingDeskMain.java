package T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine;

import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command.CancelOrderCommand;
import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command.PlaceBuyOrderCommand;
import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command.PlaceSellOrderCommand;
import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Receiver.OrderBook;

public class TradingDeskMain {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();

        BrokerageGatewayInvoker gateway = new BrokerageGatewayInvoker();

        gateway.marketClose();

        gateway.executeNow(new PlaceBuyOrderCommand(orderBook, "HDFC", "ORD-01", 100, 172.22));
        gateway.executeNow(new PlaceBuyOrderCommand(orderBook, "TCS", "ORD-02", 150, 140.75));
        gateway.executeNow(new PlaceSellOrderCommand(orderBook, "TCS", "ORD-03", 120, 150.00));

        gateway.marketOpen();

        gateway.executeNow(new PlaceBuyOrderCommand(orderBook, "RELIANCE", "ORD-04", 100, 1473.75));
        gateway.executeNow(new CancelOrderCommand(orderBook, "ORD-03"));

        System.out.println("\n[Desk] Undoing last action...");
        gateway.undoLastOrder();

        gateway.printAuditTrail();
    }
}
