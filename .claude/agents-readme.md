# Custom Agent Suite - AI/MCP Studies Project

> **Purpose:** Specialized agents to accelerate AI/MCP development, learning, and automation efficiency.
> **Created:** 2025-11-25
> **Developer:** Backend engineer (Java/Spring Boot expert) exploring Model Context Protocol (MCP)

---

## 📚 Agent Overview

This project includes **6 custom agents** and **16 custom slash commands** designed for AI/MCP development:

| Agent | Purpose | Model | When to Use |
|-------|---------|-------|-------------|
| **automation-sentinel** | Meta-agent: automation health, metrics, optimization | Sonnet | Checking automation health, generating reports, finding redundancy |
| **backend-code-reviewer** | Java/Spring Boot code review, best practices | Sonnet | Reviewing backend code after implementation |
| **learning-tutor** | Teaching concepts, structured learning, exercises | Sonnet | Learning new topics (MCP, Spring AI, etc.) |
| **pulse** | Metrics collection agent (agent/command usage, LOCs) | Haiku | Auto-triggered before automation-sentinel, collects metrics |
| **session-optimizer** | Token efficiency, session planning, workflow | Haiku | Starting sessions, optimizing token usage |
| **tech-writer** | Documentation (external + in-code), ADRs, Javadoc, OpenAPI | Sonnet | Creating ADRs, adding Javadoc, updating docs |

---

## 🎯 Quick Start

### How Agents Work

1. **Automatic Invocation** - Claude Code automatically selects agents based on your message
2. **Explicit Invocation** - You can request specific agents: "Use the backend-code-reviewer to analyze this"
3. **Complementary** - Multiple agents can work together (e.g., pulse → automation-sentinel)

### ⚠️ Anti-Cyclic Dependency Rule (CRITICAL)

**To prevent infinite loops, this hierarchy must be strictly enforced:**

```
Slash Commands (high-level orchestration)
    ↓ can call
Agents (task execution)
    ↓ can call
Other Agents (delegation)
    ↓ NEVER call
Slash Commands ❌ (would create cycle)
```

**Rules:**
- ✅ **Commands CAN call agents** - Example: `/finish-session` calls `tech-writer`
- ✅ **Agents CAN call other agents** - Example: `automation-sentinel` reads metrics from `pulse`
- ❌ **Agents MUST NEVER call commands** - Would create infinite loops

**Why this matters:**
- Commands are "entry points" (user-initiated workflows)
- Agents are "workers" (execute specific tasks)
- Workers can delegate to other workers, but never back to entry points
- Violating this rule causes cyclic dependencies and infinite loops

**Example Safe Delegation:**
```
/create-pr (command)
  → calls pulse (agent) - collects metrics
    → calls automation-sentinel (agent) - analyzes metrics  ✅ SAFE
    → NEVER calls /create-pr again  ❌ CYCLIC
```

### First Time Setup

Agents are already configured in `.claude/agents/`. No additional setup needed!

```bash
# Verify agents are available
ls .claude/agents/

# Should see (alphabetically):
# - automation-sentinel.md
# - backend-code-reviewer.md
# - learning-tutor.md
# - pulse.md
# - session-optimizer.md
# - tech-writer.md
```

---

## 🚀 Agent Usage Guide

### 1. Automation Sentinel (Meta-Agent)

**🔍 Use when:**
- Checking automation ecosystem health
- Finding redundant agents or commands
- Generating automation usage reports
- Detecting obsolete automations
- Getting optimization recommendations

**Example prompts:**
- "Check automation health"
- "Generate automation ecosystem report"
- "Are my agents redundant?"
- "Which automations are most valuable?"
- "Find unused commands"

**What you get:**
- Comprehensive health report (schema validation, dependency checks)
- Usage analytics dashboard (most/least used automations)
- Redundancy detection (overlapping responsibilities)
- Obsolescence warnings (unused automations)
- Optimization recommendations (consolidation, new automation ideas)

**Strengths:**
- Meta-level oversight (monitors all other agents)
- Data-driven recommendations (reads metrics from pulse)
- Self-monitoring (validates its own schema)
- Non-destructive (recommends, never auto-deletes)

---

### 2. Backend Code Reviewer

**🔍 Use when:**
- After implementing backend features
- Before committing significant changes
- Refactoring existing code
- Learning Spring Boot/MCP best practices
- Improving code quality

**🤖 Auto-Trigger Protocol (for Claude):**

**CRITICAL:** ALWAYS invoke this agent (never manual review) when user:
- Says "review" + backend file path (e.g., "review JsonRpcCodecImpl.java")
- Says "check/validate/analyze" + Java/Spring Boot code
- Says "I just finished implementing [Service|Controller|Repository|...]"
- Says "before committing..." or "I'm about to commit..."
- Provides code with request for feedback

**Why:** Agent's 10-dimension systematic review is always superior to ad-hoc manual review.

**Exception:** Only skip agent if user explicitly says "quick check" or "sanity check".

**Example prompts:**
- "Review my McpClientService implementation"
- "Check this MCP server for best practices"
- "I just implemented MCP protocol handling, review it"
- "Analyze test coverage for my service layer"
- "Review this MCP connection management design"

**What you get:**
- Comprehensive code review (10 dimensions)
- Strengths and issues identified
- Specific improvement recommendations
- Best practices explained
- Prioritized action items

**Strengths:**
- Enforces project conventions
- Checks OpenAPI documentation
- Validates testing patterns
- Security and performance analysis

---

### 3. Learning Tutor

**📖 Use when:**
- Learning new concepts (MCP, Spring AI, etc.)
- Understanding patterns deeply
- Getting structured lessons
- Practicing with exercises
- Reviewing fundamentals

**🤖 Auto-Trigger Protocol (for Claude):**

**CRITICAL:** ALWAYS invoke this agent when user:
- Says "teach me" + topic (e.g., "teach me JSON-RPC 2.0")
- Says "explain" + concept (e.g., "explain MCP protocol")
- Says "I want to understand" + topic
- Says "how does [concept] work?"
- Says "what's the difference between X and Y?"
- Asks conceptual questions (not implementation-specific)

**Why:** Agent provides structured lessons with exercises, not just explanations.

**Exception:** Skip for simple factual questions ("what is the HTTP status code for...").

**Example prompts:**
- "Teach me Model Context Protocol fundamentals"
- "Explain the difference between MCP client and server"
- "I want to understand Spring AI MCP integration deeply"
- "Give me exercises to practice MCP tool definitions"
- "What's the best way to learn MCP protocol patterns?"

**What you get:**
- Structured lessons (concept → example → exercise)
- Backend parallels (connects to Java/Spring knowledge)
- Hands-on exercises with solutions
- Spaced repetition for reinforcement
- Learning path recommendations

**Strengths:**
- Tailored to backend engineers
- Active learning (not just reading)
- Builds understanding, not just memorization
- Tracks progress, reinforces concepts

---

### 4. Pulse (Metrics Collection Agent)

**⚡ Use when:**
- Automatically triggered before automation-sentinel
- Manually: "Update automation metrics"
- Collecting usage data for analysis

**What it does:**
- Collects agent invocation metrics
- Collects command execution metrics
- Calculates Lines of Code (LOC) metrics
- Stores data in `.claude/metrics/usage-stats.toml`
- Operates in delta mode (incremental) or full mode (baseline)

**What you get:**
- Updated metrics file (TOML format)
- Git-tracked metrics (enables version control)
- Fast execution (<30 seconds)
- Comprehensive coverage (all agents + all commands)

**Strengths:**
- Uses Haiku model (10x cheaper than Sonnet)
- Delta mode avoids redundant full history scans
- 75% token reduction in automation workflows
- Git-aware checkpoints (uses commit SHA)

**Integration:**
```
/create-pr workflow:
1. pulse (Haiku) - Collects metrics (~800 tokens)
2. automation-sentinel (Sonnet) - Analyzes metrics (~3000 tokens)
Total: ~3800 tokens (vs 15,000 without pulse)
```

---

### 5. Session Optimizer

**⚡ Use when:**
- Starting a new work session
- Planning complex tasks
- Token usage is getting high
- Session feels unfocused
- Want to work more efficiently

**Example prompts:**
- "Help me plan this session efficiently"
- "I'm wasting tokens, how can I optimize?"
- "What's the best way to load context for this task?"
- "This conversation is getting too long"
- "Plan token-efficient implementation of MCP POC"

**What you get:**
- Session plan with token budget
- Smart file loading strategy
- Efficiency tactics (quiet mode, parallel calls)
- Warning signs for waste
- Recommended workflow

**Strengths:**
- Uses Haiku (fast, cheap)
- Reduces token waste 30-50%
- Keeps sessions focused
- Prevents scope creep

---

### 6. Tech Writer

**📝 Use when:**
- Creating/updating documentation
- Adding Javadoc to classes
- Adding OpenAPI/Swagger annotations to endpoints
- Creating ADRs (Architecture Decision Records)
- Updating ROADMAP.md or LEARNINGS.md
- Writing README sections

**🤖 Auto-Trigger Protocol (for Claude):**

**CRITICAL:** ALWAYS invoke this agent when user:
- Says "add OpenAPI annotations" + controller/endpoint name
- Says "create ADR for" + decision/topic
- Says "add Javadoc to" + class name
- Says "update LEARNINGS.md" or "update ROADMAP.md"
- Says "document this endpoint/class/method"

**Why:** Agent enforces complete documentation standards (all HTTP codes, @author tags, etc.).

**Note:** backend-code-reviewer may trigger this agent if OpenAPI docs are missing.

**Example prompts:**
- "Add OpenAPI annotations to McpToolController"
- "Create ADR for the MCP client architecture decision"
- "Add comprehensive Javadoc to McpProtocolHandler"
- "Update LEARNINGS.md with today's MCP POC insights"
- "Document this MCP endpoint with Swagger annotations"

**What you get:**
- Comprehensive external documentation (CLAUDE.md, README, LEARNINGS, ADRs)
- In-code documentation (Javadoc with @author, @date, examples)
- Complete OpenAPI/Swagger annotations (all HTTP status codes documented)
- Consistent documentation structure

**Strengths:**
- Enforces OpenAPI documentation (CRITICAL for all REST endpoints)
- Maintains documentation consistency across project
- Integrates with backend-code-reviewer (fills doc gaps)
- Automatic trigger after REST endpoint creation

---

## 🎭 Agent Combinations (Workflows)

### Workflow 1: MCP POC Development

**Goal:** Implement MCP POC from planning to documentation

**Steps:**
1. **Session Optimizer** - Plan token-efficient session
2. **Learning Tutor** - Learn MCP concepts if needed
3. **Backend Code Reviewer** - Review MCP implementation
4. **Tech Writer** - Document POC and update LEARNINGS.md

**Example:**
```
"Plan an efficient session to implement MCP hello-world POC"
→ Session Optimizer provides plan

"Teach me MCP tool definition patterns"
→ Learning Tutor provides lesson

"Review my McpServerService implementation"
→ Backend Code Reviewer analyzes quality

"Update LEARNINGS.md with MCP protocol insights"
→ Tech Writer documents lessons
```

---

### Workflow 2: Automation Optimization

**Goal:** Analyze and optimize automation ecosystem

**Steps:**
1. **Pulse** - Collect latest metrics
2. **Automation Sentinel** - Analyze health and find improvements

**Example:**
```
/create-pr (automatically triggers)
→ pulse collects metrics (~800 tokens)
→ automation-sentinel analyzes (~3000 tokens)
→ Insights added to PR description
```

---

### Workflow 3: Learning + Practice

**Goal:** Learn new MCP concept, practice, apply to project

**Steps:**
1. **Learning Tutor** - Teach concept with exercises
2. **Backend Code Reviewer** - Review implementation

**Example:**
```
"Teach me MCP sampling pattern"
→ Learning Tutor provides lesson + exercises

"Review my MCP sampling implementation"
→ Backend Code Reviewer validates approach
```

---

## 💡 Best Practices

### 1. Let Agents Choose Automatically

**✅ Good:**
```
"I need to implement MCP tool discovery"
```
Claude Code sees "implement" + "MCP" → invokes backend-code-reviewer automatically

**❌ Less Efficient:**
```
"Use the backend-code-reviewer agent to review my MCP implementation"
```
Explicit invocation works but wastes tokens

**When to be explicit:**
- Agent selection is ambiguous
- You want specific agent's approach
- Testing agent behavior

---

### 2. Use Session Optimizer at Session Start

**Start every session:**
```
@ROADMAP.md

Quick session start via session-optimizer.

Goal: [specific MCP POC or task]
Scope: [in/out]
```

**Benefits:**
- Minimal token load (ROADMAP.md only)
- Clear plan prevents waste
- Token budget awareness

---

### 3. Review Backend Code Proactively

**After implementing MCP components:**
```
"Just finished McpClientService implementation"
→ backend-code-reviewer automatically invoked

OR

"/finish-session MCP client complete"
→ Runs tests, then suggests code review
```

---

### 4. Use Pulse + Automation Sentinel for Health Checks

**Automatic workflow (via /create-pr):**
```
/create-pr "feat: MCP tool support"
→ pulse collects metrics
→ automation-sentinel analyzes health
→ PR created with insights
```

**Manual workflow:**
```
"Update automation metrics"
→ pulse collects data

"Check automation health"
→ automation-sentinel analyzes
```

---

## 🎨 Agent Personalities

### Automation Sentinel
- 🔍 **Style:** Vigilant watchdog, data analyst
- 💬 **Tone:** Analytical, metric-driven, proactive
- 🎯 **Focus:** Automation health + optimization + ROI
- 📏 **Output:** Health reports + metrics + recommendations

### Backend Code Reviewer
- 🔍 **Style:** Critical but constructive reviewer
- 💬 **Tone:** Professional, thorough, didactic
- 🎯 **Focus:** Quality + best practices + security
- 📏 **Output:** Analysis + recommendations + examples

### Learning Tutor
- 📚 **Style:** Educator using structured lessons
- 💬 **Tone:** Encouraging, methodical, engaging
- 🎯 **Focus:** Deep understanding + practice
- 📏 **Output:** Lessons + exercises + assessments

### Pulse
- ⚡ **Style:** Silent data collector, metrics gatherer
- 💬 **Tone:** Concise, factual, efficient
- 🎯 **Focus:** Accurate metrics + fast execution
- 📏 **Output:** TOML metrics file + delta reports

### Session Optimizer
- ⚡ **Style:** Efficiency expert, planner
- 💬 **Tone:** Concise, strategic, practical
- 🎯 **Focus:** Token savings + focused work
- 📏 **Output:** Plans + checklists + budgets

### Tech Writer
- 📝 **Style:** Documentation specialist, standards enforcer
- 💬 **Tone:** Precise, structured, comprehensive
- 🎯 **Focus:** Documentation completeness + consistency
- 📏 **Output:** ADRs + Javadoc + OpenAPI + READMEs

---

## 📊 Agent Selection Guide

**Use this decision tree:**

```
What's your task?
│
├─ Checking automation health / metrics → pulse → automation-sentinel
│
├─ Reviewing backend/MCP code → backend-code-reviewer
│
├─ Learning MCP/Spring AI concept → learning-tutor
│
├─ Starting session / optimizing tokens → session-optimizer
│
└─ Creating docs / ADRs / Javadoc / OpenAPI → tech-writer
```

---

## 🚦 Status & Maintenance

### Current Status
- ✅ All 6 agents created (2025-11-25: adapted from wine-reviewer)
- ✅ Tailored to AI/MCP development (backend-focused)
- ✅ Integrated with project conventions (CLAUDE.md, CODING_STYLE.md, LEARNINGS.md)
- ✅ Anti-cyclic dependency rule documented and enforced
- ✅ Alphabetically organized for easy navigation
- ✅ pulse agent added for metrics collection
- ✅ Ready to use

### Recent Updates (2025-11-25)
- ✅ **pulse** - Added metrics collection agent (Haiku, delta mode)
- ✅ **automation-sentinel** - Updated to read pulse metrics
- ✅ **start-session** - Added stack-specific loading (backend|docs|full)
- ✅ **create-pr** - Integrated pulse → automation-sentinel workflow
- ✅ Removed frontend/infra agents (not applicable to AI project)

### Maintenance
- **Update agents** when project conventions change
- **Add new agents** as new needs emerge
- **Run automation-sentinel** monthly to check ecosystem health
- **Monitor pulse metrics** in `.claude/metrics/usage-stats.toml`

### Future Improvement Ideas
- Add agent for MCP protocol debugging
- Create agent for Spring AI integration patterns
- Implement automated POC documentation generation
- Add agent for MCP server/client testing patterns

---

## 📝 Notes

**This agent suite was adapted for AI/MCP studies:**
- Backend expert (Java/Spring Boot) → MCP-focused examples
- MCP learner → Learning tutor for protocol concepts
- Quality-focused → Systematic patterns, best practices
- Token-conscious → Session optimizer + pulse agent
- Documentation-driven → Integrates with CLAUDE.md/ROADMAP.md/LEARNINGS.md

**Project Context:**
- Focus: Model Context Protocol (MCP) POCs and studies
- Stack: Java 21, Spring Boot 3, Spring AI, Maven
- No frontend or infrastructure (backend/docs only)
- Emphasis on learning and documentation

**Remember:** Agents learn from your project context. Keep CLAUDE.md, CODING_STYLE.md, ROADMAP.md, and LEARNINGS.md updated so agents have accurate information.

---

**Happy learning! 🚀**
