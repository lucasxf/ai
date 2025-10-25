---
description: Run all tests in quiet mode
---

Run all tests silently (no verbose Spring Boot logs)

```bash
cd services/api && ./mvnw test -q
```

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Directory path:** Update `services/api` to your actual backend directory

2. **Build tool:** If using Gradle:
   ```bash
   ./gradlew test --quiet
   ```

3. **Test profile:** If tests require specific profile:
   ```bash
   ./mvnw test -q -Dspring.profiles.active=test
   ```

4. **Skip integration tests:** To run only unit tests:
   ```bash
   ./mvnw test -q -DskipITs=true
   ```
