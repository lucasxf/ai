---
description: Start all Docker services (PostgreSQL + API)
---

Start all services with Docker Compose (quiet mode)

```bash
cd infra && docker compose up -d --build --quiet-pull
```

View logs if needed:
```bash
docker compose logs -f --tail=50 api
```
