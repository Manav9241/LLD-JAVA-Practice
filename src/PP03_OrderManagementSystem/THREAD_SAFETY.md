# Thread Safety Implementation in Order Management System

## Problem Statement
When multiple threads attempt to perform operations (ship/cancel) on the same order concurrently, race conditions can occur leading to:
1. Inconsistent order states (e.g., an order being both shipped and cancelled)
2. Lost updates
3. Invalid state transitions

## Solution Approach

### 1. Repository Level - ConcurrentHashMap
**Changed:** `HashMap` → `ConcurrentHashMap` in `OrderRepository`

**Why:**
- `HashMap` is not thread-safe and can cause data corruption under concurrent access
- `ConcurrentHashMap` provides thread-safe operations without external synchronization
- Allows multiple threads to read/write different orders concurrently

### 2. Domain Level - Synchronized Methods
**Changed:** Added `synchronized` keyword to `Order.ship()` and `Order.cancel()`

**Why:**
- Ensures atomic check-and-update of order status
- Prevents race conditions within a single order instance
- Guarantees that status validation and update happen as one atomic operation

### 3. Service Level - Coarse-grained Locking
**Changed:** Added `synchronized` blocks around entire service operations

**Why:**
- Ensures atomicity of the complete operation: find → validate → update → save
- Prevents Time-of-Check-Time-of-Use (TOCTOU) vulnerabilities
- Simple to understand and maintain

## Thread Safety Guarantees

### Scenario 1: Concurrent Ship and Cancel on Same Order
```
Thread 1: shipOrder("ORD1")    Thread 2: cancelOrder("ORD1")
---------------------------------------------------------------
1. Acquire lock               | Wait for lock
2. Find order                 | ...
3. Check status (CREATED)     | ...
4. Update to SHIPPED          | ...
5. Save to DB                 | ...
6. Release lock               | Acquire lock
                              | Find order
                              | Check status (SHIPPED)
                              | Throw InvalidOrderStateException ✓
```

### Scenario 2: Concurrent Order Creation
```
Thread 1: createOrder("ORD1")  Thread 2: createOrder("ORD1")
---------------------------------------------------------------
1. Acquire lock                | Wait for lock
2. Check if exists (no)        | ...
3. Create new order            | ...
4. Save to DB                  | ...
5. Release lock                | Acquire lock
                               | Check if exists (yes)
                               | Throw DuplicateOrderException ✓
```

## Trade-offs and Alternatives

### Current Approach: Coarse-grained Locking
**Pros:**
- Simple to implement and understand
- Strong consistency guarantees
- No deadlock risk (single lock)

**Cons:**
- Serializes all operations through OrderService
- Lower throughput under high concurrency
- One slow operation blocks all others

### Alternative 1: Fine-grained Locking (Per-Order)
```java
private final ConcurrentHashMap<String, Object> orderLocks = new ConcurrentHashMap<>();

public void shipOrder(String orderId) {
    Object lock = orderLocks.computeIfAbsent(orderId, k -> new Object());
    synchronized (lock) {
        // operation
    }
}
```
**Pros:** Better concurrency - operations on different orders don't block each other
**Cons:** More complex, memory overhead for locks, need lock cleanup

### Alternative 2: Optimistic Locking with Version Numbers
```java
public class Order {
    private volatile int version;
    
    public boolean tryUpdate(int expectedVersion) {
        if (this.version == expectedVersion) {
            // update and increment version
            return true;
        }
        return false;
    }
}
```
**Pros:** Better performance, no blocking
**Cons:** Retry logic needed, more complex code

### Alternative 3: Immutable Order with Copy-on-Write
**Why Not Used:**
As mentioned by the user, making orders immutable doesn't make sense because:
- Orders need to maintain identity across state changes
- Clients querying order status need to see the updated state
- Creating new objects for each state change complicates order tracking

## Immutability Analysis

**User's Concern:** "making the order/domain immutable don't feel sensible to me as the same order needs to be updated as status because whenever another request comes to check status for a given order... then giving them 'Created' will be an inconsistency from the product pov"

**Analysis:**
- ✓ Correct assessment - orders must be mutable to reflect state changes
- ✓ Same order ID should show current status, not create new order instances
- ✓ Product requirement: status changes must be reflected to all observers

**Our Solution:**
- Keep orders mutable but protect state transitions with synchronization
- Ensure all observers see consistent state through thread-safe access
- Maintain order identity while safely updating its state

## Testing
Run `ConcurrencyTestDemo.java` to see thread safety in action:
```bash
java PP03_OrderManagementSystem.ConcurrencyTestDemo
```

## Recommendations

For production systems, consider:
1. **Fine-grained locking** for better scalability
2. **Distributed locking** (e.g., Redis) for multi-instance deployments
3. **Event sourcing** for complete audit trail
4. **Database-level locking** (pessimistic/optimistic) if using real DB
5. **ReadWriteLock** if reads significantly outnumber writes
