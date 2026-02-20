# GoF Design Pattern Classification

## What Is a Design Pattern?

Before anything else — a design pattern is **not a library, not a framework, not code you copy-paste**. It is a named, proven solution to a recurring problem in software design.

Think of it like a chess opening. A "Queen's Gambit" is not a rigid sequence you must follow — it is a _strategy_ that experienced players have found effective in a certain situation. You still have to play the game yourself, but knowing the strategy by name means you can reason about it, communicate it to other players instantly, and apply it appropriately.

Design patterns are the same. When a senior engineer says "this should be a Singleton" or "use a Strategy here", they are communicating an entire structural decision in one word. That is the value — a shared vocabulary built on proven solutions.

---

## The Gang of Four

In 1994, four software engineers — Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides — published _Design Patterns: Elements of Reusable Object-Oriented Software_. They catalogued **23 design patterns** observed across large, well-designed object-oriented systems and organised them into a classification system.

This book is still the authoritative reference. When people say "GoF patterns", this is what they mean.

The GoF classified all 23 patterns along **two axes**.

---

## Axis 1 — Purpose (Primary Classification)

This is the main classification. It answers: _what kind of problem does this pattern solve?_

There are three categories.

---

### Category 1: Creational Patterns

**The question they answer: How should I create objects?**

On the surface this sounds trivial — just use `new`. But in large systems, hardcoding `new ConcreteClass()` everywhere causes problems:

- You create a tight dependency on a specific implementation
- Changing what gets created means touching every caller
- Some objects need to be shared, not recreated (think database connection pools)
- Some objects require complex multi-step construction

Creational patterns solve all of these by **abstracting the instantiation process**. The caller never needs to know _exactly_ what it is getting or exactly how it was built.

| Pattern              | The problem it solves                                                                       | Real analogy                                                                                                                                                       |
| -------------------- | ------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Singleton**        | You need exactly one instance of something across the entire application — no more, no less | The CEO of a company. There is exactly one. Everyone talks to the same person.                                                                                     |
| **Factory Method**   | You want to create objects, but you want subclasses to decide _which_ object gets created   | A logistics company that ships packages. The base company defines how shipping works, but a road subsidiary creates trucks while an air subsidiary creates planes. |
| **Abstract Factory** | You need to create families of related objects that must be used together                   | An IKEA furniture set. You pick a style (Modern / Rustic) and everything you get — chair, table, shelf — belongs to that same family. You never mix styles.        |
| **Builder**          | Object construction is complex, has many optional parts, and mixing up steps causes bugs    | Ordering a custom burger. You build it step by step: bun → patty → toppings → sauce. The builder ensures steps happen in the right order and nothing is forgotten. |
| **Prototype**        | Creating a new object from scratch is expensive, but cloning an existing one is cheap       | A photocopier. Copying an existing document is far cheaper than retyping every word.                                                                               |

---

### Category 2: Structural Patterns

**The question they answer: How should I compose objects together?**

Once you have objects, you need to assemble them into larger working systems. Structural patterns deal with **relationships and composition** — how you wire objects and classes together while keeping things flexible and maintainable.

The central tension they resolve: you want large, capable structures, but you do not want everything tightly tangled together. Adding a new feature should not require rewriting half the system.

| Pattern       | The problem it solves                                                                                    | Real analogy                                                                                                                                                                  |
| ------------- | -------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Adapter**   | Two things need to work together but have incompatible interfaces                                        | A universal power adapter. Your laptop plug is UK format but the socket is EU format. The adapter sits in between and makes them compatible without changing either.          |
| **Bridge**    | You want to separate _what something is_ from _how it works_, so both can change independently           | A TV remote (abstraction) and a TV (implementation). You can swap out the remote or swap out the TV independently — they are decoupled through a shared interface.            |
| **Composite** | You have individual objects and groups of objects, and you want to treat them the same way               | A file system. A folder can contain files or other folders. Whether you call `getSize()` on a file or a folder, it works the same way.                                        |
| **Decorator** | You want to add behaviour to an object without changing its class or subclassing                         | A coffee order. Start with an Espresso. Add a decorator for Milk. Add a decorator for Caramel. Each decorator wraps the previous one and adds to the bill and description.    |
| **Facade**    | A subsystem is complex, but most callers only need a simple interface to it                              | A hotel concierge. Behind the scenes — booking restaurants, arranging transport, calling room service — is complex. You just tell the concierge what you want. One interface. |
| **Flyweight** | You need thousands of similar objects, and storing all their data individually would use too much memory | A forest in a video game. Instead of storing full tree data for 100,000 trees, store one tree blueprint and only store position + variation for each instance.                |
| **Proxy**     | You want to control or mediate access to an object — for security, lazy loading, caching, or logging     | A bank card. You do not carry the actual money. The card is a proxy to your bank account that controls and logs every access.                                                 |

---

### Category 3: Behavioral Patterns

**The question they answer: How should objects communicate and divide responsibility?**

This is the largest and most varied category because managing _behaviour_ — how objects talk to each other, who is responsible for what, how algorithms are structured — is where most of the real complexity in software lives.

The key insight: communication and responsibility are often tangled. Behavioral patterns untangle them by giving each object a clearly defined role and defining clean communication contracts between them.

| Pattern                     | The problem it solves                                                                                       | Real analogy                                                                                                                                                                                                                        |
| --------------------------- | ----------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Chain of Responsibility** | Multiple objects might handle a request, but you do not know at coding time which one will                  | A customer support escalation system. First-line support tries to resolve it. If they cannot, it goes to second-line. If they cannot, it goes to a specialist. Each handler decides: handle it or pass it on.                       |
| **Command**                 | You want to encapsulate a request as an object so it can be queued, logged, undone, or retried              | A restaurant order ticket. The waiter does not shout your order to the chef directly. They write it on a ticket. That ticket can be queued, handed to a different chef, voided, or referenced later.                                |
| **Interpreter**             | You need to evaluate sentences in a simple language or grammar                                              | A calculator parsing `"3 + 4 * 2"`. Each token (number, operator) is an expression object that knows how to evaluate itself. Compose them into a tree and evaluate the root.                                                        |
| **Iterator**                | You want to traverse a collection without knowing how it is stored internally                               | A TV remote's channel-up button. You iterate through channels without knowing whether they are stored as an array, a linked list, or fetched from a server.                                                                         |
| **Mediator**                | Many objects interact with each other in complex ways, creating a tangled web of dependencies               | Air traffic control. Instead of each plane communicating with every other plane directly (chaos), all planes communicate only with the control tower. The tower mediates everything.                                                |
| **Memento**                 | You need to save and restore the state of an object without violating its encapsulation                     | A video game save point. The game saves your exact state. Later, you can reload and return to that exact moment — without exposing the internal save format to other parts of the system.                                           |
| **Observer**                | One object changes state and multiple other objects need to react, but you do not want them tightly coupled | A newspaper subscription. You subscribe once. Whenever a new edition is published, it is delivered to you automatically. The newspaper does not need to know who you are or what you do with it.                                    |
| **State**                   | An object's behaviour changes dramatically based on its internal state                                      | A traffic light. The same `signal()` call behaves completely differently depending on whether the current state is Red, Green, or Yellow. Each state owns its own behaviour.                                                        |
| **Strategy**                | You have multiple algorithms that do the same job, and you want to swap between them at runtime             | A sat-nav app routing you from A to B. You can choose: fastest route, shortest route, avoid tolls. All three are strategies implementing the same interface. You swap the strategy without changing the app.                        |
| **Template Method**         | The steps of an algorithm are fixed, but specific steps can be customised by subclasses                     | A recipe for bread. The steps are always the same: mix, knead, prove, bake. But a sourdough subclass provides different yeast; a gluten-free subclass uses different flour. The structure is fixed; the details vary.               |
| **Visitor**                 | You want to add new operations to a class hierarchy without modifying any of the existing classes           | A tax inspector visiting different types of property (house, office, warehouse). Each property type accepts the inspector and lets them do their work. Adding a new inspection type does not require changing the property classes. |

---

## Axis 2 — Scope (Secondary Classification)

The second axis answers: _does this pattern work primarily through inheritance (fixed at compile time) or composition (decided at runtime)?_

|                | **Class scope** — inheritance, compile-time | **Object scope** — composition, runtime                                                           |
| -------------- | ------------------------------------------- | ------------------------------------------------------------------------------------------------- |
| **Creational** | Factory Method                              | Abstract Factory, Builder, Prototype, Singleton                                                   |
| **Structural** | Adapter _(class form)_                      | Adapter _(object form)_, Bridge, Composite, Decorator, Facade, Flyweight, Proxy                   |
| **Behavioral** | Interpreter, Template Method                | Chain of Responsibility, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Visitor |

**Class scope** means the pattern is wired in via subclassing. The relationship is fixed when you compile. It is simpler but less flexible.

**Object scope** means the pattern works by objects holding references to other objects at runtime. You can swap those references, change behaviour dynamically, and compose things more freely.

The fact that the vast majority of patterns are Object-scoped is not a coincidence — it directly reflects GoF's second fundamental principle.

---

## The Two Principles Underlying Everything

GoF state two foundational design principles at the start of the book. Every one of the 23 patterns is a concrete application of one or both:

### Principle 1: Program to an interface, not an implementation

Do not write code that depends on a concrete class when an abstraction will do.

```java
// Wrong — tightly coupled to a specific implementation
MySQLDatabase db = new MySQLDatabase();

// Right — depends only on the abstraction
Database db = new MySQLDatabase();
```

When your code depends on `Database` rather than `MySQLDatabase`, you can swap in a `PostgresDatabase` or a `MockDatabase` for testing without changing the calling code. Most patterns specifically exist to make this swap possible.

### Principle 2: Favour object composition over class inheritance

Inheritance creates rigid hierarchies. A subclass is permanently bound to its parent at compile time. If the parent changes, all subclasses are affected.

Composition — holding a reference to another object — is flexible. You can swap the reference at runtime. You can combine behaviours in ways inheritance can never express without an explosion of subclasses.

```java
// Inheritance approach — locked in at compile time
class LoggingOrderService extends OrderService { ... }
class CachingOrderService extends OrderService { ... }
// What if you need both logging AND caching? Create a third class?

// Composition approach — flexible at runtime
OrderService service = new OrderService();
service = new LoggingDecorator(service);
service = new CachingDecorator(service);
```

This is why Decorator exists. This is why Strategy exists. This is why the majority of patterns are Object-scoped.

---

## Pattern Count by Category

| Category   | Count | Share |
| ---------- | ----- | ----- |
| Creational | 5     | 22%   |
| Structural | 7     | 30%   |
| Behavioral | 11    | 48%   |

Behavioral patterns make up nearly half because coordinating behaviour and responsibility between objects is where complex software actually lives. Creating objects is a one-time act; how they communicate runs your entire application.

---

## What Comes After Learning Patterns

A common mistake when learning patterns is to start seeing every problem as a nail that needs a pattern-shaped hammer. Resist this.

A pattern is only the right choice when:

1. You have the _specific recurring problem_ that pattern was designed to solve
2. The benefit it provides (flexibility, extensibility, decoupling) is actually needed in your context
3. The added complexity of the extra classes/interfaces is worth that benefit

Overusing patterns creates code that is harder to read, harder to debug, and harder to change — the exact opposite of the goal. GoF themselves say: _"the hardest part of using design patterns is knowing when to use them"_.

---

## Where the Patterns in This Repo Fall

| Folder             | Pattern        | GoF Category | Scope  |
| ------------------ | -------------- | ------------ | ------ |
| T03/DP01_Strategy  | Strategy       | Behavioral   | Object |
| T03/DP02_Factory   | Factory Method | Creational   | Class  |
| T03/DP03_Singleton | Singleton      | Creational   | Object |
| T03/DP04_Observer  | Observer       | Behavioral   | Object |
| T03/DP05_Decorator | Decorator      | Structural   | Object |
| T03/DP06_Command   | Command        | Behavioral   | Object |
