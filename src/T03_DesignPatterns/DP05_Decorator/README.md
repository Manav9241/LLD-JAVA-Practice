
# Decorator Pattern – Mario Power-Ups

## 📌 Problem Statement (Theoretical View)

In a game system, we have a base character **Mario** that represents a concrete
implementation of a game entity. Over time, Mario can acquire various power-ups
such as `HeightUp`, `GunPower`, and `StarPower`.

The design challenge arises when we want to:

- Add new abilities dynamically at runtime
- Combine multiple abilities in different orders
- Avoid creating an explosion of subclasses such as:
    - MarioWithGun
    - MarioWithStar
    - MarioWithGunAndStar
    - MarioWithHeightAndGunAndStar
    - etc.

Using inheritance alone would tightly couple behavior combinations to static
class hierarchies, making the system rigid and difficult to extend.

The **Decorator Pattern** provides a structural solution by allowing behavior
to be extended dynamically through object composition rather than subclassing.

---

## 🧩 Structural Relationships

The Decorator Pattern relies on combining two object-oriented relationships
to enable dynamic behavior extension.

### 1️⃣ Type Compatibility (Inheritance)

Each power-up class implements the same `GameCharacter` interface as Mario.

This ensures that:
- A decorated Mario is still treated as a `GameCharacter`
- The client does not know whether it is dealing with plain Mario or a powered-up Mario
- Wrapping remains transparent

In other words, every decorator behaves like the original component from the outside.

---

### 2️⃣ Behavioral Delegation (Composition)

Each power-up stores a reference to another `GameCharacter`.

When a method is called:
1. The decorator may add its own behavior.
2. It delegates the call to the wrapped object.
3. It may enhance the result before returning.

This delegation mechanism allows:
- Layered behavior modification
- Multiple power-ups to be stacked
- Runtime flexibility in combining features

---

## 📌 Short Summary

- Adds behavior dynamically through object wrapping
- Combines inheritance (IS-A) and composition (HAS-A)
- Allows flexible stacking of features at runtime
- Avoids subclass explosion
- Keeps the core component closed for modification


---