# Project Index

> Last updated: 2026-03-12
> Maps Manav's projects to Coder Army LLD lectures (adityatandon15/Low-Level-Design-Course)

---

## Lecture-to-Project Mapping

| Coder Army Lecture | Topic                             | Manav's Folder                                        | Status      |
| ------------------ | --------------------------------- | ----------------------------------------------------- | ----------- |
| Lecture 01         | OOP Basics                        | T01_OOPs/                                             | Completed   |
| Lecture 02         | Encapsulation                     | T01_OOPs/Encapsulation/                               | Completed   |
| Lecture 03         | Inheritance                       | T01_OOPs/Inheritance/                                 | Completed   |
| Lecture 04         | Polymorphism + Abstraction        | T01_OOPs/Polymorphism/, T01_OOPs/Abstraction/         | Completed   |
| Lecture 05         | SOLID (SRP, OCP, LSP, ISP)        | T02_SOLID/SRP/, OCP/, LSP/, ISP/                      | Completed   |
| Lecture 06         | SOLID (DIP)                       | T02_SOLID/DIP/                                        | Completed   |
| Lecture 07         | Document Editor (Case Study)      | P01_GoogleDocs/ (BadDesign → GoodDesign → BestDesign) | Completed   |
| Lecture 08         | Strategy Pattern                  | T03_DesignPatterns/DP01_Strategy/                     | Completed   |
| Lecture 09         | Factory Pattern                   | T03_DesignPatterns/DP02_Factory/                      | Completed   |
| Lecture 10         | Singleton Pattern                 | T03_DesignPatterns/DP03_Singleton/                    | Completed   |
| Lecture 11         | Tomato Food Delivery (Case Study) | P02_TomatoFoodDeliveryApp/ (V01 → V02)                | Completed   |
| Lecture 12         | Observer Pattern                  | T03_DesignPatterns/DP04_Observer/                     | Completed   |
| Lecture 13         | Decorator Pattern                 | T03_DesignPatterns/DP05_Decorator/                    | Completed   |
| Lecture 14         | Notification Engine (Case Study)  | P03_NotificationEngine/ (MyDesign → BetterDesign)     | Completed   |
| Lecture 15         | Command Pattern                   | T03_DesignPatterns/DP06_Command/                      | Completed   |
| Lecture 16         | Adapter Pattern                   | T03_DesignPatterns/DP07_Adapter/                      | Completed   |
| Lecture 17         | Facade Pattern                    | T03_DesignPatterns/DP08_Facade/                       | Completed   |
| Lecture 18         | Music Player System (Case Study)  | —                                                     | Not started |
| Lecture 19         | Composite Pattern                 | —                                                     | Not started |
| Lecture 20         | (TBD)                             | —                                                     | Not started |
| Lecture 21         | Proxy Pattern                     | —                                                     | Not started |
| Lecture 22-25      | (TBD)                             | —                                                     | Not started |
| Lecture 26         | Zepto Clone (Case Study)          | —                                                     | Not started |
| Lecture 27         | Tinder Clone (Case Study)         | —                                                     | Not started |
| Lecture 28         | Builder Pattern                   | —                                                     | Not started |
| Lecture 29         | Iterator Pattern                  | —                                                     | Not started |
| Lecture 30         | (TBD)                             | —                                                     | Not started |
| Lecture 31         | Splitwise App (Case Study)        | —                                                     | Not started |
| Lecture 32         | Vending Machine / State Pattern   | —                                                     | Not started |
| Lecture 33-34      | (TBD)                             | —                                                     | Not started |
| Lecture 35         | Mediator Pattern                  | —                                                     | Not started |
| Lecture 36-37      | (TBD)                             | —                                                     | Not started |
| Lecture 38         | Visitor Pattern                   | —                                                     | Not started |
| Lecture 39         | Memento Pattern                   | —                                                     | Not started |
| Lecture 40         | (TBD)                             | —                                                     | Not started |
| PracticeProblems   | Mixed exercises                   | —                                                     | Not started |
| Hackathon 01       | Hackathon project                 | —                                                     | Not started |

---

## Project Details

### P01_GoogleDocs (Lecture 07)

- **Problem**: Design a document editor supporting multiple element types, rendering, persistence
- **Versions**: BadDesign → GoodDesign → BestDesign (3-stage progressive refactoring)
- **Patterns applied**: Factory, Strategy, SRP/OCP/DIP
- **Key files**: BestDesign/DocumentEditor.java, Document.java, DocumentElementFactory.java
- **Element types**: Text, Image, Heading, UnorderedList, NextLine, TabSpace
- **Renderers**: PlainTextRenderer, HTMLRenderer
- **Persistence**: FileStorage, CloudStorage

### P02_TomatoFoodDeliveryApp (Lecture 11)

- **Problem**: Food delivery app — restaurant search, cart, checkout, payment, notifications
- **Versions**: V01_MyDesign (19 classes) → V02_BetterDesign (22 classes)
- **Patterns applied**: V01: Factory, Strategy, Singleton → V02: + Builder, Observer, Facade
- **Key evolution**: Manager→Service, String literals→Enums, Constructor→Builder, Static notification→Observer+Strategy
- **Companion docs**: FINAL_CODE_REVIEW_STATUS.md, MANAGER_VS_SERVICE_EXPLAINED.md, WHEN_TO_USE_SINGLETON.md

### P03_NotificationEngine (Lecture 14)

- **Problem**: Plug-and-play extensible notification engine with SMS/Email/Popup, storage, logging
- **Versions**: MyDesign → BetterDesign
- **Patterns applied**: MyDesign: Decorator, Observer (pull), Strategy, Singleton → BetterDesign: + Template Method, Builder, Router
- **Key evolution**: Pull→Push observer, Singleton→Injectable, flat strategies→type-based routing (EnumMap), abstract base channel with EnumSet filtering
- **Companion docs**: DESIGN_IMPROVEMENTS.md (UML), NEW_CONCEPTS_EXPLAINED.md (13 new concepts)

---

## Practice Projects (JavaPracticeProjects)

### PP01_BankAccountService

- **Architecture**: Layered — DTO → Entity → Repository → Service
- **Key concepts**: Immutable DTOs (final fields), domain validation, IBankAccountRepository interface, constructor injection
- **Status**: Completed

### PP02_OrderManagementSystem

- **Architecture**: Controller → Service → Repository with interfaces
- **Key concepts**: Custom exception hierarchy (OrderException → OrderNotFoundException, DuplicateOrderException, InvalidOrderStateException), ConcurrentHashMap, synchronized state machine, ExecutorService stress test
- **Status**: Completed

### Learning/ImmutableConfig

- **Concept**: Mutable vs immutable config in multithreaded environments
- **Status**: Completed

### Learning/PlainHttpServer

- **Concept**: Raw com.sun.net.httpserver.HttpServer, REST-like routing, ConcurrentHashMap, AtomicInteger
- **Status**: Completed

---

## Topic Folders

### T01_OOPs (Lectures 01-04)

- Abstraction, Encapsulation, Inheritance, Polymorphism
- Car domain throughout
- **Status**: Completed

### T02_SOLID (Lectures 05-06)

- All 5 principles with Violated/ and Followed/ subfolders
- Domains: ShoppingCart (SRP/OCP), BankAccounts (LSP), Shapes (ISP), ClientService+DB (DIP)
- **Status**: Completed

### T03_DesignPatterns (Lectures 08-17)

- DP01 Strategy (Robot) → DP08 Facade (Computer)
- GoF_Pattern_Classification.md covers all 23 GoF patterns conceptually
- **Status**: 8 of 23 GoF patterns implemented

---

## Notes (Separate from Coder Army)

| Week   | Topic                     | Status    |
| ------ | ------------------------- | --------- |
| Week 1 | Java Backend Fundamentals | Completed |
| Week 2 | Java Concurrency          | Completed |
| Week 3 | HTTP & Web                | Completed |
| Week 4 | DBMS (7 sub-topics)       | Completed |
