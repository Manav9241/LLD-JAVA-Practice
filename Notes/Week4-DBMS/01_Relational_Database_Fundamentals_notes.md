# DBMS Interview Preparation Notes

---

## 🟡 DAY 1: Relational Database Fundamentals

### 🟡 DAY 1 — Beginner Questions

---

#### Q1. Why does a table need a primary key?

**Answer:**
Primary keys uniquely identify each row, enable foreign key relationships, and guarantee you can reliably update/delete specific records. Without them, duplicate rows make it impossible to target individual records, breaking referential integrity and making joins ambiguous.

**SQL Example:**

```sql
-- Without PK: Can't delete specific duplicate
DELETE FROM Users WHERE name = 'John'; -- Deletes ALL Johns!

-- With PK: Precise targeting
DELETE FROM Users WHERE user_id = 1; -- Only this one
```

**Follow-ups:** Natural vs surrogate keys? Multiple PKs? Performance impact?

**Mistakes:** Only saying "uniqueness" without mentioning relationships; forgetting PKs are auto-indexed

---

#### Q2. Can a primary key be NULL? Why or why not?

**Answer:**
No. NULL means "unknown value", so you can't uniquely identify a row with it. Additionally, NULL = NULL returns FALSE in SQL, breaking identity logic. This applies to all parts of composite keys too.

**SQL Example:**

```sql
INSERT INTO Products VALUES (NULL, 'Laptop');
-- ERROR: Cannot insert NULL into primary key

-- Why NULL comparison fails
SELECT * FROM Products WHERE product_id = NULL; -- Returns nothing
```

**Follow-ups:** NULL behavior in WHERE clauses? UNIQUE vs PRIMARY KEY with NULLs?

**Mistakes:** Confusing NULL with 0/empty string; not explaining NULL = NULL is FALSE

---

#### Q3. Why is using email as a primary key a bad idea?

**Answer:**
Emails can change, causing cascading updates across all foreign keys. String-based PKs are slower (larger indexes, string comparison), and business data as identity couples data correctness to business rules. Use surrogate keys (INT) and keep email as UNIQUE.

**SQL Example:**

```sql
-- Bad: Email as PK
UPDATE Users SET email = 'new@email.com' WHERE email = 'old@email.com';
-- ERROR: FK constraint fails! Must update Orders table too (expensive)

-- Good: Surrogate key
UPDATE Users SET email = 'new@email.com' WHERE user_id = 1;
-- Clean, no FK updates needed
```

**Follow-ups:** Natural vs surrogate keys? When are natural keys OK? Performance impact?

**Mistakes:** Only mentioning "emails change" without discussing cascading; forgetting UNIQUE constraint alternative

---

#### Q4. What is the difference between NULL and an empty string?

**Answer:**
NULL = "unknown/absent value", empty string ('') = "known empty value". NULL behaves specially: comparisons fail (NULL = NULL is FALSE), aggregates ignore it, concatenation propagates it. Empty string is a normal value.

**SQL Example:**

```sql
-- Comparison
WHERE middle_name = '';      -- Finds empty strings
WHERE middle_name IS NULL;   -- Finds NULLs
WHERE middle_name = NULL;    -- Returns NOTHING! Wrong!

-- Concatenation
'John' || NULL || 'Doe'  -- Returns NULL
'John' || '' || 'Doe'    -- Returns 'JohnDoe'

-- COUNT
COUNT(middle_name)  -- Ignores NULLs, counts empty strings
```

**Follow-ups:** NULL in aggregate functions? COUNT(\*) vs COUNT(column)? When to use which?

**Mistakes:** Using = NULL instead of IS NULL; saying they're "basically the same"

---

#### Q5. Why should the database enforce NOT NULL instead of Java code?

**Answer:**
Application code can be bypassed (direct DB access, scripts, other apps), buggy, or removed in refactoring. The database is the permanent guardian—it outlives applications and guarantees integrity regardless of which client writes data. Defense-in-depth: validate in both layers.

**SQL Example:**

```sql
-- Java might have bugs, but DB catches it
INSERT INTO Employees VALUES (1, NULL, 'Doe', 'john@ex.com');
-- ERROR: first_name cannot be NULL

-- Multiple clients (Java, Python, SQL scripts) all enforced
CREATE TABLE Employees (
    first_name VARCHAR(100) NOT NULL,
    salary DECIMAL(10,2) CHECK (salary > 0)
);
```

**Follow-ups:** Should you have validation in both layers? Performance impact? Other DB constraints?

**Mistakes:** Saying "only DB" or "only app"; not mentioning multiple clients/scripts scenario

---

### 🧠 DAY 1 — Reflection Questions

---

#### 1. Why is the database stricter than application code?

**Answer:**
Database owns permanent data integrity across all apps, versions, and failures. Applications are temporary—they crash, get redeployed, and change. Data corruption is often irreversible. DB strictness prevents silent corruption that compounds over time.

**SQL Example:**

```sql
-- App logic changes over time, DB stays consistent
CREATE TABLE Users (
    age INT CHECK (age >= 0 AND age <= 150),
    account_balance DECIMAL(10,2) CHECK (account_balance >= 0)
);

-- No matter what app version, constraints hold
INSERT INTO Users VALUES (-5, -1000); -- Always rejected
```

**Follow-ups:** What if app logic conflicts with DB? Schema evolution? Performance trade-offs?

**Mistakes:** Saying "strict constraints slow development"; not mentioning DBs outlive apps

---

#### 2. What would break if primary keys didn't exist?

**Answer:**
Can't uniquely identify/update/delete specific rows. Foreign keys become impossible (no unique reference). Duplicate rows make targeting ambiguous. Relational integrity breaks, indexes less effective, concurrent transactions unsafe.

**SQL Example:**

```sql
-- Without PK: Which duplicate to update?
UPDATE Students SET grade = 'A+' WHERE name = 'Alice' AND course = 'Math';
-- Updates ALL matching rows, not just one!

-- With PK: Precise control
UPDATE Students SET grade = 'A+' WHERE student_id = 1;
```

**Follow-ups:** UNIQUE constraints sufficient? Impact on normalization? ORMs and PKs?

**Mistakes:** Thinking UNIQUE is enough (allows NULLs); forgetting FK relationships

---

#### 3. Why is "flexible schema" often dangerous?

**Answer:**
Flexible schemas delay validation to runtime, allowing inconsistent data to accumulate silently. Different developers write different structures, making queries unreliable. "Schema-less" still has implicit schemas in code. Cleaning corrupted production data is exponentially harder than preventing it with early schema design.

**SQL Example:**

```sql
-- Flexible: Different structures stored
INSERT INTO Products VALUES (1, '{"name":"Laptop","price":999}');
INSERT INTO Products VALUES (2, '{"name":"Shirt","cost":29}'); -- "cost" not "price"!
INSERT INTO Products VALUES (3, '{"name":"Phone","price":"699"}'); -- string price!

-- Queries become complex and unreliable
SELECT * FROM Products WHERE JSON_EXTRACT(product_data, '$.price') > 500;
-- Misses row 2, wrong result for row 3

-- Rigid: Enforced correctness
CREATE TABLE Products (
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0)
);
-- Invalid data rejected immediately
```

**Follow-ups:** When is flexibility appropriate? Schema-on-write vs schema-on-read? Hybrid approach?

**Mistakes:** Saying "flexible is always bad"; not mentioning data cleanup costs; forgetting query complexity

---

## 📚 Day 1 Summary

**Core Concepts**: Primary keys (unique identity), NULL handling (unknown vs empty), constraint enforcement (DB as guardian), schema design (structure vs flexibility)

**Key Takeaway**: Database is permanent truth source—strictness prevents irreversible corruption across all apps and time periods.

---
