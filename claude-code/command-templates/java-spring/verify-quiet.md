---
description: Run full verification (build + tests) in quiet mode
---

Run clean build + all tests + package verification

```bash
cd services/api && ./mvnw clean verify -q
```

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Directory path:** Update `services/api` to your actual backend directory

2. **Build tool:** If using Gradle:
   ```bash
   ./gradlew clean check --quiet
   ```

3. **CI/CD mode:** Add `-Dmaven.test.failure.ignore=false` to fail fast:
   ```bash
   ./mvnw clean verify -q -Dmaven.test.failure.ignore=false
   ```

4. **Skip specific checks:** If you want to skip integration tests or other plugins:
   ```bash
   ./mvnw clean verify -q -DskipITs=true -Dcheckstyle.skip=true
   ```
