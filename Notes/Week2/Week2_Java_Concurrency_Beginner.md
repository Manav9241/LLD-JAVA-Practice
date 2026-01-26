# Week 2 — Java Concurrency Fundamentals (Beginner README)

This README is designed to be **dropped directly into a GitHub repository**.  
It mirrors the **Week‑1 style**: fewer bullets, clear explanations, strong examples,
interview follow‑ups, and common mistakes.

---

## 1. What is a thread in Java?

### Core idea
A **thread** is the smallest unit of execution inside a Java process.
It represents one independent flow of control. Multiple threads allow a program
to perform tasks concurrently while sharing memory.

In backend systems, threads are used to handle **multiple user requests at the same time**
within a single running service.

```java
Thread t = new Thread(() -> {
    System.out.println("Running in parallel");
});
t.start();
```

### Interview follow‑ups
- Can threads run truly in parallel?
- How does a web server use threads internally?
- What is the thread lifecycle?

### Common mistakes
- Treating threads as cheap resources
- Creating threads manually for every task
- Ignoring thread lifecycle management

---

## 2. How is a thread different from a process?

### Core idea
A **process** is an independent execution environment with its own memory space.
A **thread** is a lightweight execution unit inside a process.

- Processes are isolated from each other
- Threads inside the same process **share memory**

```text
Process (JVM)
 ├── Thread A
 ├── Thread B
 └── Thread C
```

### Why this matters in backend systems
- One backend service usually runs as **one process**
- Many concurrent requests are handled by **many threads**

### Interview follow‑ups
- Why are processes more expensive than threads?
- What happens if a thread crashes vs a process crashes?
- How do containers relate to processes?

### Common mistakes
- Assuming threads are isolated like processes
- Confusing JVM process with OS process
- Thinking threads crashing kill the whole JVM

---

## 3. What does it mean when we say “race condition”?

### Core idea
A **race condition** occurs when the outcome of a program depends on
the timing or order in which threads execute.

Race conditions arise due to:
- Shared mutable state
- Concurrent access
- No proper synchronization

```java
count++;   // race condition if accessed by multiple threads
```

The program may produce **different results on different runs**.

### Interview follow‑ups
- Why are race conditions hard to reproduce?
- Why do they appear more often in production?
- How do you detect race conditions?

### Common mistakes
- Thinking race conditions always cause crashes
- Assuming tests will catch them
- Confusing race conditions with deadlocks

---

## 4. Why is `++` not thread‑safe?

### Core idea
The `++` operator looks like a single operation but is actually **three steps**:

1. Read the value
2. Increment the value
3. Write the value back

```java
count++;  // read → modify → write
```

If two threads perform these steps concurrently, one update can overwrite the other.

### Key insight
> One line of code does NOT mean one atomic operation.

### Interview follow‑ups
- Is `int` itself thread‑safe?
- How do atomic variables fix this?
- How does `synchronized` help?

### Common mistakes
- Assuming simple operations are atomic
- Using `++` on shared counters
- Ignoring visibility issues

---

## 5. Why is `HashMap` unsafe in multithreaded environments?

### Core idea
`HashMap` is designed for **single‑threaded use**.
Concurrent access can corrupt its internal structure.

```java
Map<String, Order> orders = new HashMap<>();
orders.put(id, order); // unsafe concurrently
```

### What can go wrong
- Missing entries
- Overwritten values
- Inconsistent reads
- `ConcurrentModificationException`
- Rarely, infinite loops (historically)

### Interview follow‑ups
- Why doesn’t synchronization fully solve this?
- How does ConcurrentHashMap differ?
- Why is iteration especially dangerous?

### Common mistakes
- Using HashMap in repositories
- Synchronizing only write operations
- Iterating while modifying the map

---

## 6. What does the `synchronized` keyword actually do?

### Core idea
`synchronized` provides **two guarantees**:

1. **Mutual exclusion** – only one thread executes the block at a time
2. **Visibility** – changes made by one thread are visible to others

```java
public synchronized void increment() {
    count++;
}
```

### What is being locked
- Instance method → locks on `this`
- Static method → locks on `ClassName.class`

### Interview follow‑ups
- What object is actually locked?
- Why does synchronized affect visibility?
- Why is over‑synchronization bad?

### Common mistakes
- Locking the wrong object
- Synchronizing entire classes
- Thinking synchronized fixes all concurrency issues

---

## 7. Why is creating threads manually a bad idea?

### Core idea
Creating threads manually leads to **unbounded thread growth**.
Threads are expensive in terms of memory and CPU scheduling.

```java
new Thread(task).start();
```

### Why this breaks backend systems
- Memory exhaustion
- Excessive context switching
- Unpredictable latency
- Server instability

### Interview follow‑ups
- What happens during traffic spikes?
- How many threads can a JVM realistically handle?
- Why is thread reuse important?

### Common mistakes
- One thread per request
- No upper limit on threads
- No graceful shutdown

---

## 8. What problem does `ExecutorService` solve?

### Core idea
`ExecutorService` separates **task submission** from **thread management**.
It controls thread creation, reuse, and lifecycle.

```java
ExecutorService executor = Executors.newFixedThreadPool(10);
executor.submit(() -> handleRequest());
```

### Benefits
- Bounded threads
- Predictable resource usage
- Graceful shutdown

### Interview follow‑ups
- Difference between submit() and execute()?
- Why fixed thread pools?
- Why does Spring use thread pools internally?

### Common mistakes
- Forgetting to shut down executors
- Using unbounded thread pools
- Blocking threads unnecessarily

---

## 9. Where do local variables live — stack or heap?

### Core idea
- Local variables live on the **stack**
- Objects live on the **heap**

```java
void process() {
    Order order = new Order();
}
```

- `order` reference → stack
- `Order` object → heap

### Why this matters
- Stack variables are thread‑local
- Heap objects are shared

### Interview follow‑ups
- Why are local variables thread‑safe?
- How do object references affect concurrency?

### Common mistakes
- Confusing references with objects
- Synchronizing local variables
- Over‑thinking stack safety

---

## 10. Why do multiple threads see the same object but not the same local variables?

### Core idea
Each thread has its **own stack**, but all threads share the **heap**.

```java
Order order = repository.get(id);
```

- `order` (reference) → thread‑local stack
- `Order` object → shared heap

### Consequence
- Local variables are isolated
- Shared objects require synchronization

### Interview follow‑ups
- Why do visibility issues occur?
- Why do bugs disappear when logging is added?
- How does synchronization affect memory visibility?

### Common mistakes
- Synchronizing local variables
- Ignoring shared object mutation
- Assuming reads are always safe

---

## Final Week‑2 Beginner Takeaway

Concurrency bugs occur because:
- Threads run independently
- Memory is shared
- Execution order is unpredictable

Backend engineers must:
- Identify shared mutable state
- Protect critical sections
- Use the right concurrency abstractions

If you understand this README,  
you have a **solid beginner‑level concurrency foundation**.
