# DBMS Interview Preparation Notes

---

## 🟡 DAY 4: Constraints, Indexes & Data Integrity

### 🟡 DAY 4 — Beginner Questions

---

#### Q1. Why do constraints belong in the database?

**Answer:**
Database is the final authority for data correctness—it outlives applications and protects against bugs, bypasses (direct SQL, scripts), and multiple clients. Constraints ensure permanent data integrity regardless of which application writes.

**SQL Example:**

```sql
-- App validation can be bypassed
// Java: if (age > 0) save(); // Bug or direct SQL bypasses this

-- DB constraint: Always enforced
CREATE TABLE Users (
    age INT CHECK (age >= 0 AND age <= 150)
);
INSERT INTO Users VALUES (-5); -- ERROR: Always rejected
```

**Follow-ups:** Should you validate in both app and DB? Performance cost? Constraint types?

**Mistakes:** Saying "only DB" or "only app"; not mentioning multiple clients/scripts

---

#### Q2. What is the difference between PRIMARY KEY and UNIQUE?

**Answer:**
PRIMARY KEY defines row identity, must be NOT NULL, only one per table. UNIQUE enforces business uniqueness, can allow NULLs (in some DBs), multiple allowed per table. PK for identity, UNIQUE for business rules.

**SQL Example:**

```sql
CREATE TABLE Users (
    user_id INT PRIMARY KEY,        -- Identity, NOT NULL, only one
    email VARCHAR(100) UNIQUE,      -- Business rule, can be multiple
    phone VARCHAR(20) UNIQUE,       -- Another unique constraint
    ssn VARCHAR(11) UNIQUE          -- Another one
);

-- Some DBs allow multiple NULLs in UNIQUE
INSERT INTO Users VALUES (1, 'a@x.com', NULL, NULL); -- OK
INSERT INTO Users VALUES (2, 'b@x.com', NULL, NULL); -- OK (NULL != NULL)
INSERT INTO Users VALUES (3, 'a@x.com', NULL, NULL); -- ERROR: email duplicate
```

**Follow-ups:** Can UNIQUE be NULL? Multiple UNIQUEs vs composite PK? Clustered index?

**Mistakes:** Saying they're the same; not mentioning NULL handling; forgetting "one PK per table"

---

#### Q3. Why is NOT NULL important?

**Answer:**
NOT NULL removes ambiguity—value is always present. Simplifies queries (no NULL checks), prevents incomplete data, and makes comparisons predictable. NULLs complicate logic and propagate through expressions.

**SQL Example:**

```sql
-- Without NOT NULL: Unpredictable
CREATE TABLE Products (price DECIMAL(10,2));
INSERT INTO Products VALUES (NULL);
SELECT * FROM Products WHERE price < 100; -- Misses NULL row
SELECT AVG(price) FROM Products; -- Excludes NULLs silently

-- With NOT NULL: Clear and predictable
CREATE TABLE Products (
    price DECIMAL(10,2) NOT NULL DEFAULT 0
);
INSERT INTO Products VALUES (NULL); -- ERROR: Rejected
-- All queries work predictably, no NULL surprises
```

**Follow-ups:** When to allow NULLs? DEFAULT values? NULL propagation?

**Mistakes:** Allowing NULLs "for flexibility"; not understanding NULL comparison issues

---

#### Q4. What problem do FOREIGN KEYS solve?

**Answer:**
Foreign keys enforce referential integrity—ensure relationships remain valid. Prevent orphan records (references to non-existent data), cascade deletes/updates, and maintain data consistency across tables.

**SQL Example:**

```sql
-- Without FK: Orphan data allowed
CREATE TABLE Orders (customer_id INT);
INSERT INTO Orders VALUES (999); -- Customer 999 doesn't exist! Data corrupted

-- With FK: Referential integrity enforced
CREATE TABLE Customers (customer_id INT PRIMARY KEY);
CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);
INSERT INTO Orders VALUES (1, 999); -- ERROR: FK violation
-- Also prevents deleting customers with orders
DELETE FROM Customers WHERE customer_id = 1; -- ERROR: Has orders

-- CASCADE options
FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
    ON DELETE CASCADE; -- Delete orders when customer deleted
```

**Follow-ups:** CASCADE vs RESTRICT? Performance impact? Circular FKs?

**Mistakes:** Thinking FKs are "just for documentation"; not mentioning orphan prevention

---

#### Q5. Why can constraints feel "annoying"?

**Answer:**
Constraints fail fast, blocking invalid operations immediately during development. Feels restrictive, but prevents silent corruption that's exponentially harder to fix in production. Short-term pain for long-term safety.

**SQL Example:**

```sql
-- Development: Constraint blocks quick test
INSERT INTO Orders VALUES (1, NULL, -100);
-- ERROR: customer_id cannot be NULL
-- ERROR: amount must be positive
-- Annoying! But...

-- Production without constraints: Silent corruption
INSERT INTO Orders VALUES (1, NULL, -100); -- Succeeds
-- Weeks later: "Why do we have orders with no customer?"
-- Massive data cleanup required, possibly data loss
```

**Follow-ups:** Disabling constraints? Testing with real constraints? Cost of corruption?

**Mistakes:** Disabling constraints "temporarily"; not understanding prevention vs cleanup cost

---

### 🟡 DAY 4 — Intermediate Questions

---

#### Q6. Why are indexes not free?

**Answer:**
Indexes consume storage (duplicate data structures) and require updates on every INSERT/UPDATE/DELETE. They trade write performance for read performance. Index maintenance adds overhead to all write operations.

**SQL Example:**

```sql
-- No index: Fast writes, slow reads
CREATE TABLE Products (product_id INT, name VARCHAR(100));
INSERT INTO Products VALUES (1, 'Laptop'); -- Fast
SELECT * FROM Products WHERE name = 'Laptop'; -- Slow: Full scan

-- With index: Slower writes, fast reads
CREATE INDEX idx_name ON Products(name);
INSERT INTO Products VALUES (2, 'Phone'); -- Slower: Must update index
UPDATE Products SET name = 'Gaming Laptop' WHERE product_id = 1; -- Slower
SELECT * FROM Products WHERE name = 'Laptop'; -- Fast: Index lookup

-- Multiple indexes multiply cost
CREATE INDEX idx_id ON Products(product_id);
CREATE INDEX idx_composite ON Products(name, product_id);
-- Every write now updates 3 indexes!
```

**Follow-ups:** When to add indexes? Covering indexes? Write-heavy vs read-heavy?

**Mistakes:** Adding indexes everywhere; not measuring impact; forgetting storage cost

---

#### Q7. Why can too many indexes hurt performance?

**Answer:**
Each index adds write overhead—inserts/updates must maintain all indexes. Query optimizer can be confused by too many choices. Storage bloat and maintenance overhead (fragmentation, rebuilds) compound costs.

**SQL Example:**

```sql
-- Over-indexed table
CREATE TABLE Orders (
    order_id INT PRIMARY KEY, -- Index 1
    customer_id INT,
    product_id INT,
    order_date DATE,
    status VARCHAR(20),
    amount DECIMAL(10,2)
);
CREATE INDEX idx_customer ON Orders(customer_id); -- Index 2
CREATE INDEX idx_product ON Orders(product_id);   -- Index 3
CREATE INDEX idx_date ON Orders(order_date);      -- Index 4
CREATE INDEX idx_status ON Orders(status);        -- Index 5
CREATE INDEX idx_amount ON Orders(amount);        -- Index 6
-- Every insert updates 6 indexes!

-- Better: Strategic indexes based on actual queries
CREATE INDEX idx_customer_date ON Orders(customer_id, order_date);
-- Composite index serves multiple query patterns
```

**Follow-ups:** Identifying unused indexes? Query plan analysis? Index consolidation?

**Mistakes:** "Index everything just in case"; not profiling queries; ignoring write cost

---

#### Q8. Why should CHECK constraints exist even if validation exists in code?

**Answer:**
Code can be buggy, bypassed (SQL scripts, other apps), or changed. CHECK constraints guarantee invalid states are impossible at database level, providing permanent, centralized validation that survives code changes.

**SQL Example:**

```sql
-- App validation: Can be bypassed
// Java
if (age >= 18) { save(user); } // Direct SQL bypasses this

-- CHECK constraint: Always enforced
CREATE TABLE Users (
    age INT CHECK (age >= 18),
    status VARCHAR(20) CHECK (status IN ('active', 'suspended', 'deleted')),
    salary DECIMAL(10,2) CHECK (salary >= 0),
    email VARCHAR(100) CHECK (email LIKE '%@%.%')
);

INSERT INTO Users VALUES (15, 'pending', -1000, 'invalid');
-- ERROR: Multiple constraint violations
-- No matter how data arrives (Java, Python, SQL script), rules enforced
```

**Follow-ups:** Complex CHECK constraints? Performance impact? Cross-column validation?

**Mistakes:** Trusting app validation only; not using CHECK for enum-like columns

---

### 🧠 DAY 4 — Reflection Questions

---

#### 1. Why are databases pessimistic by design?

**Answer:**
Databases assume bugs, failures, and concurrent access will occur. Pessimism ensures correctness under worst-case scenarios—constraints catch errors, transactions isolate changes, locks prevent conflicts. Optimism leads to corruption.

**SQL Example:**

```sql
-- Pessimistic: Assumes failures
CREATE TABLE Accounts (
    balance DECIMAL(10,2) CHECK (balance >= 0), -- Prevents negative
    CONSTRAINT pk PRIMARY KEY (account_id)      -- Prevents duplicates
);
BEGIN TRANSACTION; -- Assumes need for rollback
    UPDATE Accounts SET balance = balance - 100 WHERE account_id = 1;
    -- Something fails? ROLLBACK protects data
COMMIT;
```

**Follow-ups:** Optimistic vs pessimistic locking? ACID properties? Failure handling?

**Mistakes:** Assuming "it'll probably work"; not handling constraint violations

---

#### 2. Why must correctness be enforced centrally?

**Answer:**
Central enforcement (database) ensures consistent rules across all applications, services, and scripts. Distributed validation leads to inconsistencies—different clients apply different rules, creating data chaos.

**SQL Example:**

```sql
-- Central enforcement
CREATE TABLE Products (
    price DECIMAL(10,2) CHECK (price > 0)
);
-- Java app, Python script, SQL migration: ALL must follow same rule

-- Without central enforcement: Chaos
// Java: if (price > 0) save();    // Allows 0
// Python: if (price >= 0) save(); // Different rule!
// SQL: INSERT VALUES (-10);       // No validation
-- Database has inconsistent data
```

**Follow-ups:** Microservices validation? Service boundaries? Data ownership?

**Mistakes:** Trusting "we'll coordinate validation"; not having single source of truth

---

#### 3. Why is performance always a trade-off?

**Answer:**
Optimizing one dimension degrades another—indexes speed reads but slow writes, normalization ensures consistency but requires joins, constraints guarantee correctness but add overhead. Choose based on actual workload.

**SQL Example:**

```sql
-- Read-optimized: Denormalized, indexed
CREATE TABLE OrderSummary (
    order_id INT,
    customer_name VARCHAR(100), -- Duplicated data
    INDEX idx_customer (customer_name)
);
-- Fast reads, slow writes, data duplication

-- Write-optimized: Normalized, fewer indexes
CREATE TABLE Orders (order_id INT, customer_id INT);
CREATE TABLE Customers (customer_id INT PRIMARY KEY, name VARCHAR(100));
-- Fast writes, slower reads (joins needed), no duplication
```

**Follow-ups:** Read vs write optimization? Caching strategies? Measuring trade-offs?

**Mistakes:** Optimizing prematurely; not profiling real workload; assuming "faster everywhere"

---

## 📚 Day 4 Summary

**Core Concepts**: Constraints (DB-level validation), PRIMARY KEY vs UNIQUE (identity vs business rule), NOT NULL (remove ambiguity), FOREIGN KEY (referential integrity), indexes (read/write trade-off)

**Key Takeaway**: Constraints and indexes are the database's core protection mechanisms—they enforce correctness and optimize performance, but require understanding trade-offs. Always centralize validation in the database.

---
