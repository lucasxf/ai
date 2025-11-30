  Why /resume-session is better for development work:
  - /resume just brings back the chat (which may be outdated or irrelevant)
  - /resume-session reconstructs the development state by combining:
    - Project documentation (CLAUDE.md, ROADMAP.md)
    - Your saved plan/notes (from prompts/responses/)
    - Git history (what you actually implemented)
    - Current git status (what's uncommitted)


| Feature              | /resume (built-in)      | /resume-session (yours)                                                              |
|----------------------|-------------------------|--------------------------------------------------------------------------------------|
| Chat History         | ✅ Restores conversation | ❌ Starts fresh (context from files)                                                  |
| Project Context      | ❌ No automatic loading  | ✅ Auto-loads CLAUDE.md, CODING_STYLE.md, ROADMAP.md, README.md                       |
| Session Context      | ❌ No                    | ✅ Loads saved context from prompts/responses/                                        |
| Git Awareness        | ❌ No                    | ✅ Reviews recent commits, checks status                                              |
| Progress Tracking    | ❌ No                    | ✅ Identifies current step/milestone from commits + context file                      |
| Smart File Selection | ❌ Manual                | ✅ Auto-discovery, fuzzy matching, date sorting                                       |
| Summary Report       | ❌ No                    | ✅ Provides: last session context, current progress, uncommitted changes, next action |
