---
description: Add a new coding directive with smart deduplication to appropriate coding style file
argument-hint: <directive-content>
---

Add the following coding directive with smart deduplication:

**New Directive:** $ARGUMENTS

**Workflow:**

1. **Search for Similar Directives**
   - Search in CLAUDE.md (all parts: General/Backend/Frontend/Infrastructure)
   - Search in CODING_STYLE_GENERAL.md (universal cross-stack conventions)
   - Search in services/api/CODING_STYLE_BACKEND.md (Java/Spring Boot)
   - Search in apps/mobile/CODING_STYLE_FRONTEND.md (Flutter/Dart)
   - Search in infra/CODING_STYLE_INFRASTRUCTURE.md (Docker/CI/CD)
   - Look for exact matches, similar wording, or related concepts

2. **Analyze Result**
   - **EXACT MATCH FOUND** → Inform user: "This directive already exists in [file] [section]. No action taken."
   - **SIMILAR DIRECTIVE FOUND** → Show existing directive, ask: "A similar directive exists:\n\n[existing]\n\nDo you want to:\na) Update existing directive\nb) Add as separate directive\nc) Skip (no action)"
   - **ENTIRELY NEW** → Proceed to step 3

3. **Determine Correct File + Section**
   - **Backend-specific** (Java, Spring Boot, Maven, JPA) → services/api/CODING_STYLE_BACKEND.md
   - **Frontend-specific** (Flutter, Dart, Riverpod, widgets) → apps/mobile/CODING_STYLE_FRONTEND.md
   - **Infrastructure** (Docker, Testcontainers, CI/CD) → infra/CODING_STYLE_INFRASTRUCTURE.md
   - **Architecture/Project-wide** (DDD, testing strategy, documentation) → CLAUDE.md (appropriate part)
   - **Cross-stack conventions** (naming, Git workflow, universal rules) → CODING_STYLE_GENERAL.md

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
