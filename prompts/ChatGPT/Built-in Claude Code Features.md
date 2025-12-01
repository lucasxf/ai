 Built-in Claude Code Features

  /resume (built-in)

  The `/resume` command is a Claude Code built-in that allows you to continue a previous conversation thread. It brings back the chat history and
  context from a prior session, letting you pick up where you left off in that specific conversation.

  # keyword (built-in)

  The # symbol in Claude Code is a file reference tool. When you type #, it opens a file picker that lets you select files to add to the current
  conversation's context. It's essentially saying "hey, look at this file while we talk."

  ---
  Your Custom Tools vs Built-ins

  /directive vs # - Key Differences

  They serve fundamentally different purposes:

  | Feature      | # (Read)                              | /directive (Write)                                                      |
  |--------------|---------------------------------------|-------------------------------------------------------------------------|
  | Purpose      | Reference files passively             | Modify documentation actively                                           |
  | Action       | Includes file content in conversation | Writes new directive to CLAUDE.md or CODING_STYLE.md                    |
  | Intelligence | None (just loads content)             | Smart deduplication, similar directive detection                        |
  | Workflow     | One-step (select file)                | Multi-step (search → deduplicate → determine section → format → commit) |
  | Use Case     | "Look at this file while we chat"     | "Permanently add this coding rule to project conventions"               |

  Why both exist:
  - Use # when you want to discuss a file's contents
  - Use /directive when you want to codify a new coding convention into your project's standards with automatic organization and deduplication

  Example scenarios:
  - #CLAUDE.md → "Let me read the current conventions while we talk"
  - /directive "Always use constructor injection" → "Add this rule permanently to CODING_STYLE.md Part 2: Backend, check for duplicates first,
  format it properly, and optionally commit"

  ---
  /resume-session vs /resume - Enhanced Context Reconstruction

  Your /resume-session is much more sophisticated than the built-in /resume:

  | Feature              | /resume (built-in)      | /resume-session (yours)                                                              |
  |----------------------|-------------------------|--------------------------------------------------------------------------------------|
  | Chat History         | ✅ Restores conversation | ❌ Starts fresh (context from files)                                                  |
  | Project Context      | ❌ No automatic loading  | ✅ Auto-loads CLAUDE.md, CODING_STYLE.md, ROADMAP.md, README.md                       |
  | Session Context      | ❌ No                    | ✅ Loads saved context from prompts/responses/                                        |
  | Git Awareness        | ❌ No                    | ✅ Reviews recent commits, checks status                                              |
  | Progress Tracking    | ❌ No                    | ✅ Identifies current step/milestone from commits + context file                      |
  | Smart File Selection | ❌ Manual                | ✅ Auto-discovery, fuzzy matching, date sorting                                       |
  | Summary Report       | ❌ No                    | ✅ Provides: last session context, current progress, uncommitted changes, next action |

  Why `/resume-session` is better for development work:
  - `/resume` just brings back the chat (which may be outdated or irrelevant)
  - `/resume-session` reconstructs the development state by combining:
    - Project documentation (CLAUDE.md, ROADMAP.md)
    - Your saved plan/notes (from `prompts/responses/`)
    - Git history (what you actually implemented)
    - Current git status (what's uncommitted)

  Real-world example:
  # Built-in (limited)
  /resume  # Just brings back yesterday's chat

  # Yours (comprehensive)
  /resume-session comment-system
  # → Loads CLAUDE.md + CODING_STYLE.md + ROADMAP.md
  # → Finds prompts/responses/comment-system-plan.md
  # → Shows git log (last 10 commits)
  # → Shows git status (uncommitted changes)
  # → Identifies: "You're on Step 3/6 of comment-system feature"
  # → Next Action: "Implement CommentService tests"

  ---
  /review-code vs Specialized Agents - Complementary, Not Redundant

  Your /review-code command and agents serve different purposes:

  /review-code - Breadth-First Audit

  - Scope: General code quality sweep across codebase
  - Focus: Checklist-based audit (conventions, tests, docs, security, performance)
  - Output: Structured report with metrics (% coverage, % convention adherence)
  - Use Case: "Give me a health check before committing" or "What's the state of this module?"
  - Speed: Fast, automated checks
  - Depth: Surface-level (identifies issues, doesn't deeply educate)

  backend-code-reviewer - Depth-First Review

  - Scope: Expert Java/Spring Boot code review
  - Focus: Educational, detailed analysis with explanations
  - Output: Line-by-line review with "why this matters" + alternative approaches
  - Use Case: "Review my ReviewService implementation and teach me best practices"
  - Speed: Slower, thorough analysis
  - Depth: Deep (explains patterns, shows examples, references docs)

  frontend-ux-specialist - Design Focus, Not Code Review

  - Scope: UI/UX design, screen layouts, user experience
  - Focus: Material Design principles, accessibility, visual hierarchy
  - Output: Wireframes, design rationale, Flutter implementation code
  - Use Case: "How should the login screen look?" or "Design the review feed"
  - Depth: Design patterns, not code quality review

  ---
  Is /review-code Still Relevant?

  Yes, absolutely. Here's when to use each:

  | Scenario                                  | Tool                   | Why                                 |
  |-------------------------------------------|------------------------|-------------------------------------|
  | "Quick health check before committing"    | /review-code           | Fast breadth audit                  |
  | "Is this codebase following conventions?" | /review-code           | Structured checklist with metrics   |
  | "Review my new ReviewService deeply"      | backend-code-reviewer  | Educational, detailed, line-by-line |
  | "I want to learn Spring Boot patterns"    | backend-code-reviewer  | Teaching focus with examples        |
  | "How should this screen look?"            | frontend-ux-specialist | Design, not code review             |
  | "Pre-commit check across module"          | /review-code           | Broad coverage, quick               |
  | "PR review before merging"                | backend-code-reviewer  | Thorough expert review              |

  They complement each other:
  1. Run /review-code for quick audit → identifies 5 issues
  2. Use backend-code-reviewer for deep dive → explains why and how to fix
  3. Use frontend-ux-specialist for UI design → creates screen layouts

  ---
  Summary for Your Article

  Your custom automation fills gaps that Claude Code's built-ins don't address:

  - # is passive (read files) → /directive is active (write conventions with intelligence)
  - /resume is conversational (restore chat) → /resume-session is developmental (reconstruct project state from git + docs + notes)
  - Built-in tools lack domain expertise → Your agents provide specialized, educational reviews (backend expert, UX designer)
  - /review-code provides breadth (audit) → Agents provide depth (education)

  The key insight: You've built a development workflow automation layer on top of Claude Code's conversational layer. Claude Code gives you the
  conversation primitives (#, /resume), and you've built project-aware, git-integrated, convention-enforcing, educational tooling on top.


 The Manual Way (Without /directive)

  Without your custom command, you'd have to do this every time:

  # Step 1: Reference the file
  #CLAUDE.md

  # Step 2: Manually instruct
  "Search this file for any directives similar to 'Always use constructor injection'"

  # Step 3: Wait for Claude to search
  # Step 4: Tell Claude
  "Add this directive to Part 2: Backend section under Spring Boot Best Practices"

  # Step 5: Tell Claude
  "Format it as a bullet point with today's date"

  # Step 6: Review the changes
  # Step 7: Manually commit
  git add CLAUDE.md
  git commit -m "docs: Add directive - constructor injection"

  Your /directive Automates This Entire Workflow

  /directive "Always use constructor injection"

  # One command does:
  # ✅ Searches CLAUDE.md + CODING_STYLE.md for duplicates/similar
  # ✅ Detects correct file + section (Backend-specific)
  # ✅ Formats with bullet point + timestamp
  # ✅ Shows git diff for review
  # ✅ Prompts for commit with proper message format

  ---
  Why You Might Have Thought It Was Redundant

  You might have been thinking:
  - "Can't I just ask Claude to add it to CLAUDE.md?" → Yes, but you have to manually guide the entire process each time
  - "Can't I use #CLAUDE.md and tell Claude what to do?" → Yes, but that's 5-7 steps vs 1 command
  - "Doesn't Claude understand project conventions?" → Yes, but it doesn't automatically organize them without explicit instructions

  ---
  The Value of /directive

  Your /directive command is workflow automation — it:

  1. Encodes domain knowledge - Knows about your 3-part structure (General/Backend/Frontend)
  2. Prevents mistakes - Deduplication prevents redundant directives
  3. Enforces consistency - Always formats the same way with timestamps
  4. Saves cognitive load - You don't have to remember the workflow each time
  5. Creates muscle memory - /directive "..." becomes automatic

  ---
  Updated Summary for Your Article

  Your /directive command is NOT redundant. Claude Code has no built-in way to codify coding conventions into documentation with smart
  organization and deduplication.

  The pattern you've discovered:
  - Claude Code provides primitives (# for file reference, chat interface)
  - Your slash commands encode workflows (multi-step processes with domain logic)
  - Result: One command (/directive) replaces 5-7 manual steps and prevents errors

  This applies to your other commands too:
  - /resume-session - No built-in way to reconstruct dev state from git + docs + saved notes
  - /review-code - No built-in checklist-based audit command
  - /finish-session - No built-in workflow for test → doc → commit → PR

  You've essentially built a "project management DSL" on top of Claude Code's conversational interface.
