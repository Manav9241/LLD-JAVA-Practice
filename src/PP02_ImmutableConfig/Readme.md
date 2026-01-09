# Immutability vs Shared Mutability (Plain Java Practice)

## Overview

This repository contains a **plain Java practice exercise** that demonstrates the difference between **bad design (shared mutable state)** and **good design (immutable state)** in a **multithreaded environment**.

The goal of this exercise is **learning**, not building a production system.

---

## What is this exercise about?

The project intentionally implements **two versions** of the same logic:

1. **Bad Design (Mutable)**
    - A shared configuration object is **mutated by one thread**
    - Another thread **reads from the same object**
    - Results become **non-deterministic**

2. **Good Design (Immutable)**
    - Configuration is created once and **never modified**
    - All threads read the same immutable snapshot
    - Behavior is **deterministic and stable**

This mirrors **real-world backend concurrency problems**.

---

## What the exercise demonstrates

- `final` references **do not** make objects thread-safe
- Shared mutable state leads to **race conditions**
- Concurrency bugs often:
    - Do not throw exceptions
    - Appear inconsistently
    - Are hard to reproduce
- Immutable objects are **safe to share across threads** without synchronization

---

## Bad Design (Mutable)

In the mutable version:
- A `MutableConfig` object is shared between threads
- One thread continuously updates the configuration
- Another thread reads from it while performing calculations
- The same input can produce **different outputs**

This simulates real production bugs caused by **shared mutable state**.

---

## Good Design (Immutable)

In the immutable version:
- Configuration is created once using defensive copies
- No setters or mutation is allowed
- All threads read a consistent snapshot
- Incorrect results become **impossible by design**

The business logic remains unchanged — only the **data design** is fixed.

---

## Benefits of Immutability Shown

- Eliminates race conditions without locks
- Makes multithreaded behavior predictable
- Simplifies reasoning about code
- Reduces the need for synchronization
- Prevents entire classes of concurrency bugs

---

## Key Takeaway

> **Immutability does not make concurrency safer — it makes many concurrency bugs impossible.**

---

## Why this matters

Most backend concurrency issues are not caused by complex algorithms, but by **shared mutable state**.

Immutability is one of the most effective design tools for building:
- Reliable
- Maintainable
- Concurrent systems

---

## How to use this repository

1. Run the **mutable version**
    - Observe inconsistent results or detected errors
2. Run the **immutable version**
    - Observe consistent, stable output
3. Compare both designs to understand **why immutability works**

---

This exercise is meant to build **intuition**, not just knowledge.
