# Claude Code Command Templates

A reusable collection of custom slash commands for Claude Code CLI, organized by technology stack.

## 📋 Overview

This repository contains custom slash commands that can be copied into any project using Claude Code. Commands are organized by category to make it easy to pick only what you need.

## 📂 Repository Structure

```
claude-command-templates/
├── generic/              # Stack-agnostic commands (works with any project)
│   ├── directive.md      # Add coding directives to documentation
│   ├── start-session.md  # Initialize development session
│   ├── finish-session.md # Finalize session (tests, docs, commit)
│   └── review-code.md    # Automated code review
├── java-spring/          # Java/Spring Boot specific commands
│   ├── build-quiet.md    # Clean build in quiet mode
│   ├── test-quick.md     # Run tests silently
│   ├── verify-quiet.md   # Full verification (build + tests)
│   ├── api-doc.md        # Open Swagger UI
│   ├── quick-test.md     # Test specific service/class
│   ├── docker-start.md   # Start Docker services
│   ├── docker-stop.md    # Stop Docker services
│   └── update-roadmap.md # Update project roadmap
├── flutter/              # Flutter/Dart specific commands (future)
├── nodejs/               # Node.js specific commands (future)
└── README.md             # This file
```

## 🚀 Quick Start

### 1. Choose Commands

Browse the folders and identify which commands you need:

- **Generic commands** → Useful for any project (recommended for all)
- **Stack-specific commands** → Choose based on your tech stack

### 2. Copy to Your Project

```bash
# Navigate to your project
cd /path/to/your/project

# Create .claude/commands directory if it doesn't exist
mkdir -p .claude/commands

# Copy generic commands (recommended for all projects)
cp C:/repos/claude-command-templates/generic/* .claude/commands/

# Copy stack-specific commands (e.g., Java/Spring Boot)
cp C:/repos/claude-command-templates/java-spring/* .claude/commands/
```

### 3. Adapt to Your Project

Each command includes an **"Adaptation Guide"** section at the bottom. Review and adapt:

- Directory paths (`services/api` → your actual path)
- File names (`CLAUDE.md` → your documentation files)
- Build tools (Maven → Gradle, npm, etc.)
- Port numbers, service names, etc.

### 4. Use in Claude Code

```bash
# Start Claude Code CLI
claude

# Use your commands
/start-session "Working on authentication"
/quick-test UserService
/finish-session "Implemented user auth"
```

## 📚 Command Reference

### Generic Commands

#### `/directive <content>`
**Purpose:** Quickly add coding directives to your project documentation.

**When to use:**
- Capturing new coding conventions discovered during development
- Documenting architectural decisions
- Adding team guidelines

**Example:**
```bash
/directive "Always use constructor injection over field injection"
```

**Adaptation needed:**
- Requires `CLAUDE.md` file (or adapt to your docs structure)
- Specify which sections to use (General/Backend/Frontend)

---

#### `/start-session [context]`
**Purpose:** Initialize a development session with standard context loading.

**When to use:**
- Starting work on a new feature or bug
- After pulling latest changes from repo
- Beginning a focused work session

**Example:**
```bash
/start-session "Fixing payment integration bugs"
```

**Adaptation needed:**
- Update documentation file names (CLAUDE.md, CODING_STYLE.md, README.md)
- Customize what context to load (environment checks, service status)

---

#### `/finish-session [commit-context]`
**Purpose:** Complete a development session with tests, docs update, and commit.

**When to use:**
- Before pushing changes
- End of work session
- After completing a feature/fix

**Example:**
```bash
/finish-session "Implemented OAuth2 flow"
```

**Adaptation needed:**
- **CRITICAL:** Update test command for your stack (Maven, npm, pytest, etc.)
- Customize documentation files list
- Adjust commit message format if needed

---

#### `/review-code [path-or-scope]`
**Purpose:** Automated code quality analysis and improvement suggestions.

**When to use:**
- Before creating a pull request
- After implementing a feature
- Regular code quality checks

**Example:**
```bash
/review-code src/services/auth/
```

**Adaptation needed:**
- Minimal - mostly language-agnostic
- Add project-specific checks if needed (e.g., React hooks, Spring patterns)

---

### Java/Spring Boot Commands

#### `/build-quiet`
**Purpose:** Clean build without verbose Maven logs (token-efficient).

**Example:**
```bash
/build-quiet
```

**Adaptation needed:**
- Update directory path (`services/api` → your backend directory)
- Switch to Gradle if needed: `./gradlew clean build --quiet`

---

#### `/test-quick`
**Purpose:** Run all tests in quiet mode.

**Example:**
```bash
/test-quick
```

**Adaptation needed:**
- Update directory path
- Switch to Gradle if needed: `./gradlew test --quiet`

---

#### `/verify-quiet`
**Purpose:** Full verification (clean build + tests + packaging).

**Example:**
```bash
/verify-quiet
```

**Adaptation needed:**
- Update directory path
- Switch to Gradle if needed: `./gradlew clean check --quiet`

---

#### `/api-doc`
**Purpose:** Open Swagger UI in browser to view API documentation.

**Example:**
```bash
/api-doc
```

**Adaptation needed:**
- Update port number (default: `8080`)
- Update Swagger path (default: `/swagger-ui.html`)
- Update directory path for starting API

---

#### `/quick-test <ServiceName>`
**Purpose:** Run tests for a specific service or controller class.

**Example:**
```bash
/quick-test UserService
```

**Adaptation needed:**
- Update directory path
- Adjust test naming convention if different (`*Test.java` vs `*Spec.java`)
- Switch to Gradle if needed

---

#### `/docker-start`
**Purpose:** Start all Docker Compose services in quiet mode.

**Example:**
```bash
/docker-start
```

**Adaptation needed:**
- Update directory path where `docker-compose.yml` is located
- Update service names in logs command
- Specify compose file if using non-default name

---

#### `/docker-stop`
**Purpose:** Stop Docker Compose services (with option to clean volumes).

**Example:**
```bash
/docker-stop
```

**Adaptation needed:**
- Update directory path where `docker-compose.yml` is located
- Specify compose file if using non-default name

---

#### `/update-roadmap <what-was-completed>`
**Purpose:** Update project roadmap after completing tasks.

**Example:**
```bash
/update-roadmap "Integration tests with Testcontainers completed"
```

**Adaptation needed:**
- Update documentation file name (`CLAUDE.md` → your roadmap file)
- Adjust section names if different structure
- Change date format if needed

---

## 🛠️ Bootstrap Script (Coming Soon)

For convenience, a bootstrap script will be provided to automatically set up commands for new projects:

```bash
# Future feature
./bootstrap.sh java-spring /path/to/your/project
```

This will:
1. Copy generic + stack-specific commands
2. Prompt for adaptations (directory paths, ports, etc.)
3. Update commands with your specific values

## 💡 Best Practices

### When to Use Generic Commands

✅ **Always copy:**
- `/directive` - Useful for all projects
- `/start-session` - Standardizes session initialization
- `/finish-session` - Ensures you don't forget tests/docs/commits
- `/review-code` - Language-agnostic code quality checks

### When to Use Stack-Specific Commands

✅ **Copy if relevant:**
- Java/Spring Boot → Use `java-spring/` commands
- Flutter → Use `flutter/` commands (coming soon)
- Node.js → Use `nodejs/` commands (coming soon)

### Adaptation Workflow

1. **Copy first, adapt later** - Get commands into your project quickly
2. **Test each command** - Verify it works with your project structure
3. **Iterate** - Adjust paths, tool names, etc. as needed
4. **Document custom adaptations** - Add notes for your team

### Command Naming Conventions

- Use descriptive names: `/test-quick` (clear), not `/tq` (cryptic)
- Stack-specific prefixes: `/flutter-analyze`, `/npm-install`
- Action-oriented: `/build-quiet`, `/docker-start`

## 🤝 Contributing

Have a useful command to share? Contributions welcome!

### Adding New Commands

1. **Choose category:** Generic vs. stack-specific
2. **Create `.md` file** with frontmatter:
   ```markdown
   ---
   description: Short description
   argument-hint: <optional-argument-name>
   ---

   Command prompt goes here...

   ---

   ## Adaptation Guide
   List required adaptations...
   ```
3. **Test thoroughly** - Ensure it works in different project structures
4. **Document adaptations** - Clear instructions for users

### Template Structure

```markdown
---
description: One-line description (max 60 chars)
argument-hint: <optional-arg> (only if command takes arguments)
---

[Command prompt that Claude Code will execute]

$ARGUMENTS will be replaced with user input

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Adaptation point 1:** Description and examples
2. **Adaptation point 2:** Description and examples
...
```

## 📖 Resources

- [Claude Code Documentation](https://docs.claude.com/claude-code)
- [Custom Slash Commands Guide](https://docs.claude.com/claude-code/custom-commands)

## 📄 License

This template repository is provided as-is for personal and commercial use. Feel free to adapt, modify, and share.

## 🗺️ Roadmap

- [x] Generic commands
- [x] Java/Spring Boot commands
- [x] Comprehensive documentation
- [ ] Bootstrap script for easy setup
- [ ] Flutter/Dart commands
- [ ] Node.js/Express commands
- [ ] Python/FastAPI commands
- [ ] React/Next.js commands
- [ ] CI/CD workflow commands

---

**Created by:** Claude Code user community
**Last updated:** 2025-10-22
