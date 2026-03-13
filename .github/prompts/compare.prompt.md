---
agent: agent
description: "Compare my V1 vs V2 design — structured evolution analysis"
tools:
  - search/codebase
  - github/*
---

# Compare Designs

Analyze the evolution between two versions of my code (e.g., MyDesign → BetterDesign, V01 → V02, BadDesign → BestDesign).

## Comparison Guidelines

1. Read `.github/copilot/learner-profile.md` for level context
2. Read both versions of the code being compared
3. Structure the comparison as:

### What Changed (Table Format)

| Aspect | V1  | V2  | Why Better |
| ------ | --- | --- | ---------- |

### Principles Applied

- Which SOLID principles were applied or improved?
- Which design patterns were introduced or improved?
- What architectural improvements were made?

### New Concepts Introduced

- List any new Java features, patterns, or techniques used in V2 that weren't in V1
- For each, briefly explain what it is and why it was used

### What Was Already Good in V1

- Acknowledge what the learner got right from the start

### Remaining Opportunities

- AT MOST 2 suggestions for a potential V3, scoped to current learning level
- Flag anything that would require a new concept as **"Would require new concept: X"**
