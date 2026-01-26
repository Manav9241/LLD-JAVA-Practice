# DBMS Interview Preparation Notes

---

## 🟡 DAY 6: Transactions & ACID

### 🟡 DAY 6 — Beginner Questions

---

#### Q1. Why do transactions exist?

**Answer:**
Transactions group multiple operations into an atomic unit—all succeed or all fail. Prevent partial updates that leave database in inconsistent state. Essential for multi-step operations like transfers, order processing, or any logic requiring multiple writes.

**SQL Example:**

```sql
-- Without transaction: Disaster waiting
UPDATE Accounts SET balance = balance - 100 WHERE account_id = 1;
-- CRASH HERE! Money deducted but not credited
UPDATE Accounts SET balance = balance + 100 WHERE account_id = 2;
-- Result: Money vanished!

-- With transaction: Safe
BEGIN TRANSACTION;
    UPDATE Accounts SET balance = balance - 100 WHERE account_id = 1;
    UPDATE Accounts SET balance = balance + 100 WHERE account_id = 2;
COMMIT; -- Both or neither
-- CRASH before COMMIT? Automatic ROLLBACK, no money lost
```

**Follow-ups:** COMMIT vs ROLLBACK? Savepoints? Transaction scope?

**Mistakes:** Forgetting transactions for multi-step operations; not handling rollback logic

---

#### Q2. What breaks if transactions do not exist?

**Answer:**
Partial updates leave data in invalid states—half-completed transfers, orphan records, broken relationships. Application can't reason about state, data integrity lost. Manual recovery becomes impossible at scale.

**SQL Example:**

```sql
-- No transaction: Orphan data
INSERT INTO Orders (order_id, customer_id, total) VALUES (1, 123, 500);
-- CRASH!
INSERT INTO OrderItems (order_id, product_id, qty) VALUES (1, 456, 2);
-- Result: Order exists with no items! Invalid state

-- With transaction: Consistency guaranteed
BEGIN TRANSACTION;
    INSERT INTO Orders (order_id, customer_id, total) VALUES (1, 123, 500);
    INSERT INTO OrderItems (order_id, product_id, qty) VALUES (1, 456, 2);
COMMIT;
-- Both or neither, no orphans
```

**Follow-ups:** Compensating transactions? Idempotency? Saga pattern?

**Mistakes:** Assuming "it won't crash during that tiny window"; not testing failure scenarios

---

#### Q3. What does Atomicity guarantee?

**Answer:**
Atomicity: all operations in transaction complete or none do. No partial application. If any step fails, database rolls back all changes. Indivisible unit of work.

**SQL Example:**

```sql
BEGIN TRANSACTION;
    UPDATE Inventory SET quantity = quantity - 1 WHERE product_id = 1;
    INSERT INTO Orders VALUES (1, 1, 100); -- Fails (constraint violation)
    UPDATE Accounts SET balance = balance - 100 WHERE user_id = 1;
COMMIT;

-- Result: ROLLBACK (automatic)
-- Inventory NOT decremented, Account NOT charged
-- All or nothing!
```

**Follow-ups:** Write-ahead logging? Undo logs? Crash recovery?

**Mistakes:** Thinking partial commits are possible; not understanding rollback scope

---

#### Q4. What does Consistency guarantee?

**Answer:**
Consistency: database moves from one valid state to another. Transaction cannot commit if it violates constraints (FK, CHECK, NOT NULL). Database enforces rules—invalid transactions are rejected.

**SQL Example:**

```sql
BEGIN TRANSACTION;
    INSERT INTO Orders VALUES (1, 999, 100); -- customer_id 999 doesn't exist
COMMIT;
-- ERROR: Foreign key violation
-- Transaction ROLLED BACK, consistency preserved

BEGIN TRANSACTION;
    UPDATE Accounts SET balance = -500 WHERE account_id = 1; -- Violates CHECK
COMMIT;
-- ERROR: Check constraint violated
-- Transaction ROLLED BACK
```

**Follow-ups:** Application-level consistency? Eventual consistency? Consistency models?

**Mistakes:** Confusing with "correct business logic"; not understanding constraint enforcement

---

#### Q5. What does Durability guarantee?

**Answer:**
Durability: once COMMIT succeeds, changes survive crashes/power loss. Written to persistent storage (disk), not just memory. Recovery mechanisms ensure committed data never lost.

**SQL Example:**

```sql
BEGIN TRANSACTION;
    INSERT INTO CriticalData VALUES (1, 'Important');
COMMIT; -- Returns success

-- Immediate power failure!
-- On restart: INSERT still there (durability)
-- Write-ahead log replayed during recovery

-- Uncommitted transaction?
BEGIN TRANSACTION;
    INSERT INTO CriticalData VALUES (2, 'Lost');
-- CRASH before COMMIT
-- On restart: This INSERT gone (rolled back)
```

**Follow-ups:** WAL (Write-Ahead Logging)? Fsync? Durability trade-offs?

**Mistakes:** Assuming COMMIT is instant; not understanding log-based recovery

---

### 🟡 DAY 6 — Intermediate Questions

---

#### Q6. Why is Isolation required when multiple transactions run concurrently?

**Answer:**
Isolation prevents transactions from seeing each other's partial changes. Without it, concurrent transactions can read inconsistent data, producing unpredictable results. Ensures concurrent execution appears serial.

**SQL Example:**

```sql
-- Transaction A: Transfer $100
BEGIN;
    UPDATE Accounts SET balance = balance - 100 WHERE id = 1; -- Balance: 900
    UPDATE Accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;

-- Transaction B (concurrent): Calculate total
BEGIN;
    SELECT SUM(balance) FROM Accounts; -- Without isolation, might see 900 + 0 = 900 (missing $100!)
COMMIT;

-- With proper isolation: Transaction B sees either:
-- - Both accounts unchanged (before A) OR
-- - Both accounts changed (after A)
-- Never: Partial state
```

**Follow-ups:** Isolation levels? Locking mechanisms? MVCC?

**Mistakes:** Ignoring concurrent access; assuming single-user scenarios

---

#### Q7. What is a dirty read?

**Answer:**
Dirty read: reading uncommitted data from another transaction. If that transaction rolls back, you read data that "never existed". READ UNCOMMITTED allows this; higher levels prevent it.

**SQL Example:**

```sql
-- Transaction A
BEGIN;
    UPDATE Products SET price = 999 WHERE id = 1; -- Uncommitted
    -- ... doing more work ...

-- Transaction B (READ UNCOMMITTED)
BEGIN;
    SELECT price FROM Products WHERE id = 1; -- Reads 999 (dirty!)
COMMIT;

-- Transaction A
ROLLBACK; -- Price change undone, was never committed

-- Transaction B used price 999 that never existed!
-- Calculations, decisions based on non-existent data
```

**Follow-ups:** READ UNCOMMITTED use cases? Performance vs correctness?

**Mistakes:** Allowing dirty reads in production; not understanding rollback impact

---

#### Q8. What is a non-repeatable read?

**Answer:**
Non-repeatable read: same query returns different values within one transaction because another transaction committed an UPDATE between reads. Data changes mid-transaction. READ COMMITTED allows this; REPEATABLE READ prevents it.

**SQL Example:**

```sql
-- Transaction A
BEGIN;
    SELECT balance FROM Accounts WHERE id = 1; -- Returns 1000
    -- ... some processing ...

-- Transaction B (concurrent)
BEGIN;
    UPDATE Accounts SET balance = 500 WHERE id = 1;
COMMIT; -- Committed

-- Transaction A (continues)
    SELECT balance FROM Accounts WHERE id = 1; -- Returns 500 (different!)
COMMIT;

-- Same query, different results within one transaction
-- At READ COMMITTED: allowed
-- At REPEATABLE READ: second read still returns 1000
```

**Follow-ups:** REPEATABLE READ implementation? Snapshot isolation?

**Mistakes:** Not understanding isolation level behavior; unexpected query results

---

#### Q9. What is a phantom read?

**Answer:**
Phantom read: same query returns different row count because another transaction inserted/deleted rows matching the condition. Row set changes mid-transaction. REPEATABLE READ allows this; SERIALIZABLE prevents it.

**SQL Example:**

```sql
-- Transaction A
BEGIN;
    SELECT COUNT(*) FROM Orders WHERE status = 'pending'; -- Returns 5
    -- ... processing ...

-- Transaction B (concurrent)
BEGIN;
    INSERT INTO Orders VALUES (6, 'pending', ...);
COMMIT; -- New pending order

-- Transaction A (continues)
    SELECT COUNT(*) FROM Orders WHERE status = 'pending'; -- Returns 6 (phantom!)
COMMIT;

-- New row appeared! At REPEATABLE READ: happens
-- At SERIALIZABLE: prevented (range locks)
```

**Follow-ups:** Range locking? Gap locks? Index impact?

**Mistakes:** Confusing with non-repeatable read; not understanding row-level vs set-level

---

#### Q10. Why does READ COMMITTED allow non-repeatable and phantom reads?

**Answer:**
READ COMMITTED only guarantees reading committed data at query execution time. Doesn't hold locks or preserve snapshots between reads in same transaction. Other transactions can commit changes, affecting subsequent reads.

**SQL Example:**

```sql
-- READ COMMITTED behavior
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN;
    SELECT * FROM Products WHERE id = 1; -- Sees version A (committed)

-- Another transaction commits update

    SELECT * FROM Products WHERE id = 1; -- Sees version B (newly committed)
COMMIT;
-- Each read sees latest committed state, no consistency within transaction

-- REPEATABLE READ behavior
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
BEGIN;
    SELECT * FROM Products WHERE id = 1; -- Sees version A

-- Another transaction commits update

    SELECT * FROM Products WHERE id = 1; -- Still sees version A (snapshot)
COMMIT;
```

**Follow-ups:** Default isolation levels per DB? Performance implications?

**Mistakes:** Assuming READ COMMITTED provides consistency; wrong isolation for use case

---

### 🧠 DAY 6 — Reflection Questions

---

#### 1. Why are transactions considered expensive?

**Answer:**
Transactions require locking (prevents concurrent access), logging (durability), coordination (atomicity), and isolation management. All add CPU, memory, I/O overhead. Correctness isn't free—these mechanisms guarantee integrity.

**SQL Example:**

```sql
-- Simple non-transactional update: Fast but unsafe
UPDATE Accounts SET balance = 100 WHERE id = 1;

-- Transactional update: Slower but safe
BEGIN TRANSACTION; -- Acquire locks
    UPDATE Accounts SET balance = 100 WHERE id = 1; -- Write to log + table
COMMIT; -- Flush logs, release locks, coordination
-- Extra steps = overhead, but guarantees correctness
```

**Follow-ups:** Performance tuning? Batch transactions? Lock optimization?

**Mistakes:** Avoiding transactions for performance; not measuring real overhead

---

#### 2. Why is correctness prioritized over performance in transaction design?

**Answer:**
Incorrect data is permanent damage—corrupts business logic, financial records, user trust. Performance can be optimized later via caching, indexes, hardware. Can't optimize corrupted data—prevention cheaper than recovery.

**SQL Example:**

```sql
-- Fast but wrong: No transaction
UPDATE Accounts SET balance = balance - 100 WHERE id = 1;
UPDATE Accounts SET balance = balance + 100 WHERE id = 2;
-- Crash between = lost money (permanent damage)

-- Slower but correct: Transaction
BEGIN; -- Adds overhead
    UPDATE Accounts SET balance = balance - 100 WHERE id = 1;
    UPDATE Accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;
-- Crash = automatic rollback (no damage)
```

**Follow-ups:** Performance vs correctness trade-offs? Eventual consistency?

**Mistakes:** Sacrificing correctness for speed; not understanding recovery cost

---

#### 3. Why do higher isolation levels reduce concurrency?

**Answer:**
Higher isolation holds locks longer or uses broader lock ranges. Prevents more concurrent transactions from accessing same data. SERIALIZABLE blocks most concurrent access; READ COMMITTED allows more concurrency but less consistency.

**SQL Example:**

```sql
-- READ COMMITTED: High concurrency
BEGIN; -- Short-lived read locks
    SELECT * FROM Products WHERE id = 1;
COMMIT; -- Lock released immediately
-- Other transactions can read/write quickly

-- SERIALIZABLE: Low concurrency
BEGIN; -- Range locks held
    SELECT * FROM Products WHERE category = 'Electronics';
    -- Locks entire range, blocks other transactions
COMMIT; -- Locks held until commit
-- Other transactions wait = reduced concurrency
```

**Follow-ups:** Lock contention? Deadlocks? Optimistic locking?

**Mistakes:** Always using SERIALIZABLE; not understanding concurrency impact

---

### 🧠 DAY 6 — Critical Thinking Questions

---

#### Q1. What happens if the server crashes mid-transaction?

**Answer:**
Database rolls back transaction during recovery. Write-ahead logs (WAL) track all changes—on restart, uncommitted transactions are undone. Database restored to last committed consistent state. Durability for committed, atomicity for uncommitted.

**SQL Example:**

```sql
BEGIN TRANSACTION; -- Logged
    UPDATE Accounts SET balance = 500 WHERE id = 1; -- Logged but not committed
    -- CRASH HERE!

-- On restart:
-- Recovery process reads WAL
-- Sees uncommitted transaction
-- Applies UNDO: balance restored to original value
-- Database consistent as if transaction never started
```

**Follow-ups:** WAL internals? REDO vs UNDO logs? Checkpoint mechanism?

**Mistakes:** Assuming partial commits; not understanding log-based recovery

---

#### Q2. Can a DB violate consistency?

**Answer:**
Database won't violate its own constraints (FK, CHECK, NOT NULL). But can store logically incorrect data if schema doesn't capture all business rules. Consistency = schema-level, not business-logic-level.

**SQL Example:**

```sql
-- DB enforces schema constraints
CREATE TABLE Orders (
    total DECIMAL(10,2) CHECK (total > 0),
    customer_id INT REFERENCES Customers(customer_id)
);
INSERT INTO Orders VALUES (-100, 999); -- ERROR: Both constraints violated

-- But: Business logic not enforced
INSERT INTO Orders VALUES (100, 1); -- Succeeds
-- Business rule: "Order total must match sum of items"
-- DB doesn't know this rule = can be violated
-- Need: Application logic OR triggers OR CHECK constraints
```

**Follow-ups:** Triggers for business logic? Application-level validation? Trade-offs?

**Mistakes:** Thinking DB guarantees all correctness; not modeling business rules

---

#### Q3. Why are transactions slow?

**Answer:**
Transactions require locks (serialization), logs (durability writes), coordination (distributed agreement), and isolation management (versioning/blocking). Each adds I/O, CPU, and wait time. Correctness guarantees require this overhead.

**SQL Example:**

```sql
-- Non-transactional: Fast
INSERT INTO Logs VALUES (1, 'event'); -- Direct write

-- Transactional: Slower
BEGIN TRANSACTION;
    -- 1. Acquire locks
    -- 2. Write to WAL (disk I/O)
    -- 3. Write to table
    -- 4. Coordinate with other transactions
    INSERT INTO Logs VALUES (1, 'event');
    -- 5. Flush logs (fsync - expensive!)
COMMIT;
    -- 6. Release locks
-- Multiple steps = multiple overheads
```

**Follow-ups:** Optimization strategies? Group commits? Async commits?

**Mistakes:** Blaming "database slowness" without understanding ACID cost

---

#### Q4. Why are distributed transactions hard?

**Answer:**
Distributed transactions require multiple independent systems to agree on commit/rollback. Network failures create uncertainty (did remote commit?), partial crashes leave inconsistent state, and coordination protocols (2PC) are slow. CAP theorem limits guarantees.

**SQL Example:**

```sql
-- Distributed transaction (2-Phase Commit)
-- Phase 1: PREPARE
-- DB1: PREPARE transaction (can commit?)
-- DB2: PREPARE transaction (can commit?)
-- Coordinator: Both ready? Proceed

-- Phase 2: COMMIT
-- DB1: COMMIT
-- NETWORK FAILURE HERE!
-- DB2: Did coordinator say commit or rollback?
-- DB2 is blocked, uncertain state

-- Problems:
-- - Network partitions
-- - Coordinator failure
-- - Blocking (waiting for response)
-- - Performance (multiple round trips)
```

**Follow-ups:** 2PC vs 3PC? Saga pattern? Eventual consistency?

**Mistakes:** Treating distributed like local transactions; underestimating failure modes

---

## 📚 Day 6 Summary

**Core Concepts**: ACID properties (Atomicity, Consistency, Isolation, Durability), isolation levels (READ COMMITTED, REPEATABLE READ, SERIALIZABLE), read anomalies (dirty, non-repeatable, phantom), transaction overhead

**Key Takeaway**: Transactions guarantee correctness through ACID properties but add overhead. Choose isolation level based on consistency needs vs concurrency requirements. Higher isolation = more consistency, less concurrency.

---
