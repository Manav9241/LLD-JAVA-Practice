# DBMS Interview Preparation Notes

---

## 🟡 DAY 3: JOINS (Where SQL Becomes Relational)

### 🟡 DAY 3 — Beginner Questions

---

#### Q1. Why do joins exist?

**Answer:**
Normalization splits related data across multiple tables to avoid duplication. Joins reconstruct complete information at query time by combining facts from different tables based on relationships, without storing redundant data.

**SQL Example:**

```sql
-- Normalized: Data split across tables
CREATE TABLE Users (user_id INT PRIMARY KEY, name VARCHAR(100));
CREATE TABLE Orders (order_id INT PRIMARY KEY, user_id INT, amount DECIMAL);

-- Join reconstructs complete picture
SELECT u.name, o.order_id, o.amount
FROM Users u
INNER JOIN Orders o ON u.user_id = o.user_id;
-- Combines user info + order info without duplication
```

**Follow-ups:** Why not store everything in one table? Denormalization trade-offs? Join cost?

**Mistakes:** Saying "to make queries harder"; not explaining normalization benefit; forgetting data integrity

---

#### Q2. What is the difference between INNER JOIN and LEFT JOIN?

**Answer:**
INNER JOIN returns only matching rows from both tables. LEFT JOIN returns all rows from the left table plus matching rows from right (NULLs when no match). INNER filters, LEFT preserves left table.

**SQL Example:**

```sql
-- Users: 1=Alice, 2=Bob, 3=Charlie
-- Orders: order by user 1, 1, 2 (Charlie has no orders)

-- INNER JOIN: Only users WITH orders
SELECT u.name, o.order_id
FROM Users u
INNER JOIN Orders o ON u.user_id = o.user_id;
-- Returns: Alice (2 rows), Bob (1 row) — Charlie excluded

-- LEFT JOIN: ALL users, orders if they exist
SELECT u.name, o.order_id
FROM Users u
LEFT JOIN Orders o ON u.user_id = o.user_id;
-- Returns: Alice (2 rows), Bob (1 row), Charlie (1 row, order_id=NULL)
```

**Follow-ups:** RIGHT JOIN vs LEFT JOIN? FULL OUTER JOIN? When to use which?

**Mistakes:** Saying they're "almost the same"; not understanding NULL behavior; confusing syntax order

---

#### Q3. When does a LEFT JOIN return NULLs?

**Answer:**
LEFT JOIN returns NULLs for right table columns when no matching row exists in the right table for a given left table row. The left row is preserved, right side is filled with NULLs.

**SQL Example:**

```sql
-- Find users with no orders
SELECT u.user_id, u.name, o.order_id
FROM Users u
LEFT JOIN Orders o ON u.user_id = o.user_id
WHERE o.order_id IS NULL; -- Right side is NULL = no match
-- Returns users who haven't placed orders
```

**Follow-ups:** How to find unmatched rows? NULL handling in WHERE? IS NULL vs = NULL?

**Mistakes:** Using = NULL instead of IS NULL; thinking NULLs are errors; not understanding preservation

---

#### Q4. Why can joins return more rows than either table?

**Answer:**
Joins produce one result row for each matching combination. One-to-many relationships cause row multiplication—each left row appears once per matching right row. Cartesian products (missing ON) explode row counts.

**SQL Example:**

```sql
-- Users: 2 rows
-- Orders: User 1 has 3 orders

SELECT u.name, o.order_id
FROM Users u
INNER JOIN Orders o ON u.user_id = o.user_id;
-- User 1 appears 3 times (once per order) — 3+ rows from 2-row table

-- DANGER: Missing ON clause (Cartesian product)
SELECT * FROM Users u, Orders o; -- No join condition!
-- 2 users × 100 orders = 200 rows!
```

**Follow-ups:** One-to-many vs many-to-many? How to prevent duplication? DISTINCT cost?

**Mistakes:** Expecting same row count; not understanding multiplication; forgetting ON clause

---

#### Q5. Why does filtering after a LEFT JOIN change results?

**Answer:**
Filtering on right table columns in WHERE removes rows where those columns are NULL, converting LEFT JOIN behavior to INNER JOIN. To preserve LEFT JOIN semantics, move right-side filters to the ON clause.

**SQL Example:**

```sql
-- Wrong: Filter in WHERE (acts like INNER JOIN)
SELECT u.name, o.order_id
FROM Users u
LEFT JOIN Orders o ON u.user_id = o.user_id
WHERE o.amount > 100; -- Excludes users with no orders (NULL amount)
-- Charlie excluded even though LEFT JOIN intended to keep him

-- Correct: Filter in ON (preserves LEFT JOIN)
SELECT u.name, o.order_id
FROM Users u
LEFT JOIN Orders o ON u.user_id = o.user_id AND o.amount > 100;
-- Charlie included with NULL order_id
```

**Follow-ups:** ON vs WHERE clause? Filter order execution? Performance difference?

**Mistakes:** Always filtering in WHERE; not understanding NULL exclusion; thinking they're equivalent

---

### 🟡 DAY 3 — Intermediate Questions

---

#### Q6. Why are joins expensive?

**Answer:**
Joins compare rows from multiple tables, generating large intermediate result sets. Without indexes on join columns, database does full table scans. Cost grows with data size—poor indexes or missing WHERE clauses cause performance disasters.

**SQL Example:**

```sql
-- Expensive: No indexes, large tables
SELECT *
FROM Orders o
JOIN OrderItems oi ON o.order_id = oi.order_id; -- No index on order_id
-- Full scan of Orders × full scan of OrderItems = millions of comparisons

-- Optimized: Indexed join columns
CREATE INDEX idx_order_id ON OrderItems(order_id);
-- Database uses index for fast lookups instead of full scans
```

**Follow-ups:** Index impact on joins? Execution plan analysis? Join algorithms (nested loop, hash, merge)?

**Mistakes:** Saying "joins are slow" without mentioning indexes; not checking execution plans

---

#### Q7. Why does normalization force joins?

**Answer:**
Normalization eliminates data duplication by splitting related facts into separate tables. Querying complete information requires reassembling data via joins. Trade-off: storage efficiency and consistency vs query complexity.

**SQL Example:**

```sql
-- Denormalized: Duplicated data (bad)
CREATE TABLE Orders (
    order_id INT,
    customer_name VARCHAR(100), -- Duplicated for each order
    customer_email VARCHAR(100), -- Duplicated
    customer_phone VARCHAR(20)   -- Duplicated
);
-- Update email? Must update ALL orders (inconsistency risk)

-- Normalized: Separate tables (good)
CREATE TABLE Customers (customer_id INT PRIMARY KEY, name, email, phone);
CREATE TABLE Orders (order_id INT, customer_id INT);
-- Update email? One place, no duplication
-- Query complete info? Need join
SELECT o.order_id, c.name, c.email
FROM Orders o
JOIN Customers c ON o.customer_id = c.customer_id;
```

**Follow-ups:** Denormalization when? Read-heavy vs write-heavy? Materialized views?

**Mistakes:** Thinking normalization is "over-engineering"; not understanding update anomalies

---

#### Q8. Why do joins scale poorly on large datasets?

**Answer:**
Join cost grows with table sizes—more rows mean exponentially more comparisons. Without proper indexing, selective filtering, or partitioning, joins become I/O bottlenecks. Many-to-many joins and multiple joins compound the problem.

**SQL Example:**

```sql
-- Bad: Large unfiltered join
SELECT *
FROM Orders o -- 10M rows
JOIN OrderItems oi ON o.order_id = oi.order_id -- 100M rows
JOIN Products p ON oi.product_id = p.product_id; -- 1M rows
-- Massive intermediate result sets

-- Better: Filter early, indexed columns
SELECT o.order_id, p.product_name
FROM Orders o
JOIN OrderItems oi ON o.order_id = oi.order_id
JOIN Products p ON oi.product_id = p.product_id
WHERE o.order_date >= '2026-01-01' -- Reduces dataset early
  AND p.category = 'Electronics'; -- Further filtering
-- Indexes on order_id, product_id, order_date, category
```

**Follow-ups:** Query optimization strategies? Partition pruning? Covering indexes?

**Mistakes:** Not filtering early; ignoring execution plans; assuming "database will optimize"

---

### 🧠 DAY 3 — Reflection Questions

---

#### 1. Why does correctness create complexity?

**Answer:**
Correctness requires enforcing normalization, referential integrity, and avoiding duplication. These guarantees necessitate splitting data across tables and using joins to reconstruct it, increasing query complexity but ensuring data validity.

**SQL Example:**

```sql
-- Simple but wrong: Single table with duplicates
SELECT * FROM OrdersWithCustomerData; -- Easy query, corrupted data

-- Complex but correct: Normalized with joins
SELECT o.*, c.*
FROM Orders o
JOIN Customers c ON o.customer_id = c.customer_id; -- Harder query, guaranteed consistency
```

**Follow-ups:** Complexity vs correctness trade-offs? When to denormalize? Technical debt?

**Mistakes:** Choosing simplicity over correctness; not understanding long-term costs

---

#### 2. Why does relational design trade simplicity for safety?

**Answer:**
Relational design prioritizes data integrity—no duplicates, consistent relationships, enforced constraints. This makes queries more complex (joins required) but prevents silent corruption. Safety outlives convenience.

**SQL Example:**

```sql
-- Simple but unsafe: No constraints
CREATE TABLE Orders (customer_name VARCHAR(100));
INSERT INTO Orders VALUES ('Jon Doe'); -- Typo! Data corrupted

-- Complex but safe: Constraints + joins
CREATE TABLE Customers (customer_id INT PRIMARY KEY, name VARCHAR(100) UNIQUE);
CREATE TABLE Orders (customer_id INT, FOREIGN KEY (customer_id) REFERENCES Customers(customer_id));
-- Can't reference non-existent customer, guaranteed consistency
```

**Follow-ups:** NoSQL trade-offs? Eventual consistency? CAP theorem?

**Mistakes:** Valuing "easy queries" over data integrity; not planning for scale

---

#### 3. Why do ORMs try to hide joins, and why is that risky?

**Answer:**
ORMs abstract SQL to make development feel simpler—accessing related objects looks like property access. Risk: hidden N+1 queries, unoptimized joins, and massive performance problems. Abstraction hides cost.

**SQL Example:**

```sql
-- ORM code looks simple
// Java/Python
for (Order order : orders) { // Fetches orders
    print(order.getCustomer().getName()); // N queries for customers!
}
// Executes: SELECT * FROM Orders, then SELECT * FROM Customers WHERE id=? (N times)

-- SQL reality: Should be ONE query
SELECT o.order_id, c.name
FROM Orders o
JOIN Customers c ON o.customer_id = c.customer_id;
-- 1 query vs N+1 queries
```

**Follow-ups:** N+1 problem? Eager vs lazy loading? When to write raw SQL?

**Mistakes:** Trusting ORM blindly; not profiling queries; ignoring explain plans

---

## 📚 Day 3 Summary

**Core Concepts**: INNER vs LEFT joins (matching vs preservation), join multiplication (one-to-many), filtering placement (ON vs WHERE), join cost (indexes critical), normalization trade-off (consistency vs complexity)

**Key Takeaway**: Joins are the price of normalization—they enable data integrity but require careful optimization. Always index join columns, filter early, and understand what your ORM generates.

---
