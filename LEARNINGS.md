# MCP Studies - Learnings & Session Notes

> Technical discoveries, architectural decisions, and lessons learned during MCP protocol implementation.
> Organized chronologically (newest first) to track evolution of understanding.

**Author:** Lucas Xavier Ferreira
**Project:** Model Context Protocol (MCP) hands-on studies
**Goal:** Master MCP internals through production-quality POCs and publish technical articles

---

## 📋 Table of Contents

- [Session Notes](#session-notes)
  - [2025-11-04: Initial Setup & Architectural Foundations](#2025-11-04-initial-setup--architectural-foundations)
- [Key Learnings by Topic](#key-learnings-by-topic)

---

## Session Notes

### 2025-11-04: Initial Setup & Architectural Foundations

**POC:** 01 - Hello World MCP
**Duration:** ~2 hours
**Branch:** `feature/poc-01-hello-world`
**Status:** In Progress - Project structure created, protocol research completed

---

#### ☕ PART 1: BACKEND (Java/Spring Boot)

##### What Was Accomplished

**1. Project Structure Setup**
- Created Maven multi-module project structure:
  - Parent POM: `ai.mcp.helloworld` (dependency management, Java 21)
  - Child modules: `mcp-client`, `mcp-server`, `mcp-shared`
- Established 28 skeleton Java files following DDD-style layered architecture
- Package structure: `ai.mcp.helloworld.{domain|application|infrastructure|config|exception}`

**2. Protocol Research**
- Deep dive into JSON-RPC 2.0 specification
- Analyzed MCP protocol methods and message formats
- Studied content block patterns for rich tool responses

**3. Architectural Decisions**
Three critical decisions that will shape the entire MCP implementation:

---

##### Key Architectural Decisions

**Decision 1: Package Naming Convention**

**Context:** Choosing between `ai.mcp.helloworld` (Option A) vs `com.lucasxf.ai.mcp.helloworld` (Option B)

**Decision:** Use `ai.mcp.helloworld` (Option A)

**Rationale:**
1. **Attribution vs Organization:** Package names serve organizational purposes, not attribution
   - GitHub already provides authorship metadata (commits, contributor graphs)
   - Code comments and Javadoc `@author` tags capture individual contributions
   - "Watermarking" code with personal names in packages is an anti-pattern

2. **Domain Ownership:**
   - Reverse domain convention (`com.lucasxf`) implies ownership of `lucasxf.com`
   - Without owning the domain, using it violates the intent of the convention
   - Creates confusion about package origin and official status

3. **Future Publishing:**
   - If publishing to Maven Central, use `io.github.lucasxf.mcp` (GitHub-based namespace)
   - For internal/learning projects, descriptive names (`ai.mcp`) are cleaner

**Example:**
```java
// ✅ Clean, descriptive
package ai.mcp.helloworld.domain.protocol;

// ❌ Unnecessary personal branding
package com.lucasxf.ai.mcp.helloworld.domain.protocol;
```

**Learning:** Personal branding in packages is acceptable **only if**:
- You own the corresponding domain (`com.yourcompany`)
- Publishing to public repositories (use `io.github.username`)
- Otherwise, prefer descriptive names that communicate purpose

**References:**
- [Oracle Java Package Naming](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html)
- [GitHub Packages Naming Convention](https://docs.github.com/en/packages/learn-github-packages/introduction-to-github-packages)

---

**Decision 2: Integration Test Naming Pattern**

**Context:** Choosing between `*IT.java` (Maven convention) vs `*IntegrationTest.java` (descriptive)

**Decision:** Use `*IT.java` suffix

**Rationale:**
1. **Maven Plugin Recognition:**
   - **Surefire Plugin:** Runs `*Test.java` during `mvn test` (unit tests)
   - **Failsafe Plugin:** Runs `*IT.java` during `mvn verify` (integration tests)
   - Convention enables automatic test separation without complex configuration

2. **Fast Feedback Loop:**
   - Unit tests fail fast (seconds) → immediate developer feedback
   - Integration tests run later (minutes) → after unit tests pass
   - Prevents slow integration tests from blocking quick iterations

3. **Consistency:**
   - Already used in `wine-reviewer` project
   - Industry standard for Maven-based projects
   - IDE support (IntelliJ, Eclipse) recognizes pattern

**Example Test Structure:**
```
src/test/java/
├── ai/mcp/helloworld/
│   ├── domain/protocol/
│   │   └── McpRequestTest.java              # Unit test (Surefire)
│   ├── application/client/
│   │   ├── McpClientTest.java               # Unit test (mocked)
│   │   └── McpClientIT.java                 # Integration test (Failsafe, real I/O)
│   └── infrastructure/transport/
│       ├── StdioTransportTest.java          # Unit test
│       └── StdioTransportIT.java            # Integration test (process spawn)
```

**Maven Execution:**
```bash
mvn test        # Fast: Runs only *Test.java (unit tests)
mvn verify      # Complete: Runs *Test.java + *IT.java (unit + integration)
mvn test -Dit.test=McpClientIT  # Run specific integration test
```

**Learning:** `*IT.java` is not just a convention—it's a mechanism for **test lifecycle management**. Proper naming enables:
- CI/CD optimization (parallel unit tests, sequential integration tests)
- Developer workflow efficiency (quick TDD cycles)
- Clear test categorization (unit vs integration vs e2e)

**References:**
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/)
- wine-reviewer/CODING_STYLE.md (test conventions)

---

**Decision 3: DDD-Style Layered Architecture**

**Context:** Structuring packages for MCP protocol implementation (flat vs layered)

**Decision:** Use domain/application/infrastructure layers (DDD-inspired)

**Rationale:**
1. **Separation of Concerns:**
   - **Domain Layer:** Pure business logic, protocol models (no I/O, no frameworks)
   - **Application Layer:** Use cases, orchestration (client/server implementations)
   - **Infrastructure Layer:** Technical concerns (transport, serialization, I/O)
   - **Config Layer:** Spring Boot configuration (properties, beans)
   - **Exception Layer:** Domain exception hierarchy

2. **Scalability:**
   - MCP protocol has multiple concerns: models, transport, codec, tools, client, server
   - Layered structure prevents "big ball of mud" as complexity grows
   - Clear boundaries enable future extraction of `mcp-protocol-java` as standalone library

3. **Testability:**
   - Domain layer: Pure unit tests (no mocks, no Spring context)
   - Application layer: Business logic tests (mocked infrastructure)
   - Infrastructure layer: Integration tests (real I/O, process spawning)

**Package Structure:**
```
ai.mcp.helloworld/
├── domain/
│   ├── protocol/
│   │   ├── McpMessage.java              # Sealed interface (all message types)
│   │   ├── McpRequest.java              # Record (immutable request)
│   │   ├── McpResponse.java             # Record (immutable response)
│   │   ├── McpError.java                # Record (JSON-RPC error)
│   │   └── ContentBlock.java            # Sealed interface (text/image/resource)
│   └── tool/
│       ├── Tool.java                    # Interface (execute method)
│       ├── ToolRegistry.java            # Domain service (tool lookup)
│       └── ToolMetadata.java            # Record (tool definition)
│
├── application/
│   ├── client/
│   │   ├── McpClient.java               # Interface (send/receive)
│   │   └── McpClientImpl.java           # Implementation (orchestrates transport + codec)
│   ├── server/
│   │   ├── McpServer.java               # Interface (start/stop)
│   │   └── McpServerImpl.java           # Implementation (handles requests)
│   └── tools/
│       ├── AddTool.java                 # Concrete tool (a + b)
│       ├── MultiplyTool.java            # Concrete tool (a * b)
│       └── RandomTool.java              # Concrete tool (random number)
│
├── infrastructure/
│   ├── transport/
│   │   ├── Transport.java               # Interface (send/receive bytes)
│   │   └── StdioTransport.java          # Implementation (stdin/stdout)
│   └── codec/
│       ├── JsonRpcCodec.java            # Interface (encode/decode)
│       └── JacksonJsonRpcCodec.java     # Implementation (Jackson-based)
│
├── config/
│   ├── McpClientProperties.java         # @ConfigurationProperties
│   └── McpServerProperties.java         # @ConfigurationProperties
│
└── exception/
    ├── McpException.java                # Base exception (extends RuntimeException)
    ├── ProtocolException.java           # JSON-RPC protocol errors
    ├── TransportException.java          # I/O errors
    └── ToolExecutionException.java      # Tool-specific errors
```

**Layer Responsibilities:**

| Layer          | Concerns                                    | Dependencies              | Testing Strategy       |
| -------------- | ------------------------------------------- | ------------------------- | ---------------------- |
| Domain         | Protocol models, tool abstraction           | None (pure Java)          | Pure unit tests        |
| Application    | Client/server logic, tool implementations   | Domain only               | Mocked infrastructure  |
| Infrastructure | I/O, serialization, process management      | Domain + external libs    | Integration tests      |
| Config         | Spring Boot configuration                   | All layers                | Spring context tests   |
| Exception      | Domain exception hierarchy                  | None                      | Exception mapping tests|

**Learning:** Layered architecture is not just for "big" systems—it's essential for **protocol implementations** where:
- Protocol models must be framework-agnostic (future-proof)
- Transport mechanisms may change (stdio → WebSocket → HTTP)
- Business logic (tools) must be testable without I/O

**References:**
- [Domain-Driven Design by Eric Evans](https://www.domainlanguage.com/ddd/)
- wine-reviewer architecture (`/services/api/src/main/java/com/winereview/`)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

##### JSON-RPC 2.0 Protocol Insights

**What is JSON-RPC 2.0?**
A stateless, lightweight remote procedure call (RPC) protocol using JSON for encoding.

**Message Format:**
```json
{
  "jsonrpc": "2.0",           // Protocol version (required)
  "id": 123,                  // Request-response correlation (required for requests)
  "method": "tools/call",     // RPC method name (required for requests)
  "params": {                 // Method parameters (optional)
    "name": "add",
    "arguments": { "a": 5, "b": 3 }
  }
}
```

**Response Format (Success):**
```json
{
  "jsonrpc": "2.0",
  "id": 123,                  // Same ID as request
  "result": {                 // Result object (success)
    "content": [
      { "type": "text", "text": "8" }
    ]
  }
}
```

**Response Format (Error):**
```json
{
  "jsonrpc": "2.0",
  "id": 123,
  "error": {
    "code": -32602,           // Standard error code
    "message": "Invalid params",
    "data": { "detail": "Parameter 'a' is required" }
  }
}
```

**Standard Error Codes:**
| Code   | Message              | Meaning                          |
|--------|----------------------|----------------------------------|
| -32700 | Parse error          | Invalid JSON                     |
| -32600 | Invalid Request      | Missing required fields          |
| -32601 | Method not found     | Unknown method name              |
| -32602 | Invalid params       | Wrong parameter types/values     |
| -32603 | Internal error       | Server-side exception            |

**MCP-Specific Methods:**
1. **`tools/list`** - Client requests available tools from server
   ```json
   {
     "jsonrpc": "2.0",
     "id": 1,
     "method": "tools/list"
   }
   ```

2. **`tools/call`** - Client invokes specific tool
   ```json
   {
     "jsonrpc": "2.0",
     "id": 2,
     "method": "tools/call",
     "params": {
       "name": "add",
       "arguments": { "a": 5, "b": 3 }
     }
   }
   ```

**Tool Invocation Flow:**
1. Client → Server: `tools/call` with tool name + arguments (JSON-RPC request)
2. Server validates request (method exists, parameters valid)
3. Server executes tool business logic (pure domain logic)
4. Server → Client: Result as **content blocks** (JSON-RPC response)

**Content Blocks (MCP-Specific):**
MCP extends JSON-RPC with structured content types:
```java
// Text content
{ "type": "text", "text": "Result: 8" }

// Image content
{ "type": "image", "data": "base64...", "mimeType": "image/png" }

// Resource reference
{ "type": "resource", "uri": "file:///tmp/output.txt" }
```

**Key Insights:**
1. **Stateless:** Each request is independent (no session state)
   - Implication: Server must validate every request fully
   - Design choice: Store context in client, not server

2. **ID Management:** Client must generate unique IDs for correlation
   - Simple: Sequential integers (1, 2, 3, ...)
   - Robust: UUIDs (prevents collisions in concurrent scenarios)

3. **Error Handling:** Standard error codes enable protocol-level error handling
   - Client can distinguish: network errors vs protocol errors vs business errors
   - MCP adds custom error codes for domain-specific failures

**Learning:** JSON-RPC 2.0 is elegant because it's **just HTTP POST with JSON**—no complex WSDL, no schema validation overhead. MCP builds on this simplicity with domain-specific methods and content types.

**References:**
- [JSON-RPC 2.0 Specification](https://www.jsonrpc.org/specification)
- [MCP Protocol Specification](https://modelcontextprotocol.io/docs/specification/basic/messages)

---

##### Problems Solved

**1. Package Refactoring Inconsistency**

**Issue:**
- Created initial files under `com.ai.mcp.helloworld` (incorrect)
- Needed to move to `ai.mcp.helloworld` (correct)
- Manual refactoring left mismatches between directory structure and package declarations

**Solution:**
1. IDE refactor (IntelliJ: Refactor → Rename Package)
2. Verify directory structure matches package declarations:
   ```bash
   # Directory structure
   src/main/java/ai/mcp/helloworld/domain/protocol/

   # Package declaration (must match)
   package ai.mcp.helloworld.domain.protocol;
   ```

3. Find/replace verification:
   ```bash
   # Search for old package references
   grep -r "com.ai.mcp" src/

   # Should return no results after refactoring
   ```

**Root Cause:**
- Did not verify package consistency after initial creation
- Mixed manual directory creation with IDE package creation

**Prevention:**
- Always use IDE "New Package" feature (creates both directory + package declaration)
- Run `mvn clean compile` after refactoring to catch mismatches
- Add pre-commit hook to verify package/directory alignment

**Learning:** Package refactoring is **two-step**: directory structure + package declarations. Both must match exactly, or compilation fails with cryptic errors.

---

##### Technical Decisions for Next Session

**Priority 1: Implement MCP Protocol Models**
- Use **Java 21 sealed interfaces** for type-safe message hierarchy:
  ```java
  public sealed interface McpMessage
      permits McpRequest, McpResponse, McpNotification {}
  ```

- Use **records** for immutable request/response models:
  ```java
  public record McpRequest(
      String jsonrpc,
      Object id,
      String method,
      Map<String, Object> params
  ) implements McpMessage {
      // Compact constructor for validation
      public McpRequest {
          Objects.requireNonNull(jsonrpc, "jsonrpc is required");
          if (!"2.0".equals(jsonrpc)) {
              throw new IllegalArgumentException("Only JSON-RPC 2.0 is supported");
          }
      }
  }
  ```

- Use **factory methods** for convenience:
  ```java
  public static McpRequest toolsListRequest(Object id) {
      return new McpRequest("2.0", id, "tools/list", null);
  }
  ```

**Priority 2: Strict JSON-RPC 2.0 Compliance**
- Validate all required fields (`jsonrpc`, `id`, `method`)
- Implement standard error codes (-32700 to -32603)
- Handle both positional and named parameters

**Priority 3: Export Protocol Implementation Guide**
- Create offline markdown reference for JSON-RPC 2.0
- Export MCP specification key sections (tools, sampling, resources)
- Optimize tokens by avoiding repetitive web fetches

---

##### Article Ideas

1. **"Package Naming Best Practices: When to Use Your Personal Brand in Java"**
   - Reverse domain convention explained
   - Attribution vs organization
   - Publishing to Maven Central vs internal projects
   - Real-world examples (Apache, Spring, personal projects)

2. **"Understanding JSON-RPC 2.0 for Protocol Implementation in Java"**
   - Why JSON-RPC over REST/gRPC?
   - Implementing request-response correlation
   - Error handling patterns
   - Testing strategies for RPC protocols

3. **"DDD Architecture for Protocol Implementations in Java 21"**
   - Layered architecture for MCP
   - Using sealed interfaces and records for protocol models
   - Domain vs infrastructure separation
   - Testing strategy by layer

4. **"Maven Test Lifecycle: *Test.java vs *IT.java and Why It Matters"**
   - Surefire vs Failsafe plugins
   - Fast feedback loops with unit tests
   - CI/CD optimization with test separation
   - Real-world performance comparison

---

##### References

**Official Documentation:**
- [MCP Specification](https://modelcontextprotocol.io/docs)
- [JSON-RPC 2.0 Spec](https://www.jsonrpc.org/specification)
- [Spring AI MCP Docs](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/)

**Internal References:**
- wine-reviewer/CODING_STYLE.md (Java conventions, test patterns)
- wine-reviewer/services/api (DDD architecture example)
- ai/CLAUDE.md (project context, work style)

**External Resources:**
- [Effective Java by Joshua Bloch](https://www.pearson.com/en-us/subject-catalog/p/effective-java/P200000000138) (records, sealed classes)
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/) (layered architecture)

---

**Next Session Goal:** Implement MCP protocol models with Java 21 features
**Token Optimization:** Export protocol specs to offline markdown (reduce repetitive web fetches)
**Branch:** `feature/poc-01-hello-world`
**Estimated Duration:** 2-3 hours

---

#### 📱 PART 2: FRONTEND (Not Applicable)

This project focuses exclusively on backend MCP protocol implementation in Java/Spring Boot. No frontend components planned for POC 1 (Hello World MCP).

---

#### 🐳 PART 3: INFRASTRUCTURE (Future)

Infrastructure setup will be addressed in later POCs:
- **POC 2 (AWS Cost Explorer):** Docker setup for AWS credentials management
- **POC 3 (Photo Search):** Google Cloud integration, Vector DB deployment
- **POC 4-5:** Kubernetes deployment, MCP gateway patterns

---

## Key Learnings by Topic

### Architecture & Design Patterns

**1. Package Naming Philosophy**
- Package names are for **organization**, not **attribution**
- Use reverse domain only if you **own the domain**
- For GitHub projects: `io.github.username` when publishing to Maven Central
- For learning projects: descriptive names (`ai.mcp`, `protocol.jsonrpc`)

**2. Test Lifecycle Management**
- `*Test.java` = unit tests (fast, Surefire, `mvn test`)
- `*IT.java` = integration tests (slow, Failsafe, `mvn verify`)
- Naming convention enables **automated test separation** without complex configuration

**3. Layered Architecture for Protocols**
- **Domain:** Pure models (framework-agnostic, future-proof)
- **Application:** Use cases (orchestration, business logic)
- **Infrastructure:** Technical concerns (I/O, serialization, external dependencies)
- Clear boundaries enable **extraction** and **reusability**

### JSON-RPC 2.0 & MCP Protocol

**1. JSON-RPC Core Concepts**
- Stateless request-response protocol
- ID-based correlation (client generates, server echoes)
- Standard error codes (-32700 to -32603)
- Simple: just HTTP POST with JSON payloads

**2. MCP Extensions**
- Domain-specific methods: `tools/list`, `tools/call`, `resources/read`, `sampling/request`
- Content blocks: structured results (text, image, resource)
- Bidirectional communication: server can request LLM sampling via client

**3. Protocol Implementation Strategy**
- Use **sealed interfaces** for type safety (McpMessage hierarchy)
- Use **records** for immutability (McpRequest, McpResponse)
- Validate at boundaries (compact constructors, factory methods)

### Java 21 Features

**1. Sealed Interfaces**
```java
public sealed interface McpMessage
    permits McpRequest, McpResponse, McpNotification {
    // Exhaustive pattern matching in switch expressions
}
```

**2. Records**
```java
public record McpRequest(String jsonrpc, Object id, String method, Map<String, Object> params) {
    // Compact constructor for validation
    public McpRequest {
        Objects.requireNonNull(jsonrpc);
    }
}
```

**3. Pattern Matching (Future)**
```java
// Exhaustive switch with sealed types
switch (message) {
    case McpRequest r -> handleRequest(r);
    case McpResponse r -> handleResponse(r);
    case McpNotification n -> handleNotification(n);
}
```

### Maven & Build Tools

**1. Surefire vs Failsafe**
- **Surefire:** Runs during `test` phase, fails fast on first error
- **Failsafe:** Runs during `verify` phase, completes all tests even if some fail
- Integration tests need cleanup → Failsafe ensures cleanup runs even on failure

**2. Multi-Module Projects**
- Parent POM: Dependency management, plugin configuration, Java version
- Child modules: Specific dependencies, inherit from parent
- Benefit: Consistent versions, shared configuration, selective builds

### Development Workflow

**1. Token Optimization**
- Export frequently referenced specs to offline markdown
- Avoid repetitive web fetches (MCP spec, JSON-RPC spec)
- Use local documentation cache

**2. Session Documentation**
- Document decisions **immediately** (context is fresh)
- Capture **rationale**, not just "what"
- Include **alternatives considered** and **why rejected**

**3. Future-Proofing**
- Design for extraction (mcp-protocol-java as standalone library)
- Avoid framework lock-in in domain layer
- Use interfaces for infrastructure concerns (Transport, Codec)

---

**Last Updated:** 2025-11-04
**Total Sessions:** 1
**POCs Completed:** 0 (POC 1 in progress)
**Articles Published:** 0
**Next Milestone:** Complete POC 1 Hello World MCP + publish first article
