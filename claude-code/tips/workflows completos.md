
● # Development Workflow Visualization

  ## Session 1: Fresh Feature Development

  ┌──────────────────────────────────────────────────────────────────────────────┐
  │ SESSION 1: Implementing Comment System Feature                               │
  └──────────────────────────────────────────────────────────────────────────────┘

  ┌─────────────┐
  │ /start-session comment-system                                                 │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: session-optimizer (auto-triggered)                                │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Analyzing session context...                                                │
  │ ✓ Identified relevant files to load                                         │
  │ ✓ Skipping irrelevant sections for token efficiency                         │
  │ ✓ Optimized context loading strategy                                        │
  │ ✓ Estimated token usage: 8,500 tokens (within budget)                       │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 📚 CONTEXT LOADED                                                            │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ ✓ CLAUDE.md          ← Project conventions, architecture, tech stack        │
  │ ✓ CODING_STYLE.md    ← Code style rules (3-part structure)                  │
  │ ✓ ROADMAP.md         ← Current priorities, backlog                          │
  │ ✓ README.md          ← Setup instructions, features                         │
  │                                                                              │
  │ 🎯 Claude now knows:                                                        │
  │   • Constructor injection required                                          │
  │   • Integration tests use *IT.java with Testcontainers                      │
  │   • OpenAPI docs required on all endpoints                                  │
  │   • 3-part doc structure (General/Backend/Frontend)                         │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 💻 USER CODES (30 minutes)                                                   │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ User: "Create Comment entity with JPA annotations"                          │
  │ Claude: [reads CLAUDE.md → knows to use UUID, timestamptz, indexes]         │
  │                                                                              │
  │ Files created/modified:                                                     │
  │   M  services/api/src/.../domain/Comment.java                               │
  │   M  services/api/src/.../repository/CommentRepository.java                 │
  │   A  services/api/src/.../db/migration/V004__create_comments_table.sql      │
  │                                                                              │
  │ User: "Create CommentService with CRUD operations"                          │
  │ Claude: [uses constructor injection, domain exceptions, structured logging] │
  │                                                                              │
  │   A  services/api/src/.../service/CommentService.java                       │
  │   A  services/api/src/.../dto/CreateCommentRequest.java                     │
  │   A  services/api/src/.../dto/CommentResponse.java                          │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────┐
  │ User: "Add OpenAPI documentation to CommentController"                       │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: tech-writer (auto-triggered on REST endpoint detection)           │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Detected new REST endpoints in CommentController                            │
  │                                                                              │
  │ Adding OpenAPI annotations:                                                 │
  │ ✓ @Tag(name = "Comments", description = "Comment management endpoints")     │
  │ ✓ @Operation on POST /reviews/{reviewId}/comments                           │
  │ ✓ @ApiResponses: 201, 400, 401, 403, 404, 422, 500                          │
  │ ✓ @Operation on GET /reviews/{reviewId}/comments                            │
  │ ✓ @ApiResponses: 200, 401, 404, 500                                         │
  │ ✓ @Parameter annotations on reviewId path variable                          │
  │                                                                              │
  │ Updating README.md with new endpoints:                                      │
  │ ✓ Added "Comment Endpoints" section                                         │
  │ ✓ POST /reviews/{reviewId}/comments - Create comment                        │
  │ ✓ GET /reviews/{reviewId}/comments - List comments for review               │
  │                                                                              │
  │ Next: Verify at http://localhost:8080/swagger-ui.html                       │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────┐
  │ /directive "Integration tests must mock GoogleTokenValidator and S3Client"   │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🔍 DIRECTIVE WORKFLOW                                                        │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Step 1: Search for duplicates                                               │
  │   ├─ Searching CLAUDE.md (all parts)... ❌ Not found                        │
  │   └─ Searching CODING_STYLE.md (all parts)... ❌ Not found                  │
  │                                                                              │
  │ Step 2: Determine file + section                                            │
  │   ├─ Keywords: "Integration tests", "mock"                                  │
  │   ├─ Category: Backend testing                                              │
  │   └─ Target: CODING_STYLE.md → Part 2: Backend → Testing                   │
  │                                                                              │
  │ Step 3: Format and insert                                                   │
  │   ├─ Format: "- Integration tests must mock GoogleTokenValidator..."        │
  │   └─ Timestamp: "(Added 2025-11-02)"                                        │
  │                                                                              │
  │ Step 4: Show diff                                                           │
  │   diff --git a/CODING_STYLE.md b/CODING_STYLE.md                            │
  │   @@ -245,6 +245,7 @@ Integration Tests:                                       │
  │    - Use authenticated(userId) helper for security testing                  │
  │   +- Integration tests must mock GoogleTokenValidator... (Added 2025-11-02) │
  │                                                                              │
  │ Step 5: Commit prompt                                                       │
  │   Commit this change? (y/n): y                                              │
  │                                                                              │
  │ Step 6: Create commit                                                       │
  │   ✓ git add CODING_STYLE.md                                                 │
  │   ✓ git commit -m "docs: Add directive - mock external dependencies"        │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 💻 USER CODES MORE (45 minutes)                                              │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ User: "Create CommentController with POST and GET endpoints"                │
  │ Claude: [reads directive → adds OpenAPI docs, constructor injection]        │
  │                                                                              │
  │   A  services/api/src/.../controller/CommentController.java                 │
  │                                                                              │
  │ User: "Write integration tests for CommentController"                       │
  │ Claude: [reads new directive → mocks GoogleTokenValidator + S3Client]       │
  │                                                                              │
  │   A  services/api/src/.../integration/CommentControllerIT.java              │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────┐
  │ /review-code services/api/src/main/java/com/.../controller/                  │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 📊 CODE REVIEW AUDIT                                                         │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ ## Code Review Summary                                                      │
  │                                                                              │
  │ ### ✅ Strengths                                                             │
  │ - Constructor injection used correctly                                      │
  │ - OpenAPI annotations complete (@Tag, @Operation, @ApiResponses)            │
  │ - Proper HTTP status codes documented (200, 201, 400, 401, 403, 404)       │
  │ - Exception handling follows project patterns                               │
  │                                                                              │
  │ ### ⚠️ Issues Found                                                          │
  │ - MEDIUM: Missing @Parameter annotation on reviewId path variable           │
  │ - LOW: Could add structured logging in createComment method                 │
  │                                                                              │
  │ ### 💡 Recommendations                                                       │
  │ 1. Add @Parameter(description = "Review ID") to reviewId parameter          │
  │ 2. Add log.info("Creating comment for review {}", reviewId) in service      │
  │                                                                              │
  │ ### 📊 Metrics                                                               │
  │ - Test coverage: 85% (CommentControllerIT has 12 tests)                     │
  │ - Convention adherence: 95%                                                  │
  │ - Documentation completeness: 90%                                            │
  │                                                                              │
  │ Trigger backend-code-reviewer for deep analysis? (y/n): y                   │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: backend-code-reviewer (manual trigger)                            │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ # Backend Code Review: CommentController & CommentService                   │
  │                                                                              │
  │ ## 📊 Overall Assessment                                                     │
  │ Solid implementation following Spring Boot best practices. OpenAPI docs are │
  │ comprehensive. Minor improvements needed for production readiness.           │
  │                                                                              │
  │ Quality Rating: ⭐⭐⭐⭐☆ (4/5)                                              │
  │                                                                              │
  │ ## ✅ Strengths                                                               │
  │ 1. Dependency Injection: Constructor injection properly used             │
  │ 2. OpenAPI Documentation: All endpoints documented with status codes    │
  │ 3. Exception Handling: Domain exceptions properly thrown                │
  │ 4. Test Coverage: 12 integration tests covering happy + error paths     │
  │                                                                              │
  │ ## 🚨 Critical Issues (Must Fix)                                             │
  │ None - code is production-ready with minor improvements below.               │
  │                                                                              │
  │ ## ⚠️ Improvements (Should Consider)                                          │
  │                                                                              │
  │ ### Improvement 1: Add @Parameter Annotation                                │
  │ Location: CommentController.java:45                                   │
  │ Current Approach: Path variable without description                     │
  │ Suggested Enhancement: Add OpenAPI parameter documentation              │
  │ Benefit: Better API docs for consumers                                  │
  │ Example:                                                                │
  │ java                                                                     │ │ // Current
             │ │ @PathVariable UUID reviewId                                                 │ │
                              │ │ // Enhanced                                                                 │ │ @Parameter(description = "Review
   ID", example = "123e4567-e89b-12d3...")    │ │ @PathVariable UUID reviewId                                                 │ │
                                                            │
  │                                                                              │
  │ ### Improvement 2: Add Structured Logging                                   │
  │ Location: CommentService.java:32                                      │
  │ Current Approach: No logging on comment creation                        │
  │ Suggested Enhancement: Add INFO log with context                        │
  │ Benefit: Observability and debugging                                    │
  │ Example:                                                                │
  │ java                                                                     │ │ log.info("Creating comment for review {}. Author: {}", reviewId,
  userId);   │ │                                                                           │
  │                                                                              │
  │ ## 💡 Best Practices & Learning Points                                       │
  │ 1. OpenAPI @Parameter: Enhances generated docs with examples/constraints│
  │ 2. Structured Logging: Always log domain events (created, updated, etc.)│
  │ 3. UUID Path Variables: Spring auto-converts string → UUID with 400 on  │
  │    invalid format (no manual validation needed)                             │
  │                                                                              │
  │ ## 📋 Checklist Summary                                                      │
  │ - [x] Architecture aligned with project conventions                         │
  │ - [x] OpenAPI documentation complete                                        │
  │ - [x] Tests implemented with >80% coverage                                  │
  │ - [x] Security vulnerabilities addressed                                    │
  │ - [ ] Logging at INFO level for domain events (add structured logs)         │
  │                                                                              │
  │ ## 🎯 Action Items (Prioritized)                                             │
  │ Medium Priority:                                                        │
  │ 1. Add @Parameter annotation to reviewId path variable                      │
  │ 2. Add structured logging to CommentService.createComment()                 │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 💻 USER FIXES ISSUES (10 minutes)                                            │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ User: "Apply the recommendations from review"                               │
  │ Claude: [adds @Parameter, adds structured logging]                          │
  │                                                                              │
  │   M  services/api/src/.../controller/CommentController.java                 │
  │   M  services/api/src/.../service/CommentService.java                       │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────┐
  │ /update-roadmap "Completed: Comment system (entity, service, controller, tests)" │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 📋 ROADMAP UPDATE                                                            │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Changes:                                                                     │
  │                                                                              │
  │ ## ✅ Implemented (Phase F1)                                                 │
  │   [moved from "In Progress"]                                                │
  │ + - Comment System (entity, repository, service, controller, tests)         │
  │                                                                              │
  │ ## 🚧 In Progress                                                            │
  │ - - Comment System (entity, repository, service, controller, tests)         │
  │ + - Photo upload with pre-signed URLs                                       │
  │                                                                              │
  │ Updated: 2025-11-02                                                          │
  │                                                                              │
  │ Commit this? (y/n): y                                                       │
  │ ✓ git add ROADMAP.md                                                        │
  │ ✓ git commit -m "docs: Update roadmap - comment system completed"           │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 💻 USER CODES FINAL TOUCHES (15 minutes)                                     │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ User: "Update README.md with new comment endpoints"                         │
  │                                                                              │
  │   M  README.md                                                               │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────┐
  │ /finish-session "Comment system implementation"                              │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: session-optimizer (auto-triggered)                                │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Session efficiency analysis:                                                │
  │ ✓ Token usage: 42,300 / 200,000 (21% of budget)                             │
  │ ✓ Efficient context loading (used selective file reads)                     │
  │ ✓ No redundant file reads detected                                          │
  │ ✓ Agents triggered appropriately (tech-writer, backend-code-reviewer)       │
  │                                                                              │
  │ Suggestions for next session:                                               │
  │ • Consider using /save-response to save photo-upload plan                   │
  │ • LEARNINGS.md growing large - consider archiving older sessions            │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🏁 FINISH SESSION WORKFLOW                                                   │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Step 1: Run tests                                                           │
  │   $ ./mvnw verify -q                                                        │
  │   ✓ All tests passing (95 tests, 0 failures)                                │
  │                                                                              │
  │ Step 2: Prompt for documentation updates                                    │
  │   Update LEARNINGS.md with session notes? (y/n): y                          │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: tech-writer (auto-triggered for LEARNINGS.md update)              │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Creating session entry for LEARNINGS.md...                                  │
  │                                                                              │
  │ Added to LEARNINGS.md:                                                      │
  │ ────────────────────────────────────────────────────────────────────────── │
  │ ## Session: 2025-11-02 - Comment System Implementation                      │
  │                                                                              │
  │ ### ☕ Backend                                                               │
  │ Implemented:                                                            │
  │ - Comment entity with JPA annotations (UUID PK, @ManyToOne to Review)       │
  │ - CommentRepository extending JpaRepository                                 │
  │ - CommentService with createComment() and findByReviewId()                  │
  │ - CommentController with POST and GET endpoints                             │
  │ - Database migration V004__create_comments_table.sql                        │
  │ - Integration tests: CommentControllerIT (12 tests)                         │
  │                                                                              │
  │ Technical Decisions:                                                    │
  │ - Cascade delete: When review deleted, comments auto-deleted                │
  │ - Index on (review_id, created_at DESC) for fast chronological queries      │
  │ - Mocked GoogleTokenValidator and S3Client in integration tests             │
  │                                                                              │
  │ Learnings:                                                              │
  │ - OpenAPI @Parameter annotation improves generated docs with examples       │
  │ - Structured logging on domain events critical for observability            │
  │ - Integration tests with Testcontainers validate FK constraints correctly   │
  │                                                                              │
  │ New Directive Added:                                                    │
  │ - "Integration tests must mock GoogleTokenValidator and S3Client"           │
  │   (Added to CODING_STYLE.md Part 2: Backend → Testing)                      │
  │ ────────────────────────────────────────────────────────────────────────── │
  │                                                                              │
  │ ✓ LEARNINGS.md updated                                                      │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🏁 FINISH SESSION WORKFLOW (continued)                                       │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Step 3: Show git status                                                     │
  │   On branch feature/comment-system                                          │
  │   Changes not staged for commit:                                            │
  │     M  services/api/src/.../domain/Comment.java                             │
  │     M  services/api/src/.../service/CommentService.java                     │
  │     M  services/api/src/.../controller/CommentController.java               │
  │     M  README.md                                                             │
  │     M  LEARNINGS.md                                                          │
  │   Untracked files:                                                          │
  │     services/api/src/.../db/migration/V004__create_comments_table.sql       │
  │     services/api/src/.../integration/CommentControllerIT.java               │
  │                                                                              │
  │ Step 4: Show git diff                                                       │
  │   [Shows unified diff of all changes - truncated for brevity]               │
  │                                                                              │
  │ Step 5: Create commit                                                       │
  │   $ git add .                                                               │
  │   $ git commit -m "feat: Implement comment system                           │
  │                                                                              │
  │   - Add Comment entity with JPA annotations and indexes                     │
  │   - Create CommentService with CRUD operations                              │
  │   - Implement CommentController with OpenAPI documentation                  │
  │   - Add integration tests (12 tests, mocked external dependencies)          │
  │   - Add structured logging to CommentService                                │
  │   - Update README with comment endpoints                                    │
  │                                                                              │
  │   🤖 Generated with Claude Code                                             │
  │   Co-Authored-By: Claude noreply@anthropic.com"                           │
  │                                                                              │
  │   ✓ Commit created: abc1234                                                 │
  │                                                                              │
  │ Step 6: Detect feature branch + offer PR creation                           │
  │   Current branch: feature/comment-system                                    │
  │   Create pull request? (y/n): y                                             │
  │                                                                              │
  │ Step 7: Create PR                                                           │
  │   $ gh pr create --title "feat: Comment System" --body "..."                │
  │   ✓ PR created: #42                                                         │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: automation-sentinel (auto-triggered on PR creation)               │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Analyzing feature development workflow for PR #42...                        │
  │                                                                              │
  │ ## Workflow Analysis Report                                                 │
  │                                                                              │
  │ ### ✅ Automation Used Effectively                                           │
  │ - /start-session → Loaded context efficiently                               │
  │ - /directive → Added convention to CODING_STYLE.md with deduplication       │
  │ - /review-code → Caught 2 issues before commit                              │
  │ - backend-code-reviewer → Deep analysis with actionable feedback            │
  │ - tech-writer → Auto-documented endpoints, updated README, LEARNINGS.md     │
  │ - /update-roadmap → Tracked progress systematically                         │
  │ - /finish-session → Tests passed, docs updated, commit created              │
  │ - session-optimizer → Monitored token efficiency throughout                 │
  │                                                                              │
  │ ### 📊 Metrics                                                               │
  │ - Commands used: 5 (/start-session, /directive, /review-code, /update-...)  │
  │ - Agents triggered: 4 (session-optimizer, tech-writer, backend-code-...)    │
  │ - Commits created: 4 (directive, roadmap, learnings, feature)               │
  │ - Documentation updated: 4 files (CODING_STYLE, ROADMAP, LEARNINGS, README) │
  │ - Tests written: 12 integration tests                                       │
  │ - Test pass rate: 100%                                                      │
  │                                                                              │
  │ ### 💡 Workflow Efficiency                                                   │
  │ - Token usage: 42,300 / 200,000 (21% - excellent efficiency)                │
  │ - Convention codification: 1 new directive added (prevents future errors)   │
  │ - Documentation hygiene: All 4 docs updated (ROADMAP, LEARNINGS, README,    │
  │   CODING_STYLE)                                                              │
  │ - Quality gates: Code review caught issues before commit                    │
  │                                                                              │
  │ ### 🎯 Observations                                                          │
  │ ✅ Excellent workflow discipline - all steps followed                        │
  │ ✅ Directive added proactively (mocking external deps)                       │
  │ ✅ Documentation kept current with implementation                            │
  │ ✅ Agent usage appropriate (tech-writer, backend-code-reviewer)              │
  │ ⚠️  Consider: Save feature plan with /save-response for complex features     │
  │                                                                              │
  │ ### 🚀 Impact                                                                │
  │ This PR demonstrates best-in-class development workflow:                    │
  │ - Automated quality checks (review-code, agent reviews)                     │
  │ - Documentation as code (4 files updated automatically)                     │
  │ - Knowledge capture (new directive benefits all future sessions)            │
  │ - Clean git history (logical, atomic commits with context)                  │
  │                                                                              │
  │ Next developer working on comments will benefit from:                       │
  │ 1. New directive in CODING_STYLE.md (mocking pattern)                       │
  │ 2. Updated ROADMAP.md (shows what's done)                                   │
  │ 3. Session notes in LEARNINGS.md (implementation details)                   │
  │ 4. OpenAPI docs in Swagger (API contract)                                   │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ ✅ SESSION COMPLETE                                                          │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Summary:                                                                     │
  │ ✓ Tests passed (95 tests, 100% pass rate)                                   │
  │ ✓ Documentation updated (ROADMAP, LEARNINGS, README, CODING_STYLE)          │
  │ ✓ Changes committed (abc1234)                                               │
  │ ✓ PR created (#42 → develop)                                                │
  │ ✓ Automation health analyzed (automation-sentinel)                          │
  │ ✓ Token efficiency monitored (21% of budget used)                           │
  └─────────────────────────────────────────────────────────────────────────────┘

  ┌──────────────────────────────────────────────────────────────────────────────┐
  │ 📊 SESSION 1 STATE SUMMARY                                                   │
  ├──────────────────────────────────────────────────────────────────────────────┤
  │ Git Commits:  4 (directive, roadmap update, learnings, feature commit)      │
  │ Files Added:  5 (Comment.java, CommentService.java, DTOs, migration, test)  │
  │ Files Modified: 3 (CommentController.java, README.md, LEARNINGS.md)         │
  │ Tests Written: 12 integration tests                                         │
  │ Docs Updated: CODING_STYLE.md, ROADMAP.md, LEARNINGS.md, README.md          │
  │ PR Created:   #42 (feature/comment-system → develop)                        │
  │ Agents Used:  4 (session-optimizer, tech-writer, backend-code-reviewer,     │
  │               automation-sentinel)                                           │
  │ Duration:     ~2 hours                                                       │
  │ Token Usage:  42,300 / 200,000 (21%)                                         │
  └──────────────────────────────────────────────────────────────────────────────┘

  ---

  ## Session 2: Resuming Development

  ┌──────────────────────────────────────────────────────────────────────────────┐
  │ SESSION 2: Next Day - Continue with Photo Upload Feature                     │
  └──────────────────────────────────────────────────────────────────────────────┘

  ┌─────────────┐
  │ /resume-session photo-upload                                                  │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: session-optimizer (auto-triggered)                                │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Optimizing session resumption...                                            │
  │ ✓ Analyzing git history for recent changes                                  │
  │ ✓ Detecting relevant context files                                          │
  │ ✓ Prioritizing recent directives from yesterday's session                   │
  │ ✓ Estimated context load: 9,200 tokens                                      │
  │                                                                              │
  │ Recommendations:                                                            │
  │ • Loading photo-upload-plan.md (most relevant to current task)              │
  │ • Including yesterday's directive about mocking (still applicable)          │
  │ • Skipping older LEARNINGS.md entries (not relevant to photo upload)        │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🔄 CONTEXT RECONSTRUCTION                                                    │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Step 1: Load standard project context                                       │
  │   ✓ CLAUDE.md (with yesterday's directive about mocking)                    │
  │   ✓ CODING_STYLE.md                                                         │
  │   ✓ ROADMAP.md (shows comment system completed, photo upload next)          │
  │   ✓ README.md (with comment endpoints)                                      │
  │                                                                              │
  │ Step 2: Search for context file matching "photo-upload"                     │
  │   Searching prompts/responses/photo-upload.md...                           │
  │   ✓ Found: prompts/responses/photo-upload-plan.md                           │
  │                                                                              │
  │ Step 3: Review recent commits                                               │
  │   $ git log --oneline -10                                                   │
  │   abc1234 feat: Implement comment system                                    │
  │   def5678 docs: Update roadmap - comment system completed                   │
  │   ghi9012 docs: Add directive - mock external dependencies                  │
  │   jkl3456 feat: Add Review endpoints with OpenAPI docs                      │
  │   ...                                                                        │
  │                                                                              │
  │ Step 4: Check current git status                                            │
  │   $ git status                                                              │
  │   On branch develop                                                         │
  │   Your branch is up to date with 'origin/develop'                           │
  │   nothing to commit, working tree clean                                     │
  │                                                                              │
  │ Step 5: Identify current step from context file                             │
  │   From prompts/responses/photo-upload-plan.md:                              │
  │   ## Photo Upload Implementation Plan (6 steps)                             │
  │   - [ ] Step 1: Configure AWS S3 client                                     │
  │   - [ ] Step 2: Create pre-signed URL service                               │
  │   - [ ] Step 3: Add photo upload endpoint                                   │
  │   - [ ] Step 4: Update Review entity with photoUrl                          │
  │   - [ ] Step 5: Add integration tests                                       │
  │   - [ ] Step 6: Update API documentation                                    │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 📋 SESSION RESUME SUMMARY                                                    │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Last Session Context:                                                   │
  │   Completed comment system implementation with full CRUD + tests            │
  │                                                                              │
  │ Current Progress:                                                       │
  │   Comment system merged to develop. Ready to start photo upload feature.    │
  │                                                                              │
  │ Last Commit:                                                            │
  │   abc1234 - feat: Implement comment system                                  │
  │                                                                              │
  │ Uncommitted Changes:                                                    │
  │   None (clean working tree)                                                 │
  │                                                                              │
  │ Next Action:                                                            │
  │   Start Step 1: Configure AWS S3 client in application.yml                  │
  │                                                                              │
  │ Available Agents:                                                       │
  │   • backend-code-reviewer - Review S3 configuration and service             │
  │   • tech-writer - Document photo upload flow in README                      │
  │   • session-optimizer - Monitor token usage throughout session              │
  │                                                                              │
  │ Active Directives from Yesterday:                                       │
  │   • Mock GoogleTokenValidator and S3Client in integration tests             │
  │                                                                              │
  │ ✅ Ready to continue from where you left off.                                │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 💻 USER CODES (implement photo upload - 90 minutes)                          │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ User: "Implement S3 pre-signed URL service"                                 │
  │ Claude: [reads CLAUDE.md → uses constructor injection, ConfigProperties]    │
  │                                                                              │
  │ User: "Add photo upload endpoint to ReviewController"                       │
  │ Claude: [automatically triggers tech-writer for OpenAPI docs]               │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: tech-writer (auto-triggered on new REST endpoint)                 │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Detected new endpoint: POST /photos/presigned-url                           │
  │                                                                              │
  │ Adding OpenAPI annotations:                                                 │
  │ ✓ @Operation(summary = "Generate pre-signed URL for photo upload")          │
  │ ✓ @ApiResponses: 200, 401, 500                                              │
  │ ✓ @RequestBody for PresignedUrlRequest                                      │
  │                                                                              │
  │ Updating README.md:                                                         │
  │ ✓ Added "Photo Upload Flow" section                                         │
  │ ✓ Step 1: Request pre-signed URL                                            │
  │ ✓ Step 2: Upload directly to S3                                             │
  │ ✓ Step 3: Submit photo URL with review                                      │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 💻 USER CONTINUES CODING                                                     │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ User: "Write integration tests for photo upload"                            │
  │ Claude: [reads yesterday's directive → mocks S3Client automatically]        │
  │                                                                              │
  │ Files created/modified:                                                     │
  │   A  services/api/src/.../config/S3Properties.java                          │
  │   A  services/api/src/.../service/PhotoUploadService.java                   │
  │   M  services/api/src/.../controller/ReviewController.java                  │
  │   M  services/api/src/.../domain/Review.java                                │
  │   A  services/api/src/.../db/migration/V005__add_photo_url_to_reviews.sql   │
  │   A  services/api/src/.../integration/PhotoUploadIT.java                    │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────┐
  │ /finish-session "Photo upload with pre-signed URLs"                          │
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: session-optimizer (auto-triggered)                                │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Session efficiency analysis:                                                │
  │ ✓ Token usage: 38,700 / 200,000 (19% of budget)                             │
  │ ✓ Context resumption efficient (loaded only relevant files)                 │
  │ ✓ Yesterday's directive automatically applied (S3Client mocking)            │
  │ ✓ No redundant explanations (Claude remembered conventions)                 │
  │                                                                              │
  │ Time saved by /resume-session:                                              │
  │ • ~15 minutes (no re-explaining conventions)                                │
  │ • ~10 minutes (context file provided implementation plan)                   │
  │ • ~5 minutes (directive auto-applied to tests)                              │
  │                                                                              │
  │ Total estimated time saved: ~30 minutes                                     │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🏁 FINISH SESSION WORKFLOW                                                   │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Step 1: Run tests                                                           │
  │   $ ./mvnw verify -q                                                        │
  │   ✓ All tests passing (103 tests, 0 failures) [+8 new tests]                │
  │                                                                              │
  │ Step 2: Prompt for documentation updates                                    │
  │   Update LEARNINGS.md? (y/n): y                                             │
  │   Update ROADMAP.md? (y/n): y                                               │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: tech-writer (auto-triggered for doc updates)                      │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Updating LEARNINGS.md...                                                    │
  │ ✓ Added session: "2025-11-03 - Photo Upload with Pre-signed URLs"           │
  │ ✓ Documented S3 configuration with ConfigurationProperties                  │
  │ ✓ Noted pre-signed URL generation with 15-minute expiry                     │
  │ ✓ Recorded test mocking pattern for S3Client                                │
  │                                                                              │
  │ Updating ROADMAP.md...                                                      │
  │ ✓ Moved "Photo upload" from "In Progress" to "Implemented"                  │
  │ ✓ Updated priorities: Next up is Flutter authentication flow                │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🏁 FINISH SESSION (continued)                                                │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Step 3: Git status + diff                                                   │
  │   [Shows all changes - S3 config, service, controller, migration, tests]    │
  │                                                                              │
  │ Step 4: Create commit                                                       │
  │   $ git commit -m "feat: Add photo upload with S3 pre-signed URLs           │
  │                                                                              │
  │   - Configure S3Properties with ConfigurationProperties                     │
  │   - Implement PhotoUploadService with 15-minute URL expiry                  │
  │   - Add POST /photos/presigned-url endpoint                                 │
  │   - Update Review entity with photoUrl column                               │
  │   - Add integration tests (8 tests, mocked S3Client)                        │
  │   - Document photo upload flow in README                                    │
  │                                                                              │
  │   🤖 Generated with Claude Code                                             │
  │   Co-Authored-By: Claude noreply@anthropic.com"                           │
  │                                                                              │
  │   ✓ Commit created: xyz7890                                                 │
  │                                                                              │
  │ Step 5: Create PR                                                           │
  │   Create pull request? (y/n): y                                             │
  │   $ gh pr create --title "feat: Photo Upload" --body "..."                  │
  │   ✓ PR created: #43                                                         │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ 🤖 AGENT: automation-sentinel (auto-triggered)                              │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ Analyzing feature development workflow for PR #43...                        │
  │                                                                              │
  │ ## Workflow Analysis Report                                                 │
  │                                                                              │
  │ ### ✅ Automation Used Effectively                                           │
  │ - /resume-session → Restored context from yesterday + loaded plan           │
  │ - tech-writer → Auto-documented endpoint + updated README                   │
  │ - session-optimizer → Monitored efficiency, saved ~30 minutes               │
  │ - /finish-session → Tests passed, docs updated, commit created, PR created  │
  │                                                                              │
  │ ### 📊 Metrics                                                               │
  │ - Commands used: 2 (/resume-session, /finish-session)                       │
  │ - Agents triggered: 3 (session-optimizer x2, tech-writer x2)                │
  │ - Commits created: 1 (feature commit)                                       │
  │ - Documentation updated: 3 files (ROADMAP, LEARNINGS, README)               │
  │ - Tests written: 8 integration tests                                        │
  │ - Test pass rate: 100%                                                      │
  │                                                                              │
  │ ### 💡 Workflow Efficiency                                                   │
  │ - Token usage: 38,700 / 200,000 (19% - excellent)                           │
  │ - Directives from yesterday automatically applied (S3 mocking)               │
  │ - Context file provided clear implementation roadmap                        │
  │ - No redundant context loading (optimizer skipped irrelevant sections)      │
  │                                                                              │
  │ ### 🎯 Key Observations                                                      │
  │ ✅ /resume-session successfully restored development state                   │
  │ ✅ Yesterday's directive paid immediate dividends (auto-mocked S3)           │
  │ ✅ session-optimizer identified 30-minute time savings                       │
  │ ✅ tech-writer auto-triggered twice (endpoint docs + LEARNINGS update)       │
  │                                                                              │
  │ ### 📈 Cross-Session Benefits                                                │
  │ Investment from Session 1 (directive creation) yielded:                     │
  │ - Automatic test mocking (no manual decision needed)                        │
  │ - Consistent patterns across features                                       │
  │ - Documentation hygiene maintained                                          │
  │                                                                              │
  │ This demonstrates the compound effect of codifying conventions:             │
  │ Time spent in Session 1: +5 min (creating directive)                        │
  │ Time saved in Session 2: -5 min (auto-applied)                              │
  │ Time saved in future sessions: -5 min each (cumulative benefit)             │
  └─────────────────────────────────────────────────────────────────────────────┘
        │
        ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ ✅ SESSION COMPLETE                                                          │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │ ✓ Tests passed (103 tests, 100% pass rate)                                  │
  │ ✓ Documentation updated (ROADMAP, LEARNINGS, README)                        │
  │ ✓ Changes committed (xyz7890)                                               │
  │ ✓ PR created (#43 → develop)                                                │
  │ ✓ Automation health analyzed                                                │
  │ ✓ Token efficiency: 19% of budget (excellent)                               │
  │ ✓ Time saved: ~30 minutes vs fresh start                                    │
  └─────────────────────────────────────────────────────────────────────────────┘

  ┌──────────────────────────────────────────────────────────────────────────────┐
  │ 📊 SESSION 2 STATE SUMMARY                                                   │
  ├──────────────────────────────────────────────────────────────────────────────┤
  │ Context Loaded: CLAUDE.md + ROADMAP.md + photo-upload-plan.md + git history │
  │ Git Commits:    1 feature commit                                            │
  │ Files Added:    4 (S3Properties, PhotoUploadService, migration, test)       │
  │ Files Modified: 2 (ReviewController, Review entity)                         │
  │ Tests Written:  8 integration tests                                         │
  │ Docs Updated:   LEARNINGS.md, ROADMAP.md, README.md                         │
  │ PR Created:     #43 (feature/photo-upload → develop)                        │
  │ Agents Used:    3 (session-optimizer x2, tech-writer x2)                    │
  │ Duration:       ~1.5 hours                                                   │
  │ Token Usage:    38,700 / 200,000 (19%)                                       │
  │ Time Saved:     ~30 minutes (vs fresh start without /resume-session)        │
  │                                                                              │
  │ 🎯 Key Benefit: Yesterday's directive automatically applied to tests        │
  └──────────────────────────────────────────────────────────────────────────────┘

  ---

  ## Agent Interaction Map

  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ AGENT ECOSYSTEM: How Agents Work Together                                   │
  └─────────────────────────────────────────────────────────────────────────────┘

  /start-session
       │
       ▼
  ┌─────────────────────┐
  │ session-optimizer   │ ← Auto-triggered at session start
  │ (Load Context)      │   Optimizes what to load, skips irrelevant files
  └─────────┬───────────┘
            │
            ▼
  [User codes with Claude]
       │
       ├─► Detects new REST endpoint
       │        │
       │        ▼
       │   ┌─────────────────────┐
       │   │ tech-writer         │ ← Auto-triggered
       │   │ (OpenAPI Docs)      │   Adds @Operation, @ApiResponses
       │   └─────────────────────┘   Updates README with endpoint
       │
       ├─► User runs /review-code
       │        │
       │        ▼
       │   Quick breadth audit → Finds issues
       │        │
       │        ├─► User wants deep analysis
       │        │        │
       │        │        ▼
       │        │   ┌─────────────────────┐
       │        │   │backend-code-reviewer│ ← Manual trigger
       │        │   │(Deep Review)        │   Line-by-line analysis
       │        │   └─────────────────────┘   Educational feedback
       │        │
       │        └─► Issues applied → Code improved
       │
       └─► /finish-session
                │
                ▼
       ┌─────────────────────┐
       │ session-optimizer   │ ← Auto-triggered at session end
       │ (Efficiency Report) │   Token usage, time saved, suggestions
       └─────────┬───────────┘
                 │
                 ▼
       ┌─────────────────────┐
       │ tech-writer         │ ← Auto-triggered for LEARNINGS.md
       │ (Session Notes)     │   Documents what was implemented
       └─────────┬───────────┘
                 │
                 ▼
       Create PR
                 │
                 ▼
       ┌─────────────────────┐
       │automation-sentinel  │ ← Auto-triggered on PR creation
       │ (Workflow Analysis) │   Analyzes automation health
       └─────────────────────┘   Identifies improvements
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ AGENT SPECIALIZATIONS                                                       │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │                                                                              │
  │ session-optimizer                                                           │
  │ ├─ Triggers: /start-session, /finish-session                                │
  │ ├─ Purpose: Token efficiency, context optimization                          │
  │ └─ Output: Token usage report, time savings, recommendations                │
  │                                                                              │
  │ tech-writer                                                                 │
  │ ├─ Triggers: New REST endpoints, doc update prompts, /finish-session        │
  │ ├─ Purpose: OpenAPI docs, README updates, LEARNINGS.md entries              │
  │ └─ Output: Complete annotations, formatted documentation                    │
  │                                                                              │
  │ backend-code-reviewer                                                       │
  │ ├─ Triggers: Manual (after /review-code finds issues)                       │
  │ ├─ Purpose: Deep code review with educational feedback                      │
  │ └─ Output: Line-by-line analysis, best practices, learning points           │
  │                                                                              │
  │ automation-sentinel                                                         │
  │ ├─ Triggers: /create-pr (PR creation)                                       │
  │ ├─ Purpose: Meta-analysis of automation health                              │
  │ └─ Output: Workflow report, metrics, efficiency observations                │
  │                                                                              │
  └─────────────────────────────────────────────────────────────────────────────┘

  ---

  ## The Compound Effect

  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ HOW AUTOMATION COMPOUNDS ACROSS SESSIONS                                    │
  └─────────────────────────────────────────────────────────────────────────────┘

  Session 1: Investment
  ─────────────────────
  │ /directive "Mock GoogleTokenValidator and S3Client in tests"
  │     ↓
  │ Time spent: +5 minutes (creating directive)
  │     ↓
  │ Added to: CODING_STYLE.md → Part 2: Backend → Testing
  │     ↓
  │ Benefit: Every future session automatically applies this pattern

  Session 2: First Dividend
  ──────────────────────────
  │ /resume-session photo-upload
  │     ↓
  │ Loads: CODING_STYLE.md (includes yesterday's directive)
  │     ↓
  │ User: "Write integration tests for photo upload"
  │     ↓
  │ Claude: [reads directive → automatically mocks S3Client]
  │     ↓
  │ Time saved: -5 minutes (no decision needed, pattern auto-applied)

  Session 3: Continued Dividends
  ───────────────────────────────
  │ /resume-session wine-catalog
  │     ↓
  │ User: "Write integration tests for WineController"
  │     ↓
  │ Claude: [reads directive → automatically mocks S3Client again]
  │     ↓
  │ Time saved: -5 minutes (cumulative benefit continues)

  Session N: Exponential Returns
  ───────────────────────────────
  │ Every new integration test benefits from the directive
  │     ↓
  │ Investment: 5 minutes (Session 1)
  │ Returns: 5 minutes × N sessions
  │     ↓
  │ By Session 10: 50 minutes saved, 10× ROI
  │     ↓
  │ Plus: Consistent patterns, fewer bugs, cleaner code

  ┌─────────────────────────────────────────────────────────────────────────────┐
  │ AUTOMATION ROI CALCULATION                                                  │
  ├─────────────────────────────────────────────────────────────────────────────┤
  │                                                                              │
  │ Directive Creation (Session 1)                                              │
  │   Time: 5 minutes                                                           │
  │   Cost: 1× effort                                                           │
  │                                                                              │
  │ Directive Application (Sessions 2-N)                                        │
  │   Time: 0 minutes (automatic)                                               │
  │   Savings: 5 minutes per session                                            │
  │                                                                              │
  │ Break-even: Session 2 (5 min invested = 5 min saved)                        │
  │ 10 sessions: 45 minutes net savings (9× ROI)                                │
  │ 50 sessions: 245 minutes net savings (49× ROI)                              │
  │                                                                              │
  │ Additional benefits:                                                        │
  │ • Consistency: 100% pattern adherence                                       │
  │ • Quality: Fewer bugs from forgotten mocking                                │
  │ • Onboarding: New devs learn patterns automatically                         │
  │ • Documentation: Self-documenting codebase                                  │
  │                                                                              │
  └─────────────────────────────────────────────────────────────────────────────┘

  ---

  ## Summary: The Full Workflow Pattern

  ┌──────────────────────────────────────────────────────────────────────────────┐
  │ REPEATING DEVELOPMENT CYCLE                                                  │
  └──────────────────────────────────────────────────────────────────────────────┘

    ┌────────────────────────────────────────────────────┐
    │  Session Start                                     │
    │  ─────────────                                     │
    │  /start-session   or   /resume-session             │
    │       │                      │                     │
    │       ▼                      ▼                     │
    │  session-optimizer    session-optimizer            │
    │  (optimize load)      (restore + analyze)          │
    └────────┬───────────────────────┬───────────────────┘
             │                       │
             └───────────┬───────────┘
                         ▼
    ┌────────────────────────────────────────────────────┐
    │  Development Loop                                  │
    │  ────────────────                                  │
    │  ┌──────────────────────┐                          │
    │  │ Code Implementation  │◄──────┐                  │
    │  └──────┬───────────────┘       │                  │
    │         │                       │                  │
    │         ├─► Endpoint detected  │                  │
    │         │       │               │                  │
    │         │       ▼               │                  │
    │         │   tech-writer         │                  │
    │         │   (OpenAPI docs)      │                  │
    │         │                       │                  │
    │         ├─► /directive          │                  │
    │         │   (add convention)    │                  │
    │         │                       │                  │
    │         ├─► /review-code        │                  │
    │         │   (quick audit)       │                  │
    │         │       │               │                  │
    │         │       ├─► Issues? ────┤                  │
    │         │       │               │                  │
    │         │       ▼               │                  │
    │         │   backend-code-reviewer                  │
    │         │   (deep analysis)     │                  │
    │         │                       │                  │
    │         └─► Fix issues ─────────┘                  │
    │                                                    │
    │  /update-roadmap (track progress)                  │
    └────────┬───────────────────────────────────────────┘
             │
             ▼
    ┌────────────────────────────────────────────────────┐
    │  Session End                                       │
    │  ───────────                                       │
    │  /finish-session                                   │
    │       │                                            │
    │       ▼                                            │
    │  session-optimizer (efficiency report)              │
    │       │                                            │
    │       ▼                                            │
    │  Run tests (./mvnw verify -q)                      │
    │       │                                            │
    │       ▼                                            │
    │  tech-writer (update LEARNINGS.md)                 │
    │       │                                            │
    │       ▼                                            │
    │  Git commit + PR creation                          │
    │       │                                            │
    │       ▼                                            │
    │  automation-sentinel (workflow analysis)           │
    │       │                                            │
    │       ▼                                            │
    │  ✅ Session complete                                │
    └────────┬───────────────────────────────────────────┘
             │
             └──────► Next session starts here
                      (context preserved for resume)
  ┌──────────────────────────────────────────────────────────────────────────────┐
  │ KEY INSIGHTS                                                                 │
  ├──────────────────────────────────────────────────────────────────────────────┤
  │                                                                              │
  │ 1. Compound Learning                                                         │
  │    Each /directive improves Claude's behavior for ALL future sessions        │
  │                                                                              │
  │ 2. Context Preservation                                                      │
  │    /finish-session saves state → /resume-session restores it                 │
  │                                                                              │
  │ 3. Git Integration                                                           │
  │    Workflow aware of branches, commits, uncommitted changes                  │
  │                                                                              │
  │ 4. Automation Layering                                                       │
  │    Commands chain together (finish → PR → automation-sentinel)               │
  │                                                                              │
  │ 5. Documentation as Code                                                     │
  │    Directives versioned in git, evolve with codebase                         │
  │                                                                              │
  │ 6. Agent Orchestration                                                       │
  │    Auto-triggered agents handle repetitive tasks (docs, reviews, analysis)   │
  │                                                                              │
  │ 7. Token Efficiency                                                          │
  │    session-optimizer minimizes waste, maximizes relevant context             │
  │                                                                              │
  │ 8. Exponential ROI                                                           │
  │    Time invested in automation compounds across all future sessions          │
  │                                                                              │
  └──────────────────────────────────────────────────────────────────────────────┘

