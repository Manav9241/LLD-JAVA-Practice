# DBMS Interview Preparation Notes

---

## 🟡 DAY 7: Isolation Anomalies & Locking

### 🟡 DAY 7 — Beginner Questions

---

#### Q1. What is a lost update?

**Answer:**
Lost update: two transactions read same data, then both write updates based on old value. Last write wins, first update lost. Classic read-modify-write race condition. Happens even with transactions if no locking/versioning used.

**SQL Example:**

```sql
-- Initial: counter = 100
-- Transaction A
BEGIN;
    SELECT counter FROM Stats WHERE id = 1; -- Reads 100
    -- ... calculates new value: 100 + 1 = 101

-- Transaction B (concurrent)
BEGIN;
    SELECT counter FROM Stats WHERE id = 1; -- Reads 100
    -- ... calculates new value: 100 + 1 = 101
    UPDATE Stats SET counter = 101 WHERE id = 1;
COMMIT; -- First to commit

-- Transaction A (continues)
    UPDATE Stats SET counter = 101 WHERE id = 1; -- Overwrites B's update!
COMMIT;

-- Result: counter = 101 (should be 102!)
-- Transaction B's increment lost

-- Fix: Use atomic operations
UPDATE Stats SET counter = counter + 1 WHERE id = 1; -- Atomic, no lost update
```

**Follow-ups:** SELECT FOR UPDATE? Optimistic locking? Compare-and-swap?

**Mistakes:** Assuming transactions prevent all race conditions; read-then-write without locking

---

#### Q2. Why can lost updates happen even when transactions are used?

**Answer:**
Transactions guarantee atomicity (all-or-nothing) but don't prevent concurrent reads. Both transactions can read same value, calculate based on it, then write—both writes are atomic but based on stale reads. Need explicit locking or optimistic concurrency control.

**SQL Example:**

```sql
-- Both transactions are atomic but still lose updates
-- Transaction A: Atomic
BEGIN;
    value = SELECT price FROM Products WHERE id = 1; -- Read: 100
    UPDATE Products SET price = value * 1.1 WHERE id = 1; -- Write: 110
COMMIT; -- Success, atomic

-- Transaction B: Atomic (concurrent)
BEGIN;
    value = SELECT price FROM Products WHERE id = 1; -- Read: 100 (before A commits)
    UPDATE Products SET price = value * 1.2 WHERE id = 1; -- Write: 120
COMMIT; -- Success, atomic, overwrites A

-- Both atomic, both succeeded, but A's update lost!

-- Fix: Lock the read
BEGIN;
    SELECT price FROM Products WHERE id = 1 FOR UPDATE; -- Lock row
    UPDATE Products SET price = price * 1.1 WHERE id = 1;
COMMIT; -- B must wait
```

**Follow-ups:** Isolation levels preventing this? Pessimistic vs optimistic locking?

**Mistakes:** Thinking ACID automatically prevents all concurrency issues; not using FOR UPDATE

---

#### Q3. What is a deadlock?

**Answer:**
Deadlock: two+ transactions each hold locks the others need, creating circular wait. All transactions blocked indefinitely. Database detects cycle and aborts one transaction (deadlock victim) to break cycle.

**SQL Example:**

```sql
-- Transaction A
BEGIN;
    UPDATE Accounts SET balance = balance - 100 WHERE id = 1; -- Locks row 1
    -- ... processing ...
    UPDATE Accounts SET balance = balance + 100 WHERE id = 2; -- Needs lock on row 2

-- Transaction B (concurrent)
BEGIN;
    UPDATE Accounts SET balance = balance - 50 WHERE id = 2; -- Locks row 2
    -- ... processing ...
    UPDATE Accounts SET balance = balance + 50 WHERE id = 1; -- Needs lock on row 1

-- Deadlock!
-- A holds lock on 1, waits for 2
-- B holds lock on 2, waits for 1
-- Database detects, aborts one (ERROR: deadlock detected)

-- Prevention: Consistent lock order
-- Always lock accounts in ID order (1 before 2)
```

**Follow-ups:** Deadlock detection? Prevention strategies? Retry logic?

**Mistakes:** Panicking about deadlocks; not implementing retry; inconsistent lock ordering

---

#### Q4. Why are deadlocks considered normal in databases?

**Answer:**
Deadlocks are natural consequence of concurrent locking—avoiding them completely would require serializing all transactions (no concurrency). Databases detect and resolve automatically by aborting one transaction. Expected behavior, not a failure.

**SQL Example:**

```sql
-- High concurrency = occasional deadlocks expected
-- Thousands of concurrent transactions
BEGIN;
    UPDATE Orders ... WHERE order_id = X;
    UPDATE Inventory ... WHERE product_id = Y;
COMMIT;

-- With enough load, deadlocks will occur
-- Database handles it:
-- 1. Detects cycle
-- 2. Aborts youngest transaction
-- 3. Returns error to application
-- 4. Application retries

-- Application code:
try {
    executeTransaction();
} catch (DeadlockException e) {
    retry(); // Normal error handling
}
```

**Follow-ups:** Deadlock rate acceptable? Monitoring? Lock timeout vs detection?

**Mistakes:** Treating deadlocks as bugs; not implementing retry logic; over-engineering to avoid them

---

### 🟡 DAY 7 — Intermediate Questions

---

#### Q5. What is write skew?

**Answer:**
Write skew: two transactions read overlapping data, each updates different rows, result violates business rule. No direct conflict (different rows updated), so REPEATABLE READ allows both. Business invariant broken.

**SQL Example:**

```sql
-- Business rule: At least one doctor must be on-call
-- Initial: Dr. Alice and Dr. Bob both on-call

-- Transaction A (Alice goes off-call)
BEGIN;
    SELECT COUNT(*) FROM Doctors WHERE on_call = TRUE; -- Returns 2
    -- "There's 2 doctors, safe to go off-call"
    UPDATE Doctors SET on_call = FALSE WHERE name = 'Alice';
COMMIT;

-- Transaction B (Bob goes off-call, concurrent)
BEGIN;
    SELECT COUNT(*) FROM Doctors WHERE on_call = TRUE; -- Returns 2
    -- "There's 2 doctors, safe to go off-call"
    UPDATE Doctors SET on_call = FALSE WHERE name = 'Bob';
COMMIT;

-- Result: 0 doctors on-call! Business rule violated
-- Both transactions updated different rows = no conflict detected
-- At REPEATABLE READ: Allowed
-- At SERIALIZABLE: Prevented
```

**Follow-ups:** Detecting write skew? Materialized conflicts? Phantom prevention?

**Mistakes:** Thinking REPEATABLE READ prevents all anomalies; not encoding constraints

---

#### Q6. Why does write skew survive REPEATABLE READ isolation?

**Answer:**
REPEATABLE READ ensures rows you read don't change, but doesn't prevent changes to other rows or new rows appearing. Write skew updates different rows (no direct conflict), so read rows stay consistent. Need SERIALIZABLE for full protection.

**SQL Example:**

```sql
-- At REPEATABLE READ
-- Transaction A
BEGIN;
    SELECT balance FROM Accounts WHERE id = 1; -- Read row 1: 100
    SELECT balance FROM Accounts WHERE id = 2; -- Read row 2: 100
    -- Total: 200, OK to withdraw 50 from account 1
    UPDATE Accounts SET balance = 50 WHERE id = 1; -- Updates row 1
COMMIT;

-- Transaction B (concurrent)
BEGIN;
    SELECT balance FROM Accounts WHERE id = 1; -- Read row 1: 100
    SELECT balance FROM Accounts WHERE id = 2; -- Read row 2: 100
    -- Total: 200, OK to withdraw 50 from account 2
    UPDATE Accounts SET balance = 50 WHERE id = 2; -- Updates row 2
COMMIT;

-- Result: Total balance = 100 (should reject one withdrawal if total must stay >= 150)
-- REPEATABLE READ: Row 1 and 2 individually consistent, but aggregate violated
-- SERIALIZABLE: Would detect conflict
```

**Follow-ups:** Serialization anomalies? Snapshot isolation limits? Predicate locking?

**Mistakes:** Not understanding row-level vs set-level guarantees; wrong isolation level choice

---

#### Q7. How can write skew be prevented?

**Answer:**
Prevent write skew via: 1) SERIALIZABLE isolation (detects conflicts), 2) Explicit locking (SELECT FOR UPDATE on involved rows), 3) Database constraints (CHECK, triggers), 4) Materialized conflicts (dummy row to lock).

**SQL Example:**

```sql
-- Method 1: SERIALIZABLE isolation
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN;
    SELECT COUNT(*) FROM Doctors WHERE on_call = TRUE;
    UPDATE Doctors SET on_call = FALSE WHERE name = 'Alice';
COMMIT; -- Second transaction aborts with serialization error

-- Method 2: Explicit locking
BEGIN;
    SELECT * FROM Doctors WHERE on_call = TRUE FOR UPDATE; -- Lock all on-call
    -- Now second transaction must wait
    UPDATE Doctors SET on_call = FALSE WHERE name = 'Alice';
COMMIT;

-- Method 3: Constraint (best if possible)
CREATE TABLE OnCallConstraint (
    constraint_id INT PRIMARY KEY DEFAULT 1,
    on_call_count INT CHECK (on_call_count >= 1)
);
-- Both transactions try to decrement, constraint violation prevents

-- Method 4: Materialized conflict
-- Lock a dummy row representing the invariant
SELECT * FROM ShiftLock WHERE lock_id = 1 FOR UPDATE;
```

**Follow-ups:** Performance of each method? Application-level locks? Versioning?

**Mistakes:** Only relying on isolation level; not considering constraint-based prevention

---

#### Q8. Why do concurrency bugs usually appear only in production?

**Answer:**
Concurrency bugs require timing overlap—multiple transactions accessing same data simultaneously. Development/testing has low concurrency (rare overlap). Production has high load (frequent overlap), exposing race conditions that rarely occur otherwise.

**SQL Example:**

```sql
-- Development: 1-2 users, sequential requests
BEGIN;
    SELECT stock FROM Inventory WHERE product_id = 1; -- 10 units
    UPDATE Inventory SET stock = stock - 1 WHERE product_id = 1;
COMMIT;
-- Next request waits, no overlap = no race condition seen

-- Production: 1000 concurrent users
-- 100 simultaneous requests for same product
BEGIN;
    SELECT stock FROM Inventory WHERE product_id = 1; -- All read 10
    UPDATE Inventory SET stock = stock - 1 WHERE product_id = 1;
COMMIT;
-- Race condition! Overselling occurs

-- Testing must simulate concurrency
-- Use load testing tools, not manual testing
```

**Follow-ups:** Load testing? Concurrency simulation? Staging environment limits?

**Mistakes:** Not load testing; assuming low-traffic testing finds concurrency bugs

---

### 🧠 DAY 7 — Reflection Questions

---

#### 1. What does the database guarantee under concurrency?

**Answer:**
Database guarantees: atomicity (all-or-nothing), isolation per chosen level (visibility rules), constraint enforcement (FK, CHECK), durability (persistence). Does NOT guarantee business logic correctness unless explicitly encoded as constraints.

**SQL Example:**

```sql
-- DB Guarantees
CREATE TABLE Accounts (
    account_id INT PRIMARY KEY, -- Identity guaranteed
    balance DECIMAL(10,2) CHECK (balance >= 0), -- Constraint guaranteed
    user_id INT REFERENCES Users(user_id) -- FK guaranteed
);

BEGIN; -- Atomicity guaranteed
    UPDATE Accounts SET balance = balance - 100 WHERE id = 1;
COMMIT; -- Durability guaranteed

-- DB Does NOT Guarantee (business logic)
-- "Total withdrawals per day < $1000" - must enforce in app or trigger
-- "Account cannot go negative after pending transactions" - app logic
-- "Transfer must maintain system-wide balance" - app verification
```

**Follow-ups:** What to encode in DB vs app? Trigger overhead? Validation layers?

**Mistakes:** Expecting DB to enforce all business rules automatically; not using constraints

---

#### 2. What responsibility remains with application code?

**Answer:**
Application must: choose correct isolation level, enforce business invariants not expressible as constraints, handle retries/failures, implement optimistic concurrency, validate cross-table/temporal rules. DB provides primitives, app orchestrates.

**SQL Example:**

```sql
-- Application responsibilities:

// 1. Choose isolation level
BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ; // App decides

// 2. Retry on deadlock/conflict
try {
    executeTransaction();
} catch (DeadlockException e) {
    retry(); // App handles
}

// 3. Optimistic concurrency
// SELECT version, UPDATE WHERE version = old_version
// App implements versioning logic

// 4. Cross-entity validation
// "User can only have 3 active subscriptions"
// Count across tables, app enforces

// 5. Temporal rules
// "Cannot cancel order after 1 hour"
// App checks timestamp, enforces
```

**Follow-ups:** Application-level locks? Saga pattern? Compensating transactions?

**Mistakes:** Delegating everything to DB; no retry logic; wrong isolation level

---

#### 3. Why is SERIALIZABLE isolation rarely used by default?

**Answer:**
SERIALIZABLE drastically reduces concurrency—blocks transactions or causes frequent aborts. High-load systems would see massive throughput drop. Used selectively for critical operations where absolute correctness outweighs performance cost.

**SQL Example:**

```sql
-- SERIALIZABLE impact
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

-- High contention scenario
-- 1000 concurrent transactions updating popular product
BEGIN;
    SELECT stock FROM Products WHERE product_id = 1 FOR UPDATE;
    UPDATE Products SET stock = stock - 1 WHERE product_id = 1;
COMMIT;
-- Each transaction blocks others or aborts
-- Throughput: ~10-50 TPS (very low)

-- READ COMMITTED (default)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
-- Same scenario: 1000-5000 TPS (much higher)
-- Trade-off: possible anomalies, but better throughput

-- Strategy: Use SERIALIZABLE selectively
-- Critical: Financial transactions, inventory reservations
-- Non-critical: View counts, logs (use lower isolation)
```

**Follow-ups:** Performance benchmarks? Choosing isolation per transaction? Snapshot isolation?

**Mistakes:** Always using SERIALIZABLE; never using it; not measuring impact

---

### 🧠 DAY 7 — Critical Thinking Questions

---

#### Q1. Why does locking hurt scalability?

**Answer:**
Locking serializes access—concurrent transactions wait for lock release. Under high contention, wait times dominate execution, throughput drops. Parallelism lost when transactions block instead of executing concurrently.

**SQL Example:**

```sql
-- Without contention: 10 transactions/second
UPDATE Accounts SET balance = balance + 1 WHERE account_id = 1;
UPDATE Accounts SET balance = balance + 1 WHERE account_id = 2; -- Different row
-- Parallel execution, high throughput

-- With contention: 1 transaction/second
UPDATE Accounts SET balance = balance + 1 WHERE account_id = 1; -- Lock row 1
-- 100 concurrent transactions all need row 1
-- All wait in queue, serial execution
-- Throughput = 1/lock_duration, no parallelism

-- Scalability problem: Adding more servers doesn't help (same bottleneck)
```

**Follow-ups:** Lock-free algorithms? Partition data? Sharding strategies?

**Mistakes:** Not measuring contention; ignoring hot-spot rows; over-locking

---

#### Q2. Why do databases serialize writes?

**Answer:**
Concurrent writes to same data create race conditions—final value would be nondeterministic. Serialization (via locks or MVCC) ensures writes apply in defined order, producing predictable, correct results.

**SQL Example:**

```sql
-- Without serialization: Chaos
-- Initial: balance = 100
-- Transaction A: balance = balance + 50 (reads 100, writes 150)
-- Transaction B: balance = balance - 30 (reads 100, writes 70)
-- If both execute concurrently without coordination:
-- Final value: 150 or 70? Depends on timing! Nondeterministic

-- With serialization: Predictable
-- Lock-based: A locks row, B waits, final = 120
-- MVCC: A commits first (150), B reads 150, final = 120
-- Result: Deterministic, correct
```

**Follow-ups:** MVCC vs locking? Conflict detection? Write-write conflicts?

**Mistakes:** Attempting lock-free writes without versioning; assuming concurrent writes are safe

---

#### Q3. Why is "just use transactions" not enough?

**Answer:**
Transactions provide atomicity and basic isolation, but don't prevent logical race conditions (lost updates, write skew). Need correct isolation level, explicit locking, or constraints. Transactions are necessary but not sufficient.

**SQL Example:**

```sql
-- "Just use transactions" - Still broken
BEGIN TRANSACTION; // Used transaction!
    stock = SELECT stock FROM Products WHERE id = 1; // Read: 10
    if (stock > 0) {
        UPDATE Products SET stock = stock - 1 WHERE id = 1;
        INSERT INTO Orders VALUES (...);
    }
COMMIT;
// Multiple concurrent transactions: oversell! (lost update)

-- Need more: Locking or correct isolation
BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;
    SELECT stock FROM Products WHERE id = 1 FOR UPDATE; // Lock
    // Now safe
COMMIT;
```

**Follow-ups:** Default isolation levels? When transactions are sufficient? Versioning?

**Mistakes:** Using transactions without understanding isolation; default isolation assumptions

---

#### Q4. How does a database ensure correctness under concurrency?

**Answer:**
Database uses: locks (serialize conflicting access), isolation rules (visibility control), MVCC (version-based concurrency), conflict detection (abort on violation), constraints (integrity enforcement), WAL (durability). Coordinated mechanisms ensure valid committed states.

**SQL Example:**

```sql
-- Mechanisms in action
BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    -- 1. Lock acquired (write lock on row)
    UPDATE Accounts SET balance = balance - 100 WHERE id = 1;

    -- 2. Isolation: Other transactions see old version (MVCC)
    -- or wait (lock-based)

    -- 3. Constraint checked
    -- CHECK (balance >= 0) evaluated

    -- 4. WAL written (durability)
    -- Change logged before commit

    -- 5. Conflict detection
    -- If another transaction modified same row at SERIALIZABLE,
    -- this transaction aborts

COMMIT; -- All mechanisms coordinated for correctness
```

**Follow-ups:** MVCC internals? Lock types? Conflict resolution strategies?

**Mistakes:** Not understanding coordination overhead; assuming one mechanism enough

---

## 📚 Day 7 Summary

**Core Concepts**: Lost updates (read-modify-write races), deadlocks (circular lock waits), write skew (different rows, violated invariant), SERIALIZABLE (strictest isolation), locking trade-offs (correctness vs scalability)

**Key Takeaway**: Transactions + ACID don't prevent all concurrency bugs. Need correct isolation level, explicit locking, constraints, and application retry logic. Choose isolation based on correctness needs vs throughput requirements.

---
