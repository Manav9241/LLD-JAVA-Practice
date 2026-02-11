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

```mermaid
classDiagram
    %% Main Application Classes
    class TomatoMain {
        +main(String[] args)$ void
    }
    
    class TomatoApp {
        -User user
        +TomatoApp(User user)
        +initializeRestaurants() void
        +searchByLocation() List~Restaurant~
        +selectRestaurant(Restaurant r) void
        +addToCart(String itemCode) void
        +instantOrderCheckout(String orderType, String paymentMode, String accountDetails) Order
        +scheduledOrderCheckout(String orderType, String paymentMode, String accountDetails, String scheduledTime) Order
        -checkout(String orderType, String paymentMode, String accountDetails, FactoryOrder orderFactory) Order
        +payForOrder(Order order) void
        +printUserCart() void
    }
    
    %% Model Classes
    class User {
        -int userId
        -String name
        -String address
        -Cart cart
        +User(int userId, String name, String address)
        +getUserId() int
        +getName() String
        +getAddress() String
        +setAddress(String address) void
        +getCart() Cart
    }
    
    class Cart {
        -Restaurant restaurant
        -List~MenuItem~ items
        +Cart()
        +setRestaurant(Restaurant restaurant) void
        +getRestaurant() Restaurant
        +addToCart(MenuItem item) void
        +getCartItems() List~MenuItem~
        +getTotalCost() double
        +isEmpty() boolean
        +clearCart() void
    }
    
    class Restaurant {
        -int id$
        -int nextRestaurantID$
        -String name
        -String address
        -List~MenuItem~ menu
        +Restaurant(String name, String address)
        +getID() int
        +getName() String
        +getAddress() String
        +addMenuItem(MenuItem menuItem) void
        +getMenu() List~MenuItem~
    }
    
    class MenuItem {
        -String code
        -String name
        -double price
        +MenuItem(String code, String name, double price)
        +getCode() String
        +getName() String
        +setPrice(double price) void
        +getPrice() double
    }
    
    %% Order Classes
    class Order {
        <<abstract>>
        -int orderId$
        -int nextOrderID$
        -User user
        -Restaurant restaurant
        -List~MenuItem~ items
        -double totalAmount
        -IPaymentStrategy paymentStrategy
        -String scheduledTime
        +Order()
        +getType()* String
        +processPayment() boolean
        +getOrderId() int
        +getUser() User
        +setUser(User u) void
        +getRestaurant() Restaurant
        +setRestaurant(Restaurant res) void
        +getItems() List~MenuItem~
        +setItems(List~MenuItem~ items) void
        +getTotalAmount() double
        +setPaymentStrategy(IPaymentStrategy ps) void
        +getScheduledTime() String
        +setScheduledTime(String time) void
    }
    
    class DeliveryOrder {
        -String userAddress
        +getType() String
        +getUserAddress() String
        +setUserAddress(String address) void
    }
    
    class PickupOrder {
        -String restaurantAddress
        +getType() String
        +getRestaurantAddress() String
        +setRestaurantAddress(String address) void
    }
    
    %% Manager Classes (Singleton)
    class RestaurantManager {
        -RestaurantManager instance$
        -List~Restaurant~ restaurants
        -RestaurantManager()
        +getInstance()$ RestaurantManager
        +addRestaurant(Restaurant res) void
        +searchByLocation(String location) List~Restaurant~
    }
    
    class OrderManager {
        -OrderManager instance$
        -List~Order~ orders
        -OrderManager()
        +getInstance()$ OrderManager
        +addOrder(Order order) void
        +listOrders() void
    }
    
    %% Factory Pattern
    class FactoryPaymentStrategy {
        <<singleton>>
        -FactoryPaymentStrategy instance$
        -FactoryPaymentStrategy()
        +getInstance()$ FactoryPaymentStrategy
        +createPaymentStrategyObject(String paymentMethod, String accountDetails) IPaymentStrategy
    }
    
    class FactoryOrder {
        <<interface>>
        +createOrder(User user, String orderType)* Order
    }
    
    class InstantOrderFactory {
        +createOrder(User user, String orderType) Order
    }
    
    class ScheduledOrderFactory {
        -String scheduledTime
        +ScheduledOrderFactory(String scheduledTime)
        +createOrder(User user, String orderType) Order
    }
    
    %% Strategy Pattern - Payment
    class IPaymentStrategy {
        <<interface>>
        +processPayment(double amount)* void
    }
    
    class CardPaymentStrategy {
        -String cardNumber
        +CardPaymentStrategy(String cardNumber)
        +processPayment(double amount) void
    }
    
    class UPIPaymentStrategy {
        -String mobileNumber
        +UPIPaymentStrategy(String mobileNumber)
        +processPayment(double amount) void
    }
    
    %% Notification Service
    class NotificationService {
        +notify(Order order)$ void
    }
    
    %% Utilities
    class TimeUtils {
        +getCurrentTime()$ String
    }
    
    %% Relationships
    TomatoMain --> TomatoApp : uses
    TomatoMain --> User : creates
    
    TomatoApp --> User : has
    TomatoApp --> RestaurantManager : uses
    TomatoApp --> OrderManager : uses
    TomatoApp --> FactoryPaymentStrategy : uses
    TomatoApp --> FactoryOrder : uses
    TomatoApp --> InstantOrderFactory : creates
    TomatoApp --> ScheduledOrderFactory : creates
    TomatoApp --> NotificationService : uses
    
    User --> Cart : has
    Cart --> Restaurant : references
    Cart --> MenuItem : contains
    
    Restaurant --> MenuItem : contains
    
    Order --> User : has
    Order --> Restaurant : has
    Order --> MenuItem : contains
    Order --> IPaymentStrategy : uses
    
    DeliveryOrder --|> Order : extends
    PickupOrder --|> Order : extends
    
    RestaurantManager --> Restaurant : manages
    OrderManager --> Order : manages
    
    FactoryPaymentStrategy --> IPaymentStrategy : creates
    FactoryPaymentStrategy --> CardPaymentStrategy : creates
    FactoryPaymentStrategy --> UPIPaymentStrategy : creates
    
    CardPaymentStrategy ..|> IPaymentStrategy : implements
    UPIPaymentStrategy ..|> IPaymentStrategy : implements
    
    InstantOrderFactory ..|> FactoryOrder : implements
    ScheduledOrderFactory ..|> FactoryOrder : implements
    
    InstantOrderFactory --> Order : creates
    InstantOrderFactory --> DeliveryOrder : creates
    InstantOrderFactory --> PickupOrder : creates
    InstantOrderFactory --> TimeUtils : uses
    
    ScheduledOrderFactory --> Order : creates
    ScheduledOrderFactory --> DeliveryOrder : creates
    ScheduledOrderFactory --> PickupOrder : creates
    
    NotificationService --> Order : notifies about
```

## Key Features
- User can search for restaurants by location
- Shopping cart functionality with item management
- Support for both instant and scheduled orders
- Multiple order types: Delivery and Pickup
- Multiple payment strategies: UPI and Card
- Order notification system
- Centralized restaurant and order management
