# BetterDesign – Notification Engine Design Improvements

## Overview

`BetterDesign` is a ground-up redesign of the Notification Engine originally implemented in `MyDesign`.
Every improvement below is driven by a concrete weakness identified in `MyDesign`.

---

## UML Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         APPLICATION ENTRY POINT                              │
└─────────────────────────────────────────────────────────────────────────────┘

    NotificationSystemMain
        └── uses ──> NotificationService
                        │
                        └── publishes via ──> INotificationPublisher
                                                  └── impl: NotificationPublisher
                                                            │
                                                            ├── notifies ──> ConsoleNotificationLogger
                                                            ├── notifies ──> InMemoryNotificationStore
                                                            └── notifies ──> NotificationRouter

┌─────────────────────────────────────────────────────────────────────────────┐
│                              MODEL LAYER                                     │
└─────────────────────────────────────────────────────────────────────────────┘

    <<interface>> INotification
        └── impl: Notification (Builder)

    NotificationType (enum): INFO, WARNING, ALERT, PROMOTION
    NotificationPriority (enum): LOW, MEDIUM, HIGH, CRITICAL

    Decorator Chain:
        NotificationDecorator (abstract)
            ├── TimestampDecorator
            ├── SignatureDecorator
            └── PriorityTagDecorator   ← NEW

┌─────────────────────────────────────────────────────────────────────────────┐
│                         OBSERVER / EVENT BUS                                 │
└─────────────────────────────────────────────────────────────────────────────┘

    <<interface>> INotificationPublisher
        └── impl: NotificationPublisher

    <<interface>> INotificationObserver
        onNotification(INotification)          ← PUSH-BASED (vs. pull in MyDesign)
        └── implemented by: ConsoleNotificationLogger,
                            InMemoryNotificationStore,
                            NotificationRouter

┌─────────────────────────────────────────────────────────────────────────────┐
│                            ROUTING & CHANNELS                                │
└─────────────────────────────────────────────────────────────────────────────┘

    <<interface>> INotificationRouter
        └── impl: NotificationRouter  (also INotificationObserver)
                    │
                    ├── globalChannels: List<INotificationChannel>
                    └── typedChannels:  Map<NotificationType, List<INotificationChannel>>

    <<interface>> INotificationChannel
        └── AbstractNotificationChannel  (filter: type set + minimum priority)
                ├── EmailChannel
                ├── SMSChannel
                └── PushNotificationChannel

┌─────────────────────────────────────────────────────────────────────────────┐
│                               STORAGE                                        │
└─────────────────────────────────────────────────────────────────────────────┘

    <<interface>> INotificationStore
        └── impl: InMemoryNotificationStore  (also INotificationObserver)
                    ├── getAll()
                    ├── getByType(NotificationType)
                    └── size()

┌─────────────────────────────────────────────────────────────────────────────┐
│                               LOGGING                                        │
└─────────────────────────────────────────────────────────────────────────────┘

    <<interface>> INotificationLogger
        └── impl: ConsoleNotificationLogger  (also INotificationObserver)
                    ├── log(INotification)
                    └── logError(INotification, String)
```

---

## Improvements Over MyDesign

### 1. Push-Based Observer (vs. Pull-Based)

| | MyDesign | BetterDesign |
|---|---|---|
| Observer interface | `void update()` | `void onNotification(INotification)` |
| How observers get the data | Pull from `observable.getNotificationContent()` | Notification pushed directly as argument |

**Why it matters:** In MyDesign, every observer must hold a reference to the `NotificationObservable` and call `getNotificationContent()` on it. This creates a tight coupling between observers and the observable implementation. In BetterDesign, the notification is passed directly to `onNotification()`. Observers have zero knowledge of how or where the notification originated.

---

### 2. No Singleton / No Hidden Constructor Side-Effects

| | MyDesign | BetterDesign |
|---|---|---|
| `NotificationService` | Singleton (`volatile` double-checked locking) | Plain class, injected via constructor |
| `Logger` constructor | Silently calls `NotificationService.getInstance()` and registers itself | Explicit: caller calls `notificationService.registerObserver(logger)` |
| `NotificationEngine` constructor | Silently calls `NotificationService.getInstance()` | No singleton; wired externally |

**Why it matters:** Hidden side-effects in constructors make classes impossible to unit-test in isolation and create implicit global state. `BetterDesign` removes the singleton entirely—`NotificationService` is created with `new` and passed around, making the dependency graph explicit and testable.

---

### 3. Notification Type & Priority Enums

MyDesign has no notion of notification category or urgency. BetterDesign adds:
- `NotificationType` – `INFO`, `WARNING`, `ALERT`, `PROMOTION`
- `NotificationPriority` – `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

These are first-class fields on `INotification`, enabling filtering and routing decisions without string comparisons or instanceof checks.

---

### 4. Notification Built with the Builder Pattern

| | MyDesign | BetterDesign |
|---|---|---|
| Construction | `new Notification(message)` — single string | `new Notification.Builder(message).type(...).priority(...).recipient(...).build()` |
| Validation | None | Constructor guards against blank content |
| Extensibility | Add a field → break constructor callers | Add a Builder method; existing callers unchanged |

The Builder also makes `Notification` **immutable** (all fields `final`), eliminating accidental mutation bugs.

---

### 5. Channel Abstraction Replaces Engine + Strategy Combo

In MyDesign, delivering a notification through multiple channels requires:
1. Creating a `NotificationEngine` (an observer),
2. Adding `INotificationStrategy` objects to it (email, SMS, popup).

This two-layer indirection can be confusing. BetterDesign collapses both into a single `INotificationChannel` concept.

Each channel:
- Is self-contained (knows its address/config).
- Declares what it can handle (`canHandle(INotification)`).
- Sends the notification (`send(INotification)`).

`AbstractNotificationChannel` provides built-in filtering by type-set and minimum priority with a fluent API:

```java
new SMSChannel("+91 9876543210")
    .withMinimumPriority(NotificationPriority.HIGH);
```

---

### 6. Explicit Routing via NotificationRouter

MyDesign broadcasts every notification to every observer regardless of its content. BetterDesign introduces `NotificationRouter`, which supports:

- **Global channels** — receive all notifications they `canHandle()`.
- **Type-specific channels** — registered for a specific `NotificationType` and only receive matching notifications.

This means you can say "only SMS me for ALERT+HIGH", "email me everything", and "never push PROMOTION notifications" — all without modifying existing code (Open/Closed Principle).

---

### 7. Proper Storage Interface

| | MyDesign | BetterDesign |
|---|---|---|
| Storage | `List<INotification>` private field in `NotificationService` | `INotificationStore` interface + `InMemoryNotificationStore` |
| Query API | None (no getter) | `getAll()`, `getByType(type)`, `size()` |
| Swappable | No | Yes (implement `INotificationStore` for DB, Redis, etc.) |

`InMemoryNotificationStore` also implements `INotificationObserver`, so it auto-stores every notification without additional wiring.

---

### 8. Proper Logging Abstraction

| | MyDesign | BetterDesign |
|---|---|---|
| Logger | `Logger` class prints to stdout; second constructor skips observer registration (bug) | `INotificationLogger` interface + `ConsoleNotificationLogger` |
| Error logging | Not supported | `logError(INotification, String error)` method |
| Swappable | No | Yes (implement `INotificationLogger` for structured/file logging) |

---

### 9. New Decorator: PriorityTagDecorator

MyDesign provides timestamp and signature decorators. BetterDesign adds `PriorityTagDecorator`, which prepends the notification's priority level:

```
[CRITICAL] Suspicious login detected on your account!
```

This demonstrates how the improved `INotification` contract (which now exposes `getPriority()`) naturally unlocks richer decorator behaviour.

---

### 10. Bug Fix: Inconsistent Logger Registration in MyDesign

In MyDesign, `Logger` has two constructors:
- `Logger()` – registers itself with the observable (side-effect).
- `Logger(NotificationObservable)` – stores the observable but does **not** register itself, so `update()` is never called.

In `NotificationSystemMain`, a `Logger` is created but never registered with the observable:
```java
Logger logger = new Logger();   // registers OK via getInstance()
// but NotificationEngine is also an observer — Logger is never added elsewhere
```
BetterDesign eliminates this class of bug entirely because registration is always the caller's explicit responsibility.

---

## SOLID Principles Compliance

| Principle | MyDesign | BetterDesign |
|---|---|---|
| **SRP** | `NotificationService` owns observable, storage, and notification dispatch | Separated into `NotificationService` (dispatch), `INotificationStore` (storage), `INotificationLogger` (logging) |
| **OCP** | Adding a new strategy requires touching `NotificationEngine` | Register a new `INotificationChannel` without touching existing code |
| **LSP** | Decorators are consistent | Decorators are consistent; `NotificationDecorator` base properly delegates all fields |
| **ISP** | `IObservable` is fine, but `Logger` is forced to implement `update()` which pulls from observable | `INotificationObserver` has one method `onNotification()`; logger/store/router implement only what they need |
| **DIP** | `NotificationEngine` and `Logger` depend on the `NotificationService` singleton (concrete class) | Everything depends on interfaces (`INotificationPublisher`, `INotificationObserver`, `INotificationStore`, `INotificationLogger`) |

---

## Design Patterns Used

| Pattern | Where |
|---|---|
| **Observer** | `INotificationPublisher` / `INotificationObserver` (push-based) |
| **Strategy** | `INotificationChannel` — runtime-swappable delivery strategies |
| **Decorator** | `NotificationDecorator` hierarchy — `Timestamp`, `Signature`, `PriorityTag` |
| **Builder** | `Notification.Builder` — safe, readable construction of immutable notifications |
| **Template Method** | `AbstractNotificationChannel.canHandle()` — base filtering with overridable send() |
| **Facade** | `NotificationService` — single entry point hiding publisher internals |
