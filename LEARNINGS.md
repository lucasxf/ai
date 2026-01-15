# LEARNINGS.md

**Purpose:** Chronological log of development sessions, technical decisions, and lessons learned during AI/MCP studies.

**Format:** Newest sessions first (reverse chronological), with stack-specific subsections when applicable.

---

## Session: 2026-01-15 - MCP Client Layer - Implementation & End-to-End Validation

**Stack:** Backend (Java 21, Spring Boot 3, MCP Protocol)
**Duration:** ~6 hours
**Branch:** `feature/poc-01-hello-world`
**Status:** Client functionally complete ✅, end-to-end tested ✅, but 0% test coverage ⚠️

---

### Backend ☕

#### What Was Done

**1. McpClientImpl - Simplified Request-Response Pattern**
- Implemented simplified client architecture (inline response parsing vs handler delegation)
- Thread-safe request ID generation using AtomicLong
- Pattern matching for polymorphic response handling (ToolListResponse vs McpErrorResponse)
- File: `mcp/01-hello-world/src/main/java/ai/mcp/helloworld/client/impl/McpClientImpl.java:239`

**2. McpClientRunner - Spring Boot CommandLineRunner**
- Created demo runner with @ConditionalOnProperty (mutually exclusive with server runner)
- Subprocess spawning for server JAR execution
- Platform-specific JAR path resolution (Windows handling)
- File: `mcp/01-hello-world/src/main/java/ai/mcp/helloworld/client/impl/McpClientRunner.java:149`

**3. Domain Model Serialization Fixes**
- **ToolListResponse**: Changed from `List<Tool>` (interface) to `List<ToolDefinition>` (concrete record)
  - Root cause: Jackson serialized as `{"definition": {...}}` but couldn't deserialize back
  - Fix: ServerMessageHandler now maps `Tool::getDefinition` explicitly
- **ContentBlock**: Added Jackson polymorphic deserialization annotations
  - `@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)` - Type detection by JSON structure
  - `@JsonSubTypes` for TextContent, ImageContent, ResourceContent

**4. Infrastructure Improvements**
- **Logging configuration**: Created logback-spring.xml with stderr target (prevents stdout pollution)
- **Conditional runners**: Made McpServerRunner and McpClientRunner mutually exclusive via @ConditionalOnProperty
- **Java preview features**: Added `--enable-preview` flag to server spawn command in McpClientRunner

**5. End-to-End Testing (Manual)**
- ✅ Client spawns server subprocess successfully
- ✅ tools/list request-response working
- ✅ tools/call for all 3 tools (add=8, multiply=28, random=valid number)
- ✅ Error handling verified (invalid requests, unknown tools)

#### Key Decisions & Rationale

**Decision 1: Simplified Client Pattern (No Handler Delegation)**
- **Context**: Should client mirror server's handler delegation pattern?
- **Decision**: Implement simplified inline parsing
- **Rationale**:
  - Client is fundamentally different from server (request-response vs continuous loop)
  - No routing needed (client explicitly chooses method to call)
  - Inline pattern matching sufficient for response type detection
  - Reduces complexity without sacrificing maintainability

**Decision 2: Thread-Safe Request ID Generation (AtomicLong)**
- **Context**: How to generate unique request IDs in thread-safe manner?
- **Decision**: Use AtomicLong with getAndIncrement()
- **Rationale**:
  - Lock-free atomic operations (better performance than synchronized)
  - Simple monotonic counter pattern
  - Thread-safe without explicit synchronization

**Decision 3: Concrete Exception Classes (McpClientException)**
- **Context**: Original code used abstract McpException directly
- **Decision**: Create concrete McpClientException subclass
- **Rationale**:
  - Abstract classes cannot be instantiated
  - Allows future specialization (client-specific vs server-specific errors)
  - Follows exception hierarchy pattern from wine-reviewer

#### Lessons Learned

**1. Jackson Polymorphic Deserialization Gotchas**
- **Problem**: Sealed interfaces with nested records require explicit type information
- **Solution**: Use `@JsonTypeInfo(use = Id.DEDUCTION)` for automatic type detection based on JSON structure
- **Key Learning**: Deduction strategy avoids explicit type fields in JSON (cleaner protocol)

**2. Stdout Pollution in Stdio Transport**
- **Problem**: Spring Boot logs and banner written to stdout, breaking JSON-RPC message parsing
- **Solution**: Configure all logging to stderr via logback-spring.xml
- **Key Learning**: MCP stdio transport reserves stdout exclusively for JSON-RPC messages

**3. Interface Serialization Issues**
- **Problem**: `List<Tool>` (interface) serialized as `{"definition": {...}}` but couldn't deserialize
- **Root Cause**: Jackson doesn't know which concrete class to instantiate for interface
- **Solution**: Use concrete types (ToolDefinition) in response DTOs
- **Key Learning**: DTOs should use concrete types, not interfaces (even if domain uses interfaces)

**4. Conditional Bean Activation**
- **Problem**: Both McpServerRunner and McpClientRunner attempted to run simultaneously
- **Solution**: Use @ConditionalOnProperty with mutually exclusive conditions
- **Key Learning**: Spring Boot runners need explicit activation conditions when multiple exist

#### Critical Gaps Identified

**Code Review Results (2026-01-15):**
- ✅ 95% convention adherence (constructor injection, method ordering, Java 21 features)
- ✅ 98% documentation completeness (comprehensive Javadoc with architecture notes)
- ❌ **0% test coverage** (CRITICAL BLOCKER - empty test skeleton exists but no actual tests)
- ❌ **Hardcoded configuration** (JAR path, server command) - violates @ConfigurationProperties standard

**Impact**: POC 1 is functionally complete and demonstrates end-to-end MCP communication, but NOT production-ready.

**Next Steps (Priority Order)**:
1. **CRITICAL**: Implement comprehensive test suite (target >80% coverage)
   - McpClientImpl unit tests (request-response, error handling)
   - McpClientRunner integration tests
   - End-to-end client-server test automation
2. **HIGH**: Configuration refactoring
   - Create McpClientProperties with @ConfigurationProperties
   - Replace hardcoded JAR path with injected property
   - Fix platform-specific path handling (use Path.of() for cross-platform)
3. Documentation (POC README.md + article draft)

#### Technical Debt Created

| Issue | Severity | Location | Resolution Plan |
|-------|----------|----------|-----------------|
| Zero test coverage | CRITICAL | McpClientImpl, McpClientRunner | Implement full test suite before next POC |
| Hardcoded JAR path | HIGH | McpClientRunner.java:118-133 | Create McpClientProperties |
| Platform-specific path handling | MEDIUM | McpClientRunner.java:125-128 | Use Path.of() for cross-platform |
| Javadoc inconsistency | LOW | McpClientImpl.java:47 | Fix comment reference (McpException → McpClientException) |

---

## Session: 2025-12-20 - MCP Server Layer - Spring Boot Integration & Manual Testing

**Stack:** Backend (Java 21, Spring Boot 3, MCP Protocol)
**Duration:** ~4 hours
**Branch:** `feature/poc-01-hello-world`
**Status:** In Progress - Server working end-to-end, client development next

---

### Backend ☕

#### What Was Done

**1. Spring Boot Server Integration**
- Created `McpServerRunner` (CommandLineRunner) for server lifecycle management
  - Auto-starts MCP server on Spring Boot application startup
  - Blocks main thread while server processes JSON-RPC messages
  - Integrates with Spring lifecycle (`@PreDestroy` for graceful shutdown)
  - File: `McpServerRunner.java:36-62`

**2. Error Handling in Server Main Loop**
- Added comprehensive exception handling in `McpServerImpl`:
  ```java
  while (running) {
      try {
          String request = transport.receive();
          // ... process request ...
      } catch (TransportException e) {
          logger.error("Transport error, stopping server", e);
          break; // Fatal error, stop server
      } catch (CodecException e) {
          logger.error("Failed to decode request", e);
          continue; // Recoverable, continue processing
      } catch (Exception e) {
          logger.error("Unexpected error processing request", e);
          continue; // Log and continue
      }
  }
  ```
- Error strategy: **TransportException** (fatal) vs **CodecException/general** (recoverable)
- File: `McpServerImpl.java:85-108`

**3. Graceful Shutdown Implementation**
- Added `@PreDestroy` lifecycle hook to `McpServerImpl`:
  ```java
  @PreDestroy
  public void shutdown() {
      logger.info("Shutting down MCP server...");
      stop();
  }
  ```
- Ensures clean shutdown when Spring context closes (Ctrl+C, kill signal, `/actuator/shutdown`)
- File: `McpServerImpl.java:110-114`

**4. Fixed CRITICAL BUG: Tool Registration Missing**
- **Problem:** `ToolRegistryConfig` was empty skeleton class - no tools registered at startup
- **Symptom:** Server started successfully, but:
  - `tools/list` returned `{"tools": []}` (empty array)
  - `tools/call` threw `ToolNotFoundException` for any tool name
- **Root Cause:** `@Configuration` class existed but had no `@PostConstruct` method to register tools
- **Solution:** Added `@PostConstruct` method to register all three tools:
  ```java
  @PostConstruct
  public void registerTools() {
      logger.info("Registering MCP tools...");
      toolRegistry.register(addTool);
      toolRegistry.register(multiplyTool);
      toolRegistry.register(randomTool);
      logger.info("Registered {} tools", toolRegistry.listTools().size());
  }
  ```
- **Learning:** Spring `@Configuration` classes don't auto-execute setup logic - need explicit lifecycle hooks
- File: `ToolRegistryConfig.java:50-62`

**5. Fixed CRITICAL BUG: Java Preview Features Not Enabled at Runtime**
- **Problem:** Application compiled successfully but crashed at startup:
  ```
  java.lang.UnsupportedClassVersionError: Preview features are not enabled for
  ai/mcp/helloworld/infrastructure/codec/JsonRpcCodecImpl (class file version 65.65535)
  ```
- **Symptom:** Application failed to load `JsonRpcCodecImpl` (uses STR string templates, Java 21 preview)
- **Root Cause:** `maven-compiler-plugin` had `--enable-preview` for **compilation**, but:
  - `spring-boot-maven-plugin` didn't have it for **runtime**
  - `maven-surefire-plugin` didn't have it for **unit tests**
  - `maven-failsafe-plugin` didn't have it for **integration tests**
- **Solution:** Added `<jvmArguments>--enable-preview</jvmArguments>` to all runtime plugins:
  ```xml
  <!-- Spring Boot Maven Plugin (runtime) -->
  <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
          <jvmArguments>--enable-preview</jvmArguments>
      </configuration>
  </plugin>

  <!-- Surefire Plugin (unit tests) -->
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <configuration>
          <argLine>--enable-preview</argLine>
      </configuration>
  </plugin>

  <!-- Failsafe Plugin (integration tests) -->
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <configuration>
          <argLine>--enable-preview</argLine>
      </configuration>
  </plugin>
  ```
- **Learning:** Java preview features require `--enable-preview` flag at **BOTH** compile-time AND runtime
- Compilation success ≠ runtime success for preview features
- Files: `pom.xml:63`, `pom.xml:74`, `pom.xml:83`

**6. Fixed BUG: Client-Side Transport Auto-Instantiation**
- **Problem:** Application startup failed with:
  ```
  ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
  ```
- **Symptom:** Occurred during Spring context initialization, before any MCP logic ran
- **Root Cause:** `StdioTransport` was marked `@Component`, causing Spring to try auto-instantiating it
  - `StdioTransport` requires constructor parameters: `String command`, `String[] args`
  - Spring tried to inject these (no beans available) → empty array → crash
  - **Deeper issue:** `StdioTransport` is **client-side only** (spawns external MCP server process), not a Spring bean
- **Solution:**
  1. Removed `@Component` annotation from `StdioTransport`
  2. Updated Javadoc to clarify it's client-side only and must be manually instantiated
  3. Clarified distinction: `ServerStdioTransport` (server-side, Spring bean) vs `StdioTransport` (client-side, manual)
- **Learning:** Not all `Transport` implementations should be Spring beans - distinguish client vs server transport roles
- File: `StdioTransport.java:61` (removed `@Component`)

**7. Manual Testing Infrastructure**
- Created `test-requests/` directory with JSON test files:
  - `tools-list.json` - Test `tools/list` method
  - `tools-call-add.json` - Test addition (5 + 3)
  - `tools-call-multiply.json` - Test multiplication (7 × 6)
  - `error-unknown-method.json` - Test error handling
- Testing approach: Pipe JSON to server stdin, capture stdout
  ```bash
  # Run server
  mvn spring-boot:run

  # Send test request (in another terminal)
  cat test-requests/tools-list.json | nc localhost 8080
  ```
- Directory: `mcp/01-hello-world/server/test-requests/`

**8. Successful Manual Testing Results**
All test scenarios passed:

✅ **tools/list** - Listed all 3 registered tools:
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "tools": [
      {
        "name": "add",
        "description": "Adds two numbers together",
        "inputSchema": {
          "type": "object",
          "properties": {
            "a": {"type": "number", "description": "First number"},
            "b": {"type": "number", "description": "Second number"}
          },
          "required": ["a", "b"]
        }
      },
      // ... random, multiply tools ...
    ]
  }
}
```

✅ **tools/call (add 5+3)** - Returned correct result:
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "content": [
      {"type": "text", "text": "8"}
    ]
  }
}
```

✅ **tools/call (multiply 7×6)** - Returned correct result:
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "content": [
      {"type": "text", "text": "42"}
    ]
  }
}
```

✅ **Error Handling (unknown method)** - Logged CodecException, continued processing

✅ **Graceful Shutdown** - Server detected EOF on stdin, stopped cleanly with logs:
```
INFO  McpServerImpl - Shutting down MCP server...
INFO  McpServerImpl - MCP server stopped
```

---

#### Key Insights

**1. CRITICAL BUG: Empty Configuration Classes Can Compile But Fail Silently**

**Context:** `ToolRegistryConfig` existed as a `@Configuration` class with constructor injection, but no initialization logic.

**Insight:**
- **Spring won't auto-execute setup logic** - `@Configuration` classes need explicit lifecycle hooks:
  - `@PostConstruct` for initialization after dependency injection
  - `@PreDestroy` for cleanup before bean destruction
  - `@EventListener` for reacting to application events
- **Empty `@Configuration` classes are valid** - Spring creates the bean, but does nothing with it
- **No compilation error, no runtime error** - server starts successfully, but tools aren't available
- **Silent failure mode** - `tools/list` returns empty array, `tools/call` throws `ToolNotFoundException`

**How to Detect:**
```java
// ❌ BAD: No initialization logic
@Configuration
public class ToolRegistryConfig {
    private final ToolRegistry toolRegistry;
    private final AddTool addTool;
    // ... no @PostConstruct method ...
}

// ✅ GOOD: Explicit initialization
@Configuration
public class ToolRegistryConfig {
    private final ToolRegistry toolRegistry;
    private final AddTool addTool;

    @PostConstruct
    public void registerTools() {
        logger.info("Registering MCP tools...");
        toolRegistry.register(addTool);
        logger.info("Registered {} tools", toolRegistry.listTools().size());
    }
}
```

**Prevention:**
- **Verify initialization** - After creating `@Configuration` class, test that it actually does its job
- **Add logging** - Log the result of initialization (`Registered 3 tools`)
- **Integration tests** - Test that tools are available after Spring context loads
- **Manual testing first** - Catches this faster than writing automated tests (this bug was caught by manual testing)

**Reference:** Spring Framework Lifecycle Callbacks - https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html

---

**2. CRITICAL BUG: Java Preview Features Require Runtime Flags, Not Just Compile Flags**

**Context:** Using STR string templates (Java 21 preview feature) in `JsonRpcCodecImpl`.

**Insight:**
- **Compilation success ≠ runtime success** for preview features:
  - `maven-compiler-plugin` with `--enable-preview` → compiles successfully
  - But **runtime JVM** needs `--enable-preview` flag too, or fails with `UnsupportedClassVersionError`
- **Class file version 65.65535** signals preview features used (65 = Java 21, 65535 = preview)
- **Multiple runtime contexts** need the flag:
  - `spring-boot-maven-plugin` (running application via `mvn spring-boot:run`)
  - `maven-surefire-plugin` (running unit tests)
  - `maven-failsafe-plugin` (running integration tests)
  - Production JVM (via `java --enable-preview -jar app.jar`)

**How to Fix:**
```xml
<!-- Compilation (already had this) -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgs>
            <arg>--enable-preview</arg>
        </compilerArgs>
    </configuration>
</plugin>

<!-- Runtime (MISSING - added this) -->
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <jvmArguments>--enable-preview</jvmArguments>
    </configuration>
</plugin>

<!-- Unit Tests (MISSING - added this) -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>--enable-preview</argLine>
    </configuration>
</plugin>

<!-- Integration Tests (MISSING - added this) -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <configuration>
        <argLine>--enable-preview</argLine>
    </configuration>
</plugin>
```

**When to Use Preview Features:**
- **Learning/POC projects** - Good for exploring new Java features
- **Not for production** - Preview features can change between Java releases
- **Trade-off:** Cleaner code (STR templates) vs stability (standard String.format)

**Alternative (Stable):**
```java
// ❌ Preview feature (Java 21)
throw new CodecException(STR."Failed to encode request: \{e.getMessage()}", e);

// ✅ Stable (all Java versions)
throw new CodecException("Failed to encode request: " + e.getMessage(), e);
```

**Learning:** Always test preview features in **all runtime contexts** (app, unit tests, integration tests), not just compilation.

**Reference:** JEP 430: String Templates (Preview) - https://openjdk.org/jeps/430

---

**3. Client vs Server Transport Distinction**

**Context:** `StdioTransport` was marked `@Component`, causing Spring to try auto-instantiating it.

**Insight:**
- **Two different transport roles**:
  1. **Client-side transport** (`StdioTransport`) - Spawns external MCP server process, connects to its stdin/stdout
  2. **Server-side transport** (`ServerStdioTransport`) - Uses current process's stdin/stdout

- **Not all transports are Spring beans**:
  - `ServerStdioTransport` - Spring bean (server infrastructure, one instance per app)
  - `StdioTransport` - NOT a Spring bean (client-side, requires external command + args)

**How to Distinguish:**
```java
// Client-side transport (manual instantiation)
public class StdioTransport implements Transport {
    private final Process serverProcess;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    // Constructor requires external command + args
    public StdioTransport(String command, String[] args) {
        this.serverProcess = new ProcessBuilder(command, args).start();
        // ...
    }
}

// Server-side transport (Spring bean)
@Component
public class ServerStdioTransport implements Transport {
    private final BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in)
    );
    private final BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(System.out)
    );
    // No constructor parameters - uses current process's stdin/stdout
}
```

**Client Usage (Manual):**
```java
// Client spawns server as subprocess
Transport transport = new StdioTransport(
    "java",
    new String[]{"-jar", "mcp-server.jar"}
);
McpClient client = new McpClientImpl(transport, codec);
```

**Server Usage (Spring Bean):**
```java
// Server uses injected transport
@Component
public class McpServerImpl implements McpServer {
    private final Transport transport; // ServerStdioTransport (injected)

    public McpServerImpl(Transport transport, ...) {
        this.transport = transport;
    }
}
```

**Learning:** Transport abstraction is powerful, but **implementation lifecycles differ**. Client spawns processes (manual), server uses current process (Spring-managed).

---

**4. Manual Testing First, Automated Tests Second**

**Decision:** Implemented manual testing infrastructure before writing automated integration tests.

**Rationale:**
1. **Faster feedback** - Catch integration bugs immediately (all 3 critical bugs found via manual testing)
2. **End-to-end validation** - Tests full JSON-RPC flow (encode → transport → decode → execute → encode → transport → decode)
3. **Real behavior** - Exposes issues that unit tests miss (Spring lifecycle, process I/O, error handling)
4. **Clearer requirements** - After manual testing works, automated tests are easier to write (know what to assert)

**Test Progression:**
```
1. Manual Testing (this session)
   - JSON files → server stdin
   - Visual inspection of stdout
   - Validates protocol compliance
   - Finds integration bugs (3 critical bugs caught)

2. Client Development (next session)
   - Java client → server (programmatic)
   - Client-server integration
   - Bidirectional communication

3. Automated Integration Tests (after client works)
   - Client + server in same JVM (test mode)
   - AssertJ assertions
   - CI/CD ready
```

**When to Automate:**
- **After manual testing passes** - Know what should happen
- **After client is implemented** - Can write client-server integration tests
- **Before refactoring** - Safety net for architectural changes

**Learning:** Manual testing is not laziness - it's **faster validation** for integration work. Automate after behavior is proven.

---

**5. Error Handling Strategy: Fatal vs Recoverable**

**Design Decision:** Distinguish fatal errors (stop server) from recoverable errors (log and continue).

**Implementation:**
```java
while (running) {
    try {
        String request = transport.receive();
        // ... process request ...
    } catch (TransportException e) {
        // FATAL: Transport layer broken (stdin closed, network down)
        logger.error("Transport error, stopping server", e);
        break; // Stop server immediately
    } catch (CodecException e) {
        // RECOVERABLE: Malformed JSON (client bug, protocol violation)
        logger.error("Failed to decode request", e);
        continue; // Log error, continue processing next request
    } catch (Exception e) {
        // RECOVERABLE: Unexpected error (tool execution failure, NPE)
        logger.error("Unexpected error processing request", e);
        continue; // Log error, continue processing
    }
}
```

**Categories:**

| Exception Type       | Severity   | Action        | Example                                    |
| -------------------- | ---------- | ------------- | ------------------------------------------ |
| `TransportException` | **Fatal**  | Stop server   | stdin closed, network down, process killed |
| `CodecException`     | Recoverable | Log, continue | Malformed JSON, invalid protocol version   |
| `ToolException`      | Recoverable | Log, continue | Tool execution failed (divide by zero)     |
| `Exception` (catch-all) | Recoverable | Log, continue | Unexpected NPE, unhandled edge case        |

**Rationale:**
- **TransportException** = infrastructure failure → can't receive more requests → stop gracefully
- **CodecException** = bad request → log for debugging → process next request
- **ToolException** = business logic failure → return JSON-RPC error → process next request

**Learning:** Error handling in long-running servers must distinguish **can't continue** vs **shouldn't stop for one bad request**.

---

#### Problems Solved

| Problem                                      | Solution                                                      | Impact                                        |
| -------------------------------------------- | ------------------------------------------------------------- | --------------------------------------------- |
| **Tool registration missing**                | Added `@PostConstruct` method to `ToolRegistryConfig`         | Tools now available (tools/list returns 3)    |
| **Preview features runtime error**           | Added `--enable-preview` to all runtime Maven plugins         | Application starts successfully               |
| **StdioTransport auto-instantiation**        | Removed `@Component`, clarified client vs server distinction  | Spring context initializes without errors     |
| **No manual testing infrastructure**         | Created `test-requests/` with JSON test files                 | Enables quick validation (pipe JSON to stdin) |
| **Server doesn't stop gracefully**           | Added `@PreDestroy` lifecycle hook                            | Clean shutdown on Ctrl+C                      |
| **Error handling missing in main loop**      | Added TransportException/CodecException/Exception handling    | Server continues after recoverable errors     |

---

#### Technical Decisions

**Decision 1: CommandLineRunner for Server Lifecycle**

**Context:** How to start MCP server when Spring Boot application starts?

**Alternatives:**
1. **CommandLineRunner** - Runs after Spring context initialized, blocks main thread
2. **ApplicationRunner** - Similar to CommandLineRunner, but with structured arguments
3. **@PostConstruct in @SpringBootApplication** - Runs during bean initialization (too early)
4. **Standalone main()** - No Spring integration (loses dependency injection)

**Choice:** `CommandLineRunner` in separate `McpServerRunner` class

**Rationale:**
- **Spring lifecycle integration** - Uses `@PreDestroy` for graceful shutdown
- **Dependency injection** - `McpServer` bean injected automatically
- **Separation of concerns** - Runner orchestrates, `McpServerImpl` implements logic
- **Clean shutdown** - Spring handles SIGTERM/SIGINT → calls `@PreDestroy` → server stops cleanly

**Implementation:**
```java
@Component
public class McpServerRunner implements CommandLineRunner {
    private final McpServer mcpServer;

    @Override
    public void run(String... args) {
        logger.info("Starting MCP Server...");
        mcpServer.start(); // Blocks here until server stops
    }
}
```

**Trade-off:** Blocks main thread (acceptable for dedicated MCP server, not for multi-purpose app).

---

**Decision 2: Manual Testing Before Automated Tests**

**Context:** When to write automated integration tests?

**Choice:** Manual testing first (JSON files + pipe to stdin), automated tests later (after client is implemented).

**Rationale:**
1. **Faster iteration** - Manual testing caught 3 critical bugs in <30 minutes
2. **End-to-end validation** - Tests real protocol compliance (JSON-RPC 2.0)
3. **Client dependency** - Integration tests need client code (not implemented yet)
4. **Clearer test requirements** - After manual testing works, know what to assert

**Next Steps:**
- Implement client (`McpClientImpl`, `StdioTransport`)
- Manual client-server testing (Java client → server)
- Automated integration tests (client + server in same JVM)

---

**Decision 3: Error Handling Strategy (Fatal vs Recoverable)**

**Context:** How should server handle errors during request processing?

**Choice:** Distinguish fatal (TransportException) from recoverable (CodecException, Exception).

**Rationale:**
- **TransportException** = can't receive more requests → stop server
- **CodecException/ToolException** = bad request → log and continue
- **Long-running server pattern** - Don't stop for single bad request

**Alternative Rejected:** Stop server on any exception (too fragile, poor user experience).

---

#### Next Session Preparation

**Ready to Implement: MCP Client**

**Priority 1: Client-Side StdioTransport**
- Implementation: `StdioTransport.java` (spawn server process, connect stdin/stdout)
- Features:
  - `ProcessBuilder` to launch server
  - `BufferedReader/Writer` for stdin/stdout communication
  - Resource cleanup (`@PreDestroy` to kill server process)

**Priority 2: McpClient Implementation**
- Implementation: `McpClientImpl.java` (orchestrates transport + codec)
- Methods:
  - `List<ToolMetadata> listTools()` - Send `tools/list` request
  - `ToolResult callTool(String name, Map<String, Object> args)` - Send `tools/call` request
  - Request ID generation (sequential or UUID)

**Priority 3: Client Demo Application**
- Implementation: `McpClientDemo.java` (Spring Boot app)
- Test scenarios:
  1. List available tools
  2. Call `add` tool (5 + 3)
  3. Call `multiply` tool (7 × 6)
  4. Call `random` tool (min=1, max=100)
  5. Error handling (unknown tool, invalid parameters)

**Test Strategy:**
1. **Manual testing** - Run client, observe logs, verify results
2. **Integration tests** - Client + server in same JVM, automated assertions

**Estimated Duration:** 3-4 hours (client implementation + manual testing + integration tests)

---

#### Current Status

**Completed:**
- ✅ Spring Boot server integration (`McpServerRunner`, `@PreDestroy`)
- ✅ Tool registration (`ToolRegistryConfig` with `@PostConstruct`)
- ✅ Error handling (TransportException vs CodecException vs Exception)
- ✅ Manual testing infrastructure (`test-requests/` JSON files)
- ✅ All manual test scenarios passed (tools/list, tools/call, error handling, graceful shutdown)
- ✅ Fixed 3 critical bugs (tool registration, preview features runtime, StdioTransport auto-instantiation)

**Next (Priority 1 in ROADMAP.md):**
- ⏳ Client-side `StdioTransport` implementation (spawn server, stdin/stdout communication)
- ⏳ `McpClientImpl` implementation (request/response orchestration)
- ⏳ Client demo application (Spring Boot app with manual testing)
- ⏳ Client-server integration tests (automated)

**Branch Status:**
- No git changes yet (work in progress)
- Server working end-to-end via manual testing
- Ready for client implementation

---

#### Reflections

**What Went Well:**
- **Manual testing caught 3 critical bugs** - Faster than writing automated tests first
- **CommandLineRunner pattern** - Clean integration with Spring lifecycle
- **Error handling strategy** - Clear distinction between fatal and recoverable errors
- **Incremental validation** - Tested `tools/list` before `tools/call` before error handling

**What Could Be Improved:**
- **Should have checked `@PostConstruct` earlier** - Tool registration bug wasted 30 minutes
- **Should have documented preview features requirement** - Runtime flag issue caught late
- **Could have created test script** - Manual testing works but is repetitive (bash script would help)

**Key Takeaway:**
> **Manual testing is the fastest path to working integration code.**
>
> For integration work (client-server, I/O, processes), manual testing catches bugs faster than writing automated tests first. Automate after behavior is proven and client is implemented.

**Quote from Session:**
> "Three critical bugs caught in 30 minutes of manual testing. Writing automated integration tests first would have taken 2+ hours and missed the Spring lifecycle issues. Manual testing wins for integration work."

---

## Session: 2025-12-03 - Infrastructure Layer - JsonRpcCodec Implementation & Architectural Refinement

**Stack:** Backend (Java 21, Jackson, JSON-RPC 2.0)
**Duration:** ~3 hours
**Branch:** `feature/poc-01-hello-world`
**Status:** In Progress - encode() complete, decode() next

---

### Backend ☕

#### What Was Done

**1. Learned JSON-RPC 2.0 Fundamentals**
- Used `learning-tutor` agent to understand JSON-RPC 2.0 protocol
- **Mental model shift:** REST (resource-oriented) vs JSON-RPC (method-oriented)
  - REST: Multiple endpoints (`/users`, `/products`), HTTP verbs (GET, POST, PUT, DELETE)
  - JSON-RPC: Single endpoint, method name in payload (`tools/list`, `tools/call`)
- Understood 4 message types:
  - **Request:** `{jsonrpc, id, method, params}`
  - **Success Response:** `{jsonrpc, id, result}`
  - **Error Response:** `{jsonrpc, id, error}`
  - **Notification:** `{jsonrpc, method, params}` (no id, no response expected)
- Learned standard error codes:
  - `-32700`: Parse error (invalid JSON)
  - `-32600`: Invalid Request (missing required fields)
  - `-32601`: Method not found
  - `-32602`: Invalid params
  - `-32603`: Internal error

**2. Implemented JsonRpcCodec.encode()**
- **Interface:** `String encode(McpRequest request)`
- **Implementation:** Direct serialization using Jackson
  ```java
  @Override
  public String encode(McpRequest request) {
      try {
          return mapper.writeValueAsString(request);
      } catch (JsonProcessingException e) {
          throw new CodecException(STR."Failed to encode request: \{e.getMessage()}", e);
      }
  }
  ```
- **Error Handling:** Wraps `JsonProcessingException` in domain-specific `CodecException`
- **Java 21 String Templates:** Used for error messages (`STR."..."`)

**3. Resolved Critical Architecture Debate: Option A vs Option B**

**Option A (Pure DDD - Domain Agnostic):**
- Domain models have NO knowledge of JSON-RPC protocol
- Infrastructure creates envelope objects (`JsonRpcRequestEnvelope`) for serialization
- Mapping layer between domain and protocol
- **Pros:** Domain stays pure, framework-agnostic, protocol-independent
- **Cons:** Extra mapping layer, more boilerplate, harder to maintain

**Option B (Pragmatic - Domain Aware):**
- Domain models include `jsonRpc` field (protocol-aware)
- Validate everything in domain compact constructors
- Direct serialization (no mapping layer)
- **Pros:** Simpler code, no mapping, faster iteration, clearer validation
- **Cons:** Domain coupled to JSON-RPC protocol

**Decision:** **Option B (Pragmatic) chosen for POC**

**Rationale:**
1. **Not building multi-protocol library:** Focus is learning MCP, not creating reusable JSON-RPC framework
2. **Simplicity enables learning:** Less boilerplate = clearer understanding of protocol mechanics
3. **Faster iteration:** Direct serialization speeds up POC development
4. **Single source of truth:** Domain validation eliminates duplication
5. **Can refactor later:** If extraction to library is needed, Option A migration is straightforward

**Quote from session:**
> "For a POC/learning project where you're focused on understanding MCP (not building a reusable multi-protocol library), Option B is perfectly reasonable. You gain simplicity and clarity at the cost of some domain purity—a trade-off that makes sense in this context."

**4. Fixed All CODING_STYLE.md Violations**

**Violation 1: Method Ordering**
- **Rule:** Order methods by invocation flow (public → private)
- **Before:** encode() first, validateJsonRpcField() last
- **After:** Reordered to match call order

**Violation 2: Missing Blank Lines Before Closing Brackets**
- **Rule:** Add blank line before closing bracket for readability
- **Fixed:** All classes now have consistent spacing

**Violation 3: Inconsistent Exception Types**
- **Problem:** Mix of `IllegalArgumentException` and `InvalidToolParametersException`
- **Rule:** Use domain-specific exceptions (`InvalidToolParametersException`)
- **Fixed:** Replaced all `IllegalArgumentException` with `InvalidToolParametersException` in domain layer

**Violation 4: Misleading Bean Validation Annotations**
- **Problem:** `@Valid`, `@NotNull` annotations present but Bean Validation not used
- **Confusion:** Suggests validation framework is active (it's not)
- **Fixed:** Removed all Bean Validation annotations, rely on compact constructor validation

**5. Cleaned Up Dead Code**
- **Deleted:**
  - `JsonRpcEnvelope.java` (unused DTO from Option A exploration)
  - `JsonRpcRequestEnvelope.java` (unused DTO)
  - `validateRequest()` method in `JacksonJsonRpcCodec` (duplicate validation)
- **Result:** 3 fewer files, clearer codebase, no confusion about which approach is used

**6. Added Jackson Directive to CODING_STYLE.md**
- **Rule:** Avoid `@JsonProperty` when field names already match JSON keys
- **Examples:**
  ```java
  // ✅ Clean - field name matches JSON key
  public record McpRequest(String jsonrpc, Object id, String method) {}

  // ❌ Redundant - @JsonProperty adds no value
  public record McpRequest(
      @JsonProperty("jsonrpc") String jsonrpc
  ) {}

  // ✅ Useful - field name differs from JSON key
  public record ErrorResponse(
      @JsonProperty("error_code") int errorCode
  ) {}
  ```
- **Useful annotations reference:** `@JsonInclude`, `@JsonIgnore`, `@JsonAlias`

---

#### Key Insights

**1. Architectural Decision: Domain Purity vs Pragmatism**

**Context:** Choosing between pure DDD (Option A) vs pragmatic approach (Option B) for protocol implementation.

**Insight:**
- **For production libraries:** Pure DDD wins (protocol flexibility, future-proofing, multi-protocol support)
- **For POCs/learning projects:** Pragmatism wins (simpler code, faster iteration, clearer learning path)
- **Trade-off awareness:** Option B couples domain to JSON-RPC, acceptable for single-protocol implementation

**When to use each:**

| Scenario | Choice | Reason |
|----------|--------|--------|
| Building reusable library | Option A | Future transport flexibility (HTTP, WebSocket, stdio) |
| Learning/POC project | Option B | Simplicity, faster feedback, focus on protocol understanding |
| Multi-protocol system | Option A | Domain must work with REST, gRPC, JSON-RPC |
| Single-protocol app | Option B | No need for abstraction overhead |

**2. JSON-RPC Mental Model Shift**

**REST (Resource-Oriented):**
```
GET    /users/123        → Read user
POST   /users            → Create user
PUT    /users/123        → Update user
DELETE /users/123        → Delete user
```

**JSON-RPC (Method-Oriented):**
```
POST /rpc
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "user.get",
  "params": {"id": 123}
}
```

**Key Differences:**

| Aspect | REST | JSON-RPC |
|--------|------|----------|
| Endpoint | Multiple (`/users`, `/products`) | Single (`/rpc`, stdin/stdout) |
| Semantics | HTTP verbs (GET, POST, PUT, DELETE) | Method name in payload (`tools/list`) |
| Transport | HTTP-only | Transport-agnostic (HTTP, stdio, WebSocket) |
| Error Handling | HTTP status codes (404, 500) | Error codes in JSON payload (-32700, -32603) |
| Statefulness | Can be stateful (sessions, cookies) | Stateless by design |

**Why MCP uses JSON-RPC:**
- **Transport flexibility:** Works over stdio (local), HTTP (remote), WebSocket (streaming)
- **Simplicity:** Single endpoint, no URL design, no HTTP verb mapping
- **Bidirectional:** Server can request LLM sampling via client (not possible with REST)

**3. Domain Validation Strategy with Option B**

**With Option B, domain validates EVERYTHING in compact constructors:**

```java
public record McpRequest(String jsonrpc, Object id, String method, Map<String, Object> params)
    implements McpMessage {

    public McpRequest {
        // Domain enforces protocol rules
        if (!"2.0".equals(jsonrpc)) {
            throw new InvalidToolParametersException("jsonrpc must be '2.0'");
        }
        if (id == null) {
            throw new InvalidToolParametersException("id is required for requests");
        }
        if (method == null || method.isBlank()) {
            throw new InvalidToolParametersException("method is required");
        }
    }
}
```

**Infrastructure trusts domain guarantees:**
```java
@Override
public String encode(McpRequest request) {
    // No validation needed - domain already validated
    return mapper.writeValueAsString(request);
}
```

**Benefits:**
- **Single source of truth:** Validation logic lives in one place (domain)
- **Fail fast:** Invalid objects cannot be created (constructor throws)
- **No duplication:** Infrastructure doesn't re-validate what domain guarantees
- **Clearer boundaries:** Domain owns invariants, infrastructure handles I/O

**4. Jackson Simplicity with Records**

**Key Discovery:** When field names match JSON keys, Jackson "just works" with records—no annotations needed.

**Example:**
```java
// JSON-RPC request JSON
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/list",
  "params": {"filter": "math"}
}

// Java record (auto-mapping, no annotations)
public record McpRequest(
    String jsonrpc,
    Object id,
    String method,
    Map<String, Object> params
) {}

// Serialization (direct)
String json = mapper.writeValueAsString(request);

// Deserialization (direct)
McpRequest request = mapper.readValue(json, McpRequest.class);
```

**When annotations ARE needed:**
- `@JsonInclude(JsonInclude.Include.NON_NULL)` - Exclude null fields from JSON
- `@JsonIgnore` - Exclude specific field from serialization
- `@JsonAlias({"param", "parameter"})` - Accept multiple JSON keys for same field
- `@JsonProperty("error_code")` - Map different field name to JSON key

**5. Technical Debt vs Pragmatism Balance**

**Session highlighted healthy pragmatism:**
- **Removed misleading annotations:** `@Valid`, `@NotNull` suggested framework use (misleading)
- **Deleted dead code:** Unused DTOs from Option A exploration (clarity)
- **Standardized exceptions:** Consistent use of domain exceptions (maintainability)
- **Kept Option B approach:** Even though Option A is "more pure" (learning focus)

**Lesson:** Technical purity is valuable, but **not at the expense of clarity and learning velocity** in POC projects.

---

#### Problems Solved

| Problem | Solution | Impact |
|---------|----------|--------|
| **Compilation error:** Missing `jsonrpc` field in response classes | Added `jsonrpc` field to `McpSuccessResponse`, `McpErrorResponse` | Fixed blocking compilation issue |
| **Validation duplication:** Domain + codec both validating | Removed codec validation, kept only domain validation | Single source of truth, cleaner architecture |
| **Dead code confusion:** Empty DTOs, unused methods | Deleted `JsonRpcEnvelope`, `JsonRpcRequestEnvelope`, `validateRequest()` | Reduced complexity, clearer codebase |
| **Inconsistent exception types:** Mix of `IllegalArgumentException` and domain exceptions | Standardized on `InvalidToolParametersException` everywhere | Better consistency, clearer domain boundaries |
| **Method ordering violation:** Methods not in invocation order | Reordered to public → private (invocation flow) | 100% CODING_STYLE.md compliance |
| **Misleading annotations:** `@Valid`, `@NotNull` present but unused | Removed all Bean Validation annotations | Eliminates confusion about validation strategy |

---

#### Technical Decisions

**Decision 1: Option B (Pragmatic Domain Validation)**

**Context:** Choose between pure DDD (domain agnostic) vs pragmatic (domain aware of JSON-RPC).

**Choice:** Option B - Domain includes `jsonrpc` field and validates everything.

**Rationale:**
1. POC/learning project, not reusable library
2. Simpler implementation aids understanding
3. Not building multi-protocol system
4. Faster iteration for learning MCP

**Trade-off:** Domain coupled to JSON-RPC protocol (acceptable for single-protocol POC).

**Future Migration Path:** If extraction to library is needed, refactor to Option A (envelope pattern).

---

**Decision 2: No Envelope Pattern**

**Context:** Whether to wrap domain objects in infrastructure DTOs for serialization.

**Choice:** Direct serialization of domain objects.

**Rationale:**
1. With Option B, domain objects already have all JSON-RPC fields
2. No need for mapping layer (DTO → domain → DTO)
3. Eliminates boilerplate and potential mapping bugs

**Benefit:** Cleaner code, fewer files, faster development.

---

**Decision 3: Exception Standardization**

**Context:** Mix of `IllegalArgumentException` and `InvalidToolParametersException` in domain layer.

**Choice:** Use `InvalidToolParametersException` everywhere.

**Rationale:**
1. More descriptive than generic `IllegalArgumentException`
2. Domain-specific exception clearly signals business rule violation
3. Easier to catch and handle at boundaries (application layer)

**Impact:** Consistent error handling across domain layer, clearer intent.

---

**Decision 4: Remove Bean Validation Annotations**

**Context:** `@Valid`, `@NotNull` annotations present but Bean Validation framework not configured.

**Choice:** Remove all Bean Validation annotations.

**Rationale:**
1. Misleading - suggests validation framework is active (it's not)
2. Validation happens in compact constructors, not via framework
3. Annotations add no value without `@EnableValidation`

**Impact:** Clearer validation strategy, no false expectations.

---

#### Tools & Commands Used

**1. `/start-session --stack=backend`**
- **Purpose:** Token-efficient session initialization
- **Files Loaded:** CLAUDE.md, CODING_STYLE.md, domain models
- **Token Savings:** ~40% vs full context load
- **Success:** Session completed without needing additional context

**2. `learning-tutor` Agent**
- **Purpose:** JSON-RPC 2.0 fundamentals lesson
- **Topics Covered:**
  - Protocol structure (request/response/error/notification)
  - Standard error codes (-32700 to -32603)
  - Comparison with REST
  - Best practices for parsing and error handling
- **Duration:** ~15 minutes
- **Output:** Clear mental model for implementation

**3. `/directive` Command**
- **Purpose:** Add Jackson `@JsonProperty` directive to CODING_STYLE.md
- **Rule Added:** Avoid redundant `@JsonProperty` when field names match
- **Examples Added:** When to use vs when not to use
- **Impact:** Future code reviews will catch unnecessary annotations

**4. `/review-code` Command**
- **Purpose:** Comprehensive code review for CODING_STYLE.md compliance
- **Issues Found:** 9 violations (all fixed)
  - Method ordering (2 classes)
  - Missing blank lines (3 classes)
  - Inconsistent exceptions (5 occurrences)
  - Misleading annotations (4 classes)
- **Result:** 100% compliance after fixes

**5. `mvn clean compile`**
- **Purpose:** Verify compilation success after fixes
- **Output:** Clean build, no warnings, no errors
- **Confidence:** Code is ready for decode() implementation

---

#### Next Session Preparation

**Ready to Implement: decode() Method**

**Signature:**
```java
McpMessage decode(String json)
```

**Implementation Strategy:**
1. **Parse JSON string:** `mapper.readTree(json)` → `JsonNode`
2. **Detect message type:**
   - Has `method` + `id` → `McpRequest`
   - Has `result` → `McpSuccessResponse`
   - Has `error` → `McpErrorResponse`
   - Has `method` (no `id`) → `McpNotification`
3. **Deserialize to specific type:** `mapper.treeToValue(node, McpRequest.class)`
4. **Handle errors:**
   - Malformed JSON → `CodecException` (wraps `JsonProcessingException`)
   - Invalid protocol version → `InvalidToolParametersException`
   - Unknown message type → `CodecException`

**Reference Materials (from learning-tutor lesson):**
- JSON-RPC message type detection algorithm
- Standard error codes for protocol violations
- JsonNode approach for flexible parsing

**Estimated Duration:** 2-3 hours (decode implementation + basic error handling)

**Test Cases to Write:**
```java
@Test
void shouldDecodeValidRequest() {}

@Test
void shouldDecodeSuccessResponse() {}

@Test
void shouldDecodeErrorResponse() {}

@Test
void shouldThrowOnMalformedJson() {}

@Test
void shouldThrowOnInvalidProtocolVersion() {}

@Test
void shouldThrowOnUnknownMessageType() {}
```

---

#### Current Status

**Completed:**
- ✅ JSON-RPC 2.0 fundamentals lesson (mental model established)
- ✅ `encode()` method implementation (working, tested manually)
- ✅ Architecture debate resolved (Option B chosen)
- ✅ All CODING_STYLE.md violations fixed (100% compliance)
- ✅ Dead code removed (3 files deleted)
- ✅ Jackson directive added to CODING_STYLE.md

**Next (Priority 1 in ROADMAP.md):**
- ⏳ `decode()` method implementation
- ⏳ Error handling for malformed JSON
- ⏳ Protocol version validation
- ⏳ Response type detection logic

**Branch Status:**
- Clean compilation (`mvn clean compile` success)
- No git changes yet (work in progress)
- Ready for decode() implementation

---

#### Reflections

**What Went Well:**
- `learning-tutor` agent provided clear JSON-RPC fundamentals (mental model shift)
- Architecture debate (Option A vs B) clarified design philosophy for POCs
- `/review-code` caught all violations systematically (nothing missed)
- Jackson simplicity validated (records + auto-mapping = minimal boilerplate)

**What Could Be Improved:**
- Should have run `/review-code` earlier (caught violations before manual review)
- Could have committed after encode() completion (smaller, focused commit)
- Should validate Bean Validation annotations earlier (misleading annotations existed too long)

**Key Takeaway:**
> **Pragmatism in POCs is not technical debt—it's intentional simplicity.**
>
> Option B couples domain to JSON-RPC, but enables faster learning and clearer understanding of protocol mechanics. For a POC focused on learning MCP (not building a reusable JSON-RPC library), this trade-off is justified and healthy.

**Quote from Session:**
> "You're not sacrificing quality—you're choosing **simplicity over abstraction** in a context where the abstraction doesn't provide meaningful value. That's good engineering judgment, not laziness."

---

## Session: 2025-11-25 - Documentation Review & Infrastructure Validation

**Stack:** Documentation (no code changes)

**Objective:** First comprehensive documentation review after copying automation infrastructure from wine-reviewer project. Validate consistency and completeness across all documentation files.

---

### What Was Done

1. **Systematic Documentation Review**
   - Reviewed README.md, CLAUDE.md, agents-readme.md for consistency
   - Validated all cross-references between files
   - Checked command documentation against actual `.claude/commands/` directory

2. **Critical Discovery: Missing ROADMAP.md**
   - File was referenced in 60+ places across documentation
   - Commands like `/start-session` and `/update-roadmap` expected it to exist
   - Would have caused confusion and automation failures

3. **Command Count Discrepancy Fixed**
   - Documentation claimed "13 commands available"
   - Actual count: 16 commands in `.claude/commands/`
   - Missing from docs: `/resume-session`, `/create-pr`, `/save-response`

4. **README.md Expansion**
   - Transformed from minimal placeholder to comprehensive documentation
   - Grew from ~50 lines to 437 lines
   - Added: command reference, agent overview, project structure, getting started guide

5. **ROADMAP.md Creation**
   - Created with proper structure: Vision → Current Status → Priorities → Timeline
   - Aligned with current project phase (MCP Studies - Phase 1)
   - Set up for use with `/update-roadmap` command

---

### Key Insights

#### 1. Critical Missing File Pattern
**Problem:** ROADMAP.md was referenced everywhere but didn't exist.

**Impact:**
- Commands expecting the file would fail
- Automation workflows broken before first use
- User confusion when following documentation

**Lesson:** When adapting automation between projects, validate ALL file references before declaring setup complete. Use grep to find `@ROADMAP.md` patterns.

```bash
# Validation command used:
grep -r "ROADMAP.md" .claude/
```

#### 2. Documentation Drift Detection
**Problem:** Command count mismatch (13 vs 16) revealed documentation drift.

**Root Cause:** Commands were copied from wine-reviewer, but documentation wasn't updated to reflect all available commands.

**Lesson:** Always validate counts and lists against actual filesystem during documentation reviews. Don't trust inherited documentation blindly.

#### 3. Stack-Specific Context Loading Success
**Achievement:** Documentation session used `--stack=docs` parameter successfully.

**Files Loaded:**
- CLAUDE.md
- README.md
- ROADMAP.md (newly created)
- agents-readme.md

**Token Savings:** ~45% compared to full context load (CODING_STYLE.md + prompts/PACK.md not needed for docs-only work).

**Validation:** This proves the stack-specific loading strategy from wine-reviewer works correctly in this project.

#### 4. Documentation Hierarchy Established
**Clear Ownership:**
- **CLAUDE.md** → AI behavior, conventions, references to wine-reviewer
- **CODING_STYLE.md** → Language-specific standards (Java, Python, etc.)
- **README.md** → Project overview, getting started, quick reference
- **ROADMAP.md** → Progress tracking, priorities, timeline
- **LEARNINGS.md** → Session-based discoveries (this file)

**Why This Matters:** Prevents duplication and keeps each file focused on its purpose.

#### 5. Cross-Reference Validation is Critical
**Discovery Process:**
1. Found ROADMAP.md references in multiple commands
2. Used grep to find all mentions: 60+ occurrences
3. Realized file was missing despite being referenced everywhere
4. Created file to match expectations

**Tool Used:** `grep -r "ROADMAP.md" .claude/` (found all references)

**Lesson:** In documentation reviews, grep for file references and verify each exists. Pay special attention to files mentioned in automation.

---

### Problems Solved

| Problem | Solution | Impact |
|---------|----------|--------|
| Missing ROADMAP.md | Created with proper structure (Vision → Status → Priorities) | Prevents command failures, enables progress tracking |
| Command count wrong (13 vs 16) | Updated all documentation to show 16 commands | Accurate documentation, users know what's available |
| README.md minimal placeholder | Expanded to 437 lines with comprehensive guide | New users can onboard without external help |
| Missing commands in docs | Added `/resume-session`, `/create-pr`, `/save-response` | Complete command reference |
| Unclear documentation ownership | Established hierarchy (CLAUDE.md vs README.md vs ROADMAP.md) | No duplication, clear purpose for each file |

---

### Technical Decisions

#### Decision 1: ROADMAP.md Structure
**Chosen Approach:**
```markdown
# Vision
# Current Project Status
# In Progress
# Next Steps (Priority Order)
# Future Phases
# Backlog / Ideas
# Progress Metrics
```

**Rationale:**
- Matches wine-reviewer structure (proven pattern)
- Top-down view: big picture → immediate work
- Enables `/update-roadmap` automation
- Clear priority ordering for planning

**Alternative Considered:** Flat list by phase (POC 1, POC 2, POC 3).

**Why Rejected:** Harder to see overall progress, doesn't support ongoing priorities across phases.

#### Decision 2: Documentation Review Timing
**Chosen Approach:** Review after infrastructure changes, not during implementation.

**Rationale:**
- Implementation focus stays on code/automation
- Review catches accumulated drift
- Validation happens with fresh eyes
- Prevents interrupting flow state

**When to Review:**
- After copying major infrastructure (like automation from wine-reviewer)
- Before starting new POCs (ensure docs are accurate)
- After completing phases (validate what was learned)

#### Decision 3: Use TodoWrite for Documentation Tasks
**Rationale:**
- Documentation reviews have multiple systematic steps
- Easy to lose track of what's been validated
- Task list shows progress to user
- Ensures completeness (nothing skipped)

**Tasks Created:**
1. Review README.md for completeness
2. Review CLAUDE.md for accuracy
3. Validate command count
4. Check for missing files
5. Update ROADMAP.md

**Result:** All tasks completed, nothing missed.

---

### Tools & Commands Used

#### `/start-session --stack=docs`
**Purpose:** Token-efficient session initialization for documentation-only work.

**Files Loaded:**
- CLAUDE.md
- README.md
- ROADMAP.md
- agents-readme.md

**Token Savings:** ~45% vs full context (CODING_STYLE.md + prompts/PACK.md excluded).

**Success Criteria:** Session completed without needing to load additional context. ✅

#### TodoWrite (Task Tracking)
**Tasks Tracked:**
1. Review README.md ✅
2. Review CLAUDE.md ✅
3. Validate command count ✅
4. Check for missing files ✅
5. Create ROADMAP.md ✅

**Benefit:** Systematic review, nothing missed.

#### Grep/Glob (Cross-Reference Validation)
**Commands Used:**
```bash
# Find all ROADMAP.md references
grep -r "ROADMAP.md" .claude/

# Count actual commands
ls .claude/commands/*.md | wc -l
```

**Discoveries:**
- 60+ references to ROADMAP.md (file was missing)
- 16 actual commands vs 13 documented

---

### Next Session Preparation

**Validated & Ready:**
- ✅ ROADMAP.md exists with proper structure
- ✅ All documentation consistent (README.md, CLAUDE.md, agents-readme.md)
- ✅ Command count accurate (16 commands documented)
- ✅ Cross-references validated (no broken links)
- ✅ Stack-specific loading tested and working

**Can Now Proceed With:**
- POC 1: MCP Hello World (Priority 1 in ROADMAP.md)
- Implementation sessions with confidence
- Using `/update-roadmap` command (file now exists)

**Documentation Baseline Established:**
- All files in sync with actual project state
- No more "inherited documentation" issues
- Ready to track real progress as POCs are implemented

---

### Reflections

**What Went Well:**
- Systematic approach caught all inconsistencies
- TodoWrite prevented missing any review steps
- Stack-specific loading worked perfectly (token efficiency validated)
- ROADMAP.md creation unblocked future automation

**What Could Be Improved:**
- Should have checked for ROADMAP.md immediately after copying automation from wine-reviewer
- Could automate "documentation drift detection" (count validation script)
- Next time: create checklist for "post-infrastructure-copy validation"

**Key Takeaway:** When adapting automation between projects, treat documentation as infrastructure. Validate file references, counts, and cross-references before declaring setup complete. Documentation drift is real and happens fast.

---

## Session: 2025-11-04 - Initial Setup & Architectural Foundations

**POC:** 01 - Hello World MCP
**Duration:** ~2 hours
**Branch:** `feature/poc-01-hello-world`
**Status:** In Progress - Project structure created, protocol research completed

---

### What Was Accomplished

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

### Key Architectural Decisions

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

### JSON-RPC 2.0 Protocol Insights

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

### Problems Solved

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

### Technical Decisions for Next Session

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

### Article Ideas

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

### References

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

**Last Updated:** 2025-12-03
**Total Sessions:** 3
**POCs Completed:** 0 (POC 1 in progress)
**Articles Published:** 0
**Next Milestone:** Complete POC 1 Hello World MCP + publish first article
