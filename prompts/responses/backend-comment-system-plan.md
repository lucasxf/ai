  # BACKEND PLAN: Comment System (Item 4)

  | Step | Task                                    | Files Created/Modified                                                | Commit Message                                         | Test Command                           |
  |------|-----------------------------------------|-----------------------------------------------------------------------|--------------------------------------------------------|----------------------------------------|
  | 1    | Comment entity + repository + migration | Comment.java, CommentRepository.java, V3__create_comments_table.sql   | feat: Add Comment entity and repository                | ./mvnw verify                          |
  | 2    | CommentService + unit tests             | CommentService.java, CommentServiceImpl.java, CommentServiceTest.java | feat: Add CommentService with CRUD logic (15 tests)    | ./mvnw test -Dtest=CommentServiceTest  |
  | 3    | CommentController + OpenAPI docs        | CommentController.java                                                | feat: Add CommentController with OpenAPI documentation | Check Swagger UI                       |
  | 4    | Integration tests                       | CommentControllerIT.java                                              | test: Add CommentControllerIT (18 tests)               | ./mvnw test -Dtest=CommentControllerIT |
  | 5    | Exception handling                      | GlobalExceptionHandler.java (update)                                  | feat: Add comment-specific exception handling          | Full test suite                        |
  | 6    | Documentation                           | README.md                                                             | docs: Update README with comment endpoints             | Manual review                          |

  Acceptance Criteria:
  - ✅ POST /reviews/{id}/comments → 201 Created
  - ✅ GET /reviews/{id}/comments → 200 OK with pagination
  - ✅ DELETE review → Cascade deletes comments (test in IT)
  - ✅ Ownership check (403 if trying to delete someone else's comment)
  - ✅ 100% OpenAPI documentation
  - ✅ All tests passing

  ---