# Interactive Learning Guide - From V01 to V02

## 🎯 Purpose
This guide helps you understand each improvement step by step. We'll explore **why** changes were made, **when** to apply each pattern, and **how** to implement them.

---

## 📚 Learning Path

### Phase 1: Understanding SOLID Principles (Foundation)
**Estimated Time:** 2-3 hours with discussion

#### 1.1 Single Responsibility Principle (SRP)
**Question to ponder:** Should a class that manages restaurants also handle payment processing?

**Let's explore:**
- 📖 **What**: A class should have only one reason to change
- 🤔 **Why**: Reduces coupling, improves testability, easier maintenance
- 💡 **Example from V01**: `TomatoApp` class
  - Handles: initialization, search, cart, orders, payment, display
  - Problem: If payment logic changes, TomatoApp changes. If search changes, TomatoApp changes.
  
**Discussion Points:**
1. How do you identify when a class has too many responsibilities?
2. What are the signs that a class is doing too much?
3. How do you decide what to split into separate classes?

**Hands-on Exercise:**
```java
// V01: One class, many responsibilities
class TomatoApp {
    void initializeRestaurants() { /* ... */ }
    void searchByLocation() { /* ... */ }
    void addToCart() { /* ... */ }
    void checkout() { /* ... */ }
    void processPayment() { /* ... */ }
}

// Your task: List what responsibilities you see above
// Then: How would you split this into separate classes?
```

**Next Step:** Let's discuss your answers before moving to the implementation!

---

#### 1.2 Open/Closed Principle (OCP)
**Question to ponder:** If you need to add a new payment method, should you modify existing code?

**Let's explore:**
- 📖 **What**: Open for extension, closed for modification
- 🤔 **Why**: Reduces risk of breaking existing functionality
- 💡 **Example from V01**: `FactoryPaymentStrategy`

**Code to analyze together:**
```java
// V01 approach
public IPaymentStrategy createPaymentStrategyObject(String paymentMethod, String accountDetails) {
    if (paymentMethod.equalsIgnoreCase("upi")) {
        return new UPIPaymentStrategy(accountDetails);
    } else if (paymentMethod.equalsIgnoreCase("card")) {
        return new CardPaymentStrategy(accountDetails);
    } else {
        return null;
    }
}
```

**Discussion Points:**
1. What happens when we need to add "wallet" payment?
2. Where do we have to make changes?
3. What's the risk of modifying this code?
4. How can we make it extensible without modification?

**Your Challenge:**
- Before looking at V02, try to think: How would YOU make this extensible?
- What data structure could help?
- Hint: Think about storing "payment type → creator function" mappings

---

#### 1.3 Liskov Substitution Principle (LSP)
**Question to ponder:** Can you replace a parent class with any of its subclasses without breaking functionality?

**Let's explore:**
- 📖 **What**: Subtypes must be substitutable for their base types
- 🤔 **Why**: Ensures polymorphism works correctly
- 💡 **Example from V01**: `Order`, `DeliveryOrder`, `PickupOrder`

**Good LSP example:**
```java
Order order = new DeliveryOrder();  // Can substitute
boolean result = order.processPayment();  // Works the same way
```

**Discussion:** Is LSP violated in V01? Let's check together!

---

#### 1.4 Interface Segregation Principle (ISP)
**Question to ponder:** Should a class implement methods it doesn't use?

**Let's explore:**
- 📖 **What**: Don't force classes to implement unused methods
- 🤔 **Why**: Reduces unnecessary dependencies
- 💡 **Status in V01**: Actually okay! Interfaces are small and focused.

---

#### 1.5 Dependency Inversion Principle (DIP)
**Question to ponder:** Should high-level classes know about low-level implementation details?

**Let's explore:**
- 📖 **What**: Depend on abstractions, not concretions
- 🤔 **Why**: Makes code flexible and testable
- 💡 **Example from V01**: Direct instantiation

**Code to analyze:**
```java
// V01 - TomatoApp directly creates concrete factories
public Order instantOrderCheckout(...) {
    return checkout(..., new InstantOrderFactory());  // ❌ Depends on concrete class
}
```

**Discussion Points:**
1. What if we want to test with a mock factory?
2. How does this tight coupling affect flexibility?
3. What's the solution?

**Your challenge:** Think about how dependency injection could help here.

---

### Phase 2: Design Patterns Deep Dive
**Estimated Time:** 3-4 hours with discussion

#### 2.1 Factory Pattern
**The Big Question:** When do you use Factory pattern?

**Let's compare approaches:**
```java
// Approach 1: Simple Factory (V01 style)
if (type.equals("upi")) return new UPIStrategy();
if (type.equals("card")) return new CardStrategy();

// Approach 2: Map-based Factory (V02 style)
Map<String, Supplier<Strategy>> map = ...;
return map.get(type).get();

// Approach 3: Abstract Factory (when needed)
interface PaymentFactory {
    PaymentStrategy createStrategy();
}
```

**Discussion Questions:**
1. When is simple factory enough?
2. When do you need map-based approach?
3. When do you need abstract factory?
4. Trade-offs of each approach?

**Hands-on Exercise:**
Let's refactor the payment factory together, step by step!

---

#### 2.2 Strategy Pattern
**The Big Question:** When should behavior be extracted into a strategy?

**Real-world scenario:**
```
You have a notification system. 
Initially: Just console output
Later: Add email notifications
Then: Add SMS notifications
Finally: Users want to choose which channels they prefer
```

**Let's design this together:**
1. First, without Strategy pattern (like V01)
2. Then, refactor to Strategy pattern (like V02)
3. Discuss: What did we gain?

---

#### 2.3 Singleton Pattern
**The Big Question:** Do you REALLY need a Singleton?

**Let's debate:**
- ✅ **Good use cases**: Database connection pool, Logger, Configuration
- ❌ **Bad use cases**: Factories, Services, Managers

**Code Review Exercise:**
```java
// V01 has these as Singletons:
- RestaurantManager
- OrderManager  
- FactoryPaymentStrategy

// Question: Which ones TRULY need to be Singleton? Why or why not?
```

**Thread Safety Discussion:**
```java
// V01 implementation
if (instance == null) {  // ⚠️ What's the problem here?
    instance = new RestaurantManager();
}

// Let's discuss race conditions with a timeline diagram!
```

---

#### 2.4 Builder Pattern
**The Big Question:** When is a constructor not enough?

**Compare:**
```java
// Constructor approach
Order order = new Order(user, restaurant, items, type, time, payment);  // ❌ Too many params

// Builder approach
Order order = new Order.Builder()
    .user(user)
    .restaurant(restaurant)
    .items(items)
    .orderType(OrderType.DELIVERY)
    .scheduledTime(time)
    .build();  // ✅ Clear and fluent
```

**Discussion:**
1. When does a constructor become unwieldy?
2. What are the benefits of Builder?
3. Any downsides?

---

### Phase 3: Code Quality & Best Practices
**Estimated Time:** 1-2 hours with discussion

#### 3.1 Type Safety: Enums vs Strings
**The Question:** What's wrong with strings for types?

**Bug Scenario:**
```java
// Using strings
if (orderType.equals("delivrey")) {  // ❌ Typo! Runtime bug
    // ...
}

// Using enums
if (orderType == OrderType.DELIVREY) {  // ✅ Won't compile! Caught immediately
    // ...
}
```

**Your Task:** Find all the string literals in V01 that should be enums.

---

#### 3.2 Validation & Error Handling
**Question:** Where should validation happen?

**Let's explore:**
```java
// Option 1: Validate in constructor
public MenuItem(String code, String name, double price) {
    if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
    // ...
}

// Option 2: Validate in setter
public void setPrice(double price) {
    if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
    this.price = price;
}

// Option 3: Validate before calling
if (price >= 0) {
    menuItem.setPrice(price);
}
```

**Discussion:** Pros and cons of each approach?

---

#### 3.3 Security: Data Masking
**Real-world scenario:** Logs are stored and accessible to many people.

**The Problem:**
```java
System.out.println("Payment from card: " + cardNumber);  // ❌ "Payment from card: 1234-5678-9012-3456"
```

**Your Challenge:**
1. Why is this dangerous?
2. What data should be masked?
3. How would you implement masking?
4. What about regulations (PCI DSS, GDPR)?

---

### Phase 4: Refactoring Workshop
**Estimated Time:** 2-3 hours hands-on

#### Exercise 1: Refactor TomatoApp
**Goal:** Apply SRP by extracting services

**Step-by-step:**
1. Identify all responsibilities
2. Create service interfaces
3. Extract one service at a time
4. Run tests after each extraction
5. Update TomatoApp to use services

**We'll do this together, with me guiding you!**

---

#### Exercise 2: Make Factory Extensible
**Goal:** Apply OCP to payment factory

**Step-by-step:**
1. Create a map to store creators
2. Initialize with existing payment types
3. Add a registration method
4. Test by adding a new payment method without modifying factory
5. Discuss: What did we achieve?

---

#### Exercise 3: Add Notification Strategies
**Goal:** Apply Strategy pattern to notifications

**Starting point:**
```java
// V01: Static method, console only
public static void notify(Order order) {
    System.out.println("Order placed!");
}
```

**End goal:**
```java
// V02: Multiple strategies, composable
service.addStrategy(new EmailNotification());
service.addStrategy(new SMSNotification());
service.notifyOrderPlaced(order);
```

**We'll build this together from scratch!**

---

## 🗺️ How to Use This Guide

### Option A: Linear Learning (Recommended for beginners)
Start from Phase 1.1 and work through sequentially. Each section builds on the previous one.

### Option B: Topic-Based Learning
Jump to specific topics you want to understand:
- Struggling with SRP? → Phase 1.1
- Confused about when to use Factory? → Phase 2.1
- Want to understand thread safety? → Phase 2.3

### Option C: Code-First Learning
1. Pick a code review issue from CODE_REVIEW.md
2. Try to fix it yourself first
3. Then look at V02 to see the solution
4. Come back here to understand the theory

---

## 💬 Interactive Discussion Format

For each topic, we'll follow this pattern:

1. **🤔 Question/Problem**: Start with a real scenario
2. **📖 Concept**: Brief theory explanation
3. **💡 Example**: Code from V01 showing the issue
4. **🔍 Analysis**: Discuss why it's a problem
5. **💭 Your Turn**: You try to solve it
6. **✅ Solution**: We look at V02 together
7. **🎯 Key Takeaway**: Summarize the learning

---

## 📝 Discussion Questions for Each Phase

### Phase 1 - SOLID
- [ ] Can you explain SRP in your own words?
- [ ] Give an example from your own code where you violated SRP
- [ ] When is it okay to have multiple responsibilities in one class?
- [ ] How do you balance SOLID principles with practicality?

### Phase 2 - Patterns
- [ ] When would you NOT use a factory?
- [ ] Can you think of a scenario where Singleton is truly needed?
- [ ] What's the difference between Strategy and State patterns?
- [ ] When is inheritance better than composition?

### Phase 3 - Quality
- [ ] What other validations should we add?
- [ ] How do you decide what to log vs what to hide?
- [ ] Enums vs Constants - when to use which?
- [ ] What's your testing strategy for these improvements?

---

## 🎓 Learning Outcomes

By the end of this guide, you should be able to:

✅ Identify SOLID principle violations in code
✅ Explain WHY each principle matters (not just WHAT it is)
✅ Choose appropriate design patterns for different scenarios
✅ Refactor code while maintaining functionality
✅ Write more testable and maintainable code
✅ Make informed trade-offs between patterns and simplicity

---

## 🚀 Ready to Start?

**Let's begin with a simple question:**

Looking at the V01 `TomatoApp` class, can you list out all the different things it's responsible for? 

Once you've thought about it, we'll discuss why having multiple responsibilities in one class can be problematic, and then we'll look at how V02 solves this!

**Your Turn:** Which phase would you like to start with? Or do you have specific questions about any of the code review issues?
