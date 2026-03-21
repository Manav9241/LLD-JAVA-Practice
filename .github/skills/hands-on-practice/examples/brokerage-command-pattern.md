# Example: Stock Brokerage Order Execution Engine (Command Pattern)

This is a reference example showing how a hands-on exercise should look when applied to the Command design pattern.

---

## Problem Statement

You are building the order execution engine for a retail stock brokerage (like Zerodha or Robinhood).

Users submit trading instructions — buy shares, sell shares, or cancel existing orders. The system must handle these instructions under the following requirements:

1. **Pre-market queuing** — Orders submitted before market hours (before 9:15 AM) must be held and only executed when the market opens. They should all flush at once.

2. **Immediate execution** — During market hours, orders submitted should execute right away.

3. **Cancellation** — A user can cancel an order they previously placed. This should be treated as its own operation, not just "deleting" the original order.

4. **Undo** — The system should support undoing the last action. For example, if a user cancels an order and then changes their mind, undoing the cancellation should reinstate the order.

5. **Audit trail** — Every operation (executed or undone) must be recorded in an immutable log. Each entry should describe what happened in enough detail for a regulator to review.

**Domain operations available:**

- Place a buy order (requires: ticker, quantity, price)
- Place a sell order (requires: ticker, quantity, price)
- Cancel an order (requires: orderId)
- Reinstate a cancelled order (requires: orderId)

**Your task:**
Design and implement this system using the Command design pattern. Think about which classes you need, what role each one plays, what state each one carries, and how they interact.

---

## Hint Progression

This example demonstrates how hints were derived from the requirements. Each hint maps to one design decision forced by the problem:

| Hint | Requirement it addresses | Design decision it prompts            |
| ---- | ------------------------ | ------------------------------------- |
| 1    | All requirements         | Identify the domain object (receiver) |
| 2    | Queuing, undo            | Common interface for all operations   |
| 3    | Audit trail              | Interface needs self-description      |
| 4    | Undo                     | Commands must carry reversal state    |
| 5    | Cancellation + undo      | Cancel is a command, undo = reinstate |
| 6    | All requirements         | Invoker stays blind to concrete types |
| 7    | Queuing, undo, audit     | Data structures for the invoker       |

## Hints

**Hint 1:** Start by identifying what the "receiver" is in this system. What object actually knows how to place, cancel, and reinstate orders? Build that first — it should have no awareness of the pattern.

**Hint 2:** Think about what contract every operation needs to satisfy. What must every operation be able to do, regardless of whether it's a buy, sell, or cancel? This contract becomes your interface.

**Hint 3:** The audit trail requirement means each operation must be able to describe itself. Your interface might need more than just `execute()` and `undo()`.

**Hint 4:** For each type of operation (buy, sell, cancel), think about what state the object needs to carry. Don't just think about what `execute()` needs — think about what `undo()` needs too. For example: if you undo a buy, what information do you need to cancel it?

**Hint 5:** Cancellation is an operation, not a deletion. That means "cancel" is its own command. What does "undo a cancellation" mean? What receiver method would that call?

**Hint 6:** The component that manages the queue, the undo history, and the audit log should never know what specific operation it's running. It should only interact through the interface. If you find yourself writing `instanceof` or a switch statement in this class, reconsider your design.

**Hint 7:** The queue is a `List`, the undo history is a `Stack`/`Deque` (last-in, first-out), and the audit log is an append-only `List` of strings. These three structures live in the same class.

---

## Review Focus Areas

After implementation, check:

- Does the invoker/gateway class import any concrete command classes? (It shouldn't.)
- Does each command carry enough state to both execute AND undo without asking anyone else for data?
- Can you add a `StopLossOrderCommand` without touching any existing class other than the client wiring?
- Does the audit log write itself through the command's self-description, or did the user hardcode log messages in the invoker?
