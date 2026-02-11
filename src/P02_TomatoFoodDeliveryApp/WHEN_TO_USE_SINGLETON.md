# When To Use Singleton Pattern (And When Not To)

## 🎯 Your Question Is Valid!

You're practicing the Singleton pattern, and I said "not using Singleton is a big win." That seems contradictory! Let me clarify.

---

## ✅ When Singleton IS The Right Choice

### 1. **Logging Systems**
```java
public class Logger {
    private static volatile Logger instance;
    
    private Logger() {}
    
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
    
    public void log(String message) {
        // Write to file - we want ONE logger, not multiple!
    }
}
```

**Why Singleton here?**
- ✅ Only ONE log file should be written to
- ✅ Need to control file access
- ✅ Global point of access makes sense

---

### 2. **Configuration Management**
```java
public class AppConfig {
    private static AppConfig instance;
    private Properties properties;
    
    private AppConfig() {
        // Load configuration once - expensive operation
        properties = loadConfigFromFile();
    }
    
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
}
```

**Why Singleton here?**
- ✅ Configuration should be consistent across app
- ✅ Loading config is expensive (do it once)
- ✅ Everyone needs same settings

---

### 3. **Database Connection Pool**
```java
public class ConnectionPool {
    private static ConnectionPool instance;
    private List<Connection> availableConnections;
    private final int MAX_CONNECTIONS = 10;
    
    private ConnectionPool() {
        availableConnections = new ArrayList<>();
        // Create limited number of connections
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            availableConnections.add(createConnection());
        }
    }
    
    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }
    
    public Connection getConnection() {
        // Return connection from pool
    }
}
```

**Why Singleton here?**
- ✅ Must control number of database connections
- ✅ Resource management (database has limits)
- ✅ Need centralized connection handling

---

### 4. **Hardware Access**
```java
public class PrinterManager {
    private static PrinterManager instance;
    
    private PrinterManager() {
        // Initialize hardware connection
    }
    
    public static PrinterManager getInstance() {
        return instance;
    }
    
    public void print(Document doc) {
        // Only one printer - must coordinate access
    }
}
```

**Why Singleton here?**
- ✅ Physical hardware (only one printer)
- ✅ Must coordinate access to prevent conflicts
- ✅ Resource is truly unique

---

## ❌ When Singleton Is NOT Appropriate

### Your Case: RestaurantManager / OrderManager

```java
// V01 - Uses Singleton
public class RestaurantManager {
    private static RestaurantManager instance = null;
    private List<Restaurant> restaurants;
    
    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }
}
```

### Why NOT Singleton Here?

#### Problem 1: No Resource to Control
```java
// There's NO physical resource we're managing
// It's just a List in memory
// Why limit to one instance?

// What if we want different lists for different regions?
RestaurantManager delhiManager = ???  // Can't do this with Singleton!
RestaurantManager mumbaiManager = ???  // Stuck with one global instance
```

#### Problem 2: Testing Becomes Difficult
```java
// Test 1
@Test
public void testAddRestaurant() {
    RestaurantManager manager = RestaurantManager.getInstance();
    manager.addRestaurant(new Restaurant("R1", "Delhi"));
    
    List<Restaurant> results = manager.searchByLocation("Delhi");
    assertEquals(1, results.size());  // ✅ PASSES
}

// Test 2 - Runs AFTER Test 1
@Test
public void testSearchRestaurant() {
    RestaurantManager manager = RestaurantManager.getInstance();
    // PROBLEM: Restaurant "R1" from Test 1 is STILL HERE!
    manager.addRestaurant(new Restaurant("R2", "Delhi"));
    
    List<Restaurant> results = manager.searchByLocation("Delhi");
    assertEquals(1, results.size());  // ❌ FAILS! Returns 2 (R1 + R2)
}
```

**The Issue:**
- Tests share state
- Can't run tests in parallel
- Can't isolate tests
- Must remember to clean up after each test

#### Problem 3: Not Thread-Safe (Your V01 Implementation)
```java
public static RestaurantManager getInstance() {
    if (instance == null) {           // ← Thread A checks: null
                                      // ← Thread B checks: null
        instance = new RestaurantManager();  // ← Thread A creates
                                             // ← Thread B creates
    }
    return instance;
}
```

**Race Condition:**
- Two threads could both see `instance == null`
- Both create a new instance
- Suddenly you have TWO instances of your "Singleton"!

**To fix, you'd need:**
```java
public static synchronized RestaurantManager getInstance() {
    if (instance == null) {
        instance = new RestaurantManager();
    }
    return instance;
}
```

But this adds overhead!

#### Problem 4: Can't Mock for Testing
```java
public class TomatoApp {
    public List<Restaurant> searchRestaurants() {
        // Hardcoded dependency - can't replace or mock!
        return RestaurantManager.getInstance().searchByLocation(...);
    }
}

// How do you test TomatoApp without hitting real RestaurantManager?
// You CAN'T easily!
```

---

## 🤔 The Key Question: "Do I NEED Only One Instance?"

### Ask Yourself:

#### ✅ Use Singleton If:
1. **Physical resource constraint**: Only one printer, one file, one hardware device
2. **Must coordinate access**: Database connections, file handles
3. **Expensive to create**: Loading configuration, establishing connections
4. **Truly global**: Logger that writes to ONE file

#### ❌ Don't Use Singleton If:
1. **Just storing data**: Lists, maps, simple collections
2. **No resource constraint**: Memory is cheap, Lists can be many
3. **Need flexibility**: Might want different instances later
4. **Testing is important**: Need isolated, mockable instances

---

## 💡 The Real Principle

### Don't Use Singleton Just Because:
❌ "I don't need multiple objects"  
❌ "It's convenient"  
❌ "I want global access"  

### Use Singleton When:
✅ "Only ONE can exist" (physical constraint)  
✅ "Must control resource" (limited resource)  
✅ "Global coordination needed" (shared state is REQUIRED)

---

## 📚 For Your Learning Journey

### Stage 1: Learn The Pattern ✅
**V01 is perfect for this!**
- Practice Singleton syntax
- Understand the pattern
- See how it works

### Stage 2: Understand When To Use It ✅
**This is where you are now!**
- Learn when Singleton is appropriate
- Understand the trade-offs
- Know the downsides

### Stage 3: Make Informed Decisions ✅
**V02 shows this!**
- Choose patterns based on needs, not habit
- Understand testing implications
- Balance convenience vs flexibility

---

## 🎯 Specific to Your Code

### RestaurantManager / OrderManager

**Question:** Do we NEED only one instance?

**Analysis:**
- ❌ No physical resource (just a List)
- ❌ No expensive operation (ArrayList is cheap)
- ❌ No resource limit (memory can hold many Lists)
- ❌ No global coordination needed (each instance is independent)

**Conclusion:** Singleton adds constraints without benefits here.

---

## 🔄 When "Not Needing Multiple Objects" IS Enough

### Thread-Local Storage
```java
public class RequestContext {
    private static ThreadLocal<RequestContext> context = new ThreadLocal<>();
    
    public static RequestContext getCurrent() {
        RequestContext ctx = context.get();
        if (ctx == null) {
            ctx = new RequestContext();
            context.set(ctx);
        }
        return ctx;
    }
}
```

**One instance PER THREAD** - this makes sense!

### Spring/Dependency Injection
```java
@Service
@Scope("singleton")  // Default scope
public class RestaurantService {
    // Spring manages ONE instance for the entire application
    // But YOU don't implement Singleton pattern
    // Spring handles lifecycle
}
```

**Framework manages it** - you get benefits without downsides!

---

## 🎓 The Lesson

### For Practice:
✅ **V01 with Singleton** - Great for learning the pattern!

### For Production:
- Use Singleton when you have a **clear reason** (resource constraint, coordination)
- Don't use it just because you "don't need multiple objects"
- Consider testability, flexibility, thread-safety

### Your Instinct Was Right:
You wanted to practice Singleton - that's excellent! Now you're learning when to use it in real applications. Both are important skills!

---

## 💬 To Answer Your Specific Question

> "Why do we not use singleton classes when we only do not need separate object at every call?"

**Answer:** Because "not needing multiple objects" is different from "needing ONLY ONE object."

### Not Needing Multiple:
```java
// I could create 10 RestaurantService instances
RestaurantService s1 = new RestaurantService();
RestaurantService s2 = new RestaurantService();
// ...but I choose to use just one

// This is CHOICE, not CONSTRAINT
```

### Needing Only One:
```java
// I MUST have only one ConnectionPool
// Having two would cause problems
// This is REQUIREMENT, not choice
```

**The difference:**
- Singleton enforces "only one" as a **constraint**
- Use it when the constraint is **necessary**
- Don't use it just for **convenience**

---

## 🚀 Your Next Steps

1. ✅ **Keep V01 as-is** - It's good for practicing the pattern
2. ✅ **Study V02** - See when NOT to use Singleton
3. ✅ **Practice both** - Learn when each approach fits
4. ✅ **Always ask "why?"** - You're doing this perfectly!

The fact that you questioned this shows you're thinking critically. That's exactly the right approach! 🎉

---

## 📖 Further Reading

### Good Singleton Examples:
- java.lang.Runtime (JVM runtime)
- java.awt.Desktop (desktop integration)
- Spring ApplicationContext (framework container)

### Singleton Anti-Patterns:
- "God objects" that do everything
- Singletons for convenience, not necessity
- Untestable code due to hardcoded Singletons

Keep asking these questions! 👏
