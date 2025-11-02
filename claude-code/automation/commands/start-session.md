---
description: Start a new development session with standard context
argument-hint: <optional-context>
---

@CLAUDE.md
@CODING_STYLE.md
@ROADMAP.md
@README.md
@.claude/agents/README.md

**Session Context:** $ARGUMENTS

## Standard Session Initialization

1. Review ROADMAP.md to understand:
   - Current implementation status
   - What's in progress
   - Next priority tasks (Priority 1, 2, 3...)
2. Review .claude/agents/README.md to understand:
   - Available specialized agents
   - When to trigger each agent proactively
3. Review recent commits with `git log --oneline -5` to understand latest changes
4. Check current git status to see uncommitted changes
5. Provide a brief summary of:
   - Current project state (from ROADMAP.md)
   - Next priority tasks (from "Next Steps" section)
   - Any uncommitted changes that need attention
   - Quick reminder of key architectural patterns (from CLAUDE.md)
   - Available agents for this session's work

**Ready to start development following all project guidelines.**

**Note:** Load LEARNINGS.md only if user mentions investigating past decisions or specific session details.
