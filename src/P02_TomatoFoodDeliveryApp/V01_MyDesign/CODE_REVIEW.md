# Code Review: V01_MyDesign - Tomato Food Delivery App

## Overview
This document contains detailed code review comments for the V01_MyDesign implementation. Each section identifies issues, explains why they matter, and suggests improvements.

---

## 1. SOLID Principles Violations

### 1.1 Single Responsibility Principle (SRP) Violations

#### 📁 `TomatoApp.java`

**Issue #1: Multiple responsibilities in TomatoApp** (Lines 17-109)
```java
public class TomatoApp {
    private final User user;
    
    public TomatoApp(User user) {
        initializeRestaurants();  // ❌ Responsibility 1: Data initialization
        this.user = user;
    }
    
    public void initializeRestaurants() { ... }  // ❌ Responsibility 2: Restaurant setup
    public List<Restaurant> searchByLocation() { ... }  // ❌ Responsibility 3: Search operations
    public void addToCart(String itemCode) { ... }  // ❌ Responsibility 4: Cart management
    public Order instantOrderCheckout(...) { ... }  // ❌ Responsibility 5: Order creation
    public void payForOrder(Order order) { ... }  // ❌ Responsibility 6: Payment processing
    public void printUserCart() { ... }  // ❌ Responsibility 7: Display/UI
}
```

**Why this matters:**
- Hard to test individual features in isolation
- Changes to one responsibility (e.g., search logic) can affect unrelated features
- Difficult to reuse specific functionality
- Violates "a class should have only one reason to change"

**Suggested improvement:**
```java
// Separate into focused classes
public class RestaurantService {
    public List<Restaurant> searchByLocation(String location) { ... }
}

public class CartService {
    public void addItem(Cart cart, MenuItem item) { ... }
}

public class OrderService {
    public Order createOrder(User user, OrderType type) { ... }
}

public class PaymentService {
    public boolean processPayment(Order order) { ... }
}

// TomatoApp becomes a thin orchestration layer
public class TomatoApp {
    private final RestaurantService restaurantService;
    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    
    // Now only responsible for coordinating services
}
```

---

#### 📁 `Order.java`

**Issue #2: Order class has too many responsibilities** (Lines 11-108)
```java
public abstract class Order {
    private static int nextOrderID = 0;  // ❌ Manages ID generation
    private User user;
    private Restaurant restaurant;
    private List<MenuItem> items;
    private double totalAmount;
    private IPaymentStrategy paymentStrategy;  // ❌ Manages payment
    
    public boolean processPayment() {  // ❌ Handles payment logic
        if (paymentStrategy == null) {
            System.out.println("Select Payment Strategy first!!!");
            return false;
        }
        paymentStrategy.processPayment(totalAmount);
        return true;
    }
}
```

**Why this matters:**
- Order should represent an order's data, not handle payment processing
- ID generation is a separate concern
- Mixing data and behavior makes testing harder

**Suggested improvement:**
```java
// Order becomes a pure data class
public class Order {
    private final String orderId;  // ID passed from outside
    private final User user;
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private final double totalAmount;
    private OrderStatus status;
    
    // Only getters and setters, no business logic
}

// Payment handling moved to service
public class PaymentService {
    public boolean processPayment(Order order, PaymentStrategy strategy) {
        return strategy.pay(order.getTotalAmount());
    }
}
```

---

### 1.2 Open/Closed Principle (OCP) Violations

#### 📁 `FactoryPaymentStrategy.java`

**Issue #3: Factory uses if-else chains** (Lines 19-28)
```java
public IPaymentStrategy createPaymentStrategyObject(String paymentMethod, String accountDetails) {
    if (paymentMethod.equalsIgnoreCase("upi")) {  // ❌ Must modify this method
        return new UPIPaymentStrategy(accountDetails);
    } else if (paymentMethod.equalsIgnoreCase("card")) {  // ❌ for every new payment type
        return new CardPaymentStrategy(accountDetails);
    } else {
        System.out.println("Wrong payment choice!!!");
        return null;
    }
}
```

**Why this matters:**
- Adding a new payment method (e.g., "wallet", "crypto") requires modifying this class
- Violates "open for extension, closed for modification"
- Can't add payment methods at runtime
- Testing new payment types requires changing factory code

**Suggested improvement:**
```java
public class PaymentStrategyFactory {
    // Use a map for extensibility
    private final Map<PaymentMethod, Function<String, PaymentStrategy>> creators;
    
    public PaymentStrategyFactory() {
        creators = new HashMap<>();
        // Register default strategies
        creators.put(PaymentMethod.UPI, UPIPaymentStrategy::new);
        creators.put(PaymentMethod.CARD, CardPaymentStrategy::new);
    }
    
    // Allow registration of new payment methods WITHOUT modifying the class
    public void registerPaymentMethod(PaymentMethod method, Function<String, PaymentStrategy> creator) {
        creators.put(method, creator);  // ✅ Open for extension
    }
    
    public PaymentStrategy createPaymentStrategy(PaymentMethod method, String details) {
        Function<String, PaymentStrategy> creator = creators.get(method);
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
        return creator.apply(details);
    }
}

// Now adding a new payment method is easy:
// factory.registerPaymentMethod(PaymentMethod.WALLET, WalletPaymentStrategy::new);
```

---

#### 📁 `InstantOrderFactory.java` & `ScheduledOrderFactory.java`

**Issue #4: Duplicate code in order factories** (Lines 18-29 in both files)
```java
// InstantOrderFactory.java
if (orderType.equalsIgnoreCase("delivery")) {
    DeliveryOrder deliveryOrder = new DeliveryOrder();
    deliveryOrder.setUserAddress(user.getAddress());
    order = deliveryOrder;
} else if (orderType.equalsIgnoreCase("pickup")) {
    PickupOrder pickupOrder = new PickupOrder();
    pickupOrder.setRestaurantAddress(restaurant.getAddress());
    order = pickupOrder;
}

// ScheduledOrderFactory.java - EXACT SAME CODE! ❌
if (orderType.equalsIgnoreCase("delivery")) {
    DeliveryOrder deliveryOrder = new DeliveryOrder();
    deliveryOrder.setUserAddress(user.getAddress());
    order = deliveryOrder;
} else if (orderType.equalsIgnoreCase("pickup")) {
    PickupOrder pickupOrder = new PickupOrder();
    pickupOrder.setRestaurantAddress(restaurant.getAddress());
    order = pickupOrder;
}
```

**Why this matters:**
- Code duplication leads to maintenance issues
- Bug fixes must be applied in multiple places
- Inconsistency risk when one copy is updated but not the other

**Suggested improvement:**
```java
// Extract common logic
public abstract class BaseOrderFactory implements FactoryOrder {
    protected Order createOrderByType(User user, String orderType) {
        // Common order type creation logic here
        // Only implemented once!
    }
}

public class InstantOrderFactory extends BaseOrderFactory {
    @Override
    public Order createOrder(User user, String orderType) {
        Order order = createOrderByType(user, orderType);  // ✅ Reuse
        order.setScheduledTime(TimeUtils.getCurrentTime());
        return order;
    }
}

public class ScheduledOrderFactory extends BaseOrderFactory {
    private final String scheduledTime;
    
    @Override
    public Order createOrder(User user, String orderType) {
        Order order = createOrderByType(user, orderType);  // ✅ Reuse
        order.setScheduledTime(this.scheduledTime);
        return order;
    }
}
```

---

### 1.3 Dependency Inversion Principle (DIP) Violations

#### 📁 `TomatoApp.java`

**Issue #5: Direct instantiation of concrete classes** (Lines 71, 75)
```java
public Order instantOrderCheckout(String orderType, String paymentMode, String accountDetails) {
    return checkout(orderType, paymentMode, accountDetails, new InstantOrderFactory());  // ❌ Tight coupling
}

public Order scheduledOrderCheckout(String orderType, String paymentMode, String accountDetails, String scheduledTime) {
    return checkout(orderType, paymentMode, accountDetails, new ScheduledOrderFactory(scheduledTime));  // ❌ Tight coupling
}
```

**Why this matters:**
- TomatoApp depends on concrete factory implementations
- Hard to test - can't mock the factories
- Can't swap implementations without changing code
- High-level module depends on low-level details

**Suggested improvement:**
```java
public class TomatoApp {
    private final OrderFactory orderFactory;  // ✅ Depend on abstraction
    
    public TomatoApp(User user, OrderFactory orderFactory) {  // ✅ Inject dependency
        this.user = user;
        this.orderFactory = orderFactory;
    }
    
    public Order checkout(OrderType type, PaymentMethod payment, String details) {
        return orderFactory.createOrder(user, type);  // ✅ Use injected factory
    }
}

// In main:
OrderFactory factory = new InstantOrderFactory();
TomatoApp app = new TomatoApp(user, factory);  // ✅ Dependency injection
```

---

## 2. Design Pattern Issues

### 2.1 Singleton Pattern Issues

#### 📁 `RestaurantManager.java` & `OrderManager.java`

**Issue #6: Not thread-safe** (Lines 9-22)
```java
public class RestaurantManager {
    private static RestaurantManager instance = null;  // ❌ Not thread-safe
    
    public static RestaurantManager getInstance() {
        if (instance == null) {  // ❌ Race condition possible
            instance = new RestaurantManager();
        }
        return instance;
    }
}
```

**Why this matters:**
- In multi-threaded environment, multiple instances could be created
- Race condition when two threads check `if (instance == null)` simultaneously
- Can lead to subtle bugs in production

**Suggested improvement:**
```java
// Option 1: Eager initialization (thread-safe)
public class RestaurantManager {
    private static final RestaurantManager instance = new RestaurantManager();
    
    public static RestaurantManager getInstance() {
        return instance;
    }
}

// Option 2: Double-checked locking (lazy + thread-safe)
public class RestaurantManager {
    private static volatile RestaurantManager instance;
    
    public static RestaurantManager getInstance() {
        if (instance == null) {
            synchronized (RestaurantManager.class) {
                if (instance == null) {
                    instance = new RestaurantManager();
                }
            }
        }
        return instance;
    }
}

// Option 3: Better approach - avoid singleton entirely
public class RestaurantService {
    // Regular class, create as needed, inject where required
    // Much easier to test!
}
```

---

#### 📁 `FactoryPaymentStrategy.java`

**Issue #7: Unnecessary singleton** (Lines 8-17)
```java
public class FactoryPaymentStrategy {
    private static FactoryPaymentStrategy instance = null;  // ❌ Why singleton?
    
    public static FactoryPaymentStrategy getInstance() {
        if (instance == null) {
            instance = new FactoryPaymentStrategy();
        }
        return instance;
    }
}
```

**Why this matters:**
- Factory has no state, doesn't need to be singleton
- Singleton makes testing harder (global state)
- Can't have different factory configurations
- Overused pattern - not everything needs to be a singleton!

**Suggested improvement:**
```java
// Just make it a regular class
public class PaymentStrategyFactory {
    // No singleton - create instances as needed
    public PaymentStrategy createPaymentStrategy(PaymentMethod method, String details) {
        // Factory logic
    }
}

// Or make methods static if truly stateless
public class PaymentStrategyFactory {
    private PaymentStrategyFactory() {}  // Prevent instantiation
    
    public static PaymentStrategy createPaymentStrategy(PaymentMethod method, String details) {
        // Factory logic
    }
}
```

---

### 2.2 Strategy Pattern Issues

#### 📁 `NotificationService.java`

**Issue #8: Static notification service not extensible** (Lines 8-28)
```java
public class NotificationService {
    public static void notify(Order order) {  // ❌ Static - can't extend or configure
        System.out.println("\nNotification: New " + order.getType() + " order placed!");
        // ... hardcoded console output
    }
}
```

**Why this matters:**
- Can't add different notification channels (SMS, Email, Push)
- Hardcoded to console output
- Can't configure notification preferences
- Can't test without seeing console output
- Not following Strategy pattern properly

**Suggested improvement:**
```java
// Define notification strategy interface
public interface NotificationStrategy {
    void sendNotification(Order order);
}

// Multiple implementations
public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(Order order) {
        // Send email
    }
}

public class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(Order order) {
        // Send SMS
    }
}

// Notification service that can use multiple strategies
public class NotificationService {
    private final List<NotificationStrategy> strategies = new ArrayList<>();
    
    public void addStrategy(NotificationStrategy strategy) {
        strategies.add(strategy);
    }
    
    public void notifyOrderPlaced(Order order) {
        for (NotificationStrategy strategy : strategies) {
            strategy.sendNotification(order);
        }
    }
}

// Usage:
NotificationService service = new NotificationService();
service.addStrategy(new EmailNotificationStrategy());
service.addStrategy(new SMSNotificationStrategy());
service.notifyOrderPlaced(order);  // ✅ Sends via both channels
```

---

## 3. Code Quality Issues

### 3.1 Naming Conventions

#### 📁 `RestaurantManager.java`

**Issue #9: Inconsistent method naming** (Line 28)
```java
public List<Restaurant> SearchByLocation(String location) {  // ❌ Should be lowercase
    // ...
}
```

**Why this matters:**
- Java convention: methods start with lowercase
- Inconsistent with rest of codebase
- Looks like a class name

**Fix:**
```java
public List<Restaurant> searchByLocation(String location) {  // ✅ Correct
    // ...
}
```

---

### 3.2 Region Comments

#### 📁 `Order.java`

**Issue #10: Unnecessary region comments** (Lines 47-107)
```java
//region orderId
public int getOrderId() {
    return orderId;
}
//endregion

//region user
public User getUser() {
    return user;
}
//endregion
```

**Why this matters:**
- Clutters the code
- Not a Java convention (more of a C# thing)
- IDE features make these unnecessary
- Makes code harder to read

**Suggested improvement:**
```java
// Just remove them - clean Java doesn't need regions
public int getOrderId() {
    return orderId;
}

public User getUser() {
    return user;
}
```

---

### 3.3 Static ID Generation

#### 📁 `Restaurant.java` & `Order.java`

**Issue #11: Static ID generation not thread-safe** (Lines 7-8, 12-13)
```java
public class Restaurant {
    private static int nextRestaurantID = 101;  // ❌ Not thread-safe
    private final int id;
    
    public Restaurant(String name, String address) {
        this.id = nextRestaurantID;
        nextRestaurantID += 1;  // ❌ Race condition
    }
}
```

**Why this matters:**
- Two threads could get the same ID
- Not suitable for production systems
- IDs reset when application restarts
- No way to ensure uniqueness across instances

**Suggested improvement:**
```java
import java.util.UUID;

public class Restaurant {
    private final String id;
    
    public Restaurant(String id, String name, String address) {
        this.id = id;  // ✅ ID passed from outside
    }
    
    public String getId() {
        return id;
    }
}

// Or use UUID generator
public class Restaurant {
    private final String id;
    
    public Restaurant(String name, String address) {
        this.id = UUID.randomUUID().toString();  // ✅ Thread-safe, globally unique
    }
}
```

---

### 3.4 Missing Validation

#### 📁 `MenuItem.java`

**Issue #12: No validation in constructor** (Lines 8-12)
```java
public MenuItem(String code, String name, double price) {
    this.code = code;  // ❌ What if code is null or empty?
    this.name = name;  // ❌ What if name is null?
    this.price = price;  // ❌ What if price is negative?
}
```

**Why this matters:**
- Invalid data can cause runtime errors later
- Hard to debug when invalid data flows through system
- No clear contract about what's valid

**Suggested improvement:**
```java
public MenuItem(String code, String name, double price) {
    if (code == null || code.isEmpty()) {
        throw new IllegalArgumentException("MenuItem code cannot be null or empty");
    }
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("MenuItem name cannot be null or empty");
    }
    if (price < 0) {
        throw new IllegalArgumentException("MenuItem price cannot be negative");
    }
    this.code = code;
    this.name = name;
    this.price = price;
}
```

---

### 3.5 String Literals for Types

#### 📁 Multiple files

**Issue #13: Using string literals instead of enums**
```java
// In TomatoApp.java
if (orderType.equalsIgnoreCase("delivery")) {  // ❌ Typo-prone
    // ...
} else if (orderType.equalsIgnoreCase("pickup")) {
    // ...
}

// In FactoryPaymentStrategy.java
if (paymentMethod.equalsIgnoreCase("upi")) {  // ❌ Typo-prone
    // ...
}
```

**Why this matters:**
- Typos only caught at runtime ("delivrey" vs "delivery")
- No IDE autocomplete
- Hard to find all usages
- Magic strings scattered throughout code

**Suggested improvement:**
```java
// Define enums
public enum OrderType {
    DELIVERY,
    PICKUP
}

public enum PaymentMethod {
    UPI,
    CARD,
    WALLET
}

// Use in code
if (orderType == OrderType.DELIVERY) {  // ✅ Type-safe
    // ...
}
```

---

## 4. Security & Best Practices

#### 📁 `UPIPaymentStrategy.java` & `CardPaymentStrategy.java`

**Issue #14: Logging sensitive payment information** (Lines 11-13)
```java
public void processPayment(double amount) {
    System.out.println("Payment for Rs." + amount + " from " + mobileNumber + " via UPI");  // ❌ Logs full mobile number
}

public void processPayment(double amount) {
    System.out.println("Payment for Rs." + amount + " from " + cardNumber + " via Card");  // ❌ Logs full card number
}
```

**Why this matters:**
- Security risk - logs may be stored/transmitted
- PCI compliance violation for card numbers
- Privacy concerns for mobile numbers
- Production systems should never log full payment details

**Suggested improvement:**
```java
public void processPayment(double amount) {
    String masked = maskMobileNumber(mobileNumber);
    System.out.println("Payment for Rs." + amount + " from " + masked + " via UPI");  // ✅ Masked
}

private String maskMobileNumber(String number) {
    if (number.length() < 4) return "****";
    return "***" + number.substring(number.length() - 4);  // Show only last 4 digits
}
```

---

## Summary of Key Improvements Needed

| Category | Current State | Suggested Improvement |
|----------|--------------|----------------------|
| **SRP** | TomatoApp has 7 responsibilities | Split into service classes |
| **OCP** | If-else chains in factories | Map-based extensible factories |
| **DIP** | Direct instantiation of concrete classes | Dependency injection |
| **Singleton** | Not thread-safe | Use proper patterns or avoid |
| **Strategy** | Static notification service | Proper strategy pattern with multiple implementations |
| **Type Safety** | String literals everywhere | Use enums |
| **Security** | Full payment details logged | Mask sensitive data |
| **Validation** | Missing input validation | Add constructor validation |
| **ID Generation** | Static counters (not thread-safe) | Use UUID or external ID service |

---

## Next Steps for Learning

These issues are great learning opportunities! Would you like to:

1. **Deep dive into one principle** (e.g., spend time understanding SRP with examples)?
2. **Refactor one class together** step by step?
3. **Compare V01 vs V02** implementations side by side for a specific pattern?
4. **Discuss when to use** certain patterns (Singleton, Factory, Strategy)?

Let me know which area you'd like to explore first, and we can work through it interactively!
