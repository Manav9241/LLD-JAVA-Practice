---
name: hands-on-practice
description: "Use when the teaching plan reaches the hands-on practice phase. Provides the template for creating problem statements, progressive hints, and review checklists for the user to design and implement on their own. Use when the Teacher agent needs to generate a hands-on exercise for any concept or design pattern."
---

# Hands-On Practice Skill

This skill defines the format for hands-on practice exercises during teaching sessions. The Teacher agent uses this when the learning plan reaches the final (hands-on) phase.

## Core Principle

The user designs and implements the solution. The agent provides the problem, guides with hints, and reviews the result. The agent does NOT write code unless explicitly asked.

## Problem Statement Format

Every hands-on exercise must include a problem statement with these sections:

### 1. Scenario

A concrete, real-world system described in plain language. Give enough context that the user understands the domain (e.g., "You are building the order execution engine for a retail stock brokerage").

### 2. Requirements (numbered)

List the functional requirements the system must support. These describe WHAT the system needs to do, not HOW to structure the code. Each requirement should map to a capability that the concept being practiced enables.

Example:

> 1. Orders submitted before market hours must be held and only executed when the market opens.
> 2. During market hours, orders should execute right away.
> 3. Every operation must be recorded in an immutable audit log.

### 3. Domain Operations

List the concrete operations the system can perform, with their inputs. This gives the user a starting point for thinking about what their receiver/domain object needs.

Example:

> - Place a buy order (requires: ticker, quantity, price)
> - Cancel an order (requires: orderId)

### 4. Task

A clear directive followed by guiding questions that prompt the user to think about key design dimensions — without naming the solution structure.

Format: "Design and implement this system using [concept being taught]. Think about [guiding questions relevant to the concept]."

The guiding questions should vary per concept. They orient the user toward the right design dimensions without giving away the answer.

### 5. What the problem statement must NOT contain

- Class names or file names
- Pattern-specific role names (don't say "create a Receiver" or "your Invoker should...")
- Solution structure or architecture hints
- Code snippets

The problem describes the PROBLEM, not the SOLUTION.

---

## Progressive Hints Format

Provide a numbered list of hints alongside the problem statement. These are revealed one at a time, only when the user asks or is stuck.

### Hint Design Rules

1. **Order: broad → specific.** Early hints point to what to think about. Later hints address structural decisions and pitfalls.
2. **Never code snippets.** Hints are thinking prompts — "think about what state undo needs" not "add an orderId field."
3. **Each hint should unlock the next step.** If the user is stuck after Hint 3, Hint 4 should unblock them without giving away the full answer.
4. **Include anti-pattern warnings in later hints.** E.g., "if you're writing instanceof checks in this class, reconsider your design."
5. **The last hint can suggest data structures** without showing how to use them.

### How to Generate Hints

- **Derive from requirements.** Each hint should map to a design decision that one or more requirements force the user to make. Look at each requirement, identify the decision it forces, and turn that into a thinking prompt.
- **Use learned vocabulary.** Hints can reference terms the user learned during the explanation phases (e.g., "receiver," "observer"), since they've already built the mental model.
- **Mix questions and reframing statements.** Good hints alternate between asking questions ("What does undo mean here?") and reframing the user's thinking ("Cancellation is an operation, not a deletion").
- **One design decision per hint.** Each hint should cover roughly one class, one relationship, or one structural decision — not multiple at once.
- **The number of hints should vary per problem.** 5–8 is typical, but use as many as the problem needs.

---

## Review Checklist

After the user shares their implementation, review against these criteria. Give review comments, not rewrites — point out the issue and let the user fix it.

### Design Review

- Does the design follow the core principles of the concept being practiced?
- Are responsibilities cleanly separated between components?
- Can the system be extended along the dimension the pattern/concept is designed for, without modifying existing code?
- Are there any unnecessary dependencies or tight couplings between components?

### Code Review

- Are fields that should be immutable marked `final`?
- Is the domain object unaware of the pattern/abstraction wrapping it?
- Is the wiring/setup code separate from the execution/management code?
- Are there any `instanceof` checks, type switches, or other signs of leaking abstraction?

### Extensibility Test

- Pose a hypothetical that tests the concept's extensibility promise: "What if you needed to add [new variant]? What would change?"
- Evaluate whether the answer demonstrates the expected extensibility (e.g., one new class, no modification to existing ones).

---

## Example

See [examples/brokerage-command-pattern.md](examples/brokerage-command-pattern.md) for a complete example of a problem statement with hints, applied to the Command design pattern.
