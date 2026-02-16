# Observer Pattern – Channel & Subscribers

## 📌 Problem

We have a **Channel** and multiple **Subscribers**.

Whenever the Channel updates (e.g., new content), all Subscribers should be notified automatically.

The **Observer Pattern** solves this by defining a one-to-many relationship:
- One **Channel (Observable)**
- Many **Subscribers (Observers)**

The Channel stores a list of subscribers and calls `notify()` whenever something changes.

---

## 🔁 Two Update Approaches

### 1️⃣ Pull Model
- Channel calls `subscriber.update()`
- Subscriber fetches data using a getter

> Subscriber requests the updated data.

---

### 2️⃣ Push Model
- Channel calls `subscriber.update(data)`
- Channel sends required data directly

> Channel pushes the updated data.

---

## 🎯 Summary

- Observer Pattern enables automatic notifications.
- Pull = more flexible, slightly more coupled.
- Push = more decoupled, better encapsulation.
