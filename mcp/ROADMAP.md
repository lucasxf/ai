# MCP Studies Roadmap

> Project tracking for Model Context Protocol (MCP) experiments and implementation.
> Organized in 3 parts: GENERAL (cross-stack), BACKEND (Java/Spring Boot), FRONTEND (if applicable).

**Last updated:** 2025-11-04

---

## 🌍 PART 1: GENERAL (Cross-stack)

### Project Overview

This repository contains hands-on studies of the Model Context Protocol (MCP), starting with proof-of-concept implementations in Java/Spring Boot. The goal is to understand MCP's architecture, build production-quality implementations, and document learnings through technical articles.

**MCP Protocol:** A standardized protocol for LLM-to-tool communication enabling:
- Client-Server architecture (stdio, HTTP transports)
- Tool definitions and invocations
- Sampling (server requests LLM via client)
- Multi-server orchestration

### Repository Structure

```
/mcp
├── pom.xml                           # Parent POM (dependency management)
├── ROADMAP.md                        # This file - project tracking
├── 01-hello-world/                   # POC 1: Protocol basics
│   ├── pom.xml                       # Child module
│   ├── src/main/java/                # Spring Boot application
│   └── README.md                     # POC-specific documentation
├── 02-aws-cost-explorer/             # POC 2: AWS FinOps (planned)
├── 03-photo-search/                  # POC 3: NL photo search (planned)
└── mcp-protocol-java/                # Shared MCP library (planned)
```

### Git Flow Strategy

```
main                     # Stable releases
  └─ develop             # Main development
      └─ develop-mcp     # Long-lived MCP branch
          ├─ feature/poc-01-hello-world  ← Current branch
          ├─ feature/poc-02-aws-finops
          └─ feature/poc-03-photo-search
```

**Merge Strategy:**
- Feature → `develop-mcp` (via PR after POC completion)
- `develop-mcp` → `develop` (after POC validation + article draft)
- `develop` → `main` (after article publication)

### Current Phase

**Phase 1: Foundation (Weeks 1-2)**
- POC 1: Hello World MCP (In Progress)
- POC 2: AWS Cost Explorer (Planned)

---

## ☕ PART 2: BACKEND (Java/Spring Boot)

### Current Implementation Status

#### ✅ Completed

**Project Setup (2025-11-04)**
- Parent POM structure at `/mcp/pom.xml` with dependency management
- Child module `01-hello-world` with Spring Boot boilerplate
- Maven multi-module build configured (`mvn clean install` works)
- Main application class: `McpHelloWorldApplication.java`
- Configuration: `application.yml` (Spring Boot defaults)

**Technology Stack**
- Java 21 (LTS)
- Spring Boot 3.2+
- Maven multi-module project
- Spring AI (planned for MCP client integration)

#### 🚧 In Progress

**POC 1: Hello World MCP**
- Goal: Understand MCP protocol basics (client-server communication)
- Status: Boilerplate created, architectural decisions needed
- Deliverable: Working Java client + server + README + learnings

### Next Steps (Architectural Decisions)

Priority-ordered tasks for POC 1 completion:

**Priority 1: Define Package Structure for MCP Components** (Next)
- Decision needed: How to organize MCP protocol layers?
- Options:
  1. Monolithic: `/mcp/protocol`, `/mcp/client`, `/mcp/server` (simple, single POC)
  2. Layered: `/domain/protocol`, `/application/client`, `/infrastructure/transport` (DDD-style)
  3. Modular: Separate Maven modules for `mcp-protocol`, `mcp-client`, `mcp-server` (reusable)
- **Recommendation:** Start with monolithic (option 1), refactor to modular later if needed
- **Action:** Create package structure under `com.lucasxf.ai.mcp.helloworld`

**Priority 2: Implement MCP Protocol Models (Request/Response Records)**
- Task: Define MCP message types as Java records
- Components:
  - `McpRequest` - Base request record (sealed interface)
  - `ToolInvocationRequest` - Client requests tool execution
  - `ToolListRequest` - Client asks for available tools
  - `McpResponse` - Base response record (sealed interface)
  - `ToolListResponse` - Server returns tool definitions
  - `ToolInvocationResponse` - Server returns tool execution result
- Reference: [MCP Specification - Protocol Messages](https://modelcontextprotocol.io/docs)
- Conventions: Use Java 21 records, sealed interfaces, pattern matching

**Priority 3: Create Stdio Transport Layer**
- Task: Implement stdio-based MCP transport
- Components:
  - `StdioTransport` - Read/write JSON messages via stdin/stdout
  - `JsonRpcCodec` - Serialize/deserialize MCP messages
  - `TransportException` - Custom exception for transport errors
- Rationale: Stdio is simplest transport for learning protocol fundamentals
- Reference: [Spring AI MCP - Stdio Transport](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)

**Priority 4: Implement Basic Calculator Tools**
- Task: Create simple tools to demonstrate MCP tool invocation
- Tools:
  - `add(a, b)` - Returns sum of two numbers
  - `multiply(a, b)` - Returns product of two numbers
  - `random(min, max)` - Returns random integer in range
- Tool metadata: Name, description, JSON schema for parameters
- Registration: Tool registry pattern with `ToolDefinition` records

**Priority 5: Add Integration Tests with Testcontainers**
- Task: End-to-end tests for MCP client-server communication
- Approach:
  - Use ProcessBuilder to spawn server as separate process
  - Client communicates via stdio
  - Verify tool list retrieval
  - Verify tool invocation (calculator operations)
  - Verify error handling (invalid parameters, unknown tools)
- Coverage target: >80% for MCP protocol implementation

**Priority 6: Document MCP Protocol Learnings in LEARNINGS.md**
- Task: Create `/ai/LEARNINGS.md` with POC 1 insights
- Structure: Session-based entries (newest first)
- Content:
  - Challenges encountered (JSON-RPC over stdio, process management)
  - Protocol design insights (why tools?, why stdio first?)
  - Trade-offs (stdio vs HTTP transport)
  - Gotchas and best practices
  - Link to technical article draft

### Technical Debt

**None yet** - Clean slate, will track as implementation progresses.

### Upcoming POCs (Planned)

**POC 2: AWS Cost Explorer MCP**
- Goal: Integrate with existing Python MCP server (awslabs/cost-explorer-mcp-server)
- Stack: Java client + Python server (stdio transport)
- Use Case: Analyze wine-reviewer's S3 costs via natural language queries
- Deliverable: Cost analysis dashboard + article

**POC 3: Photo Search (Google Drive)**
- Goal: Natural language photo search with semantic indexing
- Stack: Java server + Vision API + Vector DB (Pinecone/Qdrant)
- Features: Semantic search, face clustering, auto-tagging
- Deliverable: Working search system + article

**POC 4: MCP Sampling**
- Goal: Server requests LLM via client (bidirectional communication)
- Pattern: Sampling API (server asks for text generation)
- Deliverable: Intelligent data pipeline + article

**POC 5: MCP Gateway**
- Goal: Server acts as client (chain pattern)
- Pattern: Aggregating multiple MCP servers
- Deliverable: Workflow orchestrator + article

### Metrics

| Metric                    | Target | Current | Status |
| ------------------------- | ------ | ------- | ------ |
| POCs Completed            | 5      | 0       | 🔴     |
| Articles Published        | 5      | 0       | 🔴     |
| Test Coverage (Backend)   | >80%   | N/A     | ⚪     |
| Integration Tests         | >20    | 0       | 🔴     |
| Development Sessions      | N/A    | 1       | ⚪     |
| GitHub Stars (target)     | 50     | N/A     | ⚪     |

**Legend:** 🟢 On track | 🟡 At risk | 🔴 Behind | ⚪ Not started

---

## 📱 PART 3: FRONTEND (Not Applicable)

This is a backend-focused project (MCP protocol implementation in Java). Frontend section not applicable.

If future POCs require UI (e.g., cost dashboard, photo search interface), this section will be populated.

---

## 📚 References

### Official Documentation
- [MCP Specification](https://modelcontextprotocol.io/docs)
- [Spring AI MCP Docs](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [Spring AI MCP Examples](https://github.com/spring-projects-experimental/spring-ai-mcp)
- [AWS Cost Explorer MCP](https://awslabs.github.io/mcp/servers/cost-explorer-mcp-server)
- [Baeldung: Spring AI + MCP](https://www.baeldung.com/spring-ai-model-context-protocol-mcp)

### Related Projects
- [wine-reviewer](https://github.com/lucasxf/wine-reviewer/tree/develop) - Reference for Java/Spring Boot patterns
- [estudos](https://github.com/lucasxf/estudos/tree/develop) - Advanced architecture (DDD, CQRS, Event Sourcing)

---

## 🎯 Success Criteria

This project will be considered successful when:

1. **Technical Understanding** ✅ Deep understanding of MCP protocol internals
2. **Production Quality** ✅ All POCs follow CODING_STYLE.md conventions (SOLID, Clean Architecture)
3. **Test Coverage** ✅ >80% test coverage for protocol implementation
4. **Documentation** ✅ Each POC has comprehensive README + lessons learned
5. **Knowledge Sharing** ✅ 5 technical articles published (one per POC)
6. **Reusability** ✅ MCP Java library extracted as standalone module

---

## 🔄 Update History

- **2025-11-04:** Initial ROADMAP.md created. POC 1 (Hello World) in progress, boilerplate complete, architectural decisions next.
