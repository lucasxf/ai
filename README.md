# AI Laboratory

> Hands-on experiments with AI/ML/LLM technologies, starting with Model Context Protocol (MCP) studies.

**Author:** Lucas Xavier Ferreira
**Focus:** Production-quality implementations, deep understanding, technical articles
**Stack:** Java 21, Spring Boot 3.2+, Maven, Docker

---

## 🌍 PART 1: GENERAL (Cross-stack)

### Repository Overview

This repository serves as a laboratory for exploring cutting-edge AI technologies through proof-of-concept implementations. Unlike throwaway prototypes, all code follows production-quality standards (SOLID, Clean Architecture, >80% test coverage) documented in [CODING_STYLE.md](./CODING_STYLE.md).

### Structure

```
/ai
├── README.md                          # This file - repository overview
├── CLAUDE.md                          # AI assistant context and guidelines
├── CODING_STYLE.md                    # Code conventions (copied from wine-reviewer)
├── LEARNINGS.md                       # Technical discoveries and session logs (planned)
├── prompts/
│   └── PACK.md                        # AI prompt patterns (copied from wine-reviewer)
├── .claude/
│   └── commands/                      # Reusable workflows (from wine-reviewer)
│       ├── README.md                  # Command documentation
│       ├── create-service.md
│       ├── add-test.md
│       └── ...
├── mcp/                               # Model Context Protocol studies (active)
│   ├── README.md                      # MCP POC index
│   ├── ROADMAP.md                     # Implementation tracking
│   ├── pom.xml                        # Parent POM
│   ├── 01-hello-world/                # POC 1: Protocol basics
│   ├── 02-aws-cost-explorer/          # POC 2: AWS FinOps (planned)
│   ├── 03-photo-search/               # POC 3: NL photo search (planned)
│   └── mcp-protocol-java/             # Shared MCP library (planned)
└── other-ai-studies/                  # Future experiments
```

### Git Flow

```
main                     # Stable releases (published articles)
  └─ develop             # Main development
      └─ develop-mcp     # Long-lived MCP branch
          ├─ feature/poc-01-hello-world  ← Current branch
          ├─ feature/poc-02-aws-finops
          └─ feature/poc-03-photo-search
```

**Merge Strategy:**
- Feature → `develop-mcp` (via PR after POC completion)
- `develop-mcp` → `develop` (after POC validation)
- `develop` → `main` (after article publication)

### References

This repository reuses conventions from:
- [wine-reviewer](https://github.com/lucasxf/wine-reviewer/tree/develop) - Spring Boot patterns, AI workflows
- [estudos](https://github.com/lucasxf/estudos/tree/develop) - Advanced architecture (DDD, CQRS)

---

## ☕ PART 2: BACKEND (Java/Spring Boot)

### MCP Studies (Active)

**Model Context Protocol (MCP):** A standardized protocol for LLM-to-tool communication, enabling:
- Client-Server architecture (stdio, HTTP, SSE transports)
- Tool definitions and invocations
- Sampling (server requests LLM via client for intelligent pipelines)
- Multi-server orchestration (servers as clients)

#### Current Status

**Project:** `/mcp` directory
**Setup:** Maven multi-module project with parent POM
**POC 1:** Hello World MCP (In Progress)
**Status:** Boilerplate created, architectural decisions next

**Progress Tracking:** See [/mcp/ROADMAP.md](./mcp/ROADMAP.md) for detailed status

#### How to Build

```bash
# Navigate to MCP directory
cd mcp

# Build all modules
mvn clean install

# Run specific POC (after implementation)
cd 01-hello-world
mvn spring-boot:run
```

#### Planned POCs

| POC | Goal                                  | Stack                              | Status      |
| --- | ------------------------------------- | ---------------------------------- | ----------- |
| 1   | MCP Protocol Basics                   | Java client + server (stdio)       | In Progress |
| 2   | AWS Cost Explorer Integration         | Java client + Python server        | Planned     |
| 3   | Photo Search (Google Drive)           | Java server + Vision API + VectorDB | Planned     |
| 4   | MCP Sampling (Server Requests LLM)    | Bidirectional communication        | Planned     |
| 5   | MCP Gateway (Server as Client)        | Multi-server orchestration         | Planned     |

#### Technology Stack

**Core:**
- Java 21 (LTS) with Virtual Threads
- Spring Boot 3.2+
- Maven 3.9+

**MCP Integration:**
- Spring AI MCP Client
- Custom stdio transport implementation
- JSON-RPC message codec

**Testing:**
- JUnit 5
- Testcontainers (for integration tests)
- Coverage target: >80%

**Documentation:**
- OpenAPI/Swagger (for REST endpoints if applicable)
- Javadoc with `@author` and `@date`
- Architecture diagrams (ASCII art / Mermaid)

#### Code Quality Standards

All code follows conventions from [CODING_STYLE.md](./CODING_STYLE.md):
- Constructor injection only (no `@Autowired` on fields)
- `@ConfigurationProperties` instead of `@Value`
- OpenAPI annotations on REST endpoints
- Test coverage >80%
- Domain exceptions hierarchy
- Java 21 features (records, sealed classes, pattern matching)

#### Available Commands

Custom workflows copied from wine-reviewer:

| Command                | Purpose                                   |
| ---------------------- | ----------------------------------------- |
| `/create-service`      | Create Spring service with tests          |
| `/add-test`            | Add test cases to existing code           |
| `/review-architecture` | Review architectural decisions            |
| `/generate-docs`       | Generate/update OpenAPI documentation    |

See `/.claude/commands/README.md` for complete documentation.

---

## 📱 PART 3: FRONTEND (Not Applicable)

This repository is currently backend-focused (MCP protocol implementation in Java).

If future POCs require UI (e.g., cost dashboard, photo search interface), Flutter/Dart conventions from [wine-reviewer](https://github.com/lucasxf/wine-reviewer/blob/develop/CODING_STYLE.md#-part-3-frontend-standards-flutterdart) will be applied.

---

## 📚 Documentation

### Main Documentation

- [CLAUDE.md](./CLAUDE.md) - AI assistant context (who I am, work style, references)
- [CODING_STYLE.md](./CODING_STYLE.md) - Code conventions (3-part structure: General/Backend/Frontend)
- [/mcp/ROADMAP.md](./mcp/ROADMAP.md) - MCP project tracking and next steps
- [LEARNINGS.md](./LEARNINGS.md) - Technical discoveries (planned, will be created during POCs)

### External Resources

**MCP Protocol:**
- [Official Specification](https://modelcontextprotocol.io/docs)
- [Spring AI MCP Docs](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [Spring AI MCP Examples](https://github.com/spring-projects-experimental/spring-ai-mcp)
- [Baeldung Tutorial](https://www.baeldung.com/spring-ai-model-context-protocol-mcp)

**Spring AI:**
- [Official Documentation](https://docs.spring.io/spring-ai/reference/)
- [GitHub Repository](https://github.com/spring-projects/spring-ai)

---

## 🎯 Project Goals

1. **Deep Understanding** - Master MCP protocol internals through hands-on implementation
2. **Production Quality** - All code follows professional standards (SOLID, Clean Architecture, tests)
3. **Knowledge Sharing** - Publish 5 technical articles (one per POC) explaining learnings
4. **Reusable Code** - Extract MCP Java library as standalone module for community
5. **Real-world Applications** - Build practical tools (cost analysis, photo search, etc.)

---

## 🚀 Getting Started

### Prerequisites

- Java 21 (LTS)
- Maven 3.9+
- Docker (for integration tests with Testcontainers)
- IDE with Java 21 support (IntelliJ IDEA recommended)

### Quick Start

```bash
# Clone the repository
git clone https://github.com/lucasxf/ai.git
cd ai

# Build MCP project
cd mcp
mvn clean install

# Run POC 1 (after implementation)
cd 01-hello-world
mvn spring-boot:run
```

### Development Workflow

1. **Checkout feature branch:**
   ```bash
   git checkout feature/poc-01-hello-world
   ```

2. **Make changes following CODING_STYLE.md**

3. **Run tests:**
   ```bash
   mvn test
   ```

4. **Check coverage (target >80%):**
   ```bash
   mvn verify
   ```

5. **Create PR to `develop-mcp` branch**

---

## 🤝 Contributing

This is a personal learning repository, but feedback and suggestions are welcome!

If you find issues or have ideas:
1. Open an issue describing the problem/idea
2. Reference specific POC (01-hello-world, 02-aws-cost-explorer, etc.)
3. Include code snippets or references if applicable

---

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 👤 Author

**Lucas Xavier Ferreira**
- Role: Tech Manager / Software Engineering Coordinator
- GitHub: [@lucasxf](https://github.com/lucasxf)
- Stack: Java 21, Spring Boot 3, microservices, distributed systems
- Philosophy: Quality over speed, production-ready over prototypes

---

## 🔄 Update History

- **2025-11-04:** Initial README created. MCP project structure setup with parent POM, POC 1 boilerplate in progress.
