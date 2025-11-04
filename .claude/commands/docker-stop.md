---
description: Stop all Docker services
---

Stop all Docker Compose services

```bash
cd infra && docker compose down
```

Stop and remove volumes (clean slate):
```bash
cd infra && docker compose down -v
```
