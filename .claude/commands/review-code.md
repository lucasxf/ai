---
description: Analyze code quality and generate improvement report
argument-hint: <optional-path-or-scope>
---

**Code Review & Analysis**

Scope: $ARGUMENTS

## Instructions

Perform a comprehensive code quality review focusing on:

### 1. Code Quality Checks
- Adherence to CODING_STYLE.md conventions
- Proper use of directives from CLAUDE.md
- Java 21 features usage (var, records, pattern matching, text blocks)
- Spring Boot best practices (constructor injection, @Transactional placement, proper stereotype usage)
- Constructor injection vs field injection
- ConfigurationProperties vs @Value
- Method ordering (public → private)
- Blank lines before closing brackets
- Import statements (no full class names)

### 2. Test Coverage Analysis
- Check if all services have corresponding tests
- Verify test naming conventions (*Test for unit, *IT for integration)
- Identify missing test scenarios
- Review test quality and assertions

### 3. Documentation Completeness
- OpenAPI/Swagger annotations on all endpoints
- Javadoc with @author and @date
- Comments in Portuguese where beneficial
- README.md accuracy vs actual implementation

### 4. Security & Best Practices
- No hardcoded credentials or secrets
- Proper exception handling with domain exceptions
- Input validation on endpoints
- Transaction boundaries in services

### 5. Performance & Optimization
- Database indexes on foreign keys
- Efficient queries (N+1 problems)
- Proper use of caching where applicable

## Output Format

Provide a structured report:
```
## Code Review Summary

### ✅ Strengths
- [List what's being done well]

### ⚠️ Issues Found
- [List issues by severity: Critical/High/Medium/Low]

### 💡 Recommendations
- [Specific actionable improvements]

### 📊 Metrics
- Test coverage: X%
- Convention adherence: Y%
- Documentation completeness: Z%
```

Focus on actionable feedback that can be addressed in the next development session.
