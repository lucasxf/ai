---
created: 2025-10-31
session: 2025-10-31
user-request: "Create `/save-response` command for saving narrow context from Claude responses"
---

# Save-Response Command Implementation Plan

## Command Purpose

Save specific sections from Claude's responses (plans, specifications, decisions) to `prompts/responses/` for later retrieval with `/resume-session`.

## Implementation Steps Completed

1. **Created `/save-response` command** (.claude/commands/save-response.md)
   - Supports custom filenames: `/save-response my-plan`
   - Auto-generates smart filenames if none provided
   - Handles ambiguity (multiple savable sections)
   - Formats content with metadata (date, context, next steps)
   - Updates optional INDEX.md

2. **Enhanced `/resume-session`** (.claude/commands/resume-session.md)
   - With argument: loads matching file
   - Without argument: lists all files for selection
   - Full path support: `@prompts/responses/file.md`

3. **Auto-Approval Configuration** (.claude/settings.json)
   - `write.autoApprove: ["prompts/responses/*.md"]`
   - `edit.autoApprove: ["prompts/responses/*.md"]`
   - No permission prompts when using `/save-response`

4. **Directory Structure** (prompts/responses/)
   - Created INDEX.md for reference
   - Listed existing plans

5. **Documentation** (CLAUDE.md)
   - Added commands to Workflow Commands section

## Usage Workflow

### Scenario A: Save with custom filename
```bash
User: "Create a plan for Humming Bird feature"
Claude: [outputs detailed plan]
User: /save-response humming-bird-feature
Claude: ✅ Saved to prompts/responses/humming-bird-feature.md
```

### Scenario B: Auto-generate filename
```bash
User: "Review authentication architecture"
Claude: [outputs review]
User: /save-response
Claude: Saving to: prompts/responses/authentication-review-2025-10-31.md
        Press Enter to confirm:
```

### Scenario C: Resume later
```bash
User: /resume-session
Claude: Available files:
        1. humming-bird-feature.md (2025-10-31)
        2. authentication-review.md (2025-10-29)
        Select:
```

## Key Design Decisions

1. **Content Extraction (Smart, Not Everything)**
   - Saves ONLY structured content (plans, tables, specs)
   - Does NOT save conversational fluff
   - Handles ambiguity by asking user which section

2. **Filename Generation (Context-Aware)**
   - Format: `{feature-name}-{action}-{date}.md`
   - Extracts key terms from user question

3. **Permission-Free Workflow**
   - Auto-approved writes to `prompts/responses/*.md`

4. **Integration with Existing Workflow**
   - Works with `/resume-session`
   - Does NOT duplicate `/finish-session`
   - Complements ROADMAP.md and LEARNINGS.md

---

## Context (Auto-generated)

**Current Project State:**
- Branch: feature/comment-system
- Last commit: 982ecc1 feat(backend): Add Comment System service layer with DTOs (WIP - Step 2/6)

**Files Created:**
- `.claude/commands/save-response.md`
- `.claude/commands/resume-session.md` (enhanced)
- `.claude/settings.json` (updated)
- `prompts/responses/INDEX.md`
- `CLAUDE.md` (updated)

**Next Steps:**
1. Test the command with a real use case
2. Commit changes with proper semantic message
3. Use in practice for next planning session
4. Gather feedback on filename auto-generation
