# Intermediate-Level Backend Questions — README

This README covers **intermediate backend thinking questions**.
Each section includes:
- Core explanation
- Small code example (where helpful)
- Interview follow-ups
- Common mistakes

You can **commit this directly** or merge it into your main backend-notes repository.

---

## Why is inheritance fragile in large systems?

### Core idea
- Inheritance creates **tight coupling** between parent and child classes.
- A change in a base class can silently break all subclasses.
- Large systems evolve frequently, making inheritance chains risky.

```java
class Account {
    double balance;
}

class SavingsAccount extends Account { }
```

Changing `Account` behavior may unintentionally affect all subclasses.

### Interview follow-ups
- What is the Fragile Base Class problem?
- How does composition reduce risk?
- When is inheritance still acceptable?

### Common mistakes
- Deep inheritance hierarchies
- Extending concrete classes
- Using inheritance for code reuse instead of modeling

---

## How does DIP make code testable?

### Core idea
- DIP ensures high-level code depends on **abstractions**, not implementations.
- This allows swapping real implementations with mocks or stubs.

```java
interface OrderRepository {
    Order findById(String id);
}

class OrderService {
    private final OrderRepository repo;
    OrderService(OrderRepository repo) {
        this.repo = repo;
    }
}
```

Tests can inject fake repositories without touching production code.

### Interview follow-ups
- How does DIP relate to dependency injection?
- Can DIP exist without frameworks?
- Difference between DIP and IoC?

### Common mistakes
- Depending directly on concrete classes
- Creating abstractions without need
- Confusing DIP with interfaces everywhere

---

## What breaks if a HashMap key is mutable?

### Core idea
- HashMap uses `hashCode()` to locate buckets.
- If the key changes after insertion, lookup fails.
- The entry becomes unreachable.

```java
Map<User, String> map = new HashMap<>();
User u = new User("1");
map.put(u, "data");
u.id = "2"; // key mutated
map.get(u); // returns null
```

### Interview follow-ups
- Why are immutable keys recommended?
- How does this affect caches?
- Can ConcurrentHashMap fix this?

### Common mistakes
- Using mutable fields in hashCode()
- Modifying keys after insertion
- Ignoring equals/hashCode contracts

---

## Why is immutability important in multi-threaded code?

### Core idea
- Immutable objects cannot change state.
- Multiple threads can safely share them.
- Entire classes of concurrency bugs disappear.

```java
final class Config {
    final String url;
    Config(String url) { this.url = url; }
}
```

### Interview follow-ups
- How does immutability reduce synchronization?
- Are immutable objects always thread-safe?
- Trade-offs of immutability?

### Common mistakes
- Assuming `final` alone means immutable
- Exposing internal mutable fields
- Mixing immutability with setters

---

## Why are runtime exceptions preferred in REST APIs?

### Core idea
- Runtime exceptions propagate naturally across layers.
- Checked exceptions clutter method signatures.
- REST APIs handle errors centrally.

```java
throw new OrderNotFoundException(id);
```

Central handler converts it to HTTP response.

### Interview follow-ups
- Difference between business and technical exceptions?
- When are checked exceptions useful?
- How are exceptions mapped to HTTP codes?

### Common mistakes
- Catching exceptions in controllers
- Using checked exceptions for business logic
- Returning null instead of throwing exceptions

---

## How does SRP reduce merge conflicts in teams?

### Core idea
- SRP isolates responsibilities into smaller classes.
- Fewer people modify the same files.
- Conflicts reduce naturally.

```java
class OrderService { }
class OrderValidator { }
class OrderRepository { }
```

### Interview follow-ups
- How does SRP improve team velocity?
- Relationship between SRP and microservices?
- Does SRP always mean smaller classes?

### Common mistakes
- God services modified by everyone
- Combining unrelated logic
- Fear of creating more classes

---

## Why does Spring inject interfaces instead of implementations?

### Core idea
- Interfaces allow Spring to swap implementations.
- Enables proxying, AOP, and testing.
- Encourages loose coupling.

```java
@Autowired
PaymentService paymentService;
```

### Interview follow-ups
- How does Spring choose the implementation?
- What happens if multiple implementations exist?
- Can Spring inject concrete classes?

### Common mistakes
- Autowiring concrete classes
- Confusing DI with Spring itself
- Overusing interfaces unnecessarily

---

## What happens if two objects have the same hashCode but are not equal?

### Core idea
- HashMap stores both in the same bucket.
- `equals()` is used to differentiate them.
- Correct behavior is preserved, performance may degrade.

```java
class A {
    int id;
    public int hashCode() { return 1; }
}
```

### Interview follow-ups
- What is a hash collision?
- How does HashMap resolve collisions?
- Why good hash functions matter?

### Common mistakes
- Assuming same hashCode means same object
- Writing poor hash functions
- Ignoring collision performance costs

---

## Intermediate Takeaway

At this level, backend design focuses on:
- Change safety
- Testability
- Concurrency correctness
- Team scalability

Understanding these answers means you are thinking like a backend engineer, not just a Java programmer.
