package T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command.ICommand;

public class BrokerageGatewayInvoker {
    private boolean isMarketOpen;
    private List<ICommand> orderQueue;
    private Deque<ICommand> orderHistory;
    private List<String> auditTrailLogs;

    public BrokerageGatewayInvoker() {
        this.isMarketOpen = false;
        this.orderQueue = new ArrayList<>();
        this.orderHistory = new ArrayDeque<>();
        this.auditTrailLogs = new ArrayList<>();
    }

    private void flushOrderQueue() {
        System.out.printf("[Gateway]\tMarket is open -- flushing %d queued orders: %n", orderQueue.size());
        if (!orderQueue.isEmpty()) {
            for (ICommand command : orderQueue) {
                executeNow(command);
            }
        }
        System.out.println();
        orderQueue.clear();
    }

    public void marketOpen() {
        isMarketOpen = true;
        flushOrderQueue();
    }

    public void marketClose() {
        isMarketOpen = false;
    }

    public void queueOrder(ICommand command) {
        orderQueue.add(command);
        System.out.println(" [Gateway]\tQueued: " + command.getAuditDescription());
    }

    public void executeNow(ICommand command) {
        if (isMarketOpen) {
            command.execute();
            orderHistory.push(command);
            auditTrailLogs.add("[EXECUTED]\t" + command.getAuditDescription());
        } else {
            queueOrder(command);
        }
    }

    public void undoLastOrder() {
        if (orderHistory.isEmpty()) {
            auditTrailLogs.add("[UNDONE]\tNothing to undo.");
            return;
        }
        ICommand command = orderHistory.pop();
        command.undo();
        auditTrailLogs.add("[UNDONE]\t" + command.getAuditDescription());
    }

    public void printAuditTrail() {
        System.out.println("\n[Audit Trail]");
        for (String log : auditTrailLogs) {
            System.out.println(log);
        }
    }
}
