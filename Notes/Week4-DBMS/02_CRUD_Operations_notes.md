# DBMS Interview Preparation Notes

---

## 🟡 DAY 2: CRUD Operations

### 🟡 DAY 2 — Beginner Questions

---

#### Q1. Why is SELECT \* discouraged?

**Answer:**
SELECT \* couples code to table structure—if columns are added/reordered, queries fetch unexpected data. It wastes bandwidth transferring unused columns and prevents index-only scans. Explicit column selection makes queries stable, readable, and efficient.

**SQL Example:**

```sql
-- Bad: SELECT *
SELECT * FROM Users; -- Returns 50 columns, you need 3

-- Good: Explicit columns
SELECT user_id, name, email FROM Users; -- Clear, efficient, stable
```

**Follow-ups:** Impact on index usage? Performance with large tables? When is SELECT \* OK?

**Mistakes:** Saying "it's just convenience"; not mentioning network/memory waste; forgetting about schema changes

---

#### Q2. What happens if UPDATE has no WHERE clause?

**Answer:**
UPDATE without WHERE modifies every row in the table, potentially causing catastrophic data loss. Without transactions, this is irreversible. Always use WHERE, even if it's WHERE 1=1 to make the intent explicit.

**SQL Example:**

```sql
-- DANGER: Updates ALL rows
UPDATE Employees SET salary = 50000;
-- Every employee now has same salary!

-- Safe: Targeted update
UPDATE Employees SET salary = 50000 WHERE emp_id = 123;

-- Best practice: Use transactions
BEGIN TRANSACTION;
UPDATE Employees SET salary = salary * 1.1 WHERE department = 'Sales';
-- Verify with SELECT before committing
SELECT * FROM Employees WHERE department = 'Sales';
COMMIT; -- or ROLLBACK if wrong
```

**Follow-ups:** How do transactions help? Can you recover without backups? Safe update mode?

**Mistakes:** Not mentioning transactions as protection; forgetting this applies to DELETE too

---

#### Q3. Why is DELETE more dangerous than UPDATE?

**Answer:**
DELETE permanently removes rows and their history—you can't "undo" it without backups. UPDATE preserves the row, just changes values. In many systems, soft deletes (status flags) are preferred over hard deletes for this reason.

**SQL Example:**

```sql
-- Hard delete: Permanent
DELETE FROM Orders WHERE order_id = 123;
-- Data GONE forever (unless you have backups/logs)

-- Soft delete: Reversible
UPDATE Orders SET is_deleted = TRUE, deleted_at = NOW() WHERE order_id = 123;
-- Row still exists, can be recovered

-- Query active records
SELECT * FROM Orders WHERE is_deleted = FALSE;
```

**Follow-ups:** Soft delete pros/cons? CASCADE deletes? Point-in-time recovery?

**Mistakes:** Not mentioning soft deletes; forgetting about foreign key cascades; saying "backups always work"

---

#### Q4. Why should filtering be done in SQL, not Java?

**Answer:**
SQL filtering uses indexes, happens at the data source, and transfers only needed rows. Java filtering loads all rows into memory, wastes network bandwidth, and bypasses database optimizations. Database is designed for filtering—use it.

**SQL Example:**

```sql
-- Bad: Fetch all, filter in Java
SELECT * FROM Orders; -- Returns 1 million rows
// Java code
orders.stream().filter(o -> o.getAmount() > 1000).collect(...)
// 999,000 rows wasted!

-- Good: Filter in SQL
SELECT order_id, customer_id, amount
FROM Orders
WHERE amount > 1000; -- Returns 1,000 rows (uses index!)
```

**Follow-ups:** What about complex business logic? When to use application filtering? Indexed vs non-indexed?

**Mistakes:** Saying "Java is faster"; not mentioning index usage; forgetting network overhead

---

#### Q5. Why does SQL prefer sets over row-by-row logic?

**Answer:**
SQL is declarative and set-based—you describe what you want, not how to get it. The query optimizer chooses the best execution plan. Row-by-row loops (cursors) bypass optimization, are slower, and don't scale. Set operations leverage parallelism and indexes.

**SQL Example:**

```sql
-- Bad: Row-by-row cursor (slow)
DECLARE cursor FOR SELECT user_id FROM Users;
OPEN cursor;
FETCH NEXT FROM cursor INTO @user_id;
WHILE @@FETCH_STATUS = 0
BEGIN
    UPDATE Orders SET processed = 1 WHERE user_id = @user_id;
    FETCH NEXT FROM cursor INTO @user_id;
END;
CLOSE cursor;

-- Good: Set-based operation (fast)
UPDATE Orders
SET processed = 1
WHERE user_id IN (SELECT user_id FROM Users);
-- Single operation, optimized execution plan
```

**Follow-ups:** When are cursors necessary? Query optimizer role? Set-based joins?

**Mistakes:** Thinking loops are "more familiar"; not understanding optimizer benefits; forgetting about scalability

---

### 🧠 DAY 2 — Reflection Questions

---

#### 1. Why does SQL force discipline?

**Answer:**
SQL operates directly on persistent, shared data. Small mistakes (missing WHERE, typo in UPDATE) have immediate, large-scale consequences affecting real users. This forces developers to be explicit, careful, and intentional with every operation.

**SQL Example:**

```sql
-- One character difference, massive impact
UPDATE Users SET status = 'banned'; -- Missing WHERE: bans EVERYONE
UPDATE Users SET status = 'banned' WHERE user_id = 123; -- Correct
```

**Follow-ups:** How to prevent mistakes? Code review for SQL? Testing strategies?

**Mistakes:** Saying "just be careful"; not mentioning transactions/rollback; forgetting staging environments

---

#### 2. Why do database operations feel "scary"?

**Answer:**
Database stores shared, production data that outlives any single application. Operations are high-risk—errors affect multiple users immediately and are hard/impossible to undo. Unlike app code (redeployable), bad data persists.

**SQL Example:**

```sql
-- App bug: Redeploy fixes it
// Java code bug - just restart

-- DB mistake: Recovery needed
DROP TABLE Orders; -- Can't just "restart" the database!
```

**Follow-ups:** How to reduce fear? Proper testing? Backup strategies?

**Mistakes:** Not mentioning backups; forgetting about transaction isolation; ignoring staging databases

---

#### 3. Why are mistakes here expensive?

**Answer:**
Database mistakes corrupt shared, persistent data across the entire system. Recovery requires downtime, backup restores, or manual data reconstruction. Lost data can mean lost business, legal issues, or destroyed user trust.

**SQL Example:**

```sql
-- Expensive mistakes
DELETE FROM Transactions; -- Lost financial records
UPDATE Users SET password_hash = 'abc'; -- All users locked out
DROP TABLE Customers; -- Business-critical data gone
```

**Follow-ups:** Cost of downtime? Backup recovery time objectives? Prevention strategies?

**Mistakes:** Saying "mistakes happen"; not mentioning financial/legal impact; forgetting about SLAs

---

## 📚 Day 2 Summary

**Core Concepts**: Explicit queries (SELECT specific columns), WHERE clause discipline (always filter), set-based operations (avoid cursors), SQL-side filtering (use indexes), transaction safety (test before commit)

**Key Takeaway**: CRUD operations work on permanent, shared data—mistakes are expensive and hard to undo. Always be explicit, use transactions, and filter at the database level.

---
