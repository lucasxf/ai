---
description: Finish session with tests, docs update, and commit
argument-hint: <optional-commit-message-context>
---

**Session Finalization Workflow**

Additional context for commit message: $ARGUMENTS

Execute the following steps in order:

## 1. Run Tests
**TODO: ADAPT THIS SECTION TO YOUR PROJECT'S TEST COMMAND**
```bash
# Example for Maven (Java):
# cd services/api && ./mvnw test -q

# Example for npm (Node.js):
# npm test

# Example for pytest (Python):
# pytest -q

# Example for Flutter (Dart):
# flutter test
```

## 2. Update Documentation
Review and update if changes were significant:
- CLAUDE.md (or equivalent: architecture, directives, learnings)
- CODING_STYLE.md (or equivalent: new patterns or conventions)
- README.md (implementation status, endpoints)
- API documentation (OpenAPI/Swagger annotations, JSDoc, etc.)

Show me which files need updates based on what was implemented this session.

## 3. Review Changes
Show consolidated git diff for all modified files so I can review before committing.

## 4. Commit
After I approve the diff, create a commit with:
- Proper semantic commit message (feat/fix/docs/refactor/test/chore)
- Reference to what was implemented
- Claude Code footer

## 5. Session Summary
Provide a brief summary:
- What was accomplished
- Test results
- What should be tackled next session
- Any blockers or pending items

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Test command (Step 1):** Replace with your actual test command
   - Maven: `./mvnw test -q`
   - Gradle: `./gradlew test --quiet`
   - npm: `npm test`
   - pytest: `pytest -q`
   - Flutter: `flutter test`
   - Go: `go test ./... -v`

2. **Documentation files (Step 2):** Update the list to match your project's documentation structure

3. **Commit conventions:** Adapt if your project uses different conventions (e.g., Conventional Commits, Angular commit style)
