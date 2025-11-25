# Major prompt goal

- To have a detailed explanation or plan to address the goals, questions, and issues presented below.

# Context

On our previous sessions and branches (namely `chore/update-settings-and-docs`, `chore/optimize-session-context-loading`, and this current `chore/optimize-commands-further`) we did lots of necessary work and learned a lot while doing it.
I believe there's precious findings and insight on said learnings.
This session is all about exploring on that.

# Why am I doing this?

While I'm currently working on only 2 projects (`/wine-reviewer` and `/ai`), whenever possible, I try to have my workflow of tools, agents, and commands, to be the most reusable and generic (apart from stack and context-specific stuff) as possible so that I (or any other engineers I share them with) may fully or partially reuse them in other projects.

I DO understand:
1. that every project is not the same
2. that my workflow needs will change according to the issue at hand
3. that more commands and more agents doesn't equal more efficiency, performance, etc. (like having 300 commands when I only use the same 20, and 80 redundant ultra-specific agents, when 12 will do the trick would be poor decision-making)
4. that building my projects as well as their respective automation workflows is a highly-iterative process and that this will "never" be "100% ready/done/finished"

But I obviously AIM for:
1. a stable optimal state (even if such state isn't a "state of the art" one, I believe that between the place I'm currently on, and that optimal state there's still room for improvement)
2. becoming a better engineer by:
   1. continuously learning more
   2. sharing knowledge and helping others in the developers community
   3. constantly reviewing and improving my workflows
   4. delivering high-quality products
   5. striving for a seamless experience and reducing friction
   6. shipping faster

# Directives

- Think hard, think it through and be thorough
- Explain your reasoning and show tradeoffs
- Be efficient
- Use `TodoWrite` and any other tools, agents, or commands you need (but prompt me for heavy context loading before hand. For example: loading all coding_style.md files)
- Avoid overengineering: or at least explicitly state that the available paths could be overkill
- DO NOT write any code yet
- DO NOT create any command or agent yet
- Feel free to fetch web data (from trustworthy sources, such as Anthropic's official documentation, forums, etc.)
- If you think this might break the gitflow for adding a broader context to the current branch, feel free to checkout to a new more appropriate branch
- Review this prompt by the end

# Technical level goals (they are NOT in priority order)

1. to avoid overspending tokens on `automation-sentinel` runs
2. pushing for more efficiency on speed, processing, and token usage when gathering metrics
3. to check whether there's room for improvement in my current workflow via:
   1. creating one or more new agents
   2. creating one or more new commands
   3. splitting up one or more of the current agents into more specialized ones (with narrower responsibilities)
   4. splitting up one or more of the current commands into more specialized ones (with narrower responsibilities)
   5. a new path we're not aware yet?

# Hypothetical scenarios (to illustrate the needs and questions below)

1. First scenario: 2 runs in a short period of time
   1. You have recently run `automation-sentinel` because I opened a PR yesterday
   2. I also worked on new stuff today and will open another PR - which will trigger `automation-sentinel` again
   3. It's okay to trigger it, as it's by design. But maybe running it a "delta" between last night and this morning could be faster and more token-effective.
2. Second scenario: I forgot to save the output
   1. You have recently run `automation-sentinel` because I opened a PR yesterday
   2. I forgot to save the output from yesterday's run
   3. I want to see the metrics
   4. I don't want to spend time and tokens running `automation-sentinel` again

# Questions

- does `automation-sentinel` currently works on some sort of "delta" approach?
- what is the time span in which it gathers metrics on agent and command usage?
- does it compare between the last pr and the current one? Or from the very beginning of the project?
- would it be more efficient to:
  - 1. manually or automatically save the output of an `automation-sentinel` run to an external file
  - 2. having said file record some auxiliary metadata (timestamps, etc.)
  - 3. input said file as an optional parameter on  `/create-pr` or `automation-sentinel` run so that it compares metrics only between the file timestamp and "now"?
