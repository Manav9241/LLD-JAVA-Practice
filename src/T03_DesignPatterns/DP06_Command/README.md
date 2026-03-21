# DP06 — Command Design Pattern

> **One-liner:** Wrap an operation in an object so you can queue, store, undo, or replay it without the executor knowing what it actually does.

---

## The Problem

When you call a method directly — `orderBook.placeBuyOrder(...)` — it executes immediately, leaves no trace, cannot be undone, and cannot be deferred. The caller and the domain logic are welded together.

```java
// The caller knows EXACTLY what it's doing and to whom
void handleUserClick(String type, OrderBook book) {
    if (type.equals("BUY"))  book.placeBuyOrder("HDFC", 100, 1620.0);
    if (type.equals("SELL")) book.placeSellOrder("TCS", 50, 3800.0);
    // Want undo? Add another if-chain. Want queue? Another if-chain. Want audit? Another.
}
```

The moment you need **any** of these — queueing, undo, audit logging, retry, batching — you have to hack the caller every time. Each new operation type adds another branch. Each new capability (undo, log, queue) adds another layer of `if` checks inside the caller. The caller becomes a growing mess that knows everything about every operation.

**The fix:** Before executing an operation, wrap it in an object. Now the operation is data — you decide _when_ and _how_ to use it.

```java
Command cmd = new PlaceBuyOrderCommand(orderBook, "HDFC", 100, 1620.0);
// The operation exists as an object now. You choose what to do with it.
gateway.submit(cmd);   // queue it? execute it? log it? all of the above? — the cmd doesn't care.
```

Once the operation is an object, you get six capabilities for free:

1. **Execute later** — queue it, schedule it, defer until a condition is met
2. **Store it** — persist to a log, database, or file for audit/compliance
3. **Undo it** — each command carries its own reverse operation
4. **Retry it** — if execution fails, resubmit the same object
5. **Serialize it** — send it over a network, replay it on another machine
6. **Compose it** — batch multiple commands into one atomic unit

That is the entire idea. Everything else is mechanics.

---

## The Four Roles

```
Receiver        → Domain object that does the real work (e.g., OrderBook, Light)
Command         → Interface: execute() + undo()
ConcreteCommand → Bridges Invoker → Receiver. Holds ALL state needed to execute AND reverse.
Invoker         → Accepts commands, decides when to run them. Only sees the Command interface.
Client          → Creates commands, wires receiver into them, hands them to the invoker.
```

**What each role knows (and doesn't know):**

- **Receiver** knows nothing about commands, invokers, or the pattern itself. It's a plain domain object with methods like `placeBuyOrder()`, `light.on()`. It exists independently of the pattern.
- **ConcreteCommand** knows its receiver and holds all the parameters needed to call a specific method on it. It also knows the _reverse_ operation (e.g., undo of buy = cancel). All fields are `final` — a command is an immutable snapshot of intent.
- **Invoker** only knows the `Command` interface. It never imports or references any concrete command or receiver. This is what makes it permanently closed to modification — it manages lifecycle (queue, execute, undo, log) without knowing what any operation actually does.
- **Client** is the only place that knows everything — it creates receivers, wraps them in concrete commands, and hands the commands to the invoker. All wiring happens here.

**Critical rule:** The Invoker is _blind_. It only calls `execute()` and `undo()`. This is not just clean design — it's the mechanical prerequisite for queueing, logging, and undo to work generically across all operation types without the Invoker changing.

### How the Pieces Connect — Execution Flow

Trace a single button press on the remote control to see every role in action:

```
1. Client (main)  → Creates Light receiver, wraps it in LightCommand, assigns to slot 0
2. Client (main)  → Calls remote.pressButton(0)
3. Invoker        → Looks up slot 0, finds an ICommand (doesn't know it's a LightCommand)
4. Invoker        → Calls command.execute()
5. ConcreteCommand → LightCommand.execute() calls this.light.on()
6. Receiver       → Light.on() prints "Light turned On!"
```

The Invoker (step 3-4) never imports `Light`, `LightCommand`, or `Fan`. Its only import is `ICommand`. You can verify this by checking `RemoteControlInvoker.java` — its import list proves its blindness. This is the mechanical proof that adding new devices requires zero changes to the Invoker.

**What changes on the second press?** The Invoker tracks a boolean toggle per slot. Second press → calls `command.undo()` → `LightCommand.undo()` → `light.off()`. The Invoker doesn't know what "undo" means — it just calls the method.

---

## How Undo Actually Works

Undo isn't magic — it works because each command carries its own reverse operation.

```
PlaceBuyOrderCommand:
    execute() → orderBook.placeBuyOrder(ticker, qty, price)
    undo()    → orderBook.cancelOrder(orderId)

CancelOrderCommand:
    execute() → orderBook.cancelOrder(orderId)
    undo()    → orderBook.reinstateOrder(orderId)
```

The Invoker maintains a history stack. When `undoLast()` is called, it pops the most recent command and calls `undo()`. The Invoker doesn't know _what_ "undo" means for that command — it just calls the method. Each command defines its own reversal internally.

This only works because every command holds enough state to reverse itself. A `PlaceBuyOrderCommand` stores the `orderId` even though `execute()` doesn't need it — `undo()` does. This is the "self-sufficient command" principle: **a command must carry everything needed for both execution and reversal.**

---

## The Three Jobs

1. **Decouple** who requests an operation from who performs it — the client submits a command, the invoker runs it, neither knows about the other's internals
2. **Store** operations as persistent, replayable data — you can serialize a command to a database row and replay it tomorrow (this is how audit logs, crash recovery, and event sourcing work)
3. **Operate on operations** — once an operation is an object, you can undo it, queue it, batch it, retry it, and log it using the same generic mechanism, regardless of what the operation actually does

---

## Where It Shows Up in Production

| System                               | Command in disguise                                                                                                                                 |
| ------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Java `Runnable`/`Callable`**       | `Runnable` = Command interface. `ThreadPoolExecutor` = Invoker. Your task class = ConcreteCommand. You've been using Command since day one of Java. |
| **RDBMS WAL**                        | SQL statements are parsed into command objects and appended to a Write-Ahead Log. Rollback = replaying undo in reverse from the log.                |
| **Trading systems**                  | Buy/sell orders queued in order books, matched, cancelled, audited. FIX protocol structures inter-broker communication entirely this way.           |
| **Message queues (Kafka, RabbitMQ)** | Messages = command objects. Consumer = Invoker. Handler = Receiver.                                                                                 |
| **Git**                              | Each commit = recorded, addressable, replayable command. `git revert` = `command.undo()`.                                                           |
| **Spring Batch**                     | Each `Step` in a batch job is a command object with built-in retry, skip, and rollback.                                                             |

The pattern is everywhere in production — it just doesn't always announce itself by name.

---

## When to Use / When Not to Use

**Use when:**

- Operations need to be **queued, scheduled, or deferred**
- You need an **audit trail** (financial, medical, legal systems)
- You need **undo/redo**
- You need **retry on failures**
- New operation types should not require changing the dispatcher (Open/Closed Principle)

**Skip when:**

- A direct method call is sufficient — no queue, no history, no undo
- The operation has no meaningful reverse and never needs to be stored
- You'd be creating a class to wrap a single line (the "light bulb problem")

**Cost:** One class per operation type. Justified when the operation carries reversal state, needs storage, or must be decoupled from the trigger.

---

## Examples in This Directory

### 1. Remote Control — `RemoteControlSmartAppliance/`

Introductory example. A remote control (Invoker) toggles smart appliances (Receivers) via button slots that hold commands. Demonstrates the basic wiring and execute/undo toggle.

| Role              | File                                                   |
| ----------------- | ------------------------------------------------------ |
| Receiver          | `Receiver/Light.java`, `Receiver/Fan.java`             |
| Command Interface | `Command/ICommand.java`                                |
| Concrete Commands | `Command/LightCommand.java`, `Command/FanCommand.java` |
| Invoker           | `RemoteControlInvoker.java`                            |
| Client            | `CommandPatternMain.java`                              |

The Invoker here is simple — it holds an array of `ICommand` slots and a boolean toggle per slot. Press once → `execute()`. Press again → `undo()`. It never imports `Light`, `Fan`, or any concrete command.

### From Remote Control to Brokerage — What Escalates

The pattern is identical. What changes is the _complexity the Invoker manages_:

| Aspect            | Remote Control                | Brokerage Gateway                                                |
| ----------------- | ----------------------------- | ---------------------------------------------------------------- |
| Receiver          | `Light`, `Fan` — toy devices  | `OrderBook` — exchange-connected domain                          |
| Command interface | `execute()` + `undo()`        | adds `getAuditDescription()` for compliance                      |
| Command state     | receiver reference only       | receiver + orderId + ticker + qty + price                        |
| Invoker state     | button slots + toggle boolean | order queue + execution history stack + audit log + market state |
| Invoker decisions | toggle on/off                 | queue vs. execute (market hours), flush on market open           |

The Invoker in both cases only imports the Command interface — never a concrete command or receiver. The brokerage gateway is more sophisticated, but it's still _blind to what it's executing_.

### 2. Brokerage Order Execution Engine — `BrokerageOrderExecutionEngine/`

Production-style example. A brokerage gateway (Invoker) manages pre-market queueing, live execution, undo, and audit trail — all without knowing what any specific order does.

| Role              | File                                                                                                         |
| ----------------- | ------------------------------------------------------------------------------------------------------------ |
| Receiver          | `Receiver/OrderBook.java`                                                                                    |
| Command Interface | `Command/ICommand.java`                                                                                      |
| Concrete Commands | `Command/PlaceBuyOrderCommand.java`, `Command/PlaceSellOrderCommand.java`, `Command/CancelOrderCommand.java` |
| Invoker           | `BrokerageGatewayInvoker.java`                                                                               |
| Client            | `TradingDeskMain.java`                                                                                       |

**Key design choices:**

- **Market-aware routing:** `executeNow()` checks `isMarketOpen` — if closed, it auto-routes to the queue. When `marketOpen()` is called, it flushes all queued commands. The Invoker decides _when_ to execute, commands don't care.
- **Self-sufficient commands:** Each command holds enough state to reverse itself. `PlaceBuyOrderCommand` stores `orderId` for its `undo()` even though `execute()` doesn't need it. `CancelOrderCommand.undo()` calls `reinstateOrder()`.
- **Self-describing audit:** The gateway calls `getAuditDescription()` after every execute/undo. No type-checking, no `instanceof`, no switch. Each command describes itself.
- **Extensibility:** Adding `StopLossOrderCommand` requires one new class + one wiring line in the client. Zero changes to the gateway. The audit trail, undo, and queueing work automatically.

---

## Bonus — Batch / Macro Command

A command that contains a list of commands. `execute()` runs all; `undo()` reverses all in reverse order. This gives you atomic batch execution for free — the Invoker still just calls `execute()` on one object.

```java
public class BatchCommand implements ICommand {
    private final List<ICommand> commands;

    public void execute() { commands.forEach(ICommand::execute); }

    public void undo() {
        // Reverse order — last executed is first undone
        ListIterator<ICommand> it = commands.listIterator(commands.size());
        while (it.hasPrevious()) it.previous().undo();
    }
}
```

The Invoker doesn't know it just ran 5 operations — it called `execute()` once. This is how algorithmic trading systems submit and roll back multi-leg strategies atomically.

This ties back to the three jobs:

- **Decouple:** The batch command is a new operation type — the Invoker doesn't change.
- **Store:** The batch itself is a command — it can be logged, serialized, replayed.
- **Operate on operations:** Undo-of-batch = undo each sub-command in reverse. Retry-of-batch = re-execute all. The generic mechanism handles it.
