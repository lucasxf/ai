# Context and knowledge base

- https://www.anthropic.com/engineering/claude-code-best-practices
- https://docs.claude.com/en/docs/build-with-claude/prompt-engineering/prompt-improver
- https://docs.claude.com/en/docs/build-with-claude/prompt-engineering/prompt-templates-and-variables

# Session goals

- To reduce token usage and heavy context loading even further
- To optmize agent, command, and context usage

# Directives

- Think this through and be thorough
- If my suggestions are poor, feel free to suggest alternatives
- Explain your reasoning and tradeoffs (if applicable)
- I don't know if Claude Code supports prompt improver, nor do I know how to use it. If it does, and if it makes sense for this scenario, use it before planning the tasks
- Even if Claude Code doesn't support prompt improver, you may rewrite this, show me and I send it again (if that makes sense and is helpful)
- Whether you use `TodoWrite` or not, show me the steps of the plan before executing it
- Once I approve the changes checkout a new branch from `develop` following the appropriate naming convention to perform them. You may use `chore/update-settings-and-docs` branch to do so. Explain your decision.
- Review this prompt by the end

# Issues to be solved

- `CODING_STYLE.md` is too large
- `/start-session` command is loading too much context, even for "small" sessions.
Sessions are documentation focused, some others are frontend, and some backend, for instance, 
but it's currently loading 1K+ lines for the full stack every time
- `/start-session` must be uploaded such that user specifies that session "tech stack" or goal (such that Claude may infer which - if any - `CODING_STYLE.md` to load)

# What MUST be done

- Break down `CODING_STYLE.md` separate files regarding their own stack (you may move them to the most appropriate directories, such as `/apps`, `/infra` or `/services`, for example)

# What COULD be done (suggestions)

- Should you find this pattern (i.e. too big a file for context loading in `/start-session`, feel free to add it to your plan)
- If it makes sense, brekdown `ROADMAP.md` following the same reasoning and strategy as `CODING_STYLE.md`

# Example successful scenarioS

- Disambiguation on unclear context: User prompts > /start-session updating documentation on API
  - Claude prompts: is the change on the frontend or the backend?
  - User selects proper stack/layer: backend
  - Claude loads `/services/CODING_STYLE.md` OR `/services/CODING_STYLE_BACKEND.md` (suggestions are welcome here)
- On clear context: User prompts > /start-session change splash screen background color to #AABBCC
    - Claude loads `/apps/CODING_STYLE.md` OR `/apps/mobile/CODING_STYLE.md`

