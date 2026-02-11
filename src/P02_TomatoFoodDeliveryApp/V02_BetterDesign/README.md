# V02_BetterDesign - Tomato Food Delivery App

## Overview
Improved implementation of the food delivery application with better adherence to SOLID principles and cleaner design patterns.

## UML Class Diagram

```
┌────────────────────────────────────────────────────────────────────────────┐
│                         MAIN APPLICATION LAYER                              │
└────────────────────────────────────────────────────────────────────────────┘

    TomatoMain
        └── uses ──> TomatoFoodDeliveryApp
                        │
                        ├── has ──> RestaurantService
                        ├── has ──> OrderService
                        ├── has ──> NotificationService
                        └── has ──> PaymentStrategyFactory

┌────────────────────────────────────────────────────────────────────────────┐
│                            MODEL CLASSES                                    │
└────────────────────────────────────────────────────────────────────────────┘

    User
        └── has ──> Cart
                      ├── references ──> Restaurant
                      └── contains ──> MenuItem*

    Restaurant
        └── contains ──> MenuItem*

    Order
        ├── has ──> User
        ├── has ──> Restaurant
        ├── contains ──> MenuItem*
        ├── uses ──> OrderType (enum)
        ├── uses ──> OrderStatus (enum)
        ├── uses ──> PaymentStrategy
        └── contains ──> Builder (inner class)

┌────────────────────────────────────────────────────────────────────────────┐
│                            ENUMERATIONS                                     │
└────────────────────────────────────────────────────────────────────────────┘

    OrderType (enum): DELIVERY, PICKUP
    PaymentMethod (enum): UPI, CARD, WALLET
    OrderStatus (enum): PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, 
                        DELIVERED, CANCELLED

┌────────────────────────────────────────────────────────────────────────────┐
│                            SERVICE LAYER                                    │
└────────────────────────────────────────────────────────────────────────────┘

    RestaurantService
        └── manages ──> Restaurant*

    OrderService
        └── manages ──> Order*

    NotificationService
        └── uses ──> NotificationStrategy*
                          │
                          ├──implements──> EmailNotificationStrategy
                          └──implements──> SMSNotificationStrategy

┌────────────────────────────────────────────────────────────────────────────┐
│                   FACTORY PATTERN - PAYMENT STRATEGY                        │
└────────────────────────────────────────────────────────────────────────────┘

    PaymentStrategyFactory
        ├── uses ──> PaymentMethod (enum)
        └── creates ──> PaymentStrategy
                          │
                          ├──implements──> UPIPaymentStrategy
                          ├──implements──> CardPaymentStrategy
                          └──implements──> WalletPaymentStrategy

┌────────────────────────────────────────────────────────────────────────────┐
│                       STRATEGY PATTERNS SUMMARY                             │
└────────────────────────────────────────────────────────────────────────────┘

    Payment Strategy Pattern:
        <<interface>> PaymentStrategy
            └── implementations: UPI, Card, Wallet

    Notification Strategy Pattern:
        <<interface>> NotificationStrategy
            └── implementations: Email, SMS
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
