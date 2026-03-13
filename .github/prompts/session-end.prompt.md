---
agent: agent
description: "End session — save context, update profile and session log"
tools:
  - search/codebase
  - edit/editFiles
  - memory/*
---

# Session End

Wrap up this session and save context for next time.

## Steps

1. Summarize what was accomplished in this session (be specific — files created/modified, concepts covered, decisions made)
2. Update `.github/copilot/session-log.md` — append a new session entry with:
   - Focus
   - Concepts covered (new or revisited)
   - Decisions made
   - Profile updates (if any)
   - Next steps
3. Update `.github/copilot/current-focus.md` with:
   - What to pick up next session
   - Any in-progress work
4. If the learner used new Java features or learned new patterns during this session, update `.github/copilot/learner-profile.md` accordingly
5. If a new project was started or completed, update `.github/copilot/project-index.md`
6. Confirm what was saved
