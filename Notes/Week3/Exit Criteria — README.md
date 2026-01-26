# Week 3 — Exit Criteria (README)

This document defines the **non‑negotiable exit criteria for Week 3**.

Week 3 is about **HTTP fundamentals and backend thinking**, not frameworks.
You should **not move forward** until every item here is true.

---

## Week 3 Exit Criteria (Do Not Skip)

You are **done with Week 3 only if all of the following are true**.

---

## 1. You can explain HTTP without mentioning Spring

### What this means
- You can explain HTTP as a **protocol**, not a framework feature.
- You understand requests, responses, methods, headers, bodies, and status codes **independently of Spring**.

### You should be able to explain:
- What HTTP is
- Why it is stateless
- How clients and servers interact
- How errors are communicated

### Red flags
- Explaining HTTP only using Spring annotations
- Saying “Spring handles it” instead of explaining behavior
- Treating HTTP as just a transport pipe

---

## 2. You can write a basic server without copying

### What this means
- You can create a simple HTTP server from scratch
- You understand what each part does, not just syntax

### You should be able to:
- Start a server on a port
- Define routes
- Handle GET and POST
- Read request bodies
- Send correct responses

### Red flags
- Copy‑pasting server code without understanding
- Not knowing why a request hangs
- Confusing server setup with business logic

---

## 3. You know why frameworks exist

### What this means
- You understand the **pain points** frameworks solve
- You don’t treat frameworks as magic

### You should be able to explain:
- Why manual routing doesn’t scale
- Why lifecycle management matters
- Why error handling must be centralized
- Why concurrency makes naive servers unsafe

### Red flags
- Thinking frameworks are just “for convenience”
- Jumping into Spring without understanding HTTP
- Overusing annotations without understanding behavior

---

## 4. You can debug request/response issues

### What this means
- You can trace a request end‑to‑end
- You can identify where failures occur

### You should be able to debug:
- Hanging requests
- Wrong status codes
- Missing or invalid headers
- Incorrect routing
- Improper request body handling

### Red flags
- Blaming tools like Postman or browser blindly
- Guessing instead of tracing lifecycle
- Not knowing where a response is constructed

---

## Final Week‑3 Check

If someone asks you:

> “What happens when I hit an API endpoint?”

You should be able to answer **step‑by‑step**, calmly and confidently,
without mentioning any framework until the very end.

---

## Final Takeaway

Week 3 is about:
- Understanding **protocols**
- Respecting **contracts**
- Debugging **behavior**, not syntax
- Building intuition before abstraction

If you meet all criteria above,  
you are **ready to move to Week 4 — Databases & SQL**.
