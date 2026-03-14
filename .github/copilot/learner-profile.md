# Learner Profile — Manav

> Last updated: 2026-03-12
> Source: Full repository analysis of Manav9241/LLD-JAVA-Practice

---

## Learning Source

- **Course**: Coder Army LLD Playlist (YouTube) by Aditya Tandon
- **Playlist**: https://youtube.com/playlist?list=PLQEaRBV9gAFvzp6XhcNFpk1WdOcyVo9qT
- **Reference Repo**: https://github.com/adityatandon15/Low-Level-Design-Course (40 Lectures + PracticeProblems + Hackathon)
- **Learning style**: Watch lecture → implement own version → iterate (MyDesign → BetterDesign) → document learnings

---

## Skill Levels by Dimension

| Dimension           | Level                 | Evidence                                                                                                                                                  |
| ------------------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **OOP**             | Strong                | 4 pillars demonstrated with Car domain, interface-first thinking, abstract classes                                                                        |
| **SOLID**           | Strong                | All 5 principles with Violated/Followed examples, actively applied in projects                                                                            |
| **Design Patterns** | Advanced-Intermediate | 8 GoF patterns implemented (Strategy, Factory, Singleton, Observer, Decorator, Command, Adapter, Facade) + multiple patterns applied together in projects |
| **Architecture**    | Intermediate-Advanced | Layered architecture (Controller→Service→Repository), DTO pattern, factory registries, event-driven observer, type-based routing                          |
| **Concurrency**     | Intermediate          | volatile, synchronized, DCL Singleton, ConcurrentHashMap, AtomicInteger, ExecutorService, CountDownLatch, race condition awareness                        |
| **Modern Java**     | Emerging              | Streams, EnumSet/EnumMap, method references, isBlank(), computeIfAbsent, UUID                                                                             |
| **Testing**         | Gap                   | Zero JUnit tests. All validation via main() demo runs                                                                                                     |
| **Build/Tooling**   | Basic                 | Raw javac, no Maven/Gradle. CI compile-only via GitHub Actions                                                                                            |
| **HTTP/Backend**    | Beginner-Intermediate | Plain HttpServer (com.sun.net.httpserver), REST concepts in notes                                                                                         |
| **DBMS**            | Theoretical           | Week 4 notes cover fundamentals through ACID/isolation, no DB code yet                                                                                    |

---

## Java Features — Known vs Not-Yet-Explored

### Actively Using

- Interfaces + implementations (everywhere)
- Abstract classes (AbstractRobot, AbstractNotificationChannel)
- Enums (OrderType, PaymentMethod, OrderStatus, NotificationType)
- Generics (standard collections usage)
- Streams (.stream().filter().collect(), .mapToDouble())
- EnumSet / EnumMap (P03 BetterDesign)
- Collections.unmodifiableList() (defensive copies)
- Builder pattern (static inner class, fluent API)
- volatile + DCL (Singleton, P03)
- synchronized (PP02 Order state machine)
- ConcurrentHashMap, AtomicInteger (PP02, PlainHttpServer)
- ExecutorService, CountDownLatch (Singleton tests, PP02)
- Function<> / method references (V02 PaymentStrategyFactory::new)
- computeIfAbsent, getOrDefault (P03 BetterDesign Router)
- UUID.randomUUID() (V02 Order)
- java.time.LocalDateTime (V02)

### Not Yet Explored

- Records
- Sealed classes
- Pattern matching (instanceof, switch)
- var (local variable type inference)
- Text blocks (""")
- Optional
- CompletableFuture
- Modules (JPMS)

---

## Coding Conventions

### Naming

- **Interface prefix**: `I` — consistently used (IDocumentElement, IPaymentStrategy, INotificationChannel, IOrderService, IBankAccountRepository)
- **Class naming**: Descriptive PascalCase (DocumentElementFactory, NotificationRouter, AbstractNotificationChannel)
- **Method naming**: camelCase (corrected from early PascalCase in T01_OOPs)
- **Package naming**: PascalCase for top-level project folders (P01_GoogleDocs), evolving toward lowercase for sub-packages (model/, service/, factory/, enums/, channel/, routing/)

### Structure

- Interface → Implementation pattern used consistently
- Constructor injection preferred (over Singleton, evolved from V01→V02)
- Main method as integration test / demo runner
- Progressive refactoring: BadDesign → GoodDesign → BestDesign (or MyDesign → BetterDesign)

### Error Handling

- Custom exception hierarchies (OrderException → OrderNotFoundException, DuplicateOrderException)
- IllegalArgumentException / IllegalStateException for validation
- Try-catch in controller layer separating business vs system errors
- No checked exceptions — entirely unchecked/runtime

### Documentation

- Companion markdown files per project: README, DESIGN_IMPROVEMENTS, NEW_CONCEPTS_EXPLAINED
- Honest self-assessment in code reviews
- Q&A format in notes: Core Idea → Code Example → Interview Follow-ups → Common Mistakes

---

## Learning Style Traits

1. **Iterative refactorer** — builds V1 first, critically reviews, builds V2
2. **Documentation-heavy** — every project gets markdown explaining design decisions and new concepts learned
3. **Interview-oriented** — notes structured for interview readiness
4. **PR-driven workflow** — uses GitHub branches per project, Copilot for code reviews
5. **Concept linker** — connects patterns across projects (Strategy in Robot → Payment → Notification)
6. **Self-reflective** — writes explicit comparison docs (MANAGER_VS_SERVICE_EXPLAINED, WHEN_TO_USE_SINGLETON)

---

## What "Improve" Means to This Learner

When Manav asks to "improve" or "make this better", it means:

- Apply the SOLID/pattern principles currently being practiced
- Introduce AT MOST 1-2 new concepts, flagged explicitly as new (if needed, else only use all the previously learnt patterns and concepts)
- Keep the same style: no build tools, no frameworks, no production boilerplate
- Keep the **design-sketch fidelity** — `System.out.println` represents real operations. This is a design practice repo, not a production project. "Improve" means improve the design (patterns, SOLID, abstractions), not the implementation depth or completeness.
- Incremental improvement — V1 → V2, not V1 → production-grade
- The learner implements it themselves — Copilot explains the WHY, learner writes the code

---

## Repository Metadata

- **Java version**: 17 (Temurin, via CI)
- **Build tool**: None (raw javac)
- **Test framework**: None
- **IDE**: IntelliJ IDEA (project .iml file present)
- **CI**: GitHub Actions — compile only, PR trigger
- **Total Java files**: ~276
