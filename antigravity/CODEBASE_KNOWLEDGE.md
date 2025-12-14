# Codebase Knowledge

## 🎯 Project Goal
**AI/MCP Studies Laboratory**
This project is a structured learning environment for mastering **Model Context Protocol (MCP)** through hands-on POCs and technical articles. It focuses exclusively on backend development using Java 21, Spring Boot 3, and Spring AI.

## 📂 Structure
- **/ai**: Root directory.
- **mcp/**: Contains MCP Proof of Concepts (e.g., `01-hello-world`, `02-aws-cost-explorer`).
- **.claude/**: Contains custom automation agents and commands (copied/adapted from `wine-reviewer`).
- **CODING_STYLE.md**: The definitive coding style guide (copied from `wine-reviewer`).
- **CLAUDE.md**: Context and instructions for the AI assistant.
- **ROADMAP.md**: Project tracking and status.
- **LEARNINGS.md**: Session logs and MCP discoveries.

## 🏗️ Architecture & Tech Stack
- **Language:** Java 21 (Virtual Threads, Records, Pattern Matching).
- **Framework:** Spring Boot 3.2+.
- **AI Integration:** Spring AI (MCP client/server).
- **Build:** Maven 3.8+.
- **Architecture Patterns:**
    - **Clean Architecture / DDD**: Clear separation of layers (Application, Domain, Infrastructure).
    - **SOLID Principles**: Strictly enforced.
    - **Constructor Injection**: Mandatory (no `@Autowired` on fields).
    - **Configuration**: `@ConfigurationProperties` (no `@Value`).
    - **Documentation**: OpenAPI/Swagger mandatory for all REST endpoints.

## 📝 Coding Style
- **Conventions:** Follows `wine-reviewer` standards.
- **Language:** Code in English; Comments/Logs can be in Portuguese.
- **Testing:** Test-After-Implementation workflow. Target >80% coverage.
- **Formatting:**
    - Line in blank before closing class bracket `}`.
    - Closing parenthesis `)` on the same line for multi-line calls.
- **Documentation:** 3-part structure (General, Backend, Frontend) for main docs.

## 🔗 Reference Project
- **Name:** `wine-reviewer`
- **Relationship:** Parent project/Source of truth for conventions.
- **Reused Assets:** `CODING_STYLE.md`, Agents, Commands.

## 🤖 Automation
- **Agents:** 6 custom agents (e.g., `backend-code-reviewer`, `tech-writer`).
- **Commands:** 16 custom slash commands (e.g., `/create-service`, `/add-test`).
