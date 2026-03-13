---
agent: agent
description: "Explain a concept or pattern using my existing code as reference points"
tools:
  - search/codebase
  - github/*
---

# Explain Concept

Explain a concept, pattern, or principle to me using my own code as anchor points.

## Explanation Guidelines

1. Read `.github/copilot/learner-profile.md` to know what I already understand
2. Read `.github/copilot/project-index.md` to find my existing implementations
3. When explaining:
   - Start with the core idea in 2-3 sentences
   - Connect to something I already know: "You used X in your P02_TomatoFoodDeliveryApp — this is similar because..."
   - Use a simple analogy if the concept is new
   - Show how it relates to patterns/principles I've already practiced
   - If there's a Coder Army lecture covering this, reference it
4. If explaining a GoF pattern:
   - Intent (one sentence)
   - When to use it (problem it solves)
   - Key participants (interface names, not full code)
   - How it relates to patterns I already know
   - A simple example scenario
5. Do NOT dump a full implementation — keep it conceptual unless I ask for code
