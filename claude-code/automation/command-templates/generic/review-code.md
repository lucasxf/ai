---
description: Analyze code quality and generate improvement report
argument-hint: <optional-path-or-scope>
---

**Code Review Analysis**

Target scope: $ARGUMENTS (if empty, review recent changes)

## Review Checklist

Analyze the specified code and provide a comprehensive report covering:

### 1. Code Quality
- **Adherence to project conventions:** Check against CODING_STYLE.md (or equivalent)
- **Naming conventions:** Variables, functions, classes follow project standards
- **Code organization:** Proper file/folder structure, separation of concerns
- **Complexity:** Identify overly complex functions (high cyclomatic complexity)
- **Duplication:** Detect code duplication and suggest refactoring

### 2. Testing
- **Test coverage:** Are there tests for new/modified code?
- **Test quality:** Are tests meaningful and cover edge cases?
- **Missing tests:** Identify untested code paths
- **Test naming:** Clear, descriptive test names

### 3. Documentation
- **Code comments:** Appropriate level of comments (not too few, not too many)
- **API documentation:** Public APIs properly documented (JSDoc, Javadoc, etc.)
- **README updates:** Is README.md updated if functionality changed?
- **Inline complexity:** Complex logic explained with comments

### 4. Security
- **Input validation:** User inputs properly validated and sanitized
- **Authentication/Authorization:** Protected endpoints have proper checks
- **Secrets:** No hardcoded credentials, API keys, or tokens
- **Dependencies:** No known vulnerabilities in dependencies
- **SQL injection:** Parameterized queries used (no string concatenation)

### 5. Performance
- **Database queries:** N+1 queries, missing indexes, inefficient queries
- **Caching:** Appropriate use of caching where beneficial
- **Algorithms:** Efficient algorithms (time/space complexity)
- **Resource cleanup:** Proper disposal of resources (connections, files, etc.)

### 6. Error Handling
- **Exception handling:** Appropriate try/catch blocks
- **Error messages:** Clear, actionable error messages
- **Logging:** Adequate logging for debugging (but not excessive)
- **Graceful degradation:** Handles failures gracefully

## Output Format

Generate a report with:
- ✅ **Strengths:** What was done well
- ⚠️ **Warnings:** Non-critical issues that should be addressed
- ❌ **Critical Issues:** Must-fix issues before merging
- 💡 **Suggestions:** Optional improvements for consideration

Include file paths and line numbers for each issue found.

---

## Adaptation Guide

**Customization options:**

1. **Coding standards reference:** Update "CODING_STYLE.md" to your actual coding standards document
2. **Add project-specific checks:** Include framework-specific best practices (React hooks, Spring Boot patterns, etc.)
3. **Remove irrelevant sections:** If your project doesn't have databases, remove SQL-related checks
4. **Add language-specific checks:** TypeScript strict mode, Python type hints, etc.
