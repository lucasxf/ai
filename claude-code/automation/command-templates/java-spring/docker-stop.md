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

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Directory path:** Update `infra` to where your `docker-compose.yml` is located

2. **Compose file:** If using non-default compose file:
   ```bash
   docker compose -f docker-compose.dev.yml down
   ```

3. **Preserve volumes:** If you want to keep data volumes:
   ```bash
   docker compose down
   ```

4. **Remove everything (including images):**
   ```bash
   docker compose down -v --rmi all
   ```

5. **Stop specific service only:**
   ```bash
   docker compose stop api
   docker compose rm -f api
   ```
