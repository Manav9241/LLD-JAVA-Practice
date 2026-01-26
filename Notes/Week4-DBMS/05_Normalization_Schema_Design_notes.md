# DBMS Interview Preparation Notes

---

## 🟡 DAY 5: Normalization & Schema Design

### 🟡 DAY 5 — Beginner Questions

---

#### Q1. Why does normalization exist?

**Answer:**
Normalization ensures each fact is stored in exactly one place, preventing contradictory data. When information is duplicated, updates can be missed or partial, causing silent inconsistencies. Single source of truth = consistent truth.

**SQL Example:**

```sql
-- Denormalized: Customer info duplicated per order
CREATE TABLE Orders (
    order_id INT,
    customer_name VARCHAR(100),
    customer_email VARCHAR(100), -- Duplicated across orders
    customer_phone VARCHAR(20)
);
-- Problem: Customer changes email, must update ALL orders (easy to miss some)

-- Normalized: Customer info in one place
CREATE TABLE Customers (
    customer_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100), -- Stored once
    phone VARCHAR(20)
);
CREATE TABLE Orders (order_id INT, customer_id INT);
-- Update email once, all orders automatically reference new value
```

**Follow-ups:** Normal forms (1NF, 2NF, 3NF)? When to denormalize? Trade-offs?

**Mistakes:** Saying "to make queries harder"; not explaining duplication problems

---

#### Q2. What problem does redundancy cause?

**Answer:**
Redundancy stores the same fact in multiple places, which can drift out of sync. Partial updates create contradictory versions of truth—database can't determine which is correct. Update anomalies and data corruption follow.

**SQL Example:**

```sql
-- Redundant: Product price stored per order
INSERT INTO Orders VALUES (1, 'Laptop', 999.99);
INSERT INTO Orders VALUES (2, 'Laptop', 999.99);
INSERT INTO Orders VALUES (3, 'Laptop', 999.99);

-- Price increases to 1099.99, but partial update
UPDATE Orders SET price = 1099.99 WHERE order_id IN (1, 2); -- Missed order 3!
-- Now: What's the real laptop price? 999.99 or 1099.99? Database doesn't know!

-- Non-redundant: Price in Products table
CREATE TABLE Products (product_id INT PRIMARY KEY, price DECIMAL(10,2));
CREATE TABLE Orders (order_id INT, product_id INT, price_at_purchase DECIMAL(10,2));
-- Historical orders keep purchase price, current price in Products (single source)
```

**Follow-ups:** Historical vs current data? Snapshot patterns? Audit trails?

**Mistakes:** Confusing redundancy with backups; not understanding drift

---

#### Q3. What is an update anomaly?

**Answer:**
Update anomaly: single logical change requires updating multiple rows. If updates are partial (some succeed, some fail), database becomes inconsistent without errors. Normalized design prevents this—update happens in one place.

**SQL Example:**

```sql
-- Update anomaly example
CREATE TABLE Courses (
    student_id INT,
    course_name VARCHAR(100),
    instructor VARCHAR(100) -- Duplicated for each student in course
);
INSERT INTO Courses VALUES (1, 'Database Systems', 'Dr. Smith');
INSERT INTO Courses VALUES (2, 'Database Systems', 'Dr. Smith');
INSERT INTO Courses VALUES (3, 'Database Systems', 'Dr. Smith');

-- Instructor changes name
UPDATE Courses SET instructor = 'Dr. Smith-Jones' WHERE student_id IN (1, 2);
-- Oops! Forgot student 3
-- Now: Who teaches Database Systems? Both names exist!

-- Normalized: Instructor stored once
CREATE TABLE Instructors (instructor_id INT PRIMARY KEY, name VARCHAR(100));
CREATE TABLE Courses (course_id INT, instructor_id INT);
-- Update instructor name once, all courses automatically reflect change
```

**Follow-ups:** Delete anomalies? Insert anomalies? 2NF and 3NF prevention?

**Mistakes:** Not understanding "single logical change, multiple physical changes"

---

#### Q4. What does 1NF prevent?

**Answer:**
First Normal Form prevents storing multiple values in a single column (arrays, comma-separated lists). Ensures data is atomic—each cell contains one value, making it indexable, searchable, and queryable without parsing.

**SQL Example:**

```sql
-- Violates 1NF: Multiple values in one column
CREATE TABLE Students (
    student_id INT,
    courses VARCHAR(200) -- 'Math,Physics,Chemistry'
);
-- Problems:
-- How to find students in Physics? LIKE '%Physics%' (slow, error-prone)
-- How to count courses per student? Parse string in application
-- Can't enforce foreign keys

-- 1NF compliant: Separate rows for each value
CREATE TABLE StudentCourses (
    student_id INT,
    course_name VARCHAR(100),
    PRIMARY KEY (student_id, course_name)
);
-- Now: SELECT * FROM StudentCourses WHERE course_name = 'Physics' (indexed, fast)
```

**Follow-ups:** JSON columns violate 1NF? Arrays in PostgreSQL? Trade-offs?

**Mistakes:** Storing comma-separated values; not understanding query complexity

---

#### Q5. Why should product data not live in the orders table?

**Answer:**
Product data belongs to the product entity, not orders. Storing it in orders duplicates facts (same product described differently), creates update anomalies (change product name = update all orders), and confuses historical vs current data.

**SQL Example:**

```sql
-- Bad: Product data in Orders
CREATE TABLE Orders (
    order_id INT,
    product_name VARCHAR(100),
    product_description TEXT,
    product_category VARCHAR(50),
    price DECIMAL(10,2)
);
-- Problems: Product renamed? Must update all historical orders (wrong!)
-- Product description typo? Inconsistent across orders

-- Good: Separate Products table
CREATE TABLE Products (
    product_id INT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    category VARCHAR(50),
    current_price DECIMAL(10,2)
);
CREATE TABLE Orders (
    order_id INT,
    product_id INT,
    price_at_purchase DECIMAL(10,2), -- Historical price snapshot
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
-- Product info stored once, orders reference + capture purchase price
```

**Follow-ups:** Historical data patterns? Snapshot vs reference? Slowly changing dimensions?

**Mistakes:** Mixing current and historical data; duplicating entity attributes

---

### 🟡 DAY 5 — Intermediate Questions

---

#### Q6. What is a transitive dependency?

**Answer:**
Transitive dependency: non-key column depends on another non-key column, not directly on primary key. Indirectly duplicates data—changing the dependent column requires finding all related rows. Violates 3NF.

**SQL Example:**

```sql
-- Transitive dependency (violates 3NF)
CREATE TABLE Employees (
    emp_id INT PRIMARY KEY,
    name VARCHAR(100),
    department_name VARCHAR(100), -- Depends on emp_id
    department_location VARCHAR(100) -- Depends on department_name, not emp_id!
);
-- Problem: Department moves location, must update ALL employees in that dept
INSERT INTO Employees VALUES (1, 'Alice', 'Sales', 'Building A');
INSERT INTO Employees VALUES (2, 'Bob', 'Sales', 'Building A');
UPDATE Employees SET department_location = 'Building B' WHERE department_name = 'Sales';
-- Transitive: emp_id → department_name → department_location

-- 3NF: Remove transitive dependency
CREATE TABLE Departments (
    dept_id INT PRIMARY KEY,
    name VARCHAR(100),
    location VARCHAR(100) -- Stored once
);
CREATE TABLE Employees (
    emp_id INT PRIMARY KEY,
    name VARCHAR(100),
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES Departments(dept_id)
);
-- Now: Update department location once, all employees automatically reflect it
```

**Follow-ups:** 2NF vs 3NF? Boyce-Codd Normal Form? Denormalization trade-offs?

**Mistakes:** Confusing with foreign keys; not seeing indirect dependency chain

---

#### Q7. Why does 3NF matter in real systems?

**Answer:**
3NF ensures single authoritative source for each fact by eliminating transitive dependencies. Prevents indirect duplication, simplifies updates (change once, not everywhere), and maintains consistency as system evolves. Essential for long-term data health.

**SQL Example:**

```sql
-- Not 3NF: Update anomaly
CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    customer_city VARCHAR(100),
    tax_rate DECIMAL(5,2) -- Depends on city, not order!
);
-- Tax rate changes in a city? Must update thousands of orders!

-- 3NF: Tax rate stored per city
CREATE TABLE Cities (
    city_name VARCHAR(100) PRIMARY KEY,
    tax_rate DECIMAL(5,2)
);
CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    city_name VARCHAR(100),
    FOREIGN KEY (city_name) REFERENCES Cities(city_name)
);
-- Update tax rate once in Cities table
```

**Follow-ups:** BCNF? 4NF and 5NF? Practical limits of normalization?

**Mistakes:** Stopping at 2NF; not recognizing transitive dependencies in design

---

#### Q8. When is denormalization justified?

**Answer:**
Denormalization justified when: read-heavy workload, data rarely changes, performance critical, joins too expensive. Must be deliberate optimization with monitoring, not lazy design. Understand trade-offs: faster reads, slower writes, duplication risk.

**SQL Example:**

```sql
-- Normalized: Requires joins
SELECT o.order_id, c.name, c.email, p.name, p.price
FROM Orders o
JOIN Customers c ON o.customer_id = c.customer_id
JOIN Products p ON o.product_id = p.product_id;
-- If this query runs millions of times/day and data rarely changes...

-- Denormalized: Redundant but fast
CREATE TABLE OrderSummary (
    order_id INT,
    customer_name VARCHAR(100), -- Duplicated
    customer_email VARCHAR(100), -- Duplicated
    product_name VARCHAR(100),   -- Duplicated
    product_price DECIMAL(10,2)  -- Duplicated
);
-- No joins, fast reads, but must keep in sync with source tables

-- Best: Materialized view (DB manages sync)
CREATE MATERIALIZED VIEW OrderSummaryView AS
SELECT o.order_id, c.name, c.email, p.name, p.price
FROM Orders o
JOIN Customers c ON o.customer_id = c.customer_id
JOIN Products p ON o.product_id = p.product_id;
```

**Follow-ups:** Materialized views? Cache layers? Data consistency strategies?

**Mistakes:** Premature denormalization; not measuring before optimizing; ignoring sync complexity

---

### 🧠 DAY 5 — Reflection Questions

---

#### 1. Why are schema mistakes hard to undo?

**Answer:**
Schema defines how all data is stored. Once production data accumulates, changes require migrations (rewrite data), maintain backward compatibility, coordinate across services, and risk data loss. Prevention cheaper than cure.

**SQL Example:**

```sql
-- Initial bad design: Email as VARCHAR(50)
CREATE TABLE Users (email VARCHAR(50));
-- Years later: Need longer emails
-- Can't just: ALTER TABLE Users MODIFY email VARCHAR(100);
-- Must: Check existing data, migrate, test, deploy carefully
-- Constraints, indexes, foreign keys, application code all affected

-- Good initial design: Plan for growth
CREATE TABLE Users (
    user_id INT PRIMARY KEY, -- Stable identity
    email VARCHAR(255) CHECK (email LIKE '%@%.%') -- Validation
);
```

**Follow-ups:** Schema migration strategies? Blue-green deployments? Backward compatibility?

**Mistakes:** Not planning for change; underestimating migration complexity

---

#### 2. Why does normalization favor correctness over convenience?

**Answer:**
Normalization prioritizes long-term data truth over short-term query simplicity. Requires more joins but prevents ambiguity, duplication, and silent corruption. Correctness outlasts convenience—queries change, but corrupted data persists forever.

**SQL Example:**

```sql
-- Convenient but wrong: Denormalized
CREATE TABLE Orders (customer_name, customer_email, ...);
-- Easy query: SELECT * FROM Orders
-- But: Customer updates email? Update ALL their orders? Historical orders wrong?

-- Correct but complex: Normalized
CREATE TABLE Customers (customer_id, name, email);
CREATE TABLE Orders (order_id, customer_id);
-- Harder query: SELECT * FROM Orders JOIN Customers ...
-- But: Customer updates email once, consistency guaranteed
```

**Follow-ups:** Views to simplify queries? ORMs hiding complexity? Cost of corruption?

**Mistakes:** Choosing easy queries over data integrity; not valuing long-term correctness

---

#### 3. Why is premature denormalization dangerous?

**Answer:**
Premature denormalization introduces duplication before understanding actual access patterns. Locks system into wrong assumptions, creates consistency problems, and makes future changes harder. Optimize based on real metrics, not guesses.

**SQL Example:**

```sql
-- Premature: "Joins will be slow" (guessing)
CREATE TABLE Orders (
    order_id INT,
    customer_name VARCHAR(100), -- Duplicated
    product_name VARCHAR(100),  -- Duplicated
    -- ...50 more duplicated columns
);
-- Turns out: Queries mostly by order_id (indexed), joins fast anyway
-- Now: Stuck with massive sync complexity for no benefit

-- Better: Start normalized, denormalize if proven necessary
CREATE TABLE Orders (order_id, customer_id, product_id);
-- Measure query performance
-- If actually slow: Add targeted denormalization (materialized view)
```

**Follow-ups:** Performance profiling? When to optimize? Reversing denormalization?

**Mistakes:** "Premature optimization is the root of all evil"; not measuring first

---

## 📚 Day 5 Summary

**Core Concepts**: Normalization (single source of truth), redundancy problems (drift/inconsistency), update anomalies (partial updates), 1NF (atomic values), 3NF (no transitive dependencies), denormalization (deliberate trade-off)

**Key Takeaway**: Normalize by default to ensure correctness. Each fact in one place prevents contradictions. Denormalize only when proven necessary with real metrics—convenience doesn't justify corruption risk.

---
