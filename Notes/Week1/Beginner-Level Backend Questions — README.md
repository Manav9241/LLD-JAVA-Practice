# Week 1 — Java Backend Fundamentals (README)

This README is designed to be **dropped directly into a GitHub repository**.  
It covers **Week‑1 backend fundamentals**, written for **learning + interview readiness**.

You can **expand this later** with:
- Diagrams
- Reference links
- Deeper examples per section

---

## 1. Why do backend systems prefer interfaces over concrete classes?

### Core idea
- Interfaces reduce coupling by separating **what a class does** from **how it does it**.
- High‑level code depends on contracts, not implementations.
- This makes systems safer to change and easier to test.

```java
interface PaymentService {
    void pay(int amount);
}

class CardPaymentService implements PaymentService {
    public void pay(int amount) { }
}

class Checkout {
    private final PaymentService paymentService;
    Checkout(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

### Interview follow‑ups
- How do interfaces help with testing?
- Why does dependency injection rely on interfaces?
- Can abstract classes replace interfaces?

### Common mistakes
- Creating interfaces for everything without need
- Putting logic inside interfaces
- Depending on concrete implementations in services

---

## 2. What problem does SRP (Single Responsibility Principle) actually solve?

### Core idea
- SRP prevents **change ripple effects**.
- A class should have **one reason to change**, not one method.
- Mixing responsibilities increases bugs during maintenance.

```java
// ❌ Violates SRP
class OrderService {
    void createOrder() {}
    void logOrder() {}
    void saveToDb() {}
}

// ✅ SRP
class OrderCreator { void createOrder() {} }
class OrderRepository { void save() {} }
class OrderLogger { void log() {} }
```

### Interview follow‑ups
- Can a class have many methods and still follow SRP?
- How does SRP reduce production bugs?
- Difference between SRP and low cohesion?

### Common mistakes
- Thinking SRP means “small classes”
- Mixing logging, persistence, and business logic
- Creating utility god classes

---

## 3. When would you choose a Map over a List?

### Core idea
- Use `Map` when access is by **key**, not position.
- Use `List` when order or iteration matters.

```java
// ❌ Inefficient for lookups
List<Order> orders;

// ✅ Backend‑friendly
Map<String, Order> ordersById = new HashMap<>();
ordersById.get("ORD-1");
```

### Interview follow‑ups
- Why are Maps common in caches?
- What happens if keys are mutable?
- Difference between HashMap and ConcurrentHashMap?

### Common mistakes
- Using List and looping to find by ID
- Using mutable objects as map keys
- Forgetting equals/hashCode contracts

---

## 4. Why is String immutable in Java?

### Core idea
- Immutability makes `String` thread‑safe by default.
- Required for safe hashing and caching.
- Improves security and memory optimization.

```java
String a = "abc";
String b = a;
a = a + "d";   // creates a new String
// b is still "abc"
```

### Interview follow‑ups
- What would break if String was mutable?
- How does the string pool work?
- Why is immutability good for concurrency?

### Common mistakes
- Assuming String changes in place
- Using StringBuilder incorrectly in concurrency
- Creating unnecessary String objects

---

## 5. What is wrong with this?

```java
catch (Exception e) { }
```

### Core idea
- This silently swallows errors.
- Breaks error propagation.
- Makes debugging extremely difficult.

```java
// Better
catch (IOException e) {
    log.error(e.getMessage());
    throw e;
}
```

### Interview follow‑ups
- When is it okay to catch Exception?
- Difference between checked and unchecked exceptions?
- Why do backend services prefer RuntimeException?

### Common mistakes
- Catching Exception everywhere
- Logging but not rethrowing
- Using exceptions for control flow

---

## 6. Why do we override equals() and hashCode() together?

### Core idea
- Hash‑based collections depend on both methods.
- Equal objects **must** have the same hash code.
- Breaking this causes lookup failures.

```java
class User {
    String id;

    @Override
    public boolean equals(Object o) {
        return o instanceof User u && id.equals(u.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
```

### Interview follow‑ups
- What happens if hashCode changes after insertion?
- Why is immutability important for keys?
- How does HashMap use hashCode internally?

### Common mistakes
- Overriding only equals()
- Using mutable fields in hashCode()
- Poor hash function implementations

---

## 7. What is the difference between HAS‑A and IS‑A?

### Core idea
- IS‑A = inheritance (tight coupling)
- HAS‑A = composition (flexible and safer)
- Backend systems strongly prefer composition

```java
// IS‑A
class Car extends Vehicle { }

// HAS‑A
class Car {
    private Engine engine;
}
```

### Interview follow‑ups
- Why is inheritance dangerous in large systems?
- Can composition fully replace inheritance?
- Where are abstract classes still useful?

### Common mistakes
- Deep inheritance hierarchies
- Extending concrete classes
- Using inheritance to reuse code

---

## 8. Why should DTOs not have setters?

### Core idea
- DTOs represent immutable data snapshots.
- Setters allow uncontrolled mutation.
- Immutability improves safety and clarity.

```java
// ❌ Mutable DTO
class OrderDTO {
    String id;
    void setId(String id) { this.id = id; }
}

// ✅ Immutable DTO
class OrderDTO {
    final String id;
    OrderDTO(String id) { this.id = id; }
}
```

### Interview follow‑ups
- Why are DTOs often immutable?
- Difference between DTO and entity?
- How does immutability help concurrency?

### Common mistakes
- Adding setters “just in case”
- Reusing entities as DTOs
- Modifying DTOs across layers

---

## How to Expand This README Later

You can add:
- UML diagrams per section
- Links to Java docs
- Real production examples
- Unit tests per concept

---

## Final Week‑1 Takeaway

Backend engineering is about:
- Designing for **change**
- Controlling **state**
- Making failures **explicit**
- Reducing **blast radius**

If you understand this README,  
you have a **strong Week‑1 backend foundation**.
