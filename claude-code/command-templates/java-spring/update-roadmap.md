---
description: Update Next Steps roadmap in CLAUDE.md
argument-hint: <what-was-completed>
---

**Update Project Roadmap**

What was completed: $ARGUMENTS

## Instructions

Update the "Next Steps (Roadmap)" section in CLAUDE.md (or equivalent project documentation):

### 1. Identify Completed Items
- Review what was just implemented/completed
- Check the "Immediate Next Steps" list for matching items

### 2. Move Completed Items
- Move finished tasks from "Immediate Next Steps" to "✅ Implemented" section
- Include completion date (today: use current date)
- Add brief description of what was delivered

### 3. Update Immediate Next Steps
- Remove completed items from priority list
- Promote next logical tasks from "Future Backlog" if applicable
- Reorder remaining items by new priority

### 4. Add New Discoveries
- If new tasks were discovered during implementation, add them to appropriate section
- Mark blockers in "Blocked/Waiting" if applicable

### 5. Update "Last updated" Timestamp
- Change the "Last updated:" line to today's date
- Add session number if tracking sessions

## Example Output

Show me:
1. What will be moved to "✅ Implemented"
2. What the new "Immediate Next Steps" list will look like
3. Any new items added to "Future Backlog"

Then update CLAUDE.md and show git diff for review.

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Documentation file:** Replace `CLAUDE.md` with your actual roadmap document:
   - `ROADMAP.md`
   - `TODO.md`
   - `docs/planning.md`
   - `PROJECT.md`

2. **Section names:** Update section names if your roadmap uses different structure:
   - "Next Steps (Roadmap)" → "TODO", "Backlog", "Sprint Planning"
   - "✅ Implemented" → "Done", "Completed", "Changelog"
   - "Immediate Next Steps" → "Current Sprint", "In Progress", "Up Next"
   - "Future Backlog" → "Icebox", "Future Work", "Nice to Have"

3. **Date format:** Adjust date format if your project uses different convention:
   - `2025-10-22` (ISO 8601)
   - `October 22, 2025`
   - `22/10/2025`

4. **Session tracking:** If not tracking sessions, remove session number logic

5. **Issue tracker integration:** If using GitHub Issues, Jira, etc., add links:
   ```markdown
   - ✅ Implement feature X ([#123](https://github.com/user/repo/issues/123))
   ```
