# Week 3 — HTTP & Web Fundamentals (Intermediate README)

This README covers **intermediate‑level HTTP and backend design questions**.  
These questions separate **backend engineers** from **tutorial‑level developers**.

The focus is on **real production behavior**, not theoretical definitions.

---

## 1. Why is HTTP stateless, and what problems does that create?

### Core idea
- HTTP is stateless to enable **scalability and simplicity**.
- Servers do not retain client context between requests.
- State must be sent with every request.

### Explanation
Statelessness allows servers to:
- Handle requests independently
- Scale horizontally
- Restart without losing client data

But it creates problems like:
- Repeated authentication
- No built‑in session tracking
- Need for tokens, cookies, or headers

### Interview follow‑ups
- How do sessions work on top of stateless HTTP?
- Why do distributed systems prefer stateless services?
- What happens if state is stored in server memory?

### Common mistakes
- Storing user state in instance variables
- Assuming load balancers route to the same server
- Confusing stateless protocol with stateless application design

---

## 2. How would a server handle 10,000 concurrent HTTP requests?

### Core idea
- Servers rely on **concurrency models**, not one thread per request.
- Efficient I/O and thread pools are critical.

### Explanation
A real server:
- Uses thread pools or event loops
- Avoids blocking operations
- Delegates I/O to non‑blocking mechanisms

Handling concurrency poorly leads to:
- Thread exhaustion
- High latency
- Server crashes

### Interview follow‑ups
- Difference between blocking and non‑blocking I/O?
- Why is thread‑per‑request inefficient?
- How do async frameworks scale better?

### Common mistakes
- Assuming one thread per request is safe
- Blocking threads on I/O or DB calls
- Ignoring back‑pressure

---

## 3. Why should GET requests be idempotent?

### Core idea
- GET requests may be **retried automatically**.
- Repeated GET calls must not change state.

### Explanation
Clients, proxies, and browsers may:
- Retry GET requests
- Cache responses
- Prefetch resources

If GET modifies data, retries can cause:
- Duplicate updates
- Corrupt state
- Hard‑to‑debug bugs

### Interview follow‑ups
- What is the difference between safe and idempotent?
- Can POST ever be idempotent?
- Why are retries dangerous for non‑idempotent requests?

### Common mistakes
- Modifying data inside GET
- Logging mutations through GET
- Using GET for actions

---

## 4. Why is returning stack traces in HTTP responses dangerous?

### Core idea
- Stack traces expose **internal implementation details**.
- They are a security and stability risk.

### Explanation
Returning stack traces reveals:
- Class names
- Package structure
- Framework internals
- Potential vulnerabilities

This information can be exploited by attackers.

### Interview follow‑ups
- Where should stack traces be logged?
- How do global exception handlers help?
- What is the difference between client errors and internal logs?

### Common mistakes
- Returning exception messages directly
- Using default error pages in production
- Treating error transparency as debugging convenience

---

## 5. What breaks if you misuse HTTP status codes?

### Core idea
- Status codes drive **client behavior, retries, and monitoring**.
- Incorrect codes break system behavior silently.

### Explanation
If errors return 200:
- Clients assume success
- Retries don’t happen
- Monitoring misses failures

If server errors return 4xx:
- Issues go unnoticed
- False blame is placed on clients

### Interview follow‑ups
- How do load balancers use status codes?
- Why do observability tools rely on them?
- What status codes trigger retries?

### Common mistakes
- Encoding errors only in response body
- Returning 200 for validation failures
- Treating HTTP as transport‑only

---

## 6. Why is path parsing (/orders/{id}) non‑trivial?

### Core idea
- URLs are strings, not structured data.
- Manual parsing is fragile and error‑prone.

### Explanation
Problems include:
- Extracting variables safely
- Handling invalid formats
- Matching correct routes
- Preventing ambiguous paths

Manual parsing quickly becomes unmaintainable.

### Interview follow‑ups
- How do routing frameworks solve this?
- What happens with overlapping routes?
- Why does routing order matter?

### Common mistakes
- Using string splitting blindly
- Ignoring invalid path formats
- Mixing routing logic with business logic

---

## 7. How does JSON deserialization fail in real systems?

### Core idea
- JSON is untyped and client‑controlled.
- Deserialization failures are common.

### Explanation
Failures occur due to:
- Missing fields
- Extra fields
- Wrong data types
- Null values
- Malformed JSON

Deserialization must be followed by validation.

### Interview follow‑ups
- Difference between deserialization and validation?
- How do schema validators help?
- Why should deserialization errors return 400?

### Common mistakes
- Trusting client JSON blindly
- Skipping validation after parsing
- Using domain entities directly for input

---

## 8. What happens if client sends invalid headers?

### Core idea
- Headers guide request interpretation.
- Invalid headers lead to incorrect parsing or rejection.

### Explanation
Invalid headers can cause:
- Parsing failures
- Security issues
- Incorrect content handling

Servers should:
- Validate critical headers
- Reject invalid requests early

### Interview follow‑ups
- What headers are mandatory for POST?
- How should servers handle unknown headers?
- Why should header validation happen early?

### Common mistakes
- Ignoring headers entirely
- Assuming defaults silently
- Parsing body without checking headers

---

## 9. Why should business logic not depend on HTTP?

### Core idea
- Business logic should be **transport‑agnostic**.
- HTTP is just one delivery mechanism.

### Explanation
Tightly coupling logic to HTTP:
- Makes testing harder
- Prevents reuse
- Spreads protocol concerns everywhere

Clean systems isolate HTTP at the edges.

### Interview follow‑ups
- How does separation of concerns improve testability?
- What layers should know about HTTP?
- How does this help future protocol changes?

### Common mistakes
- Passing HttpRequest objects into services
- Throwing HTTP exceptions from domain logic
- Mixing validation with business rules

---

## 10. What exactly does Spring Boot abstract away in request handling?

### Core idea
- Spring Boot automates the **entire HTTP lifecycle**.
- It removes boilerplate without changing fundamentals.

### Explanation
Spring handles:
- Server startup
- Routing
- Method matching
- Body parsing
- Validation
- Exception handling
- Response construction

The request lifecycle still exists — it’s just managed.

### Interview follow‑ups
- What parts of HTTP does Spring not hide?
- How does Spring enforce conventions?
- Why is understanding the lifecycle still important?

### Common mistakes
- Treating Spring as magic
- Ignoring HTTP semantics
- Overusing annotations without understanding flow

---

## Final Week‑3 Intermediate Takeaway

Backend engineering is about:
- Designing for **failure**
- Handling **concurrency**
- Respecting **protocol contracts**
- Building systems that behave correctly under load

If you understand this README,  
you are thinking like a **backend engineer**, not a tutorial follower.
