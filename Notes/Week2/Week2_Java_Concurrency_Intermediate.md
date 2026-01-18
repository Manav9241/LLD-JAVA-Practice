# Week 2 — Java Concurrency Fundamentals (Intermediate README)

This README builds on **Week 2 – Beginner concepts** and focuses on  
**backend-level concurrency thinking** required for real systems and interviews.

It is written in the **same style as Week 1 and Week 2 (Beginner)**:
- Clear core idea
- One strong supporting example
- Interview follow-ups
- Common mistakes

---

## 1. Why does synchronization reduce performance?

### Core idea
Synchronization forces **mutual exclusion**.  
Only one thread can execute a synchronized section at a time, which reduces parallelism.

```java
public synchronized void process() {
    // only one thread at a time
}
```

### Why performance drops
- Threads block and wait
- Context switching increases
- CPU cores remain underutilized

### Interview follow-ups
- Why is fine-grained locking preferred?
- When is synchronization unavoidable?
- How do concurrent data structures reduce contention?

### Common mistakes
- Synchronizing entire classes
- Using one global lock for all operations
- Blaming Java instead of lock design

---

## 2. What breaks if two threads modify the same object without synchronization?

### Core idea
Without synchronization, **check-then-act logic breaks** and object state becomes inconsistent.

```java
if (status == CREATED) {
    status = SHIPPED;
}
```

Two threads can pass the check and both mutate state.

### What can go wrong
- Invalid business states
- Lost updates
- Partial writes
- Broken invariants

### Interview follow-ups
- Why doesn’t try-catch fix this?
- Why is this worse than a crash?
- How do you make state transitions atomic?

### Common mistakes
- Assuming validation prevents concurrency bugs
- Treating objects as thread-safe by default
- Relying on exceptions for correctness

---

## 3. Why is thread safety harder with mutable objects?

### Core idea
Mutable objects allow **state to change over time**, which must be coordinated across threads.

```java
order.setStatus(SHIPPED);
```

Multiple threads mutating the same object require:
- locking
- visibility guarantees
- ordering guarantees

### Why immutability is easier
- No state changes
- No coordination needed
- Safe to share freely

### Interview follow-ups
- Why are DTOs often immutable?
- How does immutability simplify concurrency?
- What are downsides of immutability?

### Common mistakes
- Making everything mutable by default
- Exposing setters unnecessarily
- Forgetting visibility guarantees

---

## 4. Why does ConcurrentHashMap scale better than HashMap + synchronized?

### Core idea
`ConcurrentHashMap` uses **fine-grained internal locking**, not one global lock.

```java
Map<String, Order> map = new ConcurrentHashMap<>();
```

### Why it scales better
- Multiple threads can update different keys concurrently
- Reads rarely block writes
- Reduced contention under load

### Interview follow-ups
- Why doesn’t ConcurrentHashMap lock the entire map?
- What operations are atomic?
- Why can compound operations still fail?

### Common mistakes
- Assuming ConcurrentHashMap fixes all concurrency issues
- Using containsKey + put instead of putIfAbsent
- Mixing synchronization with concurrent collections incorrectly

---

## 5. Can a method be thread-safe even if it uses non-thread-safe objects?

### Core idea
Yes. **Thread safety is about usage, not the object itself**.

```java
public void addOrder(Order order) {
    synchronized (lock) {
        orders.add(order); // orders is not thread-safe
    }
}
```

The method is thread-safe because access is controlled.

### Interview follow-ups
- Is ArrayList ever safe in concurrency?
- What determines method-level thread safety?
- How does encapsulation help?

### Common mistakes
- Assuming non-thread-safe objects are always unsafe
- Exposing internal collections
- Locking inconsistently across methods

---

## 6. Why is immutability a strong concurrency guarantee?

### Core idea
Immutable objects **cannot change state**, so:
- No races
- No visibility issues
- No synchronization needed

```java
final class Order {
    final String id;
    final OrderStatus status;
}
```

### Why backend systems love immutability
- Safe sharing
- Easier reasoning
- Fewer production bugs

### Interview follow-ups
- Why is String immutable?
- How does immutability affect performance?
- Where is immutability impractical?

### Common mistakes
- Assuming final makes objects immutable
- Mutating internal fields
- Mixing immutability with setters

---

## 7. What happens if one thread updates an object but another thread doesn’t see the update?

### Core idea
This is a **visibility problem**, not a race condition.

```java
running = false;
```

Another thread may continue reading `true` indefinitely.

### Why this happens
- CPU caching
- Instruction reordering
- No happens-before relationship

### Interview follow-ups
- Why does synchronized fix visibility?
- Why does logging change behavior?
- When do visibility bugs appear?

### Common mistakes
- Assuming reads are always safe
- Ignoring memory visibility
- Debugging with println statements

---

## 8. Why do backend systems prefer thread pools over unbounded threads?

### Core idea
Thread pools provide **bounded concurrency** and predictable resource usage.

```java
ExecutorService executor = Executors.newFixedThreadPool(20);
```

### Why this matters
- Prevents memory exhaustion
- Reduces context switching
- Improves system stability

### Interview follow-ups
- What happens if threads grow unbounded?
- How do thread pools provide backpressure?
- Why does Spring rely on thread pools?

### Common mistakes
- Using cached thread pools blindly
- Blocking inside executor threads
- Forgetting graceful shutdown

---

## 9. How does poor concurrency design cause production-only bugs?

### Core idea
Concurrency bugs depend on **timing**, **load**, and **scheduling**, which differ in production.

### Why prod-only bugs happen
- Higher concurrency
- Different CPU cores
- Different memory pressure
- Different thread scheduling

### Interview follow-ups
- Why do load tests miss these bugs?
- Why do bugs disappear with logging?
- How do you diagnose prod concurrency issues?

### Common mistakes
- Assuming local tests are enough
- Ignoring stress testing
- Adding logging as a “fix”

---

## 10. Why are concurrency bugs hard to reproduce locally?

### Core idea
Concurrency bugs are **non-deterministic**.

The same code can:
- work once
- fail the next time
- behave differently on another machine

### Root causes
- Thread scheduling
- CPU timing
- Memory visibility
- Load differences

### Interview follow-ups
- How do you reproduce concurrency bugs?
- Why are stress tests important?
- How do you debug nondeterministic failures?

### Common mistakes
- Relying only on unit tests
- Assuming determinism
- Treating concurrency bugs as random

---

## Final Week-2 Intermediate Takeaway

Concurrency bugs are not mysterious — they are the result of:
- shared mutable state
- weak visibility guarantees
- incorrect assumptions about execution order

Backend engineers must design for **correctness first**, then **scalability**.

If you understand this README,  
you are thinking at an **intermediate backend engineer level**.
