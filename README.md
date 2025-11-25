# AI/MCP Studies Laboratory

![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-brightgreen.svg)
![Spring AI](https://img.shields.io/badge/Spring%20AI-MCP-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)
![License](https://img.shields.io/badge/License-Private-lightgrey.svg)

> Laboratory for AI/ML/LLM experiments focused on **Model Context Protocol (MCP)** studies, POCs, and technical articles.

**Author:** Lucas Xavier Ferreira
**Role:** Tech Manager / Software Engineering Coordinator
**Parent Project:** [wine-reviewer](https://github.com/lucasxf/wine-reviewer/tree/develop)
**Focus:** Backend-only (Java 21, Spring Boot 3, Spring AI, MCP)

---

## Purpose

This repository is a **structured learning environment** for mastering **Model Context Protocol (MCP)** through hands-on POCs and technical articles. The project follows production-quality standards from [wine-reviewer](https://github.com/lucasxf/wine-reviewer/tree/develop) while focusing exclusively on backend/MCP development.

### Key Objectives
- Master MCP protocol through progressive POCs
- Integrate Spring AI with MCP clients and servers
- Document learnings for technical articles
- Build reusable MCP patterns for future projects
- Maintain production-quality code standards

---

## Repository Structure

```
/ai
├── README.md                          # This file
├── CLAUDE.md                          # AI assistant context and instructions
├── CODING_STYLE.md                    # Java/Spring Boot conventions (from wine-reviewer)
├── LEARNINGS.md                       # MCP lessons learned (session logs)
├── prompts/
│   └── PACK.md                        # AI prompt patterns (from wine-reviewer)
├── .claude/
│   ├── agents/                        # 6 custom agents
│   │   ├── automation-sentinel.md     # Automation health monitoring
│   │   ├── backend-code-reviewer.md   # Java/Spring Boot code review
│   │   ├── learning-tutor.md          # MCP/Spring AI teaching
│   │   ├── pulse.md                   # Metrics collection
│   │   ├── session-optimizer.md       # Token efficiency
│   │   └── tech-writer.md             # Documentation automation
│   ├── commands/                      # 13 custom slash commands
│   │   ├── start-session.md           # Initialize session with context
│   │   ├── finish-session.md          # Tests + docs + commit
│   │   ├── update-roadmap.md          # Track progress
│   │   ├── directive.md               # Add coding rules
│   │   ├── review-code.md             # Code quality analysis
│   │   ├── quick-test.md              # Test specific class
│   │   ├── test-quick.md              # Run all tests quietly
│   │   ├── test-service.md            # Test service layer
│   │   ├── build-quiet.md             # Clean build
│   │   ├── verify-quiet.md            # Full verification
│   │   └── ...                        # (see commands README)
│   └── metrics/
│       └── usage-stats.toml           # Automation metrics (pulse)
├── mcp/                               # MCP studies POCs
│   ├── README.md                      # POC index with status
│   ├── 01-hello-world/                # POC 1: Protocol basics
│   │   ├── client/                    # Java MCP Client
│   │   ├── server/                    # Java MCP Server
│   │   └── README.md                  # Setup and lessons
│   ├── 02-aws-cost-explorer/          # POC 2: AWS FinOps
│   │   ├── cost-client/               # Spring Boot client
│   │   ├── wine-reviewer-analysis/    # S3 cost analysis
│   │   └── README.md
│   ├── 03-photo-search/               # POC 3: NL photo search
│   │   ├── server/                    # MCP Server (GDrive)
│   │   ├── indexer/                   # Vision API + embeddings
│   │   └── README.md
│   └── mcp-protocol-java/             # Shared MCP library
└── other-ai-studies/                  # Future experiments (ML, LLMs)
```

---

## Tech Stack

### Core Technologies
- **Java 21** - Virtual Threads, Pattern Matching, Records
- **Spring Boot 3.2+** - MCP servers, REST endpoints, configuration
- **Spring AI** - MCP client integration, LLM abstractions
- **Maven** - Dependency management, build automation
- **Python 3.10+** - When needed (e.g., AWS Cost Explorer MCP server)

### Code Quality Tools
- **JUnit 5** - Unit and integration testing (>80% coverage target)
- **Mockito** - Mocking framework
- **AssertJ** - Fluent assertions
- **Spring Boot Test** - Integration test support
- **OpenAPI/Swagger** - API documentation (mandatory)

### Principles & Patterns
- **SOLID Principles** - Clean Architecture
- **Constructor Injection** - No field injection
- **@ConfigurationProperties** - Type-safe configuration
- **Domain-Driven Design** - Clear bounded contexts
- **Test-After-Implementation** - Write code first, tests immediately after

---

## Custom Automation

This project includes **6 custom agents** and **16 custom slash commands** optimized for AI/MCP development workflows.

### 6 Custom Agents

| Agent | Purpose | Model | Primary Use |
|-------|---------|-------|-------------|
| **automation-sentinel** | Automation health, metrics, optimization | Sonnet | Health reports, redundancy detection |
| **backend-code-reviewer** | Java/Spring Boot/MCP code review | Sonnet | Post-implementation review |
| **learning-tutor** | Teaching MCP, Spring AI, patterns | Sonnet | Structured learning, exercises |
| **pulse** | Metrics collection (agent/command usage) | Haiku | Auto-triggered for analytics |
| **session-optimizer** | Token efficiency, session planning | Haiku | Session start, workflow optimization |
| **tech-writer** | Documentation, Javadoc, OpenAPI, ADRs | Sonnet | Documentation automation |

**See:** [.claude/agents-readme.md](.claude/agents-readme.md) for detailed agent documentation

### 16 Custom Slash Commands

**Workflow Commands:**
- `/start-session [context]` - Initialize session with project context
- `/finish-session [commit-context]` - Run tests, update docs, commit
- `/resume-session <feature>` - Resume previous session with context
- `/update-roadmap <what-completed>` - Track progress in ROADMAP.md
- `/create-pr [title]` - Create PR with automation analysis

**Documentation Commands:**
- `/directive <content>` - Add coding rules with deduplication
- `/review-code [path]` - Analyze code quality
- `/save-response [filename]` - Save Claude's response for later

**Testing Commands:**
- `/quick-test <ServiceName>` - Test specific class
- `/test-quick` - Run all tests quietly
- `/test-service` - Test service layer

**Build Commands:**
- `/build-quiet` - Clean build in quiet mode
- `/verify-quiet` - Full verification quietly

**Infrastructure Commands:**
- `/docker-start` - Start Docker services
- `/docker-stop` - Stop Docker services
- `/api-doc` - Open Swagger UI

**See:** [.claude/commands/README.md](.claude/commands/README.md) for detailed command documentation

### Pulse Agent: Metrics-Driven Automation

The **pulse agent** (Haiku, fast/cheap) collects automation usage metrics:
- Agent invocation counts
- Command execution frequency
- Lines of Code (LOC) trends
- Git-tracked checkpoints (`.claude/metrics/usage-stats.toml`)

**Benefits:**
- 75% token reduction in automation workflows (delta mode vs full scan)
- Data-driven optimization recommendations
- Automated redundancy detection
- Usage analytics for continuous improvement

---

## Git Flow Strategy

```
main                     # Stable releases (published articles)
  └─ develop             # Main development branch
      └─ develop-mcp     # Long-lived MCP studies branch
          ├─ feature/poc-01-hello-world
          ├─ feature/poc-02-aws-finops
          └─ feature/poc-03-photo-search
```

**Merge Strategy:**
- Feature branches → `develop-mcp` (via PR)
- `develop-mcp` → `develop` (after POC validation)
- `develop` → `main` (after article publication)

---

## MCP Studies Plan

### Phase 1: Foundation (Weeks 1-2)

**POC 1: Hello World MCP**
- Goal: Understand MCP protocol basics
- Stack: Java client + Java server (stdio transport)
- Tools: Simple calculator (add, multiply, random)
- Deliverable: Working client-server + article

**POC 2: AWS Cost Explorer**
- Goal: Integrate with existing Python MCP server
- Stack: Java client + awslabs/cost-explorer-mcp-server
- Use Case: Analyze wine-reviewer's S3 costs
- Deliverable: Cost analysis dashboard + article

### Phase 2: Integration (Weeks 3-4)

**POC 3: Photo Search (Google Drive)**
- Goal: Natural language photo search
- Stack: Java server + Vision API + Vector DB
- Features: Semantic search, face clustering, auto-tagging
- Deliverable: Working search system + article

### Phase 3: Advanced (Weeks 5-6)

**POC 4: MCP Sampling**
- Goal: Server requests LLM via client (bidirectional)
- Pattern: Intelligent data pipelines
- Deliverable: Working implementation + article

**POC 5: MCP Gateway**
- Goal: Server as client (chain pattern)
- Pattern: Aggregating multiple sources
- Deliverable: Workflow orchestrator + article

---

## Quick Start

### Prerequisites
- Java 21 (OpenJDK or Oracle JDK)
- Maven 3.8+
- Docker (for infrastructure)
- Claude Code CLI (for custom agents/commands)

### Initial Setup

```bash
# Clone repository
git clone <repository-url>
cd ai

# Verify Java and Maven
java -version   # Should show Java 21
mvn -version    # Should show Maven 3.8+

# Start automation (agents available immediately)
# No additional setup needed - agents and commands are already configured

# Optional: Verify agents and commands
ls .claude/agents/     # Should list 6 agents
ls .claude/commands/   # Should list 13 commands
```

### Starting a Development Session

```bash
# Option 1: With session-optimizer agent (recommended)
# In Claude Code CLI:
@ROADMAP.md

Quick session start via session-optimizer.

Goal: [your POC or task]
Scope: [in/out of scope items]

# Option 2: With /start-session command
/start-session working on MCP hello-world POC

# Option 3: Manual context loading
# Read CLAUDE.md, CODING_STYLE.md, ROADMAP.md, and LEARNINGS.md
```

### Running POCs

```bash
# Navigate to POC directory
cd mcp/01-hello-world/

# Follow POC-specific README
cat README.md

# Run POC (example)
cd client && mvn spring-boot:run
```

---

## Documentation

### Project Documentation
- **CLAUDE.md** - AI assistant context (conventions, patterns, workflows)
- **CODING_STYLE.md** - Java/Spring Boot conventions (from wine-reviewer)
- **LEARNINGS.md** - MCP session logs, discoveries, problems solved
- **ROADMAP.md** - Current status, next steps, backlog

### Automation Documentation
- **.claude/agents-readme.md** - Agent overview and usage guide
- **.claude/commands/README.md** - Command reference and workflows

### POC Documentation
- **mcp/README.md** - POC index with status
- **mcp/{poc}/README.md** - Individual POC setup and lessons

---

## Code Quality Standards

This project follows strict quality standards from [wine-reviewer](https://github.com/lucasxf/wine-reviewer/tree/develop):

### Architectural Standards
- **SOLID principles** enforced
- **Constructor injection only** (no `@Autowired` on fields)
- **@ConfigurationProperties** instead of `@Value`
- **OpenAPI/Swagger annotations** mandatory on all REST endpoints
- **Test coverage >80%** for new components
- **Domain exceptions** with proper HTTP status mapping

### Testing Standards
- **Test-after-implementation** workflow (implementation first, tests immediately after)
- **Unit tests** for service layer logic
- **Integration tests** for REST endpoints
- **Mock external dependencies** (no real AWS/GCP calls in tests)

### Documentation Standards
- **README.md** in every POC directory
- **Javadoc** with `@author` and `@date` on public APIs
- **OpenAPI** documentation complete (all status codes documented)
- **LEARNINGS.md** updated after each POC
- **ADRs** (Architecture Decision Records) for major decisions

---

## Reference Project

This project reuses conventions and automation from **wine-reviewer**:

**GitHub:** https://github.com/lucasxf/wine-reviewer/tree/develop

### What's Copied
- `/CODING_STYLE.md` - Java/Spring Boot conventions
- `/prompts/PACK.md` - AI prompt patterns
- `/.claude/agents/` - Custom agent suite (adapted for MCP)
- `/.claude/commands/` - Custom slash commands (adapted for MCP)

### What's Adapted
- **Agents:** Removed frontend/infra agents, focused on backend/MCP
- **Commands:** Adapted for MCP POC workflow
- **Documentation:** Added LEARNINGS.md for session-based discoveries

### Key Files to Study
- [wine-reviewer/CODING_STYLE.md](https://github.com/lucasxf/wine-reviewer/blob/develop/CODING_STYLE.md)
- [wine-reviewer/prompts/PACK.md](https://github.com/lucasxf/wine-reviewer/blob/develop/prompts/PACK.md)
- [wine-reviewer/.claude/commands/](https://github.com/lucasxf/wine-reviewer/tree/develop/.claude/commands)
- [wine-reviewer/services/api/](https://github.com/lucasxf/wine-reviewer/tree/develop/services/api) - Spring Boot architecture reference

---

## Official MCP Resources

### Documentation
- [MCP Specification](https://modelcontextprotocol.io/docs)
- [Spring AI MCP Docs](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [Spring AI MCP Examples](https://github.com/spring-projects-experimental/spring-ai-mcp)
- [AWS Cost Explorer MCP](https://awslabs.github.io/mcp/servers/cost-explorer-mcp-server)
- [Baeldung: Spring AI + MCP](https://www.baeldung.com/spring-ai-model-context-protocol-mcp)

### Tutorials
- [Model Context Protocol Introduction](https://www.anthropic.com/news/model-context-protocol)
- [Building MCP Servers](https://modelcontextprotocol.io/tutorials/building-a-server)
- [MCP Client Integration](https://modelcontextprotocol.io/tutorials/building-a-client)

---

## Work Style

### Philosophy
- **You code, Claude reviews** - You implement, Claude provides feedback
- **Thoroughness over speed** - Production-quality code, not prototypes
- **Learning through doing** - POCs as structured learning exercises
- **Documentation-driven** - Every POC results in a technical article

### Interaction Examples

**Good Interaction:**
```
You: "Review my McpClientService implementation"

Claude (via backend-code-reviewer):
"I notice you're using field injection here:
@Autowired
private McpProtocolHandler handler;

But in wine-reviewer's conventions, you consistently use constructor
injection. Consider:

private final McpProtocolHandler handler;

public McpClientService(McpProtocolHandler handler) {
    this.handler = handler;
}

This follows your established pattern and improves testability."
```

**Bad Interaction:**
```
You: "How should I implement MCP client?"

Claude: "Here's a complete implementation..."
[dumps 500 lines without asking]
```

**Key Principle:** Claude acts as technical reviewer and advisor, not primary coder.

---

## Contributing

This is a **private learning repository**. For questions or collaboration:
- Open an issue for discussion
- Reference wine-reviewer patterns in proposals
- Follow CODING_STYLE.md for consistency

---

## License

Private repository - All rights reserved.

---

## Contact

**Lucas Xavier Ferreira**
Tech Manager / Software Engineering Coordinator
GitHub: [@lucasxf](https://github.com/lucasxf)

---

**Happy learning! Explore MCP, build POCs, and document your journey.**
