# AI/MCP Studies Roadmap

**Last Updated:** 2025-12-03
**Current Branch:** feature/poc-01-hello-world
**Project Status:** MCP POC 1 - Domain layer complete, JsonRpcCodec encode() complete, decode() next

---

## 🎯 Vision

Master Model Context Protocol (MCP) through structured POCs while maintaining production-quality code standards and comprehensive documentation.

---

## 📊 Current Implementation Status

### ✅ Completed: Repository Foundation

**Infrastructure & Automation (2025-11-25)**
- ✅ Repository structure established
- ✅ Documentation framework complete (CLAUDE.md, CODING_STYLE.md, README.md)
- ✅ 6 custom agents configured (automation-sentinel, backend-code-reviewer, learning-tutor, pulse, session-optimizer, tech-writer)
- ✅ 16 custom slash commands implemented
- ✅ Metrics collection system (pulse agent + usage-stats.toml)
- ✅ Git flow strategy defined (main → develop → develop-mcp → features)
- ✅ Reference patterns from wine-reviewer adapted

**Documentation (2025-11-25)**
- ✅ CLAUDE.md - AI assistant context and conventions (comprehensive review completed)
- ✅ CODING_STYLE.md - Java/Spring Boot standards (4 parts: General/Backend/Frontend/Infrastructure)
- ✅ README.md - Repository overview with automation guide (expanded from 1 line to 437 lines)
- ✅ .claude/agents-readme.md - Agent suite documentation (created from scratch)
- ✅ .claude/commands/README.md - Command reference (fixed count: 13→16 commands)
- ✅ LEARNINGS.md - Session logs (structure defined)
- ✅ ROADMAP.md - Project tracking (created from scratch)
- ✅ All cross-references validated for consistency
- ✅ Missing commands documented (/resume-session, /create-pr, /save-response)

**Completed Initial Setup (Session: 2025-11-25)**
- ✅ Comprehensive documentation review and validation
- ✅ Fixed command count discrepancy (13 → 16 commands)
- ✅ Created missing ROADMAP.md (heavily referenced but didn't exist)
- ✅ Expanded README.md from placeholder to full documentation (1 → 437 lines)
- ✅ Added missing commands to command reference documentation
- ✅ Validated all cross-references between documentation files
- ✅ Repository ready for MCP POC implementation

### ✅ Completed: MCP POC 1 - Domain Layer (2025-11-30)

**Protocol Models (domain.protocol/)**
- ✅ MCP message hierarchy with Java 21 sealed interfaces (McpMessage, McpRequest, McpResponse)
- ✅ Concrete request/response records (ToolListRequest, ToolInvocationRequest, ToolListResponse, ToolInvocationResponse)
- ✅ Compact constructors with validation
- ✅ Factory methods for convenience
- ✅ All in same package (sealed class compliance)

**Tool Abstraction (domain.tool/)**
- ✅ Tool interface with execute() contract
- ✅ AbstractTool helper class (parameter extraction, Number flexibility)
- ✅ ToolDefinition record (name, description, schema)
- ✅ ToolRegistry with domain exceptions
- ✅ ContentBlock sealed interface (TextContent, ImageContent, ResourceContent)

**Calculator Tools (application.tools/)**
- ✅ AddTool - Integer addition
- ✅ MultiplyTool - Long multiplication (overflow prevention)
- ✅ RandomTool - Random number generation

**Exception Hierarchy (exception/)**
- ✅ McpException base class
- ✅ ToolNotFoundException
- ✅ InvalidToolParametersException
- ✅ TransportException (defined, not yet used)
- ✅ CodecException (defined, not yet used)

**Build Status:**
- ✅ Clean compilation (mvn clean compile)
- ✅ 31 Java source files
- ✅ 100% CODING_STYLE.md compliance (as of 2025-12-03)
- ⚠️ Tests deferred for exploration phase (0 tests currently)

---

## 🚧 In Progress

### Priority 1: MCP POC 1 - Hello World (Current - Week 1)

**Goal:** Understand MCP protocol basics

**Status:** Domain layer complete, JsonRpcCodec encode() complete, decode() next

**What's Done:**
- ✅ Project structure created (mcp/01-hello-world/)
- ✅ Domain layer complete (protocol models, tools, abstractions)
- ✅ Calculator tools implemented (add, multiply, random)
- ✅ Clean compilation (31 Java source files)
- ✅ JsonRpcCodec encode() method implemented (2025-12-03)
- ✅ CODING_STYLE.md violations fixed (method ordering, blank lines, exception standardization)
- ✅ Jackson @JsonProperty directive added to CODING_STYLE.md
- ✅ Option B architecture validated (domain-level validation with jsonRpc field)
- ✅ Dead code removed (JsonRpcEnvelope, JsonRpcRequestEnvelope, unused validation)
- ✅ 100% CODING_STYLE.md compliance achieved

**What's Next:**
1. Infrastructure layer - Complete JsonRpcCodec decode() method
2. Infrastructure layer - StdioTransport implementation
3. Application layer (McpClient, McpServer)
4. Integration testing (deferred until after exploration)
5. Documentation (POC README.md + LEARNINGS.md update)

**Current Focus:** JsonRpcCodec decode() method implementation

**Deliverables (Updated):**
- ✅ Domain layer with Java 21 features
- 🚧 Infrastructure layer (codec: encode ✅, decode 🚧 | transport: ⏳)
- 🚧 Application layer (client + server)
- ⏳ Integration testing (deferred for exploration)
- ⏳ POC documentation
- ⏳ Technical article draft

**Note on Testing Strategy:**
- Unit/integration tests deferred for exploration phase
- Focus on understanding MCP protocol through working implementation
- Tests to be added after POC demonstrates end-to-end functionality

---

## 📋 Next Steps (Prioritized)

### Priority 1: MCP POC 1 - Infrastructure Layer (Current)

**Goal:** Implement codec and transport for MCP communication

**Status:** JsonRpcCodec encode() complete, decode() next

**Implementation Tasks:**
1. JsonRpcCodec (infrastructure/codec/) - 50% complete
   - ✅ Encode McpRequest/McpResponse to JSON-RPC 2.0
   - 🚧 Decode JSON-RPC to McpRequest/McpResponse (NEXT)
   - ✅ Jackson-based serialization configured
   - ⏳ Error handling for malformed JSON

2. StdioTransport (infrastructure/transport/) - Not started
   - Read JSON-RPC messages from stdin
   - Write JSON-RPC messages to stdout
   - Line-based protocol (newline-delimited JSON)
   - Process management (spawn server process)

**Deliverables:**
- 🚧 Working JSON codec (encode ✅ | decode 🚧)
- ⏳ Working stdio transport (read/write)
- ⏳ Unit tests for codec (if time permits)

**Estimated Duration:** 1-2 hours remaining (decode + transport)

---

### Priority 2: MCP POC 1 - Application Layer

**Goal:** Implement MCP client and server

**Implementation Tasks:**
1. McpServer (application/server/)
   - Handle tools/list requests
   - Handle tools/call requests
   - Invoke tools via ToolRegistry
   - Return ContentBlock results
   - Error handling (tool not found, invalid params)

2. McpClient (application/client/)
   - Send tools/list request
   - Send tools/call request
   - Parse responses
   - Handle errors

**Deliverables:**
- Working MCP server
- Working MCP client
- End-to-end communication (client ↔ server)

**Estimated Duration:** 2-3 hours

---

### Priority 3: MCP POC 1 - Testing & Documentation

**Goal:** Validate POC and document learnings

**Tasks:**
1. Integration testing
   - Test client-server communication
   - Test all calculator tools
   - Test error scenarios
   - (Unit tests optional for exploration phase)

2. Documentation
   - Create mcp/01-hello-world/README.md
   - Update LEARNINGS.md with architectural decisions
   - Draft technical article

**Deliverables:**
- POC README with setup instructions
- Updated LEARNINGS.md
- Technical article draft (1st version)

**Estimated Duration:** 1-2 hours

---

### Priority 4: MCP POC 2 - AWS Cost Explorer (Week 2)

**Goal:** Integrate with existing Python MCP server

**Implementation Plan:**
1. Create mcp/02-aws-cost-explorer/ structure
2. Setup AWS credentials and permissions
3. Integrate awslabs/cost-explorer-mcp-server
4. Implement Java client with Spring Boot
5. Build wine-reviewer S3 cost analysis dashboard
6. Testing and validation
7. Documentation

**Deliverables:**
- Cost analysis tool operational
- wine-reviewer S3 costs analyzed
- Dashboard with visualizations
- Technical article draft (2nd version)

**Estimated Duration:** 4-6 hours implementation + 2-3 hours documentation

---

### Priority 5: Documentation Review Cycle (After Each POC)

**Continuous Tasks:**
- Update LEARNINGS.md with POC insights
- Refine technical article drafts
- Update ROADMAP.md progress
- Review and improve automation based on metrics

---

## 📅 Future POCs (Phase 2 & 3)

### Phase 2: Integration (Weeks 3-4)

**POC 3: Photo Search (Google Drive)**
- Goal: Natural language photo search
- Stack: Java server + Vision API + Vector DB
- Features: Semantic search, face clustering, auto-tagging
- Status: 📋 Planned

### Phase 3: Advanced (Weeks 5-6)

**POC 4: MCP Sampling**
- Goal: Server requests LLM via client (bidirectional)
- Pattern: Intelligent data pipelines
- Status: 📋 Planned

**POC 5: MCP Gateway**
- Goal: Server as client (chain pattern)
- Pattern: Aggregating multiple sources
- Status: 📋 Planned

---

## 🔧 Automation Improvements Backlog

### Metrics & Analytics
- ⏳ Integrate pulse metrics with automation-sentinel reports
- ⏳ Create dashboard for automation usage trends
- ⏳ Implement automated redundancy detection alerts

### Documentation
- ⏳ Create ADR template for MCP architectural decisions
- ⏳ Automate LEARNINGS.md structure enforcement
- ⏳ Generate POC documentation templates

### Code Quality
- ⏳ Integrate SonarQube for static analysis
- ⏳ Setup automated test coverage reports
- ⏳ Implement pre-commit hooks for code quality checks

---

## 📈 Success Metrics

### Code Quality Targets
- Test coverage >80% for all POCs
- Zero critical SonarQube issues
- All REST endpoints documented with OpenAPI

### Documentation Targets
- README.md in every POC directory
- LEARNINGS.md updated after each session
- Technical article draft for each POC
- ADRs for major architectural decisions

### Learning Targets
- 5 POCs completed with working code
- 5 technical articles published
- MCP protocol mastery demonstrated
- Reusable patterns documented

---

## 🗓️ Timeline Overview

**November 2025**
- Week 4: Repository setup & automation infrastructure ✅

**December 2025**
- Week 1: POC 1 - Hello World
- Week 2: POC 2 - AWS Cost Explorer
- Week 3: POC 3 - Photo Search (start)
- Week 4: POC 3 - Photo Search (complete)

**January 2026**
- Week 1: POC 4 - MCP Sampling
- Week 2: POC 5 - MCP Gateway
- Week 3-4: Documentation refinement, article publishing

---

## 📝 Notes

### JsonRpcCodec Architecture Decision (2025-12-03)

**Decision:** Option B - Domain-level validation with jsonRpc field in domain models

**Implementation Details:**
- ✅ `McpRequest` and `McpResponse` include `jsonRpc` field ("2.0")
- ✅ Compact constructors validate jsonRpc value at domain level
- ✅ JsonRpcCodec encode() delegates to ObjectMapper with @JsonProperty annotations
- ✅ Dead code removed (JsonRpcEnvelope, JsonRpcRequestEnvelope, unused validation)
- ✅ Jackson @JsonProperty directive added to CODING_STYLE.md

**Rationale:**
- Cleaner separation: domain models own their JSON-RPC compliance
- Better encapsulation: validation happens at construction time
- Simpler codec: single ObjectMapper.writeValueAsString() call
- More maintainable: no intermediate envelope classes

**Code Quality Achievements:**
- 100% CODING_STYLE.md compliance (method ordering, blank lines, exceptions)
- Clean compilation with mvn clean compile
- All architecture decisions documented

**Next Step:** Implement decode() method with JSON-RPC parsing and domain model reconstruction

---

### Testing Strategy Decision (2025-11-30)

**Decision:** Defer unit/integration tests for POC 1 exploration phase

**Rationale:**
- Primary goal: Understand MCP protocol through hands-on implementation
- POC nature: Exploratory learning, not production system
- Tests planned after end-to-end functionality demonstrated

**Action Items (Future):**
- Add tests after POC 1 demonstrates working client-server communication
- Target >80% coverage for domain layer (Tool, ToolRegistry, AbstractTool)
- Integration tests for JsonRpcCodec and StdioTransport
- End-to-end tests for complete client-server scenarios

**Documentation:**
- Test deferral documented in ROADMAP.md
- Will revisit testing strategy after Phase 1 POCs complete

---

### Architecture Decisions Pending
- Vector database selection for POC 3 (Pinecone vs Weaviate vs pgvector)
- MCP transport layer for production use (stdio vs SSE)

### Dependencies
- Java 21 ✅
- Maven 3.8+ ✅
- Spring Boot 3.2+ ✅
- Spring AI (MCP support) ✅
- Docker ✅
- Claude Code CLI ✅

### References
- [MCP Specification](https://modelcontextprotocol.io/docs)
- [Spring AI MCP Docs](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [wine-reviewer](https://github.com/lucasxf/wine-reviewer/tree/develop) - Reference project

---

## 🔄 Maintenance

**Update Frequency:**
- After completing each POC
- After significant automation changes
- Weekly progress review (recommended)

**Owned By:** Lucas Xavier Ferreira
**Last Review:** 2025-12-03
