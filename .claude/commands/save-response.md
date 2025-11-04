---
description: Save Claude's last response (or specific section) to prompts/responses/ for later retrieval
argument-hint: <optional-filename>
---

**Save Response Workflow**

This command saves specific content from Claude's last response to a file in `prompts/responses/` for later retrieval with `/resume-session`.

**Arguments:**
- `$ARGUMENTS` (optional): Filename without extension (e.g., `comment-system-plan`)
- If empty: Auto-generate filename from context

---

## Step 1: Identify Content to Save

**Analyze my previous response** and identify savable sections:
- TodoWrite plans/task lists
- Implementation plans (tables, step-by-step guides)
- Architecture decisions
- Technical specifications
- Code review results

**If multiple savable sections exist:**
- List all sections with numbers
- Ask user: "Which section should I save? (1/2/3/all)"
- Wait for user response

**If only one savable section:**
- Proceed to save it automatically

**If no clear savable content:**
- Error: "No savable content found in last response. Use this command after requesting plans, specifications, or structured output."

---

## Step 2: Generate Filename

**If `$ARGUMENTS` provided:**
- Use as filename: `prompts/responses/{$ARGUMENTS}.md`
- Example: `/save-response comment-system-plan` → `prompts/responses/comment-system-plan.md`

**If `$ARGUMENTS` empty (auto-generate):**
1. Extract key terms from user's last message (feature names, action verbs)
2. Format: `{feature-name}-{action}-{YYYY-MM-DD}.md`
3. Examples:
   - User asked: "plan for comment system" → `comment-system-plan-2025-10-31.md`
   - User asked: "review authentication flow" → `authentication-review-2025-10-31.md`
   - User asked: "implement Humming Bird feature" → `humming-bird-implementation-2025-10-31.md`

**Show proposed filename to user:**
- "Saving to: `prompts/responses/{filename}.md`"
- "Press Enter to confirm, or type new filename:"
- Wait for user input

---

## Step 3: Format Content

Create file with this structure:

```markdown
---
created: {YYYY-MM-DD}
session: {session-date}
user-request: "{brief summary of user's question}"
---

# {Descriptive Title}

{CONTENT TO SAVE - ONLY THE RELEVANT SECTION}

---

## Context (Auto-generated)

**Current Project State:**
- Branch: {git branch name}
- Last commit: {git log -1 --oneline}

**Related Files:**
{List of files mentioned in the saved content, if any}

**Next Steps:**
{If plan has next steps, include them here}
```

**Important:**
- DO NOT include metadata from Claude's response (like "Here's a plan...")
- DO NOT include conversational fluff ("Let me help you...")
- ONLY include the structured content itself (plan, table, steps, decisions)

---

## Step 4: Save File

1. Create `prompts/responses/` directory if it doesn't exist
2. Write file to `prompts/responses/{filename}.md`
3. Confirm to user: "✅ Saved to `prompts/responses/{filename}.md`"
4. Remind user: "Use `/resume-session {filename}` to load this context later"

---

## Step 5: Optional - Update Index

Check if `prompts/responses/INDEX.md` exists:
- If YES: Append entry to index
- If NO: Skip (index is optional)

Index entry format:
```markdown
- **{filename}.md** ({date}) - {brief description}
```

---

## Examples

### Example 1: Save with custom filename
```
User: "Create a plan for implementing the comment system"
Claude: [outputs detailed plan with TodoWrite]
User: /save-response comment-system-implementation
Claude: ✅ Saved to prompts/responses/comment-system-implementation.md
```

### Example 2: Auto-generate filename
```
User: "Review the authentication architecture and suggest improvements"
Claude: [outputs architecture review with recommendations]
User: /save-response
Claude: Saving to: prompts/responses/authentication-review-2025-10-31.md
        Press Enter to confirm, or type new filename: [waits]
User: [presses Enter]
Claude: ✅ Saved to prompts/responses/authentication-review-2025-10-31.md
```

### Example 3: Multiple sections (ambiguity)
```
User: "Plan the Humming Bird feature AND plan the Humming Bird screen"
Claude: [outputs two plans]
User: /save-response
Claude: Found multiple savable sections:
        1. Humming Bird Feature Plan (macro - 12 steps)
        2. Humming Bird Screen Plan (micro - 4 steps)
        3. All sections combined
        Which should I save? (1/2/3): [waits]
User: 1
Claude: ✅ Saved to prompts/responses/humming-bird-feature-plan-2025-10-31.md
```

---

## Integration with /resume-session

Files saved with `/save-response` are automatically discoverable by `/resume-session`:

```bash
# Load specific file
/resume-session comment-system-implementation

# List all saved responses (if no argument provided)
/resume-session
# → Shows list of files in prompts/responses/
```

---

## Permissions Note

This command has **auto-approval** for writing to `prompts/responses/*.md` (configured in `.claude/settings.json`). No permission prompts will appear.

---

## Anti-Patterns (What NOT to Save)

❌ **Don't save:**
- Entire conversation transcripts
- ROADMAP.md updates (use `/finish-session` instead)
- LEARNINGS.md entries (use `/finish-session` instead)
- General explanations without structure
- Metadata or conversational responses

✅ **Do save:**
- Implementation plans (step-by-step)
- TodoWrite task lists
- Architecture decision documents
- Technical specifications
- Code review results with action items
- Feature design documents
