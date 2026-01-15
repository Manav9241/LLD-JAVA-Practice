# Stretch-Level Backend Questions — README

These questions go **beyond syntax and patterns**.  
They test whether you understand **trade-offs, system behavior, and real-world backend design**.

Each section includes:
- Core explanation
- Small illustrative examples (where useful)
- Interview follow-ups
- Common mistakes

This file can be committed directly to your backend learning repository.

---

## Can you violate SRP without increasing class size?

### Core idea
- Yes. SRP is about **reasons to change**, not lines of code.
- A small class can still have multiple responsibilities.
- Violations often happen subtly, not through size.

```java
class UserService {
    void createUser() {}
    void sendWelcomeEmail() {}
}
```

The class is small, but:
- User creation
- Email notification  
  are **two separate reasons to change**.

### Interview follow-ups
- Can a large class still follow SRP?
- How do you detect SRP violations early?
- Is SRP subjective?

### Common mistakes
- Equating SRP with small classes
- Mixing side effects (email, logging) into core logic
- Ignoring future change scenarios

---

## Can immutability ever hurt performance?

### Core idea
- Yes, immutability can increase object creation.
- Excessive copying can increase memory pressure.
- Trade-offs depend on usage patterns.

```java
// Immutable but creates many objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result = result + i;
}
```

In such cases, mutable helpers are preferred.

```java
StringBuilder sb = new StringBuilder();
```

### Interview follow-ups
- When is immutability worth the cost?
- How do functional languages handle this?
- How does JVM optimize immutable objects?

### Common mistakes
- Making everything immutable blindly
- Ignoring hot paths and performance-critical code
- Confusing immutability with inefficiency

---

## Why does HashMap allow one null key?

### Core idea
- `null` is treated as a special case.
- HashMap allows one `null` key mapped to bucket zero.
- This supports legacy usage and convenience.

```java
Map<String, String> map = new HashMap<>();
map.put(null, "value");
```

### Interview follow-ups
- Why does ConcurrentHashMap not allow null keys?
- How does HashMap internally handle null?
- Should null keys be used in production?

### Common mistakes
- Assuming all Maps behave the same
- Using null keys in shared or concurrent code
- Relying on null instead of Optional or explicit values

---

## How does bad exception design leak internal details?

### Core idea
- Throwing low-level exceptions exposes internal structure.
- Stack traces or messages can reveal implementation details.
- This is a security and maintenance risk.

```java
throw new SQLException("column user_pwd not found");
```

A better approach:

```java
throw new UserCreationException("Failed to create user");
```

### Interview follow-ups
- Difference between technical and domain exceptions?
- Where should exception translation happen?
- How does this relate to API security?

### Common mistakes
- Returning database error messages to clients
- Exposing stack traces in APIs
- Using generic exceptions everywhere

---

## Can you design a system without inheritance?

### Core idea
- Yes. Many modern systems use **composition + interfaces only**.
- Behavior is injected, not inherited.
- This reduces coupling and increases flexibility.

```java
class ReportService {
    private Formatter formatter;
}
```

No class hierarchy is required.

### Interview follow-ups
- Where are abstract classes still useful?
- How do frameworks avoid inheritance abuse?
- Is inheritance ever unavoidable?

### Common mistakes
- Assuming inheritance is required for polymorphism
- Replacing inheritance with over-engineered composition
- Avoiding inheritance even when it is appropriate

---

## Stretch-Level Takeaway

These questions focus on:
- Trade-offs, not rules
- System evolution, not static design
- Thinking in constraints and consequences

If you can reason through these, you are operating at a **strong backend design level**, not just an implementation level.
