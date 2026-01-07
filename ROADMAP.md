# AI/MCP Studies Roadmap

**Last Updated:** 2026-01-07
**Current Branch:** feature/poc-01-hello-world
**Project Status:** MCP POC 1 - Server layer COMPLETE ✅, Client layer next 🚧

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

**Documentation Enhancement (Session: 2026-01-07)**
- ✅ CODING_STYLE.md - Added 5 important design principles and best practices:
  - DRY (Don't Repeat Yourself) with abstraction guidance
  - ETC (Easier to Change) with concrete examples
  - SHY (Shy Code / Law of Demeter) with real-world scenarios
  - Orthogonality with impact analysis examples
  - Rich Domain Models (Tell, Don't Ask) with comprehensive refactoring examples

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
- ✅ Error response models (McpError, McpErrorResponse)
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
- ✅ 33 Java source files (domain + infrastructure codec)
- ✅ 100% CODING_STYLE.md compliance (as of 2025-12-08)
- ⚠️ Tests deferred for exploration phase (0 tests currently)

### ✅ Completed: MCP POC 1 - Infrastructure Layer - JsonRpcCodec (2025-12-08)

**JsonRpcCodec Implementation (infrastructure.codec/)**
- ✅ JsonRpcCodec encode() method implemented (2025-12-03)
- ✅ JsonRpcCodec decode() method implemented (2025-12-08)
- ✅ Response type detection via structural inspection (isError field presence)
- ✅ Error response decoding with nested error object extraction
- ✅ Jackson-based serialization/deserialization
- ✅ @JsonProperty annotations for JSON-RPC field mapping
- ✅ 100% CODING_STYLE.md compliance achieved
- ✅ Jackson @JsonProperty directive added to CODING_STYLE.md

**Architecture Decision:**
- Option B validated: Domain-level validation with jsonRpc field in domain models
- Clean separation: domain models own their JSON-RPC compliance
- Simpler codec: single ObjectMapper calls for encode/decode
- No intermediate envelope classes needed

**What's Done:**
- ✅ Encode McpRequest/McpResponse to JSON-RPC 2.0 format
- ✅ Decode JSON-RPC 2.0 to McpRequest/McpResponse/McpErrorResponse
- ✅ Error handling for malformed JSON (via CodecException)
- ✅ Response type discrimination logic (error vs success)
- ✅ All CODING_STYLE.md violations fixed (method ordering, blank lines, exception standardization)
- ✅ Dead code removed (JsonRpcEnvelope, JsonRpcRequestEnvelope, unused validation)

**Progress:**
- Infrastructure layer codec: 100% COMPLETE ✅
- Infrastructure layer transport: 100% COMPLETE ✅

### ✅ Completed: MCP POC 1 - Infrastructure Layer - ServerStdioTransport (2025-12-16)

**ServerStdioTransport Implementation (server.impl/)** 
- ✅ Server-side stdio transport implemented (reads from stdin, writes to stdout)
- ✅ Newline-delimited JSON message handling
- ✅ Request validation (null checks, JSON-RPC version validation, ID validation)
- ✅ Integration with JsonRpcCodec
- ✅ Error handling for I/O failures (via TransportException)
- ✅ Comprehensive Javadoc documentation

**What's Done:**
- ✅ Read JSON-RPC requests from stdin
- ✅ Write JSON-RPC responses to stdout
- ✅ Request ID validation (String or Number only)
- ✅ JSON-RPC 2.0 version validation
- ✅ Thread-safe BufferedReader/PrintWriter usage

### ✅ Completed: MCP POC 1 - Application Layer - ServerMessageHandler (2025-12-16)

**ServerMessageHandler Implementation (server/)**
- ✅ Handles tools/list requests (returns available tools from ToolRegistry)
- ✅ Handles tools/call requests (invokes tools and returns results)
- ✅ Request validation (null checks, ID validation, method validation)
- ✅ Error handling with proper JSON-RPC error codes
- ✅ Type-safe request handling with pattern matching (instanceof)
- ✅ Comprehensive Javadoc with @param, @return, @throws
- ⚠️ Error codes currently positive (should be negative per JSON-RPC spec - pending fix)

**What's Done:**
- ✅ tools/list request handling
- ✅ tools/call request handling with parameter extraction
- ✅ Request validation (null ID, invalid method, missing parameters)
- ✅ JSON-RPC error response mapping (InvalidToolParametersException → -32602)
- ✅ Pattern matching for type-safe McpRequest casting
- ✅ Comprehensive Javadoc (public methods only, per new directive)

### ✅ Completed: MCP POC 1 - Application Layer - Spring Boot Server Integration (2025-12-20)

**McpServerApplication Implementation (server/)**
- ✅ Spring Boot CommandLineRunner integration
- ✅ Tool registration system with @PostConstruct (ToolRegistryConfig)
- ✅ Automatic tool discovery and registration (AddTool, MultiplyTool, RandomTool)
- ✅ Server main loop with graceful error handling
- ✅ TransportException and CodecException handling
- ✅ @PreDestroy lifecycle hook for resource cleanup
- ✅ Java preview features runtime configuration (--enable-preview in pom.xml)

**What's Done:**
- ✅ Spring Boot application main entry point
- ✅ Dependency injection wiring (ServerStdioTransport, ServerMessageHandler, ToolRegistry)
- ✅ Tool registration via @PostConstruct (CRITICAL FIX - tools were not being registered)
- ✅ Server main loop (read request → handle → write response)
- ✅ Error handling with proper logging (TransportException, CodecException)
- ✅ Graceful shutdown with @PreDestroy
- ✅ Fixed Java preview features for runtime (STR string templates require --enable-preview)
- ✅ Removed @Component from StdioTransport (was causing client-side instantiation errors)

**Manual Testing Infrastructure:**
- ✅ test-requests/ directory created with JSON test files
- ✅ tools-list.json (tools/list request)
- ✅ tools-call-add.json (add tool invocation)
- ✅ tools-call-multiply.json (multiply tool invocation)
- ✅ tools-call-invalid.json (error handling validation)
- ✅ All manual tests PASSED (server fully operational)

**Status:** Server layer 100% COMPLETE ✅ and TESTED ✅

---

## 🚧 In Progress

### Priority 1: MCP POC 1 - Hello World (Current - Week 1)

**Goal:** Understand MCP protocol basics

**Status:** Server layer COMPLETE ✅ and TESTED ✅, Client layer NEXT 🚧

**What's Done:**
- ✅ Project structure created (mcp/01-hello-world/)
- ✅ Domain layer complete (protocol models, tools, abstractions, error models)
- ✅ Calculator tools implemented (add, multiply, random)
- ✅ Clean compilation (33 Java source files)
- ✅ JsonRpcCodec encode() method implemented (2025-12-03)
- ✅ JsonRpcCodec decode() method implemented (2025-12-08)
- ✅ Response type detection with structural inspection
- ✅ Error response decoding (nested error object extraction)
- ✅ CODING_STYLE.md violations fixed (method ordering, blank lines, exception standardization)
- ✅ Jackson @JsonProperty directive added to CODING_STYLE.md
- ✅ Option B architecture validated (domain-level validation with jsonRpc field)
- ✅ Dead code removed (JsonRpcEnvelope, JsonRpcRequestEnvelope, unused validation)
- ✅ 100% CODING_STYLE.md compliance achieved
- ✅ ServerStdioTransport implemented (server-side stdio transport with validation)
- ✅ ServerMessageHandler implemented (tools/list and tools/call request handling)
- ✅ Request validation (null checks, ID validation, JSON-RPC version)
- ✅ JSON-RPC error response mapping with proper error codes
- ✅ Pattern matching for type-safe request handling
- ✅ Javadoc directive added to CODING_STYLE.md (public methods only, not private)
- ✅ McpServerApplication with Spring Boot integration (CommandLineRunner)
- ✅ Tool registration system with @PostConstruct (CRITICAL FIX)
- ✅ Server main loop with error handling and graceful shutdown
- ✅ Java preview features runtime configuration (--enable-preview in pom.xml)
- ✅ Manual testing infrastructure (test-requests/ directory)
- ✅ All manual tests PASSED (tools/list, tools/call, error handling)

**What's Next:**

1. Application layer - McpClientDemo implementation (CURRENT FOCUS)
   - Client-side stdio transport
   - Request sending (tools/list, tools/call)
   - Response parsing and handling
   - End-to-end client-server communication
2. Integration testing (deferred until after client implementation)
3. Documentation (POC README.md + LEARNINGS.md update)

**Current Focus:** Client development - implement MCP client to communicate with the completed server





**Deliverables (Updated):**
- ✅ Domain layer with Java 21 features
- ✅ Infrastructure layer (codec: ✅ COMPLETE | transport: ✅ COMPLETE)
- 🚧 Application layer (Server: ✅ COMPLETE & TESTED | Client: 🚧 IN PROGRESS)
- ⏳ Integration testing (deferred until after client implementation)
- ⏳ POC documentation
- ⏳ Technical article draft

**Note on Testing Strategy:**
- Unit/integration tests deferred for exploration phase
- Focus on understanding MCP protocol through working implementation
- Tests to be added after POC demonstrates end-to-end functionality

---

## 📋 Next Steps (Prioritized)

### Priority 1: MCP POC 1 - Client Implementation (Current)

**Goal:** Implement MCP client to communicate with the completed server

**Status:** Server complete ✅ and tested ✅, Client implementation next 🚧

**Implementation Tasks:**
1. ClientStdioTransport (client.impl/)
   - ⏳ Implement client-side stdio transport
   - ⏳ Spawn MCP server subprocess (ProcessBuilder)
   - ⏳ Write JSON-RPC requests to server's stdin
   - ⏳ Read JSON-RPC responses from server's stdout
   - ⏳ Process lifecycle management (start, stop, cleanup)
   - ⏳ Error handling for I/O and process failures
   - ⏳ Integration with JsonRpcCodec

2. McpClientDemo (client/)
   - ⏳ Send tools/list request and display available tools
   - ⏳ Send tools/call requests (add, multiply, random)
   - ⏳ Parse and display responses
   - ⏳ Handle error responses
   - ⏳ Demonstrate end-to-end client-server communication

**Deliverables:**
- ⏳ Working client-side stdio transport with process management
- ⏳ Working MCP client demo application
- ⏳ End-to-end communication validated (client ↔ server)

**Estimated Duration:** 3-4 hours

---

### Priority 2: MCP POC 1 - Testing & Documentation

**Goal:** Validate POC and document learnings

**Status:** Pending client implementation ⏳

**Tasks:**
1. Integration testing
   - ⏳ Test end-to-end client-server communication
   - ⏳ Test all calculator tools via client (add, multiply, random)
   - ⏳ Test error scenarios (invalid tool, invalid parameters)
   - ⏳ Validate JSON-RPC protocol compliance
   - ⏳ (Unit tests optional for exploration phase)

2. Documentation
   - ⏳ Create mcp/01-hello-world/README.md (architecture, setup, lessons learned)
   - ⏳ Update LEARNINGS.md with architectural decisions and key insights
   - ⏳ Draft technical article (MCP protocol basics, Java implementation)

**Deliverables:**
- ⏳ POC README with architecture diagram and setup instructions
- ⏳ Updated LEARNINGS.md with session summary
- ⏳ Technical article draft (1st version)

**Estimated Duration:** 2-3 hours (after client completion)

---

### Priority 3: MCP POC 2 - AWS Cost Explorer (Week 2)

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

### JsonRpcCodec Architecture Decision (2025-12-03, completed 2025-12-08)

**Decision:** Option B - Domain-level validation with jsonRpc field in domain models

**Implementation Details:**
- ✅ `McpRequest` and `McpResponse` include `jsonRpc` field ("2.0")
- ✅ Compact constructors validate jsonRpc value at domain level
- ✅ JsonRpcCodec encode() delegates to ObjectMapper with @JsonProperty annotations
- ✅ JsonRpcCodec decode() implemented with response type detection
- ✅ Response type discrimination via structural inspection (isError field presence)
- ✅ Error response decoding with nested error object extraction
- ✅ Dead code removed (JsonRpcEnvelope, JsonRpcRequestEnvelope, unused validation)
- ✅ Jackson @JsonProperty directive added to CODING_STYLE.md

**Rationale:**
- Cleaner separation: domain models own their JSON-RPC compliance
- Better encapsulation: validation happens at construction time
- Simpler codec: single ObjectMapper calls for encode/decode
- More maintainable: no intermediate envelope classes

**Code Quality Achievements:**
- 100% CODING_STYLE.md compliance (method ordering, blank lines, exceptions)
- Clean compilation with mvn clean compile
- All architecture decisions documented
- ✅ JsonRpcCodec COMPLETE (encode + decode)

**Status:** COMPLETE ✅ (2025-12-08)
**Next Step:** StdioTransport implementation

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
**Last Review:** 2026-01-07
