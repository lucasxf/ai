---
description: Create pull request and analyze feature development workflow
argument-hint: <optional-pr-title>
---

@ROADMAP.md
@.claude/agents/automation-sentinel.md

**Pull Request Creation + Feature Analysis Workflow**

Additional context for PR title: $ARGUMENTS

**⚠️ Anti-Cyclic Dependency Note:**
This command delegates analysis to `automation-sentinel` agent. The agent MUST NEVER call `/create-pr` back (would create infinite loop). Agents are "workers", commands are "entry points".

Execute the following steps in order:

## 1. Detect Current Branch and Validate

```bash
# Get current branch
CURRENT_BRANCH=$(git branch --show-current)

# Check if on a feature branch (not main/develop)
if [[ "$CURRENT_BRANCH" == "main" || "$CURRENT_BRANCH" == "develop" ]]; then
  echo "❌ ERROR: Cannot create PR from main/develop branch"
  echo "Current branch: $CURRENT_BRANCH"
  exit 1
fi

# Show current branch
echo "Current branch: $CURRENT_BRANCH"
```

## 2. Determine Base Branch

**Check which base branch to target:**
```bash
# Default to 'develop' if it exists locally or remotely, otherwise 'main'
if git show-ref --verify --quiet refs/heads/develop; then
  BASE_BRANCH="develop"
elif git show-ref --verify --quiet refs/remotes/origin/develop; then
  BASE_BRANCH="develop"
else
  BASE_BRANCH="main"
fi

echo "Base branch: $BASE_BRANCH"
```

**Prompt user for confirmation:**
"Target base branch is `$BASE_BRANCH`. Is this correct? (y/n)"

If NO → Ask user: "Which base branch should this PR target?"

## 3. Check for Uncommitted Changes (Soft Warning)

```bash
# Check for uncommitted changes (excluding local settings)
UNCOMMITTED=$(git status --porcelain | grep -v '.claude/settings.local.json' || true)

if [[ -n "$UNCOMMITTED" ]]; then
  echo "⚠️ WARNING: Uncommitted changes detected"
  echo ""
  git status --short | grep -v '.claude/settings.local.json'
  echo ""
  echo "💡 Tip: Consider using /finish-session to commit + test before creating PR"
  echo ""
  echo "What would you like to do?"
  echo "  1. Cancel and use /finish-session first (recommended)"
  echo "  2. Commit now with manual message"
  echo "  3. Continue anyway (PR will only include committed work)"
  echo ""
fi
```

**Handle user choice:**
- **Option 1 (Cancel):** Exit with message "Run /finish-session, then /create-pr again"
- **Option 2 (Commit now):**
  - Prompt: "Enter commit message:"
  - Stage all changes: `git add -A` (excluding .claude/settings.local.json)
  - Commit with user's message + Claude Code footer
  - Continue to PR creation
- **Option 3 (Continue):**
  - Warn: "⚠️ Note: Uncommitted changes won't be included in this PR"
  - Continue to PR creation

**Rationale:** Soft warning educates users about best practices (/finish-session workflow) while maintaining flexibility for alternative workflows (draft PRs, hotfixes, manual commits).

## 4. Collect Automation Metrics (pulse agent)

**CRITICAL:** Update metrics BEFORE creating PR to include metrics file in the PR commit.

**Automatically trigger `pulse` agent** (Haiku - fast, cheap) to update automation metrics:

**Mode:** `--mode=delta` (incremental update since last run)

**What pulse does:**
1. Checks `.claude/metrics/usage-stats.toml` for last metrics checkpoint
2. Scans git commits since last checkpoint
3. Counts new agent/command invocations
4. Updates TOML file with consolidated totals (incremental)
5. Completes in ~30 seconds, ~500-1000 tokens (Haiku)

**Output:** Updated `.claude/metrics/usage-stats.toml`

---

## 5. Commit Metrics Changes

**Check if metrics file was modified and commit it:**

```bash
# Check if metrics file was modified by pulse
if git status --porcelain | grep -q '.claude/metrics/usage-stats.toml'; then
  echo "📊 Metrics updated by pulse agent, committing changes..."

  # Stage metrics file only
  git add .claude/metrics/usage-stats.toml

  # Commit with standard message
  git commit .claude/metrics/usage-stats.toml -m "chore: Update automation metrics via pulse

Updated by pulse agent before PR creation.

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"

  echo "✅ Metrics committed to feature branch"
else
  echo "ℹ️ No metrics changes to commit"
fi
```

**Rationale:** Committing metrics before PR creation ensures the PR includes all changes (code + metrics) and avoids manual intervention to add uncommitted metrics files later.

---

## 6. Generate PR Title and Description

**Determine PR title:**
- If `$ARGUMENTS` is provided → Use it as title
- If `$ARGUMENTS` is empty → Generate from branch name and commits

**Auto-generate title from branch name:**
```bash
# Extract feature name from branch (e.g., feature/comment-system → Comment System)
FEATURE_NAME=$(echo "$CURRENT_BRANCH" | sed 's|feature/||' | sed 's|-| |g' | awk '{for(i=1;i<=NF;i++){$i=toupper(substr($i,1,1)) substr($i,2)}}1')

# Default title format
PR_TITLE="feat: $FEATURE_NAME"
```

**Auto-generate description from commit history:**
```bash
# Remove any existing file to avoid stale data
rm -f /tmp/pr_commits.txt

# Get commits unique to this branch (not in base branch)
git log $BASE_BRANCH..HEAD --pretty=format:"- %s" > /tmp/pr_commits.txt

# Show preview
echo "Commits in this branch:"
if [[ -f /tmp/pr_commits.txt && -s /tmp/pr_commits.txt ]]; then
  cat /tmp/pr_commits.txt
else
  echo "(No commit messages found or failed to generate commit list.)"
fi
```

**Create description template:**
```markdown
## Summary
[Brief description of what this feature implements]

## Changes
$(if [[ -s /tmp/pr_commits.txt ]]; then cat /tmp/pr_commits.txt; else echo "(No commit messages found or failed to generate commit list.)"; fi)

## Testing
- [ ] Unit tests passing (backend: 103 tests)
- [ ] Integration tests passing (Testcontainers)
- [ ] Flutter tests passing (if applicable)
- [ ] Manual testing completed

## Documentation
- [ ] ROADMAP.md updated
- [ ] OpenAPI/Swagger annotations added (if new endpoints)
- [ ] README.md updated (if user-facing changes)

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

**Prompt user:**
"Generated PR title: `$PR_TITLE`"
"Edit title? (y/n)"

If yes → Ask: "Enter new PR title:"

## 7. Create Pull Request with GitHub CLI

```bash
# Create PR using gh CLI
gh pr create \
  --base "$BASE_BRANCH" \
  --head "$CURRENT_BRANCH" \
  --title "$PR_TITLE" \
  --body "$(cat <<'EOF'
[Generated description from step 4]
EOF
)"
```

**Handle gh CLI errors:**
- If `gh` not installed → Show error: "GitHub CLI not found. Install: https://cli.github.com/"
- If not authenticated → Show error: "Not authenticated. Run: `gh auth login`"
- If PR already exists → Show existing PR URL

**Capture PR URL:**
```bash
# Get PR URL for newly created PR
PR_URL=$(gh pr view --json url --jq .url)
echo "✅ Pull Request created: $PR_URL"
```

## 8. Analyze Feature Development Workflow (automation-sentinel agent)

**Automatically trigger `automation-sentinel` agent** (Sonnet - deep analysis):

**Mode:** `--mode=delta` (reads pre-collected metrics from TOML file that was committed in Step 5)

**IMPORTANT:** automation-sentinel runs in **read-only analysis mode** - it reads the metrics file but does NOT modify any files. Metrics were already collected and committed by pulse in Steps 4-5.

**Provide context to automation-sentinel:**
- **Feature branch:** `$CURRENT_BRANCH`
- **Base branch:** `$BASE_BRANCH`
- **Commits in feature:** Output of `git log $BASE_BRANCH..HEAD`
- **Files changed:** Output of `git diff $BASE_BRANCH..HEAD --name-only`
- **Duration:** First commit date → Last commit date
- **PR URL:** `$PR_URL`
- **Metrics file:** `.claude/metrics/usage-stats.toml` (fresh data from pulse)

**Request from automation-sentinel:**
Generate a **Feature Development Report** with:
1. **Workflow Analysis:**
   - Which agents were likely used (infer from commit messages, file types)
   - Which commands were invoked (grep git log for "/command" patterns)
   - Development patterns observed (e.g., test-driven, backend-first, etc.)

2. **Metrics:**
   - Total commits in feature branch
   - Files modified (breakdown by backend/frontend/infrastructure)
   - Feature duration (days from first to last commit)
   - Estimated time investment (if calculable)

3. **Recommendations:**
   - What went well (patterns to repeat)
   - Potential improvements for next feature
   - Suggested agent usage for similar features

4. **Learning Capture:**
   - Update internal automation usage metrics
   - Identify most valuable agents for this feature type
   - Flag any automation gaps (repeated manual work)

**Output Format:**
```markdown
# Feature Development Report: $FEATURE_NAME

**Branch:** $CURRENT_BRANCH → $BASE_BRANCH
**Duration:** [First commit date] to [Last commit date] ([X] days)
**Commits:** [N] commits
**PR:** $PR_URL

## Workflow Analysis
[automation-sentinel's analysis here]

## Metrics
[automation-sentinel's metrics here]

## Recommendations
[automation-sentinel's recommendations here]

## Next Steps
- Review PR: $PR_URL
- Prepare for merge to $BASE_BRANCH
- Consider automation improvements identified above
```

**Rationale:** This automatic analysis captures real-world automation usage patterns at natural feature boundaries, providing actionable insights for continuous improvement. Since metrics were collected and committed before PR creation (Steps 4-5), the PR already includes all changes and automation-sentinel simply provides read-only analysis.

---

## 9. Final Summary

Provide comprehensive summary:
```
✅ Pull Request Created

📋 PR Details:
- Title: $PR_TITLE
- URL: $PR_URL
- Base: $BASE_BRANCH ← $CURRENT_BRANCH
- Commits: [N] commits

📊 Feature Analysis:
[Show key highlights from automation-sentinel report]

🎯 Next Steps:
1. Review PR at: $PR_URL
2. Address any CI/CD failures
3. Wait for approval and merge
4. Consider automation recommendations above

💡 Automation Insights:
[Top 3 recommendations from automation-sentinel]
```

**Note:** Keep report concise but actionable. Full detailed report is available by asking automation-sentinel directly.
