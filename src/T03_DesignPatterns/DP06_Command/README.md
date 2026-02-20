# DP06 — Command Design Pattern

## What Is the Command Pattern?

The Command pattern is a **behavioural design pattern** that answers one question:

> What if an operation — not just data — needed to be treated as an object?

Normally, when you want something done, you call a method directly:

```java
orderBook.placeBuyOrder("AAPL", 100, 182.50);
```

That call happens immediately, leaves no trace, cannot be undone, cannot be queued, cannot be re-executed later.

The Command pattern says: **before you execute that operation, wrap it in an object first**.

```java
Command cmd = new PlaceBuyOrderCommand(orderBook, "AAPL", 100, 182.50);
// The operation is now data. You decide when and how to use it.
broker.submit(cmd);
```

Now the operation is a first-class value. You can:

- **Execute it now or later** (schedule it, queue it)
- **Store it** (audit log, history)
- **Undo it** by calling a reverse operation defined on the same object
- **Retry it** if it fails
- **Serialize it** and send it over a network
- **Compose multiple commands** into a batch that executes atomically

That is the entire idea. The rest is mechanics.

---

## The Core Structure

There are four roles:

```
Client          → Creates the Command object, wires it up
Command         → Interface: execute() and undo()
ConcreteCommand → Knows the operation + holds all state to execute AND reverse it
Receiver        → The domain object that does the actual work
Invoker         → Accepts commands, decides when to run them, manages history
```

The critical separation: **the Invoker never knows what it is executing**. It only ever sees the `Command` interface. This is not just good design taste — it is what makes queueing, logging, and undo mechanically possible without the Invoker being rewritten each time a new operation is added.

---

## Is This Pattern Just Academic Padding?

No. It is one of the most heavily used patterns in real systems — it just often goes by a different name.

| System                               | What Command looks like there                                                                                                                                                                 |
| ------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Every RDBMS**                      | SQL statements are parsed into command objects and appended to a Write-Ahead Log before being applied. Rollback works because the log is a command history.                                   |
| **Financial trading systems**        | Buy/sell instructions are `Order` objects — queued in an order book, matched, cancelled, audited. FIX protocol (the standard for inter-broker communication) is entirely structured this way. |
| **Java `Runnable` / `Callable`**     | `Runnable` IS the Command interface. `ThreadPoolExecutor` IS the Invoker. Your task class IS the ConcreteCommand. You have been using Command pattern since day one of Java.                  |
| **Java `ExecutorService`**           | `submit(Callable task)` — you wrap work in an object and hand it to a scheduler. That is Command.                                                                                             |
| **Message queues (Kafka, RabbitMQ)** | Messages are command objects. The consumer is the Invoker. The handler is the Receiver.                                                                                                       |
| **Git**                              | Each commit is a recorded, addressable, replayable command. `git revert` is `command.undo()`.                                                                                                 |
| **Spring Batch**                     | Each `Step` in a batch job is a command object with built-in retry, skip, and rollback behaviour.                                                                                             |

The pattern is everywhere in production. It just does not always announce itself.

---

## A Real System: Stock Brokerage Order Execution Engine

A retail brokerage (think Zerodha, Robinhood) processes trading instructions.

Users submit orders — buy 100 shares of HDFC at market price, place a limit sell for TCS at ₹3800, cancel order #4421. These instructions arrive from millions of users and need to be:

1. **Validated and queued** before market opens (pre-market orders)
2. **Executed** when the market opens or the price condition is met
3. **Audited** — regulators legally require a full log of every instruction
4. **Cancellable** before execution
5. **Retried** if the exchange gateway times out

Calling `orderBook.placeOrder(...)` directly breaks down completely here. You have no queue, no audit log, no cancel, no retry — and adding any of those means hacking the caller code every time.

---

## Implementation

### The Receiver — `OrderBook.java`

The domain object that does actual work. It knows how to place and cancel orders — nothing else.

```java
public class OrderBook {

    public void placeBuyOrder(String ticker, int quantity, double price) {
        System.out.printf("[OrderBook] BUY  %d x %s @ %.2f submitted to exchange%n",
                quantity, ticker, price);
    }

    public void placeSellOrder(String ticker, int quantity, double price) {
        System.out.printf("[OrderBook] SELL %d x %s @ %.2f submitted to exchange%n",
                quantity, ticker, price);
    }

    public void cancelOrder(String orderId) {
        System.out.printf("[OrderBook] Order %s CANCELLED%n", orderId);
    }

    public void reinstateOrder(String orderId) {
        System.out.printf("[OrderBook] Order %s REINSTATED%n", orderId);
    }
}
```

---

### The Command Interface — `TradeCommand.java`

```java
public interface TradeCommand {
    void execute();
    void undo();
    String getAuditDescription();
}
```

---

### Concrete Command — `PlaceBuyOrderCommand.java`

```java
public class PlaceBuyOrderCommand implements TradeCommand {

    private final OrderBook orderBook;
    private final String orderId;
    private final String ticker;
    private final int quantity;
    private final double price;

    public PlaceBuyOrderCommand(OrderBook orderBook, String orderId,
                                String ticker, int quantity, double price) {
        this.orderBook = orderBook;
        this.orderId   = orderId;
        this.ticker    = ticker;
        this.quantity  = quantity;
        this.price     = price;
    }

    @Override
    public void execute() {
        orderBook.placeBuyOrder(ticker, quantity, price);
    }

    @Override
    public void undo() {
        orderBook.cancelOrder(orderId);
    }

    @Override
    public String getAuditDescription() {
        return String.format("BUY %d x %s @ %.2f [orderId=%s]", quantity, ticker, price, orderId);
    }
}
```

---

### Concrete Command — `PlaceSellOrderCommand.java`

```java
public class PlaceSellOrderCommand implements TradeCommand {

    private final OrderBook orderBook;
    private final String orderId;
    private final String ticker;
    private final int quantity;
    private final double price;

    public PlaceSellOrderCommand(OrderBook orderBook, String orderId,
                                 String ticker, int quantity, double price) {
        this.orderBook = orderBook;
        this.orderId   = orderId;
        this.ticker    = ticker;
        this.quantity  = quantity;
        this.price     = price;
    }

    @Override
    public void execute() {
        orderBook.placeSellOrder(ticker, quantity, price);
    }

    @Override
    public void undo() {
        orderBook.cancelOrder(orderId);
    }

    @Override
    public String getAuditDescription() {
        return String.format("SELL %d x %s @ %.2f [orderId=%s]", quantity, ticker, price, orderId);
    }
}
```

---

### Concrete Command — `CancelOrderCommand.java`

```java
/**
 * Undo of a cancellation = reinstate the original order.
 * The command holds enough context to reverse itself without asking anyone else.
 */
public class CancelOrderCommand implements TradeCommand {

    private final OrderBook orderBook;
    private final String orderId;
    private final String ticker;
    private final int quantity;
    private final double price;
    private final String side; // "BUY" or "SELL"

    public CancelOrderCommand(OrderBook orderBook, String orderId,
                              String ticker, int quantity, double price, String side) {
        this.orderBook = orderBook;
        this.orderId   = orderId;
        this.ticker    = ticker;
        this.quantity  = quantity;
        this.price     = price;
        this.side      = side;
    }

    @Override
    public void execute() {
        orderBook.cancelOrder(orderId);
    }

    @Override
    public void undo() {
        // Undoing a cancellation reinstates the original order
        orderBook.reinstateOrder(orderId);
    }

    @Override
    public String getAuditDescription() {
        return String.format("CANCEL %s order %s (%d x %s @ %.2f)",
                side, orderId, quantity, ticker, price);
    }
}
```

---

### The Invoker — `BrokerageGateway.java`

The engine. It owns the execution queue, the audit log, and the undo history.
It has zero knowledge of what any specific order does — it only ever calls `.execute()` and `.undo()`.

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BrokerageGateway {

    // Orders staged before market opens
    private final List<TradeCommand> pendingQueue = new ArrayList<>();

    // History of executed commands (used for undo)
    private final Deque<TradeCommand> executedHistory = new ArrayDeque<>();

    // Immutable regulatory audit trail — never deleted
    private final List<String> auditLog = new ArrayList<>();

    public void queue(TradeCommand command) {
        pendingQueue.add(command);
        System.out.println("[Gateway] Queued: " + command.getAuditDescription());
    }

    public void executeNow(TradeCommand command) {
        command.execute();
        executedHistory.push(command);
        auditLog.add("[EXECUTED] " + command.getAuditDescription());
    }

    // Called when market opens — flush all pre-market orders
    public void flushQueue() {
        System.out.println("\n[Gateway] Market open — flushing " + pendingQueue.size() + " queued orders");
        for (TradeCommand command : pendingQueue) {
            executeNow(command);
        }
        pendingQueue.clear();
    }

    public void undoLast() {
        if (executedHistory.isEmpty()) {
            System.out.println("[Gateway] Nothing to undo.");
            return;
        }
        TradeCommand command = executedHistory.pop();
        command.undo();
        auditLog.add("[UNDONE]   " + command.getAuditDescription());
    }

    public void printAuditLog() {
        System.out.println("\n=== AUDIT LOG ===");
        auditLog.forEach(System.out::println);
    }
}
```

---

### Client — `TradingDeskMain.java`

```java
public class TradingDeskMain {

    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        BrokerageGateway gateway = new BrokerageGateway();

        // Pre-market: queue orders before exchange opens
        gateway.queue(new PlaceBuyOrderCommand(orderBook,  "ORD-001", "HDFC",     50, 1620.00));
        gateway.queue(new PlaceBuyOrderCommand(orderBook,  "ORD-002", "TCS",     100, 3790.00));
        gateway.queue(new PlaceSellOrderCommand(orderBook, "ORD-003", "INFY",     75, 1510.00));

        // Market opens: all queued orders execute
        gateway.flushQueue();

        // Live trading: immediate execution
        gateway.executeNow(new PlaceBuyOrderCommand(orderBook, "ORD-004", "RELIANCE", 200, 2450.00));

        // User cancels the order they just placed
        gateway.executeNow(new CancelOrderCommand(orderBook, "ORD-004", "RELIANCE", 200, 2450.00, "BUY"));

        // User changes their mind — undo the cancellation (reinstates the order)
        System.out.println("\n[Desk] Undoing last action...");
        gateway.undoLast();

        // Print full regulatory audit trail
        gateway.printAuditLog();
    }
}
```

**Output:**

```
[Gateway] Queued: BUY 50 x HDFC @ 1620.00 [orderId=ORD-001]
[Gateway] Queued: BUY 100 x TCS @ 3790.00 [orderId=ORD-002]
[Gateway] Queued: SELL 75 x INFY @ 1510.00 [orderId=ORD-003]

[Gateway] Market open — flushing 3 queued orders
[OrderBook] BUY  50 x HDFC @ 1620.00 submitted to exchange
[OrderBook] BUY  100 x TCS @ 3790.00 submitted to exchange
[OrderBook] SELL 75 x INFY @ 1510.00 submitted to exchange
[OrderBook] BUY  200 x RELIANCE @ 2450.00 submitted to exchange
[OrderBook] Order ORD-004 CANCELLED

[Desk] Undoing last action...
[OrderBook] Order ORD-004 REINSTATED

=== AUDIT LOG ===
[EXECUTED] BUY 50 x HDFC @ 1620.00 [orderId=ORD-001]
[EXECUTED] BUY 100 x TCS @ 3790.00 [orderId=ORD-002]
[EXECUTED] SELL 75 x INFY @ 1510.00 [orderId=ORD-003]
[EXECUTED] BUY 200 x RELIANCE @ 2450.00 [orderId=ORD-004]
[EXECUTED] CANCEL BUY order ORD-004 (200 x RELIANCE @ 2450.00)
[UNDONE]   CANCEL BUY order ORD-004 (200 x RELIANCE @ 2450.00)
```

---

## What This Example Actually Demonstrates

Notice what `BrokerageGateway` does **not** contain:

- No `if (type == BUY)` checking
- No switch on order type
- No knowledge of tickers, quantities, or prices
- No knowledge of what "undo" means for any particular order

It only calls `.execute()` and `.undo()`. You can add 10 new order types —
`StopLossOrderCommand`, `BracketOrderCommand`, `GoodTillCancelledCommand` — and the Gateway code does not change at all. That is the Open/Closed Principle flowing directly from the Command pattern.

The audit log also works for free. The Gateway never inspects what happened — each command describes itself via `getAuditDescription()`. The log writes itself.

---

## The Pattern's Three Real Jobs

**1. Decouple who requests an operation from who performs it**
The client submits a `TradeCommand`. The gateway runs it. Neither knows about the other's internals.

**2. Make operations into persistent, storable data**
You can serialize a `PlaceBuyOrderCommand` to a database row and replay it tomorrow.
This is exactly how exchange systems achieve crash recovery and regulatory compliance.

**3. Enable operations on operations**
Once an operation is an object — not a bare method call — you can undo it, queue it, batch it, retry it, and log it using the same generic mechanism regardless of what the operation actually does.

---

## Bonus — Batch / Macro Command

The same pattern gives you atomic batch execution for free:

```java
public class BatchTradeCommand implements TradeCommand {

    private final List<TradeCommand> commands;

    public BatchTradeCommand(List<TradeCommand> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void execute() {
        commands.forEach(TradeCommand::execute);
    }

    @Override
    public void undo() {
        // Undo in reverse order so rollback is clean
        ListIterator<TradeCommand> it = commands.listIterator(commands.size());
        while (it.hasPrevious()) {
            it.previous().undo();
        }
    }

    @Override
    public String getAuditDescription() {
        return "BATCH of " + commands.size() + " orders";
    }
}
```

One undo rolls back the entire batch atomically. This is how algorithmic trading systems submit and roll back multi-leg strategies.

---

## When to Use It

- You need operations to be **queued, scheduled, or deferred**
- You need an **audit trail** of every action (financial, medical, legal systems)
- You need **undo/redo**
- You need **retry logic** on failures
- You want to add new operations **without touching the dispatcher**

## When Not to Use It

- You just need to call a method. Use a method.
- There is no queuing, history, logging, or undo requirement.
- The operation has no meaningful inverse and will never need to be stored or deferred.
- You would be creating a class just to wrap a single line — that is the light bulb problem.

The cost of this pattern is one class per operation type. That cost is justified when the operation needs to carry state (especially reversal state), be stored, or be decoupled from whomever triggered it.
