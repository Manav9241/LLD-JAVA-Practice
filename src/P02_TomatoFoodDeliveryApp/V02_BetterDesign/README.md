# V02_BetterDesign - Tomato Food Delivery App

## Overview
Improved implementation of the food delivery application with better adherence to SOLID principles and cleaner design patterns.

## UML Class Diagram

```mermaid
classDiagram
    %% Main Application
    class TomatoMain {
        +main(String[] args)$ void
        -initializeRestaurants(RestaurantService restaurantService)$ void
    }
    
    class TomatoFoodDeliveryApp {
        -RestaurantService restaurantService
        -OrderService orderService
        -NotificationService notificationService
        -PaymentStrategyFactory paymentStrategyFactory
        +TomatoFoodDeliveryApp()
        +getRestaurantService() RestaurantService
        +getOrderService() OrderService
        +getNotificationService() NotificationService
        +searchRestaurantsByLocation(String location) List~Restaurant~
        +addItemToCart(User user, Restaurant restaurant, String itemId) void
        +createOrder(User user, OrderType orderType, LocalDateTime scheduledTime) Order
        +processOrderPayment(Order order, PaymentMethod paymentMethod, String paymentDetails) boolean
        +displayCart(User user) void
        +displayOrders() void
    }
    
    %% Enums
    class OrderType {
        <<enumeration>>
        DELIVERY
        PICKUP
    }
    
    class PaymentMethod {
        <<enumeration>>
        UPI
        CARD
        WALLET
    }
    
    class OrderStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        PREPARING
        OUT_FOR_DELIVERY
        DELIVERED
        CANCELLED
    }
    
    %% Model Classes
    class User {
        -String userId
        -String name
        -String location
        -Cart cart
        +User(String userId, String name, String location)
        +getUserId() String
        +getName() String
        +getLocation() String
        +setLocation(String location) void
        +getCart() Cart
        +equals(Object o) boolean
        +hashCode() int
        +toString() String
    }
    
    class Cart {
        -Restaurant restaurant
        -Map~String, Integer~ itemQuantities
        -Map~String, MenuItem~ items
        +Cart()
        +setRestaurant(Restaurant restaurant) void
        +getRestaurant() Restaurant
        +addItem(MenuItem item) void
        +removeItem(String itemId) void
        +getItems() List~MenuItem~
        +getItemsWithQuantity() Map~MenuItem, Integer~
        +getTotalCost() double
        +isEmpty() boolean
        +clearCart() void
        +getItemCount() int
    }
    
    class Restaurant {
        -String id
        -String name
        -String location
        -Map~String, MenuItem~ menu
        +Restaurant(String id, String name, String location)
        +getId() String
        +getName() String
        +getLocation() String
        +addMenuItem(MenuItem item) void
        +getMenuItem(String itemId) MenuItem
        +getMenu() List~MenuItem~
        +equals(Object o) boolean
        +hashCode() int
        +toString() String
    }
    
    class MenuItem {
        -String id
        -String name
        -double price
        -String description
        +MenuItem(String id, String name, double price, String description)
        +getId() String
        +getName() String
        +getPrice() double
        +getDescription() String
        +equals(Object o) boolean
        +hashCode() int
        +toString() String
    }
    
    class Order {
        -String orderId
        -User user
        -Restaurant restaurant
        -List~MenuItem~ items
        -double totalAmount
        -OrderType orderType
        -LocalDateTime orderTime
        -LocalDateTime scheduledTime
        -OrderStatus status
        -PaymentStrategy paymentStrategy
        -Order(Builder builder)
        +getOrderId() String
        +getUser() User
        +getRestaurant() Restaurant
        +getItems() List~MenuItem~
        +getTotalAmount() double
        +getOrderType() OrderType
        +getOrderTime() LocalDateTime
        +getScheduledTime() LocalDateTime
        +getStatus() OrderStatus
        +setStatus(OrderStatus status) void
        +getPaymentStrategy() PaymentStrategy
        +setPaymentStrategy(PaymentStrategy paymentStrategy) void
        +processPayment() boolean
        +toString() String
    }
    
    class Builder {
        -User user
        -Restaurant restaurant
        -List~MenuItem~ items
        -double totalAmount
        -OrderType orderType
        -LocalDateTime scheduledTime
        -PaymentStrategy paymentStrategy
        +user(User user) Builder
        +restaurant(Restaurant restaurant) Builder
        +items(List~MenuItem~ items) Builder
        +orderType(OrderType orderType) Builder
        +scheduledTime(LocalDateTime scheduledTime) Builder
        +paymentStrategy(PaymentStrategy paymentStrategy) Builder
        +build() Order
    }
    
    %% Service Classes
    class RestaurantService {
        -List~Restaurant~ restaurants
        +RestaurantService()
        +addRestaurant(Restaurant restaurant) void
        +getRestaurantById(String id) Restaurant
        +searchByLocation(String location) List~Restaurant~
        +getAllRestaurants() List~Restaurant~
    }
    
    class OrderService {
        -List~Order~ orders
        +OrderService()
        +placeOrder(Order order) void
        +getOrderById(String orderId) Order
        +getOrdersByUserId(String userId) List~Order~
        +getAllOrders() List~Order~
        +updateOrderStatus(String orderId, OrderStatus status) void
    }
    
    class NotificationService {
        -List~NotificationStrategy~ notificationStrategies
        +NotificationService()
        +addNotificationStrategy(NotificationStrategy strategy) void
        +removeNotificationStrategy(NotificationStrategy strategy) void
        +notifyOrderPlaced(Order order) void
    }
    
    %% Factory Pattern
    class PaymentStrategyFactory {
        -Map~PaymentMethod, Function~ strategyCreators
        +PaymentStrategyFactory()
        +createPaymentStrategy(PaymentMethod method, String paymentDetails) PaymentStrategy
        +registerPaymentMethod(PaymentMethod method, Function creator) void
    }
    
    %% Strategy Pattern - Payment
    class PaymentStrategy {
        <<interface>>
        +pay(double amount)* boolean
        +getPaymentMethod()* String
    }
    
    class UPIPaymentStrategy {
        -String upiId
        +UPIPaymentStrategy(String upiId)
        +pay(double amount) boolean
        +getPaymentMethod() String
    }
    
    class CardPaymentStrategy {
        -String cardNumber
        +CardPaymentStrategy(String cardNumber)
        +pay(double amount) boolean
        +getPaymentMethod() String
        -maskCardNumber() String
    }
    
    class WalletPaymentStrategy {
        -String walletId
        +WalletPaymentStrategy(String walletId)
        +pay(double amount) boolean
        +getPaymentMethod() String
    }
    
    %% Strategy Pattern - Notification
    class NotificationStrategy {
        <<interface>>
        +sendNotification(Order order)* void
    }
    
    class EmailNotificationStrategy {
        +sendNotification(Order order) void
    }
    
    class SMSNotificationStrategy {
        +sendNotification(Order order) void
    }
    
    %% Relationships
    TomatoMain --> TomatoFoodDeliveryApp : uses
    TomatoMain --> User : creates
    
    TomatoFoodDeliveryApp --> RestaurantService : has
    TomatoFoodDeliveryApp --> OrderService : has
    TomatoFoodDeliveryApp --> NotificationService : has
    TomatoFoodDeliveryApp --> PaymentStrategyFactory : has
    TomatoFoodDeliveryApp --> User : uses
    TomatoFoodDeliveryApp --> Restaurant : uses
    TomatoFoodDeliveryApp --> Order : creates
    
    User --> Cart : has
    Cart --> Restaurant : references
    Cart --> MenuItem : contains
    
    Restaurant --> MenuItem : contains
    
    Order --> User : has
    Order --> Restaurant : has
    Order --> MenuItem : contains
    Order --> OrderType : uses
    Order --> OrderStatus : uses
    Order --> PaymentStrategy : uses
    Order +-- Builder : contains
    Builder --> Order : builds
    
    RestaurantService --> Restaurant : manages
    OrderService --> Order : manages
    NotificationService --> NotificationStrategy : uses
    
    PaymentStrategyFactory --> PaymentMethod : uses
    PaymentStrategyFactory --> PaymentStrategy : creates
    PaymentStrategyFactory --> UPIPaymentStrategy : creates
    PaymentStrategyFactory --> CardPaymentStrategy : creates
    PaymentStrategyFactory --> WalletPaymentStrategy : creates
    
    UPIPaymentStrategy ..|> PaymentStrategy : implements
    CardPaymentStrategy ..|> PaymentStrategy : implements
    WalletPaymentStrategy ..|> PaymentStrategy : implements
    
    EmailNotificationStrategy ..|> NotificationStrategy : implements
    SMSNotificationStrategy ..|> NotificationStrategy : implements
```

## Key Architectural Improvements vs V01

### 1. **Enhanced Separation of Concerns**
- **Service Layer**: V01 uses `TomatoApp` as an orchestration class (Facade pattern), which is valid. V02 further separates business logic into dedicated service classes (`RestaurantService`, `OrderService`, `NotificationService`) for better modularity and testability
- **Manager vs Service**: V01 uses Singleton managers for data storage. V02 uses non-singleton services with dependency injection for better testability and flexibility
- **Each class has a single, well-defined purpose**

### 2. **SOLID Principles Enhancements**
- **Single Responsibility Principle (SRP)**:
  - V01: `TomatoApp` orchestrates multiple workflows (good), but also handles initialization
  - V02: Further granular separation with dedicated services for each domain concept

- **Open/Closed Principle (OCP)**:
  - V01: `FactoryPaymentStrategy` uses if-else chains requiring modification for new payment types
  - V02: Map-based factory allows runtime registration via `registerPaymentMethod()` without code modification
  - Notification system supports multiple strategies without code changes

- **Dependency Inversion Principle (DIP)**:
  - V01: Direct instantiation of concrete factories in `TomatoApp`
  - V02: Dependency injection pattern with service abstractions
  - No bloated interfaces forcing unnecessary implementations

- **Dependency Inversion Principle (DIP)**:
  - V01: Direct instantiation of concrete factories in `TomatoApp`
  - V02: Dependency injection pattern with service abstractions

### 3. **Design Pattern Improvements**
- **Factory Pattern**: 
  - V01: Uses singleton factory with if-else chains
  - V02: Map-based factory with functional approach allows runtime registration of new payment methods
- **Strategy Pattern**: 
  - V01: Good implementation for payment strategies
  - V02: Extended to include notification strategies (Email, SMS) for multi-channel notifications
- **Builder Pattern**: Added for Order creation, providing fluent API and enforcing required fields
- **Singleton Pattern**:
  - V01: Uses basic singleton (not thread-safe)
  - V02: Removed singletons in favor of regular services for better testability

### 4. **Enhanced Type Safety**
- V01: Uses string literals for order types, payment methods
- V02: Enums for `OrderType`, `PaymentMethod`, and `OrderStatus` prevent typos and runtime errors

### 5. **Improved Data Structures**
- V01: Cart uses List (O(n) operations)
- V02: Cart uses Map for O(1) item lookup and quantity tracking
- V02: Restaurant menu uses Map for efficient item retrieval

### 6. **Better Encapsulation**
- V01: Some direct field access patterns
- V02: Defensive copying in getters, immutable fields where appropriate (using `final`)
- V02: No static mutable state (removed static ID generation)

### 7. **Extensibility**
- V01: Adding new payment methods requires modifying factory code (violates OCP)
- V02: Easy to add new payment methods via `registerPaymentMethod()` without code modification
- V02: Easy to add new notification channels via strategy pattern

### 8. **Security Enhancements**
- V01: Payment details logged in plain text
- V02: Sensitive data masking for payment information (card numbers, UPI IDs, wallet IDs)

### 9. **Code Quality**
- Proper exception handling with meaningful error messages
- Override `equals()`, `hashCode()`, and `toString()` for model classes
- Consistent naming conventions
- Better comments and documentation

## Why This Design is Better

1. **Maintainability**: Changes to one component don't ripple through the system
2. **Testability**: Service layer can be easily mocked for unit testing
3. **Scalability**: New features can be added without modifying existing code
4. **Readability**: Clear separation of concerns makes code easier to understand
5. **Robustness**: Comprehensive validation and proper exception handling
6. **Flexibility**: Strategy and factory patterns allow runtime behavior changes
