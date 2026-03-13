---
agent: agent
description: "Start a new learning session — loads context and orients the conversation"
tools:
  - search/codebase
  - memory/*
---

# Session Start

Load my learning context and help me pick up where I left off.

## Steps

1. Read `.github/copilot/current-focus.md` to understand what I was last working on
2. Read `.github/copilot/session-log.md` — check the most recent session entry
3. Read `.github/copilot/learner-profile.md` if you need to recalibrate on my level
4. Summarize briefly:
   - What I was working on last time
   - Any pending next steps from the previous session
   - Where I am in the Coder Army lecture progression
5. Ask me what I want to work on today
