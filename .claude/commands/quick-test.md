---
description: Run tests for a specific service class
argument-hint: <ServiceName>
---

**Quick Test for Specific Service**

Service to test: $ARGUMENTS

## Instructions

Run targeted tests for the specified service or class:

### 1. Determine Test Scope
If service name provided (e.g., "ReviewService"):
- Run both unit tests: `ReviewServiceTest`
- Run integration tests if they exist: `ReviewControllerIT` (since services are tested through controllers in integration tests)

### 2. Execute Tests in Quiet Mode
```bash
cd services/api

# Run unit test
./mvnw test -q -Dtest=$ARGUMENTS*Test

# Run related integration tests (if applicable)
./mvnw test -q -Dtest=*IT -Dgroups=$ARGUMENTS
```

### 3. Report Results
- Show test execution summary
- Highlight any failures with stack traces
- Report execution time
- Show coverage impact if available

### 4. Troubleshooting
If tests fail:
- Show detailed error messages
- Identify which test methods failed
- Suggest possible fixes based on error type

## Examples

**Usage:**
- `/quick-test ReviewService` → Runs ReviewServiceTest + ReviewControllerIT
- `/quick-test AuthService` → Runs AuthServiceTest + AuthControllerIT
- `/quick-test CommentController` → Runs CommentControllerTest + CommentControllerIT

**Output:**
```
✅ ReviewServiceTest: 15 tests passed (0.8s)
✅ ReviewControllerIT: 23 tests passed (3.2s)

Total: 38 tests, 0 failures
```
