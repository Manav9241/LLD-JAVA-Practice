# New Concepts Explained – BetterDesign vs MyDesign

This file is a self-study guide covering every concept that appears in `BetterDesign`
but was **not** used in `MyDesign`. Each section explains the concept from scratch,
shows a minimal code example, and then points to exactly where it is used in
`BetterDesign` so you can read the real code alongside the explanation.

---

## Table of Contents

1. [Builder Pattern + Static Inner Class](#1-builder-pattern--static-inner-class)
2. [Immutability](#2-immutability)
3. [Push-Based vs Pull-Based Observer](#3-push-based-vs-pull-based-observer)
4. [Enums as Rich Types](#4-enums-as-rich-types)
5. [EnumSet and EnumMap](#5-enumset-and-enummap)
6. [Template Method Pattern](#6-template-method-pattern)
7. [Fluent API / Method Chaining](#7-fluent-api--method-chaining)
8. [Dependency Injection (Constructor Injection)](#8-dependency-injection-constructor-injection)
9. [Implementing Multiple Interfaces on One Class](#9-implementing-multiple-interfaces-on-one-class)
10. [Java Streams](#10-java-streams)
11. [Defensive Collections](#11-defensive-collections)
12. [Useful Map Methods: computeIfAbsent and getOrDefault](#12-useful-map-methods-computeifabsent-and-getordefault)
13. [Fail-Fast Input Validation with isBlank()](#13-fail-fast-input-validation-with-isblank)

---

## 1. Builder Pattern + Static Inner Class

### What problem does it solve?

Look at how `Notification` was created in **MyDesign**:

```java
INotification notification = new Notification("Your order has been shipped!");
```

It takes only one parameter. Now imagine the requirements grow and the notification
also needs a `type`, a `priority`, and a `recipient`. The constructor becomes:

```java
new Notification("Your order has been shipped!", NotificationType.INFO,
                 NotificationPriority.MEDIUM, "user@example.com");
```

When you later read this call, you have no idea which argument means what.
This is called the **Telescoping Constructor** problem.

What if some fields are optional? You might end up with multiple overloaded constructors:

```java
new Notification(message)
new Notification(message, type)
new Notification(message, type, priority)
new Notification(message, type, priority, recipient)
```

This is hard to maintain, easy to mix up arguments, and impossible to make
the object immutable (see Section 2).

### The Builder Pattern

The Builder Pattern separates **construction** from **the object itself**.
A separate inner class collects all the optional configuration and then
produces a fully initialized object at the end.

```
Builder (collects config) ──→ build() ──→ Notification (final, immutable)
```

### Minimal example

```java
public class Pizza {
    private final String crust;    // required
    private final String sauce;    // optional
    private final String topping;  // optional

    // Private constructor — only the Builder can call this
    private Pizza(Builder b) {
        this.crust   = b.crust;
        this.sauce   = b.sauce;
        this.topping = b.topping;
    }

    // -- Getters --
    public String getCrust()   { return crust;   }
    public String getSauce()   { return sauce;   }
    public String getTopping() { return topping; }

    // ── Static inner Builder class ──────────────────────────────────────────
    public static class Builder {
        private final String crust;   // required — set in Builder constructor
        private String sauce   = "tomato";  // optional default
        private String topping = "none";    // optional default

        public Builder(String crust) {
            this.crust = crust;
        }

        public Builder sauce(String sauce) {
            this.sauce = sauce;
            return this;   // <── this is Fluent API, covered in Section 7
        }

        public Builder topping(String topping) {
            this.topping = topping;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}

// Usage
Pizza p = new Pizza.Builder("thin crust")
                   .sauce("pesto")
                   .topping("mushrooms")
                   .build();
```

It is now **self-documenting** — each field name is visible at the call site.

### What is a Static Inner Class?

A **static inner class** is a class defined *inside* another class using the
`static` keyword. Because it is static it does **not** need an instance of the
outer class to exist.

```java
public class Outer {
    public static class Inner {   // can be created as: new Outer.Inner()
        // ...
    }
}
```

The Builder is always a static inner class of the object it builds. This keeps
them logically grouped together (you find the Builder right next to the class
it constructs) while ensuring the Builder can be instantiated before any
`Notification` exists.

### In BetterDesign

`Notification.java` → `Notification.Builder` (static inner class)

```java
INotification notification = new Notification.Builder("Your order has been shipped!")
        .type(NotificationType.INFO)
        .priority(NotificationPriority.MEDIUM)
        .recipient("customer@example.com")
        .build();
```

---

## 2. Immutability

### What does it mean?

An object is **immutable** if its state cannot change after it is constructed.
In Java, you achieve immutability by:

1. Declaring all fields `final` (they can only be assigned once).
2. Providing no setters.
3. Making the constructor `private` so only controlled code creates instances
   (as with the Builder).
4. Returning defensive copies from getters that return collections.

### Why is it useful?

| Problem with mutable objects | How immutability helps |
|---|---|
| Two threads read and write the same object simultaneously → data corruption | An immutable object can be shared freely across threads; no synchronisation needed |
| Code far from the constructor changes a field you didn't expect | Impossible — fields are `final` |
| Hard to reason about what the object "looks like" at any given point | It always looks the same as when it was created |

### In MyDesign

`Notification` in MyDesign only stores one `final String contents`, so it
is *accidentally* immutable for that one field. But there is no enforcement
at the design level.

### In BetterDesign

Every field in `Notification` is declared `final`:

```java
public class Notification implements INotification {
    private final String contents;
    private final NotificationType type;
    private final NotificationPriority priority;
    private final String recipient;

    private Notification(Builder builder) { /* assigned once */ }
    // no setters anywhere
}
```

The private constructor means the *only* way to create a `Notification` is
through the Builder, which enforces validation (see Section 13) before the
object is even created.

---

## 3. Push-Based vs Pull-Based Observer

You already know the Observer pattern from **MyDesign**. What changed in
**BetterDesign** is *how* the observer receives data — a subtle but important
distinction.

### Pull-based (MyDesign)

```
Observable ──setNotification()──→ calls update() on every observer
                                          │
                                          └── observer calls
                                              observable.getNotificationContent()
                                              to READ the data itself
```

The observer *pulls* the data from the observable after being told "something
changed". To do this it must hold a direct reference to the observable:

```java
// MyDesign Logger
public class Logger implements IObserver {
    private final NotificationObservable observable;  // must hold a reference

    public void update() {
        // Must go back to the observable to get the data
        System.out.println(observable.getNotificationContent());
    }
}
```

**Problems:**
- The observer is coupled to the concrete `NotificationObservable` class.
- If two observers run at different speeds (in a multi-threaded system),
  the second observer might see a *new* notification that replaced the one
  it was supposed to process.

### Push-based (BetterDesign)

```
Publisher ──publish(notification)──→ calls onNotification(notification) on
                                       every observer, passing the data directly
```

The observable *pushes* the data into the observer as an argument.
The observer does not need to know who published it or how to fetch it:

```java
// BetterDesign ConsoleNotificationLogger
public class ConsoleNotificationLogger implements INotificationObserver {
    // No reference to the publisher at all!

    @Override
    public void onNotification(INotification notification) {
        // Data arrives ready to use — no need to "go get it"
        System.out.println("[LOG] " + notification.getContents());
    }
}
```

**Benefits:**
- Observers are completely decoupled from the publisher.
- Each observer always sees the exact notification that triggered it.
- The observer can be tested standalone by passing a notification directly
  to `onNotification()` — no observable setup required.

### Side-by-side

| | Pull (MyDesign) | Push (BetterDesign) |
|---|---|---|
| Observer interface | `void update()` | `void onNotification(INotification n)` |
| Observer needs a publisher reference | Yes | No |
| Coupling | High — observer depends on `NotificationObservable` | Low — observer depends only on `INotification` |
| Testability | Requires constructing a full observable | Pass any `INotification` directly |

### In BetterDesign

`observer/INotificationObserver.java` and `observer/NotificationPublisher.java`

---

## 4. Enums as Rich Types

### What you already know

An `enum` is a fixed set of named constants:

```java
enum Direction { NORTH, SOUTH, EAST, WEST }
```

### What BetterDesign adds on top

**MyDesign** has no notion of notification category or urgency. Any filtering or
routing decision based on what *kind* of notification it is would require raw
strings like `"ALERT"` — a typo would compile fine but fail silently at runtime.

**BetterDesign** declares:

```java
public enum NotificationType    { INFO, WARNING, ALERT, PROMOTION }
public enum NotificationPriority { LOW, MEDIUM, HIGH, CRITICAL }
```

These are added as first-class fields on `INotification`:

```java
public interface INotification {
    String getContents();
    NotificationType getType();       // NEW
    NotificationPriority getPriority(); // NEW
    String getRecipient();             // NEW
}
```

Now every piece of code that receives an `INotification` can ask *what kind*
and *how urgent* it is. This powers the routing and filtering in `BetterDesign`.

### Comparing enums with `ordinal()`

Every enum constant has an implicit integer index (its *ordinal*) starting at 0.
`BetterDesign` uses this to implement a "minimum priority" filter without
building a separate comparison table:

```java
// NotificationPriority: LOW=0, MEDIUM=1, HIGH=2, CRITICAL=3

notification.getPriority().ordinal() >= minimumPriority.ordinal()

// Example: notification is HIGH (2), minimumPriority is HIGH (2) → 2 >= 2 → true  ✓
// Example: notification is LOW (0),  minimumPriority is HIGH (2) → 0 >= 2 → false ✗
```

This means the order in which you list constants in the enum declaration
matters. Always declare them from lowest to highest.

### In BetterDesign

`model/NotificationType.java`, `model/NotificationPriority.java`, and
`channel/AbstractNotificationChannel.java` (`canHandle` method)

---

## 5. EnumSet and EnumMap

### What they are

Java provides two specialised collection classes for use with enums:

| Class | Replaces | Advantage |
|---|---|---|
| `EnumSet<E>` | `HashSet<E>` when E is an enum | Implemented as a bit-vector — extremely fast `contains()` |
| `EnumMap<K, V>` | `HashMap<K, V>` when K is an enum | Uses a dense array internally — faster than HashMap, preserves enum declaration order |

These are **drop-in replacements** — they implement the same `Set` and `Map`
interfaces, so the rest of your code does not change.

### EnumSet

```java
// Create a set containing only ALERT and WARNING
Set<NotificationType> alertTypes = EnumSet.of(NotificationType.ALERT,
                                              NotificationType.WARNING);

// Create a set containing ALL values of the enum
Set<NotificationType> all = EnumSet.allOf(NotificationType.class);

// Check membership — O(1) bit operation
alertTypes.contains(NotificationType.INFO);   // false
alertTypes.contains(NotificationType.ALERT);  // true
```

### EnumMap

```java
// Map from NotificationType to a list of channels
Map<NotificationType, List<INotificationChannel>> typedChannels
        = new EnumMap<>(NotificationType.class);

typedChannels.put(NotificationType.ALERT, List.of(smsChannel));

// Iterate in enum declaration order (INFO, WARNING, ALERT, PROMOTION)
for (Map.Entry<NotificationType, List<INotificationChannel>> entry : typedChannels.entrySet()) {
    System.out.println(entry.getKey() + " → " + entry.getValue());
}
```

### In BetterDesign

`channel/AbstractNotificationChannel.java` uses `EnumSet.allOf()` and
`EnumSet.copyOf()`.

`routing/NotificationRouter.java` uses `EnumMap` for `typedChannels`.

---

## 6. Template Method Pattern

### What it is

The **Template Method Pattern** defines the *skeleton* of an algorithm in a
base class, leaving some steps to subclasses to fill in.

Think of it as: "Here is *how* we do things. You only need to tell me *what*
to do in these specific steps."

```
AbstractClass
  ├── templateMethod()    ← final; calls step1() then step2()
  ├── step1()             ← concrete; shared implementation
  └── step2()             ← abstract; each subclass fills this in
```

### Relation to Strategy Pattern (which you already know)

| | Strategy | Template Method |
|---|---|---|
| Mechanism | Composition — behaviour injected as an interface | Inheritance — behaviour defined in a subclass |
| When to use | Behaviour needs to change at **runtime** | Structure of algorithm is fixed; only specific steps change |
| Relationship | "has-a" | "is-a" |

Both patterns achieve the same goal (flexible behaviour) through different means.

### In BetterDesign — AbstractNotificationChannel

`AbstractNotificationChannel` implements the Template Method pattern:

```java
public abstract class AbstractNotificationChannel implements INotificationChannel {

    // ── Template: filtering logic is shared by all channels ─────────────────
    @Override
    public boolean canHandle(INotification notification) {
        return supportedTypes.contains(notification.getType())
                && notification.getPriority().ordinal() >= minimumPriority.ordinal();
    }

    // ── Abstract steps: each concrete channel fills in these ─────────────────
    @Override
    public abstract String getChannelName();

    @Override
    public abstract void send(INotification notification);
}
```

`EmailChannel` only needs to say *how* it sends a notification:

```java
public class EmailChannel extends AbstractNotificationChannel {
    @Override
    public String getChannelName() { return "Email[" + emailAddress + "]"; }

    @Override
    public void send(INotification notification) {
        System.out.println("Sending Email to " + emailAddress + ":\n"
                           + notification.getContents());
    }
}
```

`EmailChannel` gets the filtering logic for free from the base class. If you
later change the filtering rules, you change them in one place (`AbstractNotificationChannel`)
and all channels immediately benefit.

### In BetterDesign

`channel/AbstractNotificationChannel.java` and its three concrete subclasses:
`EmailChannel`, `SMSChannel`, `PushNotificationChannel`.

---

## 7. Fluent API / Method Chaining

### What it is

A **Fluent API** is a style of writing code where methods return the same
object (`this`), allowing you to chain multiple calls in one readable sentence.

### Without fluent API

```java
AbstractNotificationChannel channel = new SMSChannel("+91 9876543210");
channel.withMinimumPriority(NotificationPriority.HIGH);
channel.supportingTypes(NotificationType.ALERT, NotificationType.WARNING);
```

### With fluent API (BetterDesign)

```java
INotificationChannel channel = new SMSChannel("+91 9876543210")
        .withMinimumPriority(NotificationPriority.HIGH)
        .supportingTypes(NotificationType.ALERT, NotificationType.WARNING);
```

The second form reads almost like an English sentence describing the channel's
configuration.

### How to implement it

The only rule is: **return `this`** from every configuration method.

```java
public AbstractNotificationChannel withMinimumPriority(NotificationPriority priority) {
    this.minimumPriority = priority;
    return this;   // ← the entire "trick"
}
```

### Where you already used it without knowing

The Builder pattern in Section 1 is itself a fluent API:
```java
new Notification.Builder("Hello")
    .type(...)
    .priority(...)
    .build();
```

Each of `type()`, `priority()`, `recipient()` returns `this` (the Builder),
so you can keep calling methods on the same Builder object.

### In BetterDesign

`channel/AbstractNotificationChannel.java` — `withMinimumPriority()` and
`supportingTypes()`.

`model/Notification.java` — `Builder.type()`, `Builder.priority()`,
`Builder.recipient()`.

---

## 8. Dependency Injection (Constructor Injection)

### The Singleton problem you already know

In **MyDesign**, `NotificationService` is a Singleton:

```java
public static NotificationService getInstance() {
    if (INSTANCE == null) {
        synchronized (NotificationService.class) {
            if (INSTANCE == null) {
                INSTANCE = new NotificationService();
            }
        }
    }
    return INSTANCE;
}
```

Other classes reach into it directly:

```java
// MyDesign NotificationEngine constructor
public NotificationEngine() {
    this.observable = NotificationService.getInstance().getObservable();
    this.observable.addObserver(this);  // hidden side-effect!
}
```

**Problems:**
- `NotificationEngine` is secretly coupled to the global state of `NotificationService`.
- You cannot test `NotificationEngine` without the real `NotificationService` running.
- If two components each call `getInstance()`, they both talk to the same
  shared mutable state — changes in one can break the other.

### Dependency Injection

**Dependency Injection (DI)** means: instead of an object reaching out to
create or find its dependencies, you *give* (inject) the dependencies to it
through the constructor (or a setter).

```
Without DI:  Object ──→ creates/fetches its own dependencies
With DI:     Caller ──→ creates dependencies ──→ passes them into Object's constructor
```

### Minimal example

```java
// Without DI — tightly coupled
public class OrderService {
    private final DatabaseService db = new DatabaseService();  // hardcoded dependency
}

// With DI — loose coupling
public class OrderService {
    private final DatabaseService db;

    public OrderService(DatabaseService db) {  // dependency is GIVEN from outside
        this.db = db;
    }
}

// Usage
DatabaseService realDb   = new RealDatabaseService();
DatabaseService fakeDb   = new FakeDatabaseService();  // for testing

OrderService service     = new OrderService(realDb);   // production
OrderService testService = new OrderService(fakeDb);   // unit test
```

### In BetterDesign

`service/NotificationService.java` offers both a zero-arg constructor (for
convenience) and an injectable constructor (for testing):

```java
public NotificationService() {
    this.publisher = new NotificationPublisher();  // default
}

public NotificationService(INotificationPublisher publisher) {
    this.publisher = publisher;  // injected — can pass a fake publisher in tests
}
```

No class in BetterDesign calls `getInstance()`. All wiring is done explicitly
in `NotificationSystemMain`:

```java
NotificationService notificationService = new NotificationService();
notificationService.registerObserver(logger);
notificationService.registerObserver(store);
notificationService.registerObserver(router);
```

Everything is visible, explicit, and testable.

---

## 9. Implementing Multiple Interfaces on One Class

### Java allows a class to implement any number of interfaces

```java
public class MyClass implements InterfaceA, InterfaceB, InterfaceC { ... }
```

The class simply has to provide implementations for all the methods declared
across all those interfaces.

### Why it's powerful

It lets a single class play multiple *roles* without duplicating code.

### In BetterDesign

`InMemoryNotificationStore` is both a **store** and an **observer**:

```java
public class InMemoryNotificationStore
        implements INotificationStore, INotificationObserver {

    // Role 1: INotificationStore
    public void store(INotification n)  { ... }
    public List<INotification> getAll() { ... }
    public List<INotification> getByType(NotificationType t) { ... }
    public int size()                   { ... }

    // Role 2: INotificationObserver
    public void onNotification(INotification notification) {
        store(notification);  // auto-stores when a notification arrives
    }
}
```

Because it implements `INotificationObserver`, you can do:

```java
notificationService.registerObserver(store);
```

…and every notification that is published automatically gets stored —
no extra wiring needed. At the same time the `store` variable exposes the
full `INotificationStore` API (query methods) to whoever needs to read
stored notifications.

The same idea is applied to `ConsoleNotificationLogger` (implements
`INotificationLogger` + `INotificationObserver`) and `NotificationRouter`
(implements `INotificationRouter` + `INotificationObserver`).

---

## 10. Java Streams

### What are Streams?

Introduced in Java 8, **Streams** let you express data-processing pipelines
declaratively — like describing *what* you want instead of *how* to loop through it.

A stream pipeline has three parts:

```
source.intermediateOperation1().intermediateOperation2().terminalOperation()
```

| Part | Examples | Effect |
|---|---|---|
| **Source** | `list.stream()` | Wraps the collection in a stream |
| **Intermediate** | `filter(...)`, `map(...)`, `sorted(...)` | Transforms the stream lazily |
| **Terminal** | `collect(...)`, `count()`, `forEach(...)` | Triggers evaluation and produces a result |

### Without streams (imperative style)

```java
List<INotification> alertNotifications = new ArrayList<>();
for (INotification n : store) {
    if (n.getType() == NotificationType.ALERT) {
        alertNotifications.add(n);
    }
}
```

### With streams (declarative style)

```java
List<INotification> alertNotifications = store.stream()
        .filter(n -> n.getType() == NotificationType.ALERT)
        .collect(Collectors.toList());
```

Both do the same thing. The stream version is shorter, reads naturally, and
chains additional operations without nesting loops.

### Lambda expressions

`n -> n.getType() == NotificationType.ALERT` is a **lambda expression** —
an anonymous function passed as an argument. Read it as:
"for each element `n`, evaluate `n.getType() == NotificationType.ALERT`".

### Common intermediate operations

```java
.filter(n -> condition)          // keep only elements where condition is true
.map(n -> transform(n))          // transform each element to something else
.sorted()                        // sort (natural order or with a Comparator)
.distinct()                      // remove duplicates
.limit(5)                        // take at most 5 elements
```

### Common terminal operations

```java
.collect(Collectors.toList())          // collect into a new List
.collect(Collectors.toUnmodifiableList()) // collect into an unmodifiable List
.count()                               // count how many elements
.forEach(n -> doSomething(n))          // perform an action for each element
.findFirst()                           // return first element (Optional)
.anyMatch(n -> condition)              // true if any element matches
```

### In BetterDesign

`storage/InMemoryNotificationStore.java` — `getByType()` method:

```java
public List<INotification> getByType(NotificationType type) {
    return store.stream()
            .filter(n -> n.getType() == type)
            .collect(Collectors.toUnmodifiableList());
}
```

---

## 11. Defensive Collections

### What is defensive programming?

**Defensive programming** means writing code that prevents callers from
accidentally (or intentionally) corrupting internal state.

### The problem with returning a plain List

```java
// Inside InMemoryNotificationStore
private final List<INotification> store = new ArrayList<>();

public List<INotification> getAll() {
    return store;  // DANGEROUS — returns the actual internal list
}

// External caller
List<INotification> all = notificationStore.getAll();
all.clear();   // ← just wiped out the entire store!
```

Because you returned a reference to the *actual* internal `ArrayList`, the
caller can mutate it — and you'll never know.

### Solution: Unmodifiable views

`Collections.unmodifiableList(list)` wraps an existing list in a read-only
view. Any attempt to add, remove, or clear through the wrapper throws
`UnsupportedOperationException`:

```java
public List<INotification> getAll() {
    return Collections.unmodifiableList(store);  // safe — view only
}
```

The caller can iterate, read, and pass it around, but cannot modify it.
The underlying `store` is still the one source of truth.

### Collectors.toUnmodifiableList()

When building a list from a stream (Section 10), use `Collectors.toUnmodifiableList()`
instead of `Collectors.toList()` for the same protection:

```java
return store.stream()
        .filter(n -> n.getType() == type)
        .collect(Collectors.toUnmodifiableList());  // ← safe result
```

### In BetterDesign

`storage/InMemoryNotificationStore.java` — `getAll()` uses
`Collections.unmodifiableList()`, and `getByType()` uses
`Collectors.toUnmodifiableList()`.

---

## 12. Useful Map Methods: computeIfAbsent and getOrDefault

Both methods reduce boilerplate when working with maps whose values are
collections (very common in routing tables, group-by results, caches, etc.).

### computeIfAbsent

**Problem without it** — building a map from key → list:

```java
// Verbose manual check
if (!typedChannels.containsKey(type)) {
    typedChannels.put(type, new ArrayList<>());
}
typedChannels.get(type).add(channel);
```

**With `computeIfAbsent`** — if the key is absent, compute and insert a value;
either way, return the (existing or new) value:

```java
typedChannels.computeIfAbsent(type, k -> new ArrayList<>()).add(channel);
```

Read as: "Get the list for `type`. If none exists, create a new `ArrayList`
and store it. Then add `channel` to that list."

The `k -> new ArrayList<>()` is a lambda that is only called when the key is
missing.

### getOrDefault

When you want to *read* a value from a map but safely handle the case where
the key does not exist:

```java
// Old way
List<INotificationChannel> specific = typedChannels.get(notification.getType());
if (specific == null) {
    specific = Collections.emptyList();
}

// With getOrDefault
List<INotificationChannel> specific =
        typedChannels.getOrDefault(notification.getType(), Collections.emptyList());
```

If the key is found, you get its value. If not, you get the default value
(`Collections.emptyList()` here). The map itself is not modified.

### In BetterDesign

`routing/NotificationRouter.java`:

```java
// registerChannel — adds to (or creates) the list for a type
typedChannels.computeIfAbsent(type, k -> new ArrayList<>()).add(channel);

// route — reads the list for a type, empty list if none registered
List<INotificationChannel> specific =
        typedChannels.getOrDefault(notification.getType(), Collections.emptyList());
```

---

## 13. Fail-Fast Input Validation with isBlank()

### What is fail-fast?

**Fail-fast** means: detect invalid input as early as possible and throw a
clear exception immediately, rather than letting bad data silently propagate
through the system and cause a confusing error somewhere else.

The best place to validate is usually the **constructor**, because the object
should never exist in an invalid state to begin with.

### isBlank() vs isEmpty()

Both are `String` methods:

| Method | Returns `true` for |
|---|---|
| `isEmpty()` | `""` (zero characters) |
| `isBlank()` | `""` and `"   "` (only whitespace) — available since Java 11 |

Use `isBlank()` when you want to treat a message of just spaces the same as
an empty message (a notification saying `"   "` is useless).

### In BetterDesign

`model/Notification.java` — the Builder constructor:

```java
public Builder(String contents) {
    if (contents == null || contents.isBlank()) {
        throw new IllegalArgumentException("Notification contents cannot be empty");
    }
    this.contents = contents;
}
```

This means it is **impossible** to create a `Notification` with empty or
whitespace-only content. In **MyDesign**, `new Notification("")` would
silently succeed and an empty notification would travel through the whole
system before anyone noticed.

By throwing `IllegalArgumentException` in the constructor:
- The error message is clear and immediate.
- The stack trace points directly to where bad data was introduced.
- No downstream code needs to guard against `null` or blank contents.

---

## Quick Reference — Where Each Concept Appears in BetterDesign

| Concept | File(s) |
|---|---|
| Builder Pattern + Static Inner Class | `model/Notification.java` |
| Immutability (`final` fields) | `model/Notification.java` |
| Push-based Observer | `observer/INotificationObserver.java`, `observer/NotificationPublisher.java` |
| Enums as rich types + `ordinal()` | `model/NotificationType.java`, `model/NotificationPriority.java`, `channel/AbstractNotificationChannel.java` |
| `EnumSet` | `channel/AbstractNotificationChannel.java` |
| `EnumMap` | `routing/NotificationRouter.java` |
| Template Method Pattern | `channel/AbstractNotificationChannel.java` + `EmailChannel`, `SMSChannel`, `PushNotificationChannel` |
| Fluent API / Method Chaining | `channel/AbstractNotificationChannel.java`, `model/Notification.java` (Builder) |
| Dependency Injection | `service/NotificationService.java`, `BetterDesign/NotificationSystemMain.java` |
| Multiple Interface Implementation | `storage/InMemoryNotificationStore.java`, `logging/ConsoleNotificationLogger.java`, `routing/NotificationRouter.java` |
| Java Streams + Lambda | `storage/InMemoryNotificationStore.java` (`getByType`) |
| Defensive Collections | `storage/InMemoryNotificationStore.java` (`getAll`, `getByType`) |
| `computeIfAbsent` + `getOrDefault` | `routing/NotificationRouter.java` |
| Fail-fast validation + `isBlank()` | `model/Notification.java` (Builder constructor) |
