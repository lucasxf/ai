---
description: Resume development session with context from previous session
argument-hint: <feature-or-context-file>
---

@CLAUDE.md
@CODING_STYLE.md
@ROADMAP.md
@README.md

**Resuming Session:** $ARGUMENTS

## Session Context Loading

1. Load standard project context (CLAUDE.md, CODING_STYLE.md, ROADMAP.md, README.md) ✅

2. **Context File Selection:**

   **If `$ARGUMENTS` is EMPTY:**
   - List all files in `prompts/responses/` (sorted by modification date, newest first)
   - Show each file with: filename, date, and first line (brief description)
   - Offer options:
     1. Auto-load latest file (most recent by modification date)
     2. Select by number from list
     3. Type filename manually
     4. Skip and continue without specific context
   - Wait for user input

   **If `$ARGUMENTS` provided (e.g., "comment-system"):**
   - Search for context file matching: `prompts/responses/*{$ARGUMENTS}*.md`
   - If exact match → load it
   - If multiple matches → list them and ask which to load
   - If no match → show available files and let user pick

   **If `$ARGUMENTS` is a full path (e.g., "@prompts/responses/file.md"):**
   - Load that specific file directly

3. Review recent commits with `git log --oneline -10` to see progress since last session
4. Check current git status for uncommitted changes
5. Identify current step/milestone from:
   - Last commit message (e.g., "WIP - Step 2/6")
   - Modified files in git status
   - Content of loaded context file (plan/checklist)

## Summary Output

Provide a concise summary:
- **Last Session Context:** [from loaded context file]
- **Current Progress:** [which step/milestone based on git status]
- **Last Commit:** [from git log]
- **Uncommitted Changes:** [files modified]
- **Next Action:** [what should happen next based on plan]
- **Available Agents:** [relevant agents for this work]

**Ready to continue from where you left off.**

---

## Integration with /save-response

This command works seamlessly with `/save-response`:
- Files saved with `/save-response` are auto-discovered in `prompts/responses/`
- Use filename (without extension) to load: `/resume-session comment-system-plan`
- Or use no arguments to see all available files: `/resume-session`

---

## Examples

**Example 1: Resume with specific context**
```bash
/resume-session comment-system-implementation
# → Loads prompts/responses/comment-system-implementation.md + project context
```

**Example 2: List and select**
```bash
/resume-session
# → Shows list:
#    1. comment-system-implementation.md (2025-10-31) - 6-step backend plan
#    2. authentication-review.md (2025-10-29) - Architecture review
#    3. humming-bird-feature.md (2025-10-28) - Feature specification
#
# Select option (1-3) or press Enter for latest: [waits for input]
```

**Example 3: No matches, show options**
```bash
/resume-session nonexistent-feature
# → No matches found for "nonexistent-feature"
# → Available files in prompts/responses/:
#    [shows list]
# → Enter filename or press Enter to skip: [waits for input]
```
