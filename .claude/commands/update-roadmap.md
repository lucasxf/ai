---
description: Update project roadmap in ROADMAP.md
argument-hint: <what-was-completed>
---

@ROADMAP.md

**Update Project Roadmap**

What was completed: $ARGUMENTS

## Instructions

Update ROADMAP.md with the following steps:

### 1. Identify Completed Items
- Review what was just implemented/completed: $ARGUMENTS
- Check the "Next Steps (Priority Order)" list for matching items
- Check "In Progress" section for items that may now be completed

### 2. Move Completed Items to "Implemented" Section
- Move finished tasks from "In Progress" or "Next Steps" → "✅ Implemented"
- Add under appropriate subsection (Backend API / Mobile App / Infrastructure)
- Include what was delivered (brief bullet points)
- No need for completion date (tracked in git commits)

### 3. Update "In Progress" Section
- Remove completed items
- Add new items currently being worked on
- Keep only actively in-progress work (not future plans)

### 4. Update "Next Steps (Priority Order)"
- Remove completed items from priority list
- Promote next logical tasks from "Future Backlog" if applicable
- Reorder remaining items by new priority (1, 2, 3, 4...)
- Update task details if scope changed

### 5. Add New Discoveries
- If new tasks were discovered during implementation, add to:
  - "Next Steps" (if high priority)
  - "Future Backlog" (if post-MVP)
  - "Blocked/Waiting" (if waiting on decisions/dependencies)

### 6. Update Metadata
- Change "Last updated:" timestamp at top to today's date + session context
- Update "Metrics" table if test count or endpoint count changed

## Output Format

Show me a preview before editing:
1. What will be moved to "✅ Implemented" (and which subsection)
2. What the new "🎯 Next Steps" list will look like (Priority 1, 2, 3...)
3. Any new items added to "📍 Future Backlog" or "🚫 Blocked"
4. Updated "In Progress" section

Then update ROADMAP.md and show git diff for review.
