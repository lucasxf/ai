---
description: Open API documentation (Swagger UI)
---

Open Swagger UI in browser

**URL:** http://localhost:8080/swagger-ui.html

Make sure the API is running first:
```bash
cd services/api && ./mvnw spring-boot:run
```

---

## Adaptation Guide

**Required adaptations for your project:**

1. **Port number:** Update `8080` if your API runs on a different port
   - Common alternatives: `3000`, `8081`, `5000`

2. **Swagger path:** Update path based on your springdoc-openapi configuration:
   - `/swagger-ui.html` (default)
   - `/swagger-ui/index.html` (springdoc-openapi v2+)
   - `/api-docs` (custom configuration)

3. **Directory path:** Update `services/api` to your actual backend directory

4. **Build tool:** If using Gradle:
   ```bash
   ./gradlew bootRun
   ```

5. **Profile:** If API requires specific profile to run:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

6. **Alternative documentation tools:**
   - **Redoc:** `/redoc` (if using redoc-openapi)
   - **OpenAPI JSON:** `/v3/api-docs` (raw OpenAPI spec)
   - **Postman:** Import from `/v3/api-docs` URL
