---
agent: agent
description: "Save current session context to handoffs — mid-session checkpoint"
tools:
  - search/codebase
  - edit/editFiles
---

# Save Session Context

Checkpoint the current session so it can be resumed in a new chat window.

## Steps

1. Ask me to briefly describe what I've been working on and where I am (if not obvious from conversation history)
2. Capture the current state:
   - What I'm working on and what stage (coding V1 / reviewing / planning V2 / coding V2 / comparing)
   - Any in-progress plan with progress markers
   - Open questions or unresolved decisions
   - Scratch notes — anything from this session that would be lost without saving
3. Overwrite `.github/copilot/handoffs.md` with the detailed state — be **verbose**, preserve all working context so another chat session can fully resume
4. Confirm what was saved
