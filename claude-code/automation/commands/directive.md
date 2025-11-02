---
description: Add a new coding directive to CLAUDE.md or CODING_STYLE.md with smart deduplication
argument-hint: <directive-content>
---

Add the following coding directive with smart deduplication:

**New Directive:** $ARGUMENTS

**Workflow:**

1. **Search for Similar Directives**
   - Search in CLAUDE.md (all parts: General/Backend/Frontend/Infrastructure)
   - Search in CODING_STYLE.md (all parts: General/Backend/Frontend/Infrastructure)
   - Look for exact matches, similar wording, or related concepts

2. **Analyze Result**
   - **EXACT MATCH FOUND** → Inform user: "This directive already exists in [file] [section]. No action taken."
   - **SIMILAR DIRECTIVE FOUND** → Show existing directive, ask: "A similar directive exists:\n\n[existing]\n\nDo you want to:\na) Update existing directive\nb) Add as separate directive\nc) Skip (no action)"
   - **ENTIRELY NEW** → Proceed to step 3

3. **Determine Correct File + Section**
   - **Backend-specific** (Java, Spring Boot, Maven, JPA) → CODING_STYLE.md Part 2: Backend
   - **Frontend-specific** (Flutter, Dart, Riverpod, widgets) → CODING_STYLE.md Part 3: Frontend
   - **Infrastructure** (Docker, Testcontainers, CI/CD) → CODING_STYLE.md Part 4: Infrastructure
   - **Architecture/Project-wide** (DDD, testing strategy, documentation) → CLAUDE.md Part 1: General
   - **Cross-stack conventions** (naming, Git workflow) → CODING_STYLE.md Part 1: General

4. **Add Directive**
   - Format with proper markdown (bullet point or subsection as appropriate)
   - Include clear examples if applicable
   - Add timestamp: "(Added YYYY-MM-DD)"
   - Add to appropriate section (don't create new sections unless necessary)

5. **Show Changes**
   - Display git diff for user review
   - Summarize: "Added directive to [file] → [section]"

6. **Commit Prompt**
   - Ask: "Commit this change? (y/n)"
   - If yes, create commit with message: "docs: Add directive - [brief summary]"

**Important:**
- Avoid duplicates at all costs
- When in doubt about file/section, ask user
- Use clear, concise language
- Follow existing formatting style in target file
