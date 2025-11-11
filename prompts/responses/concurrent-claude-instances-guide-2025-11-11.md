---
created: 2025-11-11
session: 2025-11-11
user-request: "Running multiple Claude Code instances on same repository - concurrency issues and best practices"
---

# Concurrent Claude Code Instances Guide

## ✅ Safe Scenarios

1. **Different branches with isolated file changes**
   - Instance 1: `chore/testing-refactoring` working on `services/api/src/test/**`
   - Instance 2: `chore/new-agent` working on `.claude/agents/**` and documentation
   - **Safe** if the files being edited don't overlap

2. **Read-only operations**
   - Both instances reading/searching code simultaneously
   - **Safe** - no conflicts

## ⚠️ Potential Issues

### 1. Git Working Directory Conflicts
- **Problem:** Git stores state in `.git/index` (staging area)
- If both instances run `git add` or `git commit` simultaneously, they compete for the same staging area
- **Example:**
  ```bash
  # Terminal 1 (branch A)
  git add services/api/...

  # Terminal 2 (branch B) - simultaneously
  git add .claude/agents/...
  # ⚠️ Both writing to .git/index
  ```

### 2. Shared Configuration Files
- **Problem:** Both might try to modify:
  - `.claude/settings.local.json`
  - `.claude/commands.json`
  - Root-level documentation (`CLAUDE.md`, `ROADMAP.md`, `LEARNINGS.md`)
- **Example scenario:**
  - Instance 1 finishes and runs `/finish-session` → updates `ROADMAP.md`
  - Instance 2 later finishes → overwrites `ROADMAP.md` with outdated version

### 3. Docker/Process Conflicts
- **Problem:** If both try to start/stop Docker containers:
  ```bash
  # Terminal 1
  docker compose up -d

  # Terminal 2
  docker compose down -v  # ⚠️ Kills Terminal 1's containers
  ```

### 4. File System Race Conditions
- **Problem:** IDEs and file watchers may get confused
- Git branch switching while another instance has files open
- Build artifacts in shared directories (`target/`, `build/`)

## 🛡️ Best Practices for Parallel Sessions

### 1. Strictly Separate File Scopes
```bash
# Terminal 1 - Testing refactoring
Branch: chore/testing-refactoring
Files: services/api/src/test/**/*.java
      services/api/CODING_STYLE_BACKEND.md (testing section only)

# Terminal 2 - Agent creation
Branch: chore/new-agent-workflow-docs
Files: .claude/agents/new-agent.md
      .claude/agents/README.md
      prompts/workflow-documentation.md
```

### 2. Coordinate Shared Resource Access
- **Shared documentation strategy:**
  - Designate one session for `ROADMAP.md` / `LEARNINGS.md` updates
  - Or: both sessions update at different times (not simultaneously)

### 3. Use Different Docker Compose Profiles (if applicable)
```yaml
# docker-compose.yml
services:
  api:
    profiles: ["default", "testing"]

  api-dev:
    profiles: ["dev"]
```
```bash
# Terminal 1
docker compose --profile testing up

# Terminal 2
docker compose --profile dev up
```

### 4. Commit Coordination
- **Workflow:**
  1. Finish work in Instance 1 → Commit → Push
  2. Instance 2 pulls latest changes before committing
  3. Or: Use git worktrees (advanced - see below)

## 🚀 Advanced Solution: Git Worktrees

Git worktrees allow multiple branches checked out simultaneously in different directories:

```bash
# Setup worktrees
cd C:\repo\wine-reviewer

# Create worktree for testing refactoring
git worktree add ../wine-reviewer-testing chore/testing-refactoring

# Create worktree for agent work
git worktree add ../wine-reviewer-agent chore/new-agent

# Now you have:
# C:\repo\wine-reviewer\         (main worktree)
# C:\repo\wine-reviewer-testing\ (testing branch)
# C:\repo\wine-reviewer-agent\   (agent branch)
```

**Benefits:**
- ✅ Completely isolated working directories
- ✅ No git staging area conflicts
- ✅ Can run different Docker Compose setups if needed
- ✅ No branch switching interference

**Cleanup:**
```bash
# Remove worktrees when done
git worktree remove ../wine-reviewer-testing
git worktree remove ../wine-reviewer-agent
```

## 📋 Recommended Workflow Options

### Option A: Sequential (Safest)
1. Complete testing refactoring → commit → push
2. Then start agent/docs work in new session

### Option B: Parallel with Git Worktrees (Recommended)
```bash
# Terminal 1
cd C:\repo\wine-reviewer-testing  # worktree
claude  # work on testing refactoring

# Terminal 2
cd C:\repo\wine-reviewer-agent  # worktree
claude  # work on agent and docs
```

### Option C: Parallel Same Directory (Requires Discipline)
1. **Before starting:**
   - Define strict file boundaries (no overlap)
   - Document which session owns shared resources (ROADMAP, LEARNINGS)

2. **During work:**
   - Avoid git operations simultaneously
   - Don't run Docker commands in both
   - Don't modify shared config files in both

3. **Before committing:**
   - Instance 1: Commit → Push
   - Instance 2: Pull changes → Resolve conflicts if any → Commit → Push

## 🎯 Recommendation Summary

**For testing refactoring + agent/docs scenario:**
- **Best:** Use Git Worktrees (Option B) for true isolation
- **Safe:** Work sequentially (Option A) if tasks are manageable
- **Avoid:** Running both in same directory simultaneously without strict coordination

---

## Context (Auto-generated)

**Current Project State:**
- Branch: chore/testing-standardization-and-improvement
- Last commit: 1112268 Merge pull request #29 from lucasxf/chore/optimize-commands-further

**Related Concepts:**
- Git worktrees for isolated branch checkouts
- Git staging area (.git/index) conflicts
- Shared configuration file coordination
- Docker Compose profiles for environment separation

**Next Steps:**
- Decide on workflow approach (sequential vs parallel)
- If parallel: Set up git worktrees or define strict file boundaries
- Document session ownership for shared resources (ROADMAP, LEARNINGS)
