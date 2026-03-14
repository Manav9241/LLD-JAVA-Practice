# Reusable Session Blueprint

---

## Phase 1 — Setup + V1 (you alone)

1. `/start`
   - "Starting Lecture {N} {Topic}. Going to code V1 from the lecture first."

2. Code V1 yourself (following lecture)
   - No prompts needed. This is your transcription pass.
   - `/save` if you need to split across chat sessions.

## Phase 2 — Understand (agent helps)

3. `/explain`
   - "Explain the pattern/concepts used in my V1. Why did the lecture design it this way? What problem does this structure solve?"

4. `/review`
   - "Review my V1. What's good in the design? What could be better at the design level?"

## Phase 3 — Improve (you code, agent plans)

5. `/plan`
   - "Based on the review, plan a V2 that addresses those improvements. Give me class/interface structure only."

6. Code V2 yourself
   - Apply the planned improvements. Same sketch fidelity as V1.

## Phase 4 — Reflect + Close

7. `/compare`
   - "Compare V1 vs V2. What improved in the design and why?"

8. `/wrap`
   - End session. Condense + update permanent files.

---

## Stage-to-Prompt Map

| Your stage | Prompt | Why |
|-----------|--------|-----|
| Just opened chat | `/start` | Restore context |
| Coding V1 from lecture | None | You're following the lecture |
| V1 done, want to understand WHY | `/explain` | Internalize the design rationale |
| Want feedback on V1 design | `/review` | Get max 2 design improvements |
| Want to plan V2 based on review | `/plan` | Shape the improved design |
| Coding V2 | None | You're applying improvements |
| V2 done, want evolution analysis | `/compare` | Structured reflection |
| Might lose context | `/save` | Checkpoint anytime |
| Done for the day | `/wrap` | Persist + condense |

---

## Quick Flow

```
/start → "Lecture {N}, coding V1 from lecture"
  ↓
Code V1 (no prompts)
  ↓
/explain → "Why is V1 designed this way?"
  ↓
/review → "What's good? What could improve?"
  ↓
/plan → "Plan V2 based on review feedback"
  ↓
Code V2 (no prompts)
  ↓
/compare → "V1 vs V2 evolution"
  ↓
/wrap
```
