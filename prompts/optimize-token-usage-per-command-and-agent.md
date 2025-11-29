# Directives

- think hard on this and be thorough
- explain your reasoning
- show tradeoffs and recommendations (like, enhancing the command would consume so many tokens it woulnd't be worth doing that everytime and that should remain a manual task performed by the user)
- do NOT update any file or generate any code before asking me and providing details

# Goal

To take efficiency and optimization even further

# Context

On our previous sessions I realized on my own that it wasn't
quite efficient to have a single CODING_STYLE.md file encompassing
all of the project's stack, as well as having /start-session and /finish-session both load it
on every single session. So I decided to split it and update the commands.

# Hypothesis and recommended paths

Should that be the case that (one of the following alternatives):

1. `automation-sentinel` not only checks which agents and commands are being used, but overall token usage per agent/command and recommend improvements (such as splitting coding_style up in stack focused files)
2. `session-optmizer` is enhanced to perform the responsibilities described in item 1
3. A new agent is created to handle that specific task?
4. none of the above
