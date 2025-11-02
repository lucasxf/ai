---
description: Clean build in quiet mode
---

Clean build without verbose Maven logs

```bash
cd services/api && ./mvnw clean install -q
```

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Directory path:** Update `services/api` to your actual backend directory
   - Monorepo examples: `backend/`, `api/`, `server/`
   - Single repo: Use `.` (current directory)

2. **Build tool:** If using Gradle instead of Maven:
   ```bash
   ./gradlew clean build --quiet
   ```

3. **Multi-module projects:** Specify module if needed:
   ```bash
   ./mvnw clean install -q -pl :module-name
   ```
