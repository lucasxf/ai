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

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Directory path:** Update `infra` to where your `docker-compose.yml` is located
   - Common alternatives: `.` (root), `docker/`, `deployment/`

2. **Service names:** Update `api` in logs command to your actual service names:
   ```bash
   docker compose logs -f --tail=50 backend
   docker compose logs -f --tail=50 postgres database
   ```

3. **Compose file:** If using non-default compose file:
   ```bash
   docker compose -f docker-compose.dev.yml up -d --build --quiet-pull
   ```

4. **Additional services:** Add health checks or wait commands if needed:
   ```bash
   docker compose up -d --build --quiet-pull && \
   docker compose exec -T postgres pg_isready -U myuser
   ```

5. **Environment files:** Specify environment file if needed:
   ```bash
   docker compose --env-file .env.dev up -d --build --quiet-pull
   ```
