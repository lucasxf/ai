| Feature      | # (Read)                              | /directive (Write)                                                      |
|--------------|---------------------------------------|-------------------------------------------------------------------------|
| Purpose      | Reference files passively             | Modify documentation actively                                           |
| Action       | Includes file content in conversation | Writes new directive to CLAUDE.md or CODING_STYLE.md                    |
| Intelligence | None (just loads content)             | Smart deduplication, similar directive detection                        |
| Workflow     | One-step (select file)                | Multi-step (search → deduplicate → determine section → format → commit) |
| Use Case     | "Look at this file while we chat"     | "Permanently add this coding rule to project conventions"               |
