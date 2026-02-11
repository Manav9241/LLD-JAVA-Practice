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

### 1. **SOLID Principles Compliance**
- **Single Responsibility Principle (SRP)**:
  - Separated concerns into dedicated service classes (`RestaurantService`, `OrderService`, `NotificationService`)
  - Main app class (`TomatoFoodDeliveryApp`) acts as a facade, delegating to services
  - Each class has a single, well-defined purpose

- **Open/Closed Principle (OCP)**:
  - `PaymentStrategyFactory` uses a map-based approach for easy extension without modification
  - New payment methods can be added via `registerPaymentMethod()`
  - Notification system supports multiple strategies without code changes

- **Liskov Substitution Principle (LSP)**:
  - All strategy implementations are proper substitutes for their interfaces
  - No inheritance hierarchies that violate LSP

- **Interface Segregation Principle (ISP)**:
  - Small, focused interfaces (`PaymentStrategy`, `NotificationStrategy`)
  - No bloated interfaces forcing unnecessary implementations

- **Dependency Inversion Principle (DIP)**:
  - High-level modules depend on abstractions (interfaces)
  - `TomatoFoodDeliveryApp` depends on service abstractions, not concrete implementations

### 2. **Design Pattern Improvements**
- **Factory Pattern**: Map-based factory with functional approach allows runtime registration of new payment methods
- **Strategy Pattern**: Cleaner implementation with proper interfaces for both payment and notification
- **Builder Pattern**: Added for Order creation, providing fluent API and enforcing required fields
- **Facade Pattern**: `TomatoFoodDeliveryApp` acts as a facade, simplifying the complex subsystem

### 3. **Better Separation of Concerns**
- Clear distinction between model, service, strategy, factory, and enums packages
- Business logic moved from models to services
- Validation centralized in model constructors

### 4. **Enhanced Type Safety**
- Enums for `OrderType`, `PaymentMethod`, and `OrderStatus` instead of strings
- Compile-time type checking prevents runtime errors

### 5. **Improved Data Structures**
- Cart uses `Map` for O(1) item lookup and quantity tracking
- Restaurant menu uses `Map` for efficient item retrieval
- Immutable fields where appropriate (using `final`)

### 6. **Better Encapsulation**
- Defensive copying in getters (e.g., `getItems()` returns new list)
- No static mutable state (removed static ID generation)
- Proper validation in setters and constructors

### 7. **Extensibility**
- Easy to add new payment methods via factory registration
- Easy to add new notification channels
- Service-based architecture allows for easy mocking and testing

### 8. **Code Quality**
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
