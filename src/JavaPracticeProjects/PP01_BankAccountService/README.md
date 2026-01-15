# Bank Account System - Clean Architecture Practice

## 📋 Overview
A Bank Account application demonstrating clean separation of concerns between **data holders**, **behavior classes**, and **service classes**. This project showcases proper layering and responsibility distribution in object-oriented design.

## 🎯 Purpose
Practice the distinction between:
- **Entities** (Domain logic & business rules)
- **DTOs** (Data transfer objects)
- **Services** (Operation coordination)
- **Repositories** (Data persistence)

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│          Main (Entry Point / Controller)        │
│  - Orchestrates setup                           │
│  - Creates DTOs from input                      │
└──────────────────┬──────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────┐
│           BankAccountService                    │
│  - Coordinates operations                       │
│  - Handles multi-account transactions           │
│  - No business rules or balance manipulation    │
└──────────┬────────────────────┬─────────────────┘
           │                    │
           ▼                    ▼
┌──────────────────┐   ┌──────────────────────────┐
│  BankAccount     │   │  IBankAccountRepository  │
│  (Entity)        │   │  - Persistence contract  │
│  - Validates     │   │  - get/save operations   │
│  - Enforces      │   └──────────┬───────────────┘
│    rules         │              │
│  - Protects      │              ▼
│    correctness   │   ┌────────────────────────────┐
└──────────────────┘   │ InMemoryBankAccountRepo   │
                       │ - HashMap storage          │
                       └────────────────────────────┘
```

## 📦 Components

### 1. **Entity Layer** (`entities/`)
**`BankAccount`** - The core domain entity
- **Responsibility**: Protect account invariants and enforce business rules
- **Contains**: Balance state, validation logic, transaction rules
- **Key Methods**:
  - `deposit(amount)` - Validates and adds funds
  - `withdraw(amount)` - Validates, checks balance, deducts funds
  - `getBalance()` - Read-only balance accessor

**Design Principle**: Entity owns its correctness. No external class can violate account rules.

### 2. **DTO Layer** (`dto/`)
**Purpose**: Transport data between layers without behavior

- **`DepositRequestDTO`** - Carries deposit request data
- **`WithdrawRequestDTO`** - Carries withdrawal request data  
- **`TransferRequestDTO`** - Carries transfer request data

**Design Principle**: DTOs are dumb data carriers - no validation, no logic, just getters.

### 3. **Service Layer**
**`BankAccountService`** - Application service coordinator
- **Responsibility**: Orchestrate operations across entities and persistence
- **Key Operations**:
  - `deposit(DepositRequestDTO)` - Get account → call entity method → save
  - `withdraw(WithdrawRequestDTO)` - Get account → call entity method → save
  - `transfer(TransferRequestDTO)` - Coordinate two-account operation
  - `getBalance(accountId)` - Query operation

**Design Principle**: Service coordinates intent, never manipulates balance directly.

### 4. **Repository Layer** (`repository/`)
**`IBankAccountRepository`** - Persistence abstraction
- **Contract**: `getAccountById(id)`, `save(account)`

**`InMemoryBankAccountRepository`** - Concrete implementation
- **Storage**: HashMap-based in-memory store
- **Swappable**: Can be replaced with SQL/NoSQL implementation

**Design Principle**: Repository stores state, knows nothing about business rules.

### 5. **Controller** (`BankAccountMain`)
**Responsibility**: Application entry point and input translation
- Sets up infrastructure (repository)
- Seeds initial data
- Creates service instance
- Translates raw input into DTOs
- Invokes service operations

## 🚫 What We Explicitly Avoided

| ❌ Anti-Pattern | ✅ Our Approach |
|----------------|----------------|
| Controller calling `account.withdraw()` | Controller → Service → Entity |
| Entity saving itself (`account.save()`) | Service coordinates Entity + Repository |
| Service holding balance (`service.balance`) | Balance lives only in Entity |
| DTO with validation logic | DTOs are pure data carriers |
| God service doing everything | Single Responsibility: coordinate only |

## 🔑 Key Design Decisions

### ✅ **Entities Protect Correctness**
```java
// Entity enforces rules - cannot be bypassed
public void withdraw(double amount) {
    validateAmount(amount);
    if(amount > balance) {
        throw new IllegalStateException("Insufficient Funds");
    }
    balance -= amount;
}
```

### ✅ **Services Coordinate Intent**
```java
// Service orchestrates but never manipulates balance
public void transfer(TransferRequestDTO request) {
    BankAccount from = repository.getAccountById(request.getFromAccountId());
    BankAccount to = repository.getAccountById(request.getToAccountId());
    
    from.withdraw(request.getAmount());  // Entity enforces rules
    to.deposit(request.getAmount());      // Entity enforces rules
    
    repository.save(from);                // Repository handles persistence
    repository.save(to);
}
```

### ✅ **Controllers Translate Input**
```java
// Main translates raw data into DTOs and delegates to service
service.deposit(new DepositRequestDTO("A1", 200));
service.withdraw(new WithdrawRequestDTO("A2", 100));
service.transfer(new TransferRequestDTO("A1", "A2", 300));
```

### ✅ **Repositories Store State**
```java
// Repository is a simple store - no business logic
public void save(BankAccount account) {
    dbStore.put(account.getAccountId(), account);
}
```

## 🎓 Design Principles Demonstrated

1. **Single Responsibility Principle (SRP)**
   - Entity: Business rules
   - Service: Coordination
   - Repository: Persistence
   - DTO: Data transport

2. **Dependency Inversion Principle (DIP)**
   - Service depends on `IBankAccountRepository` interface
   - Concrete implementation injected via constructor

3. **Open/Closed Principle (OCP)**
   - Can add new account types without changing service
   - Can swap repository implementation without changing service

4. **Separation of Concerns**
   - Data (DTO) ≠ Behavior (Entity) ≠ Coordination (Service) ≠ Storage (Repository)

## 🚀 Running the Application

```bash
javac JavaPracticeProjects.PP01_BankAccountService/BankAccountMain.java
java JavaPracticeProjects.PP01_BankAccountService.BankAccountMain
```

**Expected Output:**
```
A1 balance: 900.0
A2 balance: 700.0
```

## 🔄 Extensibility

The design supports easy extension:

### Add New Operations
```java
// Add to service - entity already has the methods
public void bulkTransfer(List<TransferRequestDTO> transfers) {
    for(TransferRequestDTO t : transfers) {
        transfer(t);
    }
}
```

### Swap Persistence
```java
// Implement IBankAccountRepository with MySQL/MongoDB
IBankAccountRepository repo = new MySQLBankAccountRepository();
BankAccountService service = new BankAccountService(repo);
```

### Add New Client Interfaces
```java
// CLI, REST API, Batch - all use same service
class BankRestController {
    private BankAccountService service;
    // Convert HTTP requests to DTOs → call service
}
```

## 📚 Learning Outcomes

1. **Data holders (DTOs)** carry information without behavior
2. **Behavior classes (Entities)** enforce rules and protect state
3. **Service classes** coordinate operations across multiple objects
4. **Repositories** abstract persistence without business logic
5. **Controllers** translate external input into internal operations

This layered approach ensures maintainability, testability, and extensibility while keeping each component focused on its core responsibility.

---

**Project Type**: Practice Project (Low-Level Design)  
**Focus**: Clean Architecture & Separation of Concerns
