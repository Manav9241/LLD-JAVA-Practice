# Week 3 — HTTP & Web Fundamentals (README)

This README covers **HTTP fundamentals for backend engineers**, focused on **real systems + interview readiness**, not browser trivia.

---

## 1. What is HTTP and why is it stateless?

### Core idea
- HTTP is a **request–response protocol**.
- Stateless means **each request is independent**.
- The server does not remember previous requests by default.

### Explanation
In HTTP, every request must carry **all the information** needed to process it.
The server does not rely on past interactions.

```text
Request 1: GET /orders
Request 2: GET /orders/1
```

The server treats these as **completely unrelated** unless the client sends identity/state again.

### Why statelessness exists
- Enables horizontal scaling
- Makes servers restart-safe
- Avoids per-client memory on server

### Interview follow-ups
- How do sessions work if HTTP is stateless?
- How do JWTs preserve identity?
- What breaks if servers keep state in memory?

### Common mistakes
- Assuming server “remembers” users
- Storing user state in instance variables
- Confusing HTTP statelessness with application statelessness

---

## 2. Difference between GET and POST?

### Core idea
- GET is for **reading data**
- POST is for **creating or triggering actions**
- Semantics matter more than syntax

```http
GET /orders        → fetch orders
POST /orders       → create order
```

### Key differences

| Aspect | GET | POST |
|------|----|-----|
| Purpose | Read | Create / Action |
| Side effects | No | Yes |
| Idempotent | Yes | No |
| Cacheable | Yes | No |

### Interview follow-ups
- Why must GET be idempotent?
- When is POST not about creation?
- Can POST return data?

### Common mistakes
- Using POST for reads
- Using GET for mutations
- Ignoring idempotency rules

---

## 3. Why does POST not return cached responses?

### Core idea
- POST represents **state-changing operations**
- Caching POST responses can cause **data corruption**

### Explanation
Caching assumes:
> “Same request → same response”

This is **not true for POST**.

```http
POST /orders
```

Sending it twice may create **two orders**.
Caching this would cause clients to receive stale or incorrect data.

### Interview follow-ups
- What makes a request cacheable?
- Why are GET requests safe to cache?
- How do CDNs treat POST requests?

### Common mistakes
- Expecting POST to be cacheable
- Retrying POST blindly in distributed systems
- Not using idempotency keys for POST

---

## 4. What is the purpose of HTTP status codes?

### Core idea
- Status codes communicate **outcome + responsibility**
- Machines rely on status codes more than response bodies

### Explanation
Status codes answer:
> Did the request succeed?  
> If not, whose fault is it?

| Range | Meaning |
|-----|--------|
| 2xx | Success |
| 4xx | Client error |
| 5xx | Server error |

### Interview follow-ups
- Why are 4xx and 5xx treated differently by clients?
- How do retries depend on status codes?
- Why do monitoring tools care about status codes?

### Common mistakes
- Always returning 200
- Encoding errors only in response body
- Using wrong status codes for validation errors

---

## 5. Difference between 200 and 201?

### Core idea
- 200 = successful request
- 201 = **new resource created**

### Explanation

```http
GET /orders/1 → 200 OK
POST /orders  → 201 Created
```

201 explicitly tells the client:
> “Something new now exists because of this request”

Often includes:
```http
Location: /orders/123
```

### Interview follow-ups
- Should PUT return 200 or 204?
- When is 204 better than 200?
- Does 201 always need a body?

### Common mistakes
- Returning 200 for creation
- Ignoring REST semantics
- Not returning resource location

---

## 6. What happens if a server always returns 200?

### Core idea
- The server **lies**
- Clients, retries, and monitoring break

### Explanation
If errors are hidden behind 200:
- Clients assume success
- Retries never happen
- Monitoring never triggers alerts

```json
HTTP 200
{
  "error": "Something went wrong"
}
```

This is **worse than a crash**.

### Interview follow-ups
- How do clients decide retries?
- Why do load balancers care about status codes?
- How does observability rely on status codes?

### Common mistakes
- Encoding failure inside success response
- Treating HTTP as “transport only”
- Ignoring machine consumers

---

## 7. What is JSON and why not XML?

### Core idea
- JSON is lightweight and language-agnostic
- Easier to parse and generate than XML

### Comparison

| JSON | XML |
|----|----|
| Less verbose | Verbose |
| Maps naturally to objects | Requires schema |
| Faster parsing | Slower |
| Widely supported | Legacy heavy |

```json
{
  "id": 1,
  "item": "Laptop"
}
```

### Interview follow-ups
- Why not binary formats?
- What are drawbacks of JSON?
- How does schema validation work with JSON?

### Common mistakes
- Assuming JSON is type-safe
- Trusting client JSON blindly
- Ignoring validation

---

## 8. What is Content-Type?

### Core idea
- Content-Type tells the server **how to interpret the body**

### Explanation

```http
Content-Type: application/json
```

Means:
> “The request body is JSON — parse it as JSON”

If missing or wrong:
- Parsing fails
- Request should be rejected (400)

### Interview follow-ups
- Difference between Content-Type and Accept?
- What happens if Content-Type is wrong?
- Why is this header mandatory for POST?

### Common mistakes
- Ignoring Content-Type
- Parsing body blindly
- Accepting invalid formats

---

## 9. What happens if request body is missing?

### Core idea
- Missing body is a **client error** if body is required
- Backend must validate explicitly

### Explanation
For:
```http
POST /orders
```

If body is missing:
- Server cannot create order
- Correct response → `400 Bad Request`

Never assume defaults silently.

### Interview follow-ups
- Difference between missing vs null?
- Should missing body ever be allowed?
- How do validation frameworks handle this?

### Common mistakes
- Allowing partial data
- Creating invalid state
- Skipping validation

---

## 10. Why does `/health` endpoint exist?

### Core idea
- `/health` answers: **“Is the service alive?”**
- Used by machines, not humans

### Explanation
Health endpoints are used by:
- Load balancers
- Kubernetes
- Monitoring systems

```http
GET /health → 200 OK
```

If unhealthy:
```http
503 Service Unavailable
```

### Interview follow-ups
- Difference between liveness and readiness?
- Why should `/health` be lightweight?
- Should `/health` hit the database?

### Common mistakes
- Adding business logic to health checks
- Making health checks slow
- Returning 200 when dependencies are down

---

## Final Week-3 Takeaway

HTTP is not just a transport layer.

Backend engineering requires:
- Respecting **protocol semantics**
- Designing for **machines, not humans**
- Making failures **explicit and actionable**
- Understanding why frameworks exist

If you understand this README,  
you have a **solid Week-3 backend foundation**.
