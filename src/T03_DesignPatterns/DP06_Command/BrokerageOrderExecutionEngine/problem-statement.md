# Problem Statement — Brokerage Order Execution Engine

## The Problem

You're building the order execution engine for a retail stock brokerage (think Zerodha, Robinhood). Users submit trading instructions — buy 100 shares of HDFC, sell 150 TCS, cancel order ORD-03.

**Requirements:**

1. **Pre-market queueing** — Orders submitted before market opens must be queued and executed automatically when the market opens
2. **Live execution** — Orders submitted during market hours execute immediately
3. **Undo** — The last executed action can be reversed (undo a buy = cancel it, undo a cancel = reinstate it)
4. **Audit trail** — Every execution and undo must be logged for regulatory compliance
5. **Extensibility** — Adding new order types (stop-loss, bracket orders) should not require changing the gateway

**Why direct method calls break down:**

```java
void submitOrder(String type, String ticker, int qty, double price) {
    if (type.equals("BUY")) orderBook.placeBuyOrder(ticker, qty, price);
    else if (type.equals("SELL")) orderBook.placeSellOrder(ticker, qty, price);
    // undo? audit? queue? all hacked in here...
}
```

You have no queue. No audit log. No undo. No way to add retry without modifying the dispatcher. Every new order type means another `else if`. The gateway becomes a tangled mess.

---

## Thought Process

**Step 1 — List what the gateway needs to do with an order.** Queue it, execute it, undo it, log it. These are four operations _on_ the order itself — not domain operations like "place a buy." The gateway needs to manipulate operations as things, not call them directly.

**Step 2 — Recognize the shape.** Every order, regardless of type, has the same lifecycle from the gateway's perspective: it can be executed, it can be reversed, and it can describe itself for logging. That's a uniform interface: `execute()`, `undo()`, `getAuditDescription()`.

**Step 3 — Each command must be self-sufficient.** A `PlaceBuyOrderCommand` needs to hold the receiver (OrderBook), ticker, quantity, price, and orderId — everything needed to both execute _and_ undo. The command is a snapshot of intent. The gateway never needs to ask "what kind of order is this?"

**Step 4 — The gateway manages lifecycle, not logic.** The gateway holds a queue (pre-market), a history stack (for undo), and an audit log (for compliance). It only calls `execute()`, `undo()`, and `getAuditDescription()`. It imports only the command interface — never any concrete command or the OrderBook.

**Step 5 — Market state is the gateway's concern.** When market is closed, `executeNow()` routes to the queue. When `marketOpen()` is called, it flushes the queue. This is invoker-level policy — it doesn't change any command logic.

---

## Intuition

The core insight is: **trading instructions are not method calls — they are objects with a lifecycle.** An order needs to exist as a thing before it's executed so it can be queued, and it needs to continue existing after execution so it can be undone and audited. Wrapping each instruction in a command object gives it that lifecycle for free.

The second insight: **undo works because each command knows its own reverse.** Undo-of-buy = cancel. Undo-of-cancel = reinstate. The gateway doesn't need to understand any of this — it just pops the last command and calls `undo()`. This is what makes the system extensible without the invoker ever changing.

---

## Solution — Role Mapping

| Role              | Class                                                                 | What it does                                                                                                 |
| ----------------- | --------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| Receiver          | `OrderBook`                                                           | Domain object — `placeBuyOrder()`, `placeSellOrder()`, `cancelOrder()`, `reinstateOrder()`                   |
| Command Interface | `ICommand`                                                            | `execute()` + `undo()` + `getAuditDescription()`                                                             |
| Concrete Commands | `PlaceBuyOrderCommand`, `PlaceSellOrderCommand`, `CancelOrderCommand` | Hold receiver + all state needed to execute and reverse. Each maps execute/undo to specific receiver methods |
| Invoker           | `BrokerageGatewayInvoker`                                             | Manages market state, order queue, execution history, audit log. Only imports `ICommand`                     |
| Client            | `TradingDeskMain`                                                     | Creates OrderBook, creates commands with wiring, submits to gateway                                          |

**Key design decisions:**

- `executeNow()` checks `isMarketOpen` — if closed, auto-routes to queue. The invoker decides _when_, commands don't care.
- `marketOpen()` triggers `flushOrderQueue()` — all queued commands execute in submission order.
- `CancelOrderCommand` only needs `orderId` for both execute and undo — minimal state for the operation.
- The audit trail writes itself: the gateway calls `getAuditDescription()` after every execute/undo. No type-checking needed.

**Extensibility test:** Adding `StopLossOrderCommand` requires one new class that implements `ICommand` and holds (OrderBook, ticker, quantity, triggerPrice, orderId). The gateway doesn't change. The audit trail works automatically. Undo works automatically.
