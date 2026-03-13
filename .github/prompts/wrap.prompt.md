---
agent: agent
description: "Wrap up session — condense handoffs and update all context files"
tools:
  - search/codebase
  - edit/editFiles
---

# Wrap Up Session

End-of-session cleanup. Condense the working state and update permanent records.

## Job 1 — Condense handoffs.md

1. Read `.github/copilot/handoffs.md`
2. Strip completed plan steps, resolved questions, and stale scratch notes
3. Compress remaining content to the minimum needed to resume next session
4. Overwrite `handoffs.md` with the condensed version

## Job 2 — Update persistent context files

1. **session-log.md**: Append a new session entry with:
   - Focus: what was worked on
   - Concepts covered (new or revisited)
   - Decisions made
   - Profile updates (if any)
   - Next steps
2. **learner-profile.md**: Update only if new Java features or patterns were used in this session
3. **project-index.md**: Update only if a project was started or completed

## Confirm

Summarize what was updated across all files.
