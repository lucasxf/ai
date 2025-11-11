  ---
  📋 Enhanced Comment System Plan


| Step | Task                                      | Files Created/Modified                                                                  | Commit Message                                           | Verification                             | Notes                                                                   |
|------|-------------------------------------------|-----------------------------------------------------------------------------------------|----------------------------------------------------------|------------------------------------------|-------------------------------------------------------------------------|
| 1    | Comment entity + repository + migration   | `Comment.java`<br>`CommentRepository.java`<br>`V3__create_comments_table.sql`           | feat: Add Comment entity, repository, and migration      | `./mvnw verify -q`                       | ⚠️ Test cascade delete constraint in Flyway migration                   |
| 2    | DTOs + Service interface + implementation | `CreateCommentRequest.java`<br>`CommentResponse.java`<br>`CommentService.java`<br>`CommentServiceImpl.java` | feat: Add Comment DTOs and service with business logic   | Manual review (no tests yet)             | Focus on CRUD methods, ownership validation                             |
| 3    | CommentService unit tests                 | `CommentServiceTest.java`                                                               | test: Add CommentServiceTest with 15+ unit tests         | `./mvnw test -q -Dtest=CommentServiceTest` | Cover all CRUD, edge cases, ownership checks                            |
| 4    | CommentController + OpenAPI docs          | `CommentController.java`                                                                | feat: Add CommentController with OpenAPI documentation   | Check Swagger UI (`/swagger-ui.html`)    | **CRITICAL:** Document all HTTP status codes (200, 201, 400, 403, 404, 422) |
| 5    | Integration tests                         | `CommentControllerIT.java`                                                              | test: Add CommentControllerIT with 18+ integration tests | `./mvnw test -q -Dtest=CommentControllerIT` | Test cascade delete, pagination, authentication, ownership              |
| 6    | Documentation + final verification        | `README.md`<br>`ROADMAP.md`                                                             | docs: Update README and ROADMAP for comment system       | `./mvnw verify -q` (full suite)          | Update API endpoints section, move task to "Implemented"                |

  ---
  ✨ Key Enhancements Made

  1. Step 2 Split: Separated DTOs from service to follow "one responsibility per commit" principle
  2. Quiet Mode: Added -q flag to all Maven commands (token optimization)
  3. Swagger Verification: Made OpenAPI documentation verification explicit in Step 4
  4. Documentation Step: Combined README + ROADMAP updates in Step 6 (efficient)
  5. Verification Strategy: Each step has clear verification command

  ---
  🎯 Acceptance Criteria (from your plan)

  - ✅ POST /api/reviews/{reviewId}/comments → 201 Created (with user authentication)
  - ✅ GET /api/reviews/{reviewId}/comments → 200 OK (pagination: page, size, sort)
  - ✅ DELETE /api/comments/{commentId} → 204 No Content (ownership check)
  - ✅ CASCADE DELETE: Deleting review deletes all its comments (database constraint + IT test)
  - ✅ OWNERSHIP: 403 Forbidden if user tries to delete another user's comment
  - ✅ OpenAPI: 100% documentation with all HTTP status codes
  - ✅ TESTS: All 103+ tests passing (unit + integration)

  ---
  🚀 Workflow for Each Step

  When you complete a step:

  1. Stage your changes: git add .
  2. Let me know: "Step X completed and staged"
  3. I will:
    - Review code quality (backend-code-reviewer agent)
    - Check CODING_STYLE.md adherence
    - Verify OpenAPI documentation (if applicable)
    - Suggest improvements or approve commit
  4. You commit: Use the suggested commit message
  5. Repeat for next step

  ---
  🎭 Available Agents for This Session

  Proactive agents for your workflow:
  - backend-code-reviewer 🔍 - Auto-triggered after each step for quality checks
  - tech-writer 📝 - For OpenAPI annotations (Step 4) and documentation (Step 6)
  - learning-tutor 📚 - If you want to understand specific patterns (e.g., cascade delete constraints)

  Note: I'll invoke agents automatically based on your milestone updates. You don't need to request them explicitly.

  ---
  📐 Key Architectural Reminders

  From CLAUDE.md + CODING_STYLE.md:

  1. Exception Handling:
    - Use domain exceptions (ResourceNotFoundException, UnauthorizedAccessException)
    - No generic RuntimeException for business logic
  2. Testing (CRITICAL):
    - Test-After-Implementation: Unit tests immediately after service implementation
    - Integration Tests: Use Testcontainers pattern (AbstractIntegrationTest)
    - Authentication Helper: Use authenticated(userId) in integration tests
  3. OpenAPI Documentation (CRITICAL):
    - @Tag on controller class
    - @Operation on each endpoint method
    - @ApiResponses for ALL status codes (200, 201, 400, 403, 404)
    - @Parameter for path/query params
  4. Code Conventions:
    - Constructor injection (never @Autowired on fields)
    - Method order: public → private (top-down invocation flow)
    - Closing parenthesis on same line (lambdas, method calls)
    - Blank line before closing bracket (except records)
  5. Database:
    - Cascade delete in Flyway migration: ON DELETE CASCADE
    - Test cascade behavior in integration tests

  ---
  🔍 Git Status Check

● Bash(git log --oneline -5)
  ⎿  4dec020 Merge pull request #10 from lucasxf/docs/update-roadmap-auth-complete
     8a1056a docs: Update ROADMAP.md - Auth UI Integration completed
     b434386 Merge pull request #1 from lucasxf/feature/auth-ui-integration
     … +2 lines (ctrl+o to expand)

● Bash(git status --short)
  ⎿   M .claude/settings.local.json       
      M apps/mobile/pubspec.lock
     ?? "prompts/creating 2 new agents.md"
     … +2 lines (ctrl+o to expand)

● Uncommitted Changes:
  - .claude/settings.local.json (modified) - Local agent settings
  - apps/mobile/pubspec.lock (modified) - Flutter dependency lock
  - prompts/ (new files) - Prompt drafts (not critical)

  Recommendation: These changes are unrelated to comment system work. You can:
  - Commit them separately before starting: git add .claude/ apps/mobile/pubspec.lock && git commit -m "chore: Update local settings and Flutter lock file"
  - Or stash them: git stash (retrieve later with git stash pop)

  ---
  ✅ Ready to Start!

  Your plan is thorough and approved. Proceed with Step 1: Comment entity + repository + migration.

  When Step 1 is staged, let me know and I'll:
  1. Review entity design (JPA annotations, validation, relationships)
  2. Review repository interface (Spring Data query methods)
  3. Review Flyway migration (CASCADE constraint, indexes)
  4. Approve or suggest improvements before commit

  Good luck! 🚀