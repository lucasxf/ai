---
description: Run tests for a specific service class
---

Run unit tests for {{SERVICE_NAME}}

```bash
cd services/api && ./mvnw test -q -Dtest={{SERVICE_NAME}}Test
```

Example usage:
- `/test-service ReviewService`
- `/test-service AuthService`
