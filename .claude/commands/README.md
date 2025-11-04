# Custom Slash Commands - Wine Reviewer Project

> **Purpose:** Streamline common development workflows with reusable, context-aware commands.
> **Created:** 2025-11-03
> **Developer:** Backend engineer (Java/Spring Boot expert) learning Flutter/Dart

---

## 📚 Command Overview

This project includes **13 custom slash commands** designed to accelerate development workflows:

| Command | Category | Purpose | When to Use |
|---------|----------|---------|-------------|
| **start-session** | Workflow | Load context, plan session | Beginning every work session |
| **finish-session** | Workflow | Run tests, update docs, commit | Ending work session |
| **update-roadmap** | Documentation | Update ROADMAP.md status | After completing features |
| **directive** | Documentation | Add coding rules with deduplication | When new patterns emerge |
| **review-code** | Quality | Analyze code against standards | Before committing, after refactoring |
| **quick-test** | Testing | Run tests for specific class | Testing individual components |
| **test-quick** | Testing | Run all tests quietly | Quick verification |
| **test-service** | Testing | Run tests for service class | Backend service testing |
| **build-quiet** | Build | Clean build in quiet mode | Building without noise |
| **verify-quiet** | Build | Full verification quietly | CI/CD-like local verification |
| **docker-start** | Infrastructure | Start Docker services | Local development setup |
| **docker-stop** | Infrastructure | Stop Docker services | Cleanup after development |
| **api-doc** | Documentation | Open Swagger UI | API documentation review |

---

## 🎯 Quick Start

### How Slash Commands Work

1. **Type the command** - Start with `/` in Claude Code CLI
2. **Auto-expansion** - Command expands to full prompt with context
3. **Execution** - Claude Code processes the expanded prompt
4. **Result** - Get structured output based on command purpose

### First Time Setup

Commands are already configured in `.claude/commands/`. No additional setup needed!

```bash
# Verify commands are available
ls .claude/commands/

# Should see:
# - start-session.md
# - finish-session.md
# - update-roadmap.md
# - directive.md
# - review-code.md
# - quick-test.md
# - test-quick.md
# - test-service.md
# - build-quiet.md
# - verify-quiet.md
# - docker-start.md
# - docker-stop.md
# - api-doc.md
```

---

## 🚀 Command Usage Guide

### 1. Workflow Commands

#### `/start-session [context]`

**📍 Purpose:** Initialize development session with project context

**When to use:**
- Beginning every work session
- After being away from project
- Before starting new feature
- When context is lost

**What it does:**
1. Loads core documentation (CLAUDE.md, CODING_STYLE.md, ROADMAP.md, README.md)
2. Reviews recent git commits
3. Checks current git status
4. Summarizes current project state
5. Identifies next priority tasks

**Example usage:**
```bash
# Basic session start
/start-session

# With specific context
/start-session working on authentication flow

# With goal focus
/start-session implementing review submission
```

**What you get:**
- Current project status summary
- Next priority tasks from ROADMAP.md
- Recent changes context
- Key architectural patterns reminder
- Ready to work efficiently

---

#### `/finish-session [commit-context]`

**🏁 Purpose:** Complete session with tests, documentation, and commit

**When to use:**
- End of work session
- After completing feature
- Before taking break
- When ready to commit changes

**What it does:**
1. Runs all tests (unit + integration)
2. Checks test results
3. Prompts for documentation updates
4. Shows git diff
5. Creates commit with proper message

**Example usage:**
```bash
# Basic session finish
/finish-session

# With commit context
/finish-session implemented review service

# With specific message context
/finish-session added authentication flow with Google OAuth
```

**What you get:**
- Test execution results
- Documentation update prompts
- Git diff review
- Properly formatted commit
- Session closure checklist

---

#### `/update-roadmap <what-was-completed>`

**🗺️ Purpose:** Update ROADMAP.md with progress

**When to use:**
- After completing major feature
- After significant milestone
- When priorities change
- During `/finish-session`

**What it does:**
1. Moves completed items to "Implemented" section
2. Updates metrics (test count, endpoints, etc.)
3. Reprioritizes "Next Steps"
4. Updates "Last updated" timestamp
5. Adds new discovered tasks

**Example usage:**
```bash
# After feature completion
/update-roadmap completed authentication service with 18 tests

# After milestone
/update-roadmap finished Flutter network layer setup

# Multiple completions
/update-roadmap implemented review CRUD and comment endpoints
```

**What you get:**
- Updated ROADMAP.md reflecting current state
- Accurate progress tracking
- Clear next priorities
- Metrics updated

---

### 2. Documentation Commands

#### `/directive <content>`

**📝 Purpose:** Add new coding directive with smart deduplication

**When to use:**
- New coding pattern identified
- Convention established
- Best practice discovered
- Anti-pattern encountered

**What it does:**
1. Analyzes proposed directive
2. Checks for duplicates in CLAUDE.md and CODING_STYLE.md
3. Determines best location (general/backend/frontend)
4. Adds directive with date stamp
5. Updates file structure if needed

**Example usage:**
```bash
# Add new rule
/directive Always use constructor injection, never field injection

# Add convention
/directive Format numbers with underscores: 3_600_000 not 3600000

# Add pattern
/directive Use @ConfigurationProperties instead of @Value for type safety
```

**What you get:**
- Directive added to appropriate file
- No duplicates (smart deduplication)
- Organized by stack (general/backend/frontend)
- Date-stamped for tracking

---

#### `/review-code [path-or-scope]`

**🔍 Purpose:** Analyze code quality and adherence to standards

**When to use:**
- Before committing significant changes
- After implementing feature
- During refactoring
- Learning best practices

**What it does:**
1. Analyzes code against CODING_STYLE.md
2. Checks test coverage
3. Verifies conventions (OpenAPI, exceptions, etc.)
4. Identifies improvements
5. Generates prioritized recommendations

**Example usage:**
```bash
# Review specific file
/review-code services/api/src/main/java/com/winereviewer/api/service/ReviewServiceImpl.java

# Review package
/review-code services/api/src/main/java/com/winereviewer/api/controller/

# Review entire backend
/review-code services/api
```

**What you get:**
- Code quality analysis
- Convention adherence report
- Specific improvement recommendations
- Best practices reminders
- Prioritized action items

---

### 3. Testing Commands

#### `/quick-test <ServiceName>`

**⚡ Purpose:** Run unit + integration tests for specific class

**When to use:**
- After implementing testable class
- During test-after-implementation workflow
- Testing specific component
- Quick verification

**What it does:**
1. Finds test files (*Test.java and *IT.java)
2. Runs unit tests for class
3. Runs integration tests for class
4. Reports results with timing
5. Shows coverage if available

**Example usage:**
```bash
# Test specific service
/quick-test ReviewService

# Test controller
/quick-test ReviewController

# Test with full package path
/quick-test com.winereviewer.api.service.ReviewService
```

**What you get:**
- Unit test results
- Integration test results
- Test timing
- Coverage metrics
- Pass/fail summary

---

#### `/test-quick`

**🏃 Purpose:** Run all tests in quiet mode

**When to use:**
- Quick full test suite check
- Before committing
- After small changes
- CI/CD-like verification

**What it does:**
1. Runs `./mvnw test -q` (quiet mode)
2. Shows only failures and summary
3. Minimal token usage
4. Fast feedback

**Example usage:**
```bash
# Run all tests quietly
/test-quick
```

**What you get:**
- Test summary (pass/fail counts)
- Failure details (if any)
- Minimal noise
- Fast execution

---

#### `/test-service`

**🧪 Purpose:** Run tests for specific service class

**When to use:**
- Testing backend service layer
- After service implementation
- Before committing service changes

**What it does:**
1. Prompts for service name
2. Runs tests matching pattern
3. Shows detailed results
4. Identifies missing tests

**Example usage:**
```bash
# Test specific service
/test-service

# (Prompts for service name)
# Input: ReviewService
```

**What you get:**
- Service test results
- Missing test warnings
- Coverage recommendations

---

### 4. Build Commands

#### `/build-quiet`

**🔨 Purpose:** Clean build in quiet mode

**When to use:**
- After dependency updates
- Before deployment
- Token-efficient build verification

**What it does:**
1. Runs `./mvnw clean install -q`
2. Shows only errors and summary
3. Minimal output
4. Fast feedback

**Example usage:**
```bash
# Clean build quietly
/build-quiet
```

**What you get:**
- Build success/failure
- Error details (if any)
- Minimal token usage

---

#### `/verify-quiet`

**✅ Purpose:** Full verification (build + tests) quietly

**When to use:**
- Before pushing to remote
- CI/CD-like local verification
- After major changes
- Pre-commit verification

**What it does:**
1. Runs `./mvnw clean verify -q`
2. Executes all tests (unit + integration)
3. Runs static analysis
4. Shows only summary

**Example usage:**
```bash
# Full verification quietly
/verify-quiet
```

**What you get:**
- Build + test results
- Static analysis results
- Coverage summary
- Pass/fail status

---

### 5. Infrastructure Commands

#### `/docker-start`

**🐳 Purpose:** Start Docker services (PostgreSQL + API)

**When to use:**
- Beginning local development
- After machine restart
- Before running integration tests
- Local environment setup

**What it does:**
1. Runs `docker compose up -d --build --quiet-pull`
2. Starts PostgreSQL container
3. Starts API container
4. Verifies services are healthy

**Example usage:**
```bash
# Start all services
/docker-start
```

**What you get:**
- Services started in background
- Health check status
- Connection details
- Ready to develop

---

#### `/docker-stop`

**🛑 Purpose:** Stop Docker services

**When to use:**
- End of development session
- Freeing system resources
- Before machine shutdown
- Cleanup after work

**What it does:**
1. Runs `docker compose down`
2. Stops all containers
3. Removes networks
4. (Optional) Removes volumes with `-v` flag

**Example usage:**
```bash
# Stop services, keep data
/docker-stop

# Stop services, remove data
/docker-stop -v
```

**What you get:**
- Services stopped
- Resources freed
- Optional data cleanup

---

#### `/api-doc`

**📖 Purpose:** Open API documentation (Swagger UI)

**When to use:**
- After creating/updating endpoints
- Testing API manually
- Reviewing OpenAPI documentation
- Sharing API specs

**What it does:**
1. Checks if API is running
2. Opens Swagger UI in browser
3. Shows available endpoints
4. Provides testing interface

**Example usage:**
```bash
# Open Swagger UI
/api-doc
```

**What you get:**
- Swagger UI opened
- Interactive API documentation
- Endpoint testing interface
- Request/response examples

---

## 🎭 Command Categories

### Workflow Commands (Session Management)
- **start-session** - Begin work session with context
- **finish-session** - End session with tests and commit
- **update-roadmap** - Track progress

**Use case:** Daily workflow management

---

### Documentation Commands (Code Standards)
- **directive** - Add coding rules
- **review-code** - Analyze code quality

**Use case:** Maintaining code quality and standards

---

### Testing Commands (Quality Assurance)
- **quick-test** - Test specific class
- **test-quick** - Run all tests quietly
- **test-service** - Test service layer

**Use case:** Test-after-implementation workflow

---

### Build Commands (Compilation & Verification)
- **build-quiet** - Clean build
- **verify-quiet** - Full verification

**Use case:** Pre-commit verification

---

### Infrastructure Commands (Local Environment)
- **docker-start** - Start services
- **docker-stop** - Stop services
- **api-doc** - Open API docs

**Use case:** Local development environment management

---

## 💡 Best Practices

### 1. Always Start with `/start-session`

**✅ Good workflow:**
```bash
# Open Claude Code
/start-session implementing authentication

# Work on feature...

/finish-session authentication complete
```

**❌ Poor workflow:**
```bash
# Open Claude Code
"Help me implement authentication"
# (Missing context, wastes tokens)
```

---

### 2. Use `/finish-session` Before Committing

**✅ Good workflow:**
```bash
# Feature complete
/finish-session review service implemented

# Runs tests
# Updates docs
# Creates commit
```

**❌ Poor workflow:**
```bash
# Feature complete
git add .
git commit -m "stuff"
# (No tests run, docs not updated)
```

---

### 3. Test Incrementally with `/quick-test`

**✅ Good workflow:**
```bash
# Implement ReviewService
/quick-test ReviewService

# Implement ReviewController
/quick-test ReviewController

# Full verification
/verify-quiet
```

**❌ Poor workflow:**
```bash
# Implement everything
./mvnw test
# (Slow, verbose, wastes tokens)
```

---

### 4. Update Roadmap Regularly

**✅ Good workflow:**
```bash
# After each significant feature
/update-roadmap completed authentication with 18 tests

# During finish-session (automatic prompt)
/finish-session
```

**❌ Poor workflow:**
```bash
# Work for weeks
# Roadmap becomes stale
# Lost track of progress
```

---

### 5. Use Quiet Mode Commands for Efficiency

**✅ Token-efficient:**
```bash
/build-quiet
/verify-quiet
/test-quick
```

**❌ Token-wasteful:**
```bash
./mvnw clean install
./mvnw verify
./mvnw test
# (Verbose output wastes tokens)
```

---

## 🎨 Command Design Principles

All commands follow these principles:

1. **Context-Aware** - Load necessary project files
2. **Token-Efficient** - Minimize output, use quiet modes
3. **Verifiable** - Produce clear success/failure results
4. **Documented** - Clear purpose and usage
5. **Composable** - Can be chained in workflows
6. **Consistent** - Follow naming conventions
7. **Safe** - No destructive operations without confirmation

---

## 📊 Command Selection Guide

**Use this decision tree:**

```
What do you need?
│
├─ Start/end session → start-session / finish-session
│
├─ Update progress → update-roadmap
│
├─ Add coding rule → directive
│
├─ Review code → review-code
│
├─ Test specific class → quick-test
│
├─ Run all tests → test-quick / verify-quiet
│
├─ Build project → build-quiet
│
├─ Start/stop Docker → docker-start / docker-stop
│
└─ View API docs → api-doc
```

---

## 🔄 Command Workflows

### Workflow 1: Daily Development

**Goal:** Standard development session

**Commands:**
```bash
# Morning start
/start-session implementing review feature

# Work on code...

# Test as you go
/quick-test ReviewService

# End of day
/finish-session review feature complete
```

---

### Workflow 2: Feature Implementation

**Goal:** Complete feature from start to finish

**Commands:**
```bash
# Start
/start-session implementing authentication

# Start local environment
/docker-start

# Implement feature...

# Test incrementally
/quick-test AuthService
/quick-test AuthController

# Review code
/review-code services/api/src/main/java/com/winereviewer/api/service/AuthServiceImpl.java

# Full verification
/verify-quiet

# Finish
/finish-session authentication complete
/update-roadmap completed authentication with Google OAuth
```

---

### Workflow 3: Bug Fix

**Goal:** Fix bug with tests

**Commands:**
```bash
# Start
/start-session fixing review rating validation bug

# Run relevant tests
/quick-test ReviewService

# Fix bug...

# Verify fix
/quick-test ReviewService

# Full verification
/test-quick

# Finish
/finish-session fixed rating validation bug
```

---

### Workflow 4: Refactoring

**Goal:** Improve code quality

**Commands:**
```bash
# Start
/start-session refactoring exception handling

# Review current state
/review-code services/api/src/main/java/com/winereviewer/api/exception/

# Refactor...

# Verify nothing broke
/verify-quiet

# Review improvements
/review-code services/api/src/main/java/com/winereviewer/api/exception/

# Finish
/finish-session refactored exception hierarchy
```

---

## 🚦 Status & Maintenance

### Current Status
- ✅ All 13 commands created
- ✅ Integrated with project conventions
- ✅ Optimized for token efficiency
- ✅ Ready to use

### Maintenance
- **Update commands** when workflows change
- **Add new commands** as needs emerge
- **Archive commands** if no longer useful
- **Share commands** via template repository

### Improvement Ideas
- Add command for generating OpenAPI documentation
- Add command for database migration creation
- Add command for Flutter-specific workflows
- Add command for CI/CD pipeline testing

---

## 📝 Command Templates

**Location:** `C:\repo\claude-command-templates\`

These commands are designed to be reusable across projects:
- Copy `.claude/commands/` to new projects
- Customize paths and project-specific details
- Maintain consistent workflow patterns

---

## 🔗 Related Resources

**Documentation:**
- **CLAUDE.md** - Custom slash commands section
- **ROADMAP.md** - Updated by `/update-roadmap`
- **CODING_STYLE.md** - Referenced by `/directive` and `/review-code`
- **README.md** - Project overview

**Agents:**
- **session-optimizer** - Works with `/start-session` and `/finish-session`
- **backend-code-reviewer** - Works with `/review-code`
- See `.claude/agents/README.md` for full agent suite

---

## 📐 Command vs Agent

**When to use commands:**
- Structured, repeatable workflows
- Token-efficient operations
- Quick context loading
- Standard development tasks

**When to use agents:**
- Complex reasoning needed
- Design decisions
- Learning and teaching
- Code review with analysis

**Both together:**
```bash
# Start with command for context
/start-session implementing review feature

# Use agent for design
"Design the review submission screen" → frontend-ux-specialist

# Use command for testing
/quick-test ReviewService

# Use agent for review
"Review my ReviewService implementation" → backend-code-reviewer

# Finish with command
/finish-session review feature complete
```

---

## 💭 Philosophy

**Commands embody the principle:**
> "Automate the routine, so you can focus on the creative."

They handle:
- ✅ Context loading
- ✅ Repetitive tasks
- ✅ Token optimization
- ✅ Standard workflows

So you can focus on:
- 🎨 Design
- 💡 Problem-solving
- 🚀 Innovation
- 📚 Learning

---

**Happy coding! 🚀**
