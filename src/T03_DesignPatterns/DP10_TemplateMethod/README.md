# Template Method Design Pattern — Behavioral (GoF)

> **"Define the skeleton; let subclasses fill in the steps."**

---

## Quick Reference

| Aspect | Detail |
|---|---|
| **What** | Abstract class defines a `final` method with fixed step order; subclasses override specific steps |
| **Why** | Avoid duplicating the same algorithm sequence across subclasses |
| **Example** | ML training pipeline: `loadData → preprocess → train → evaluate → save` |
| **Key rule** | Template method must be `final` — subclasses change *steps*, never the *order* |

---

## Structure

```
   IModelTrainer (abstract)
   + trainPipeline()  ← final (template method)
   # loadData()       ← concrete default
   # preprocessData() ← concrete default
   # trainModel()     ← abstract (MUST override)
   # evaluateModel()  ← abstract (MUST override)
   # saveModel()      ← concrete default (CAN override = hook)
          │
    ┌─────┴──────────┐
DecisionTreeTrainer  NeuralNetworkTrainer
  trainModel()         trainModel()
  evaluateModel()      evaluateModel()
                       saveModel() ← overrides default
```

---

## Step Types

| Type | Example | Subclass must override? |
|---|---|---|
| **Concrete** | `loadData()`, `preprocessData()` | No — shared default |
| **Abstract** | `trainModel()`, `evaluateModel()` | Yes — forced |
| **Hook** | `saveModel()` | Optional — has default, can override |

---

## Pitfalls

- **Forgetting `final`** on template method → subclass can break the entire pipeline
- **Fragile base class** → changing step order silently breaks all subclasses
- **All steps abstract** → template adds no value, just use an interface

---

## Template Method vs Strategy

| | Template Method | Strategy |
|---|---|---|
| Mechanism | Inheritance | Composition |
| What varies | Individual steps | Entire algorithm |
| Binding | Compile-time | Runtime (swappable) |
