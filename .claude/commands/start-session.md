---
description: Start a new development session with stack-specific context loading
argument-hint: <optional: --stack=backend|docs|full or context description>
---

# Session Context Loading Strategy

## Step 1: Determine Stack Focus

**If user provided `--stack` parameter:**
- `--stack=backend` → Load backend-specific files (Java/Spring Boot/MCP)
- `--stack=docs` → Load minimal documentation files
- `--stack=full` → Load all files (legacy behavior)

**If NO `--stack` parameter provided:**
- Analyze `$ARGUMENTS` for context clues:
  - Keywords: "MCP", "client", "server", "protocol", "Java", "Spring", "POC" → **backend**
  - Keywords: "documentation", "README", "CLAUDE.md", "docs", "LEARNINGS" → **docs**
- If ambiguous or unclear, use **AskUserQuestion** to prompt:
  - Question: "Which stack are you working on for this session?"
  - Options:
    1. Backend (Java/Spring Boot/MCP) - POCs, servers, clients, protocol implementation
    2. Documentation - CLAUDE.md, README.md, LEARNINGS.md, coding styles
    3. Full Context - Load everything (use sparingly)

## Step 2: Load Files Based on Stack

### Backend Session (AI/MCP Development)
@CLAUDE.md
@CODING_STYLE.md
@ROADMAP.md
@README.md
@.claude/agents-readme.md
@LEARNINGS.md

### Documentation Session
@CLAUDE.md
@ROADMAP.md
@README.md
@.claude/agents-readme.md

### Full Context Session (Legacy)
@CLAUDE.md
@CODING_STYLE.md
@ROADMAP.md
@README.md
@LEARNINGS.md
@.claude/agents-readme.md

**Session Context:** $ARGUMENTS

## Step 3: Standard Session Initialization

1. Review ROADMAP.md to understand:
   - Current implementation status
   - What's in progress
   - Next priority tasks (Priority 1, 2, 3...)
2. Review .claude/agents-readme.md to understand:
   - Available specialized agents
   - When to trigger each agent proactively
3. Review recent commits with `git log --oneline -5` to understand latest changes
4. Check current git status to see uncommitted changes
5. Provide a brief summary of:
   - **Stack focus for this session** (backend/docs/full)
   - Current project state (from ROADMAP.md)
   - Next priority tasks (from "Next Steps" section)
   - Any uncommitted changes that need attention
   - Quick reminder of key architectural patterns (from CLAUDE.md)
   - Available agents for this session's work
   - **Token savings achieved** (if stack-specific loading was used)

**Ready to start development following all project guidelines.**

**Note:** LEARNINGS.md is loaded for backend sessions (MCP learning insights) but skipped for docs-only sessions.

## Token Savings Report

After loading, report approximate token savings:
- Backend session: ~15% reduction vs full load (LEARNINGS.md included)
- Documentation session: ~45% reduction vs full load
