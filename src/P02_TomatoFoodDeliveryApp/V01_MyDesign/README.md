# V01_MyDesign - Tomato Food Delivery App

## Overview
Initial implementation of a food delivery application similar to Zomato, demonstrating core OOP principles and design patterns.

## Architecture
- **TomatoMain**: Client class that interacts with the application
- **TomatoApp**: Orchestration class (Facade pattern) that coordinates the flow of all functionalities
- **Manager Classes**: Singleton managers for restaurant and order data management
- **Factory Pattern**: Used for creating payment strategies and orders
- **Strategy Pattern**: Used for payment processing

## UML Class Diagram

```
┌────────────────────────────────────────────────────────────────────────────┐
│                         MAIN APPLICATION LAYER                              │
└────────────────────────────────────────────────────────────────────────────┘

    TomatoMain
        └── uses ──> TomatoApp (Orchestration/Facade)
                        │
                        ├── has ──> User
                        │
                        └── uses ──> RestaurantManager (Singleton)
                        └── uses ──> OrderManager (Singleton)
                        └── uses ──> FactoryPaymentStrategy (Singleton)
                        └── uses ──> NotificationService

┌────────────────────────────────────────────────────────────────────────────┐
│                            MODEL CLASSES                                    │
└────────────────────────────────────────────────────────────────────────────┘

    User
        └── has ──> Cart
                      ├── references ──> Restaurant
                      └── contains ──> MenuItem*

    Restaurant
        └── contains ──> MenuItem*

┌────────────────────────────────────────────────────────────────────────────┐
│                         ORDER HIERARCHY                                     │
└────────────────────────────────────────────────────────────────────────────┘

    <<abstract>> Order
        ├── has ──> User
        ├── has ──> Restaurant
        ├── contains ──> MenuItem*
        └── uses ──> IPaymentStrategy
        
    DeliveryOrder ────extends───> Order
    PickupOrder   ────extends───> Order

┌────────────────────────────────────────────────────────────────────────────┐
│                     FACTORY PATTERN - ORDER CREATION                        │
└────────────────────────────────────────────────────────────────────────────┘

    <<interface>> FactoryOrder
        │
        ├──implements──> InstantOrderFactory
        │                   └── creates ──> Order (Delivery/Pickup)
        │
        └──implements──> ScheduledOrderFactory
                            └── creates ──> Order (Delivery/Pickup)

┌────────────────────────────────────────────────────────────────────────────┐
│                   FACTORY PATTERN - PAYMENT STRATEGY                        │
└────────────────────────────────────────────────────────────────────────────┘

    FactoryPaymentStrategy (Singleton)
        └── creates ──> IPaymentStrategy
                          │
                          ├──implements──> CardPaymentStrategy
                          └──implements──> UPIPaymentStrategy

┌────────────────────────────────────────────────────────────────────────────┐
│                      MANAGER CLASSES (SINGLETON)                            │
└────────────────────────────────────────────────────────────────────────────┘

    RestaurantManager
        └── manages ──> Restaurant*

    OrderManager
        └── manages ──> Order*

┌────────────────────────────────────────────────────────────────────────────┐
│                         UTILITY SERVICES                                    │
└────────────────────────────────────────────────────────────────────────────┘

    NotificationService
        └── notifies about ──> Order

    TimeUtils
        └── provides current time utilities
```

## Key Features
- User can search for restaurants by location
- Shopping cart functionality with item management
- Support for both instant and scheduled orders
- Multiple order types: Delivery and Pickup
- Multiple payment strategies: UPI and Card
- Order notification system
- Centralized restaurant and order management
