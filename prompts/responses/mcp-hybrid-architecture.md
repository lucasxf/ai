---
created: 2025-12-15
session: 2025-12-15
user-request: "Propose alternatives for running both MCP client and server simultaneously for learning purposes"
---

# MCP Hybrid Architecture: Self-Contained Client + Server Demo

## Architecture Overview

**Core Concept:**
```
Single Spring Boot Application Startup:
┌─────────────────────────────────────────────┐
│                                             │
│  1. Server starts (background thread)      │
│     - Listens on stdin/stdout              │
│     - Handles MCP requests                 │
│                                             │
│  2. Client starts (main thread)            │
│     - Spawns server as subprocess          │
│     - Sends demo requests                  │
│     - Prints responses                     │
│                                             │
└─────────────────────────────────────────────┘

Result: You see both sides of MCP communication in one run!
```

## File Structure (Clean Separation)

```
mcp/01-hello-world/
└── src/main/java/ai/mcp/helloworld/
    │
    ├── server/                           # Server-only code
    │   ├── McpServerApplication.java     # Server main()
    │   ├── McpServer.java                # Interface
    │   ├── McpServerImpl.java            # Implementation
    │   ├── ServerMessageHandler.java     # Request dispatcher
    │   └── ServerStdioTransport.java     # NEW: System.in/out wrapper
    │
    ├── client/                           # Client-only code
    │   ├── McpClientDemo.java            # Client main() + demo
    │   ├── McpClient.java                # Interface
    │   └── McpClientImpl.java            # Implementation
    │
    ├── shared/                           # Shared by both
    │   ├── domain/                       # Existing domain layer
    │   ├── infrastructure/
    │   │   ├── codec/                    # JsonRpcCodec (shared)
    │   │   └── transport/
    │   │       ├── Transport.java        # Interface
    │   │       └── StdioTransport.java   # Client transport (existing)
    │   └── config/
    │       └── ToolRegistryConfig.java
    │
    └── McpHelloWorldApplication.java    # Optional: unified launcher
```

## Implementation Steps

### Step 1: Create ServerStdioTransport (New Class)

**Purpose:** Reads from System.in (client sends), writes to System.out (client receives)

```java
package ai.mcp.helloworld.server;

@Component
public class ServerStdioTransport implements Transport {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerStdioTransport() {
        // Server reads from System.in (parent sends requests here)
        this.reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );

        // Server writes to System.out (parent reads responses here)
        this.writer = new BufferedWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
        );
    }

    @Override
    public void send(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();  // Critical!
        } catch (IOException e) {
            throw new TransportException("Failed to send", e);
        }
    }

    @Override
    public String receive() {
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new TransportException("EOF - client disconnected");
            }
            return line;
        } catch (IOException e) {
            throw new TransportException("Failed to receive", e);
        }
    }

    @Override
    public boolean isConnected() {
        return true;  // System.in/out always "connected"
    }

    @Override
    public void close() {
        // Don't close System.in/out (owned by JVM)
    }
}
```

**Lines of code:** ~30 lines

---

### Step 2: Implement ServerMessageHandler (Business Logic)

**Purpose:** String-based request dispatcher with tool execution

```java
package ai.mcp.helloworld.server;

@Component
@Slf4j
public class ServerMessageHandler {

    private final ToolRegistry toolRegistry;

    public ServerMessageHandler(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    public McpMessage handle(McpRequest request) {
        log.info("[Server] Handling request: {}", request.method());

        return switch (request.method()) {
            case "tools/list" -> handleToolsList(request);
            case "tools/call" -> handleToolCall(request);
            default -> McpErrorResponse.methodNotFound(
                request.id(),
                "Unknown method: " + request.method()
            );
        };
    }

    private ToolListResponse handleToolsList(McpRequest request) {
        List<Tool> tools = toolRegistry.getAllTools();
        log.info("[Server] Returning {} tools", tools.size());
        return new ToolListResponse(
            request.id(), "2.0", null,
            new ToolListResponse.ToolListResult(tools)
        );
    }

    private McpMessage handleToolCall(McpRequest request) {
        try {
            String toolName = (String) request.params().get("name");
            Map<String, Object> args = (Map) request.params().get("arguments");

            log.info("[Server] Executing tool: {} with args: {}", toolName, args);

            Tool tool = ToolRegistry.getTool(toolName);
            List<ContentBlock> result = tool.execute(args);

            return ToolInvocationResponse.create(request.id(), result);

        } catch (ToolNotFoundException e) {
            return McpErrorResponse.methodNotFound(request.id(), e.getMessage());
        } catch (InvalidToolParametersException e) {
            return McpErrorResponse.invalidParams(request.id(), e.getMessage());
        } catch (Exception e) {
            log.error("[Server] Unexpected error", e);
            return McpErrorResponse.internalError(request.id(), e.getMessage());
        }
    }
}
```

**Key Features:**
- ✅ String-based method dispatch (simple, debuggable)
- ✅ Clear error mapping (domain exceptions → JSON-RPC errors)
- ✅ Easy to extend (just add new `case "method/name" -> handler()`)

**Lines of code:** ~60 lines

---

### Step 3: Implement McpServerApplication (Server Main)

**Purpose:** Standalone server that listens on stdin and handles MCP requests

```java
package ai.mcp.helloworld.server;

@SpringBootApplication
@Slf4j
public class McpServerApplication {

    public static void main(String[] args) {
        log.info("[Server] Starting MCP Server...");

        var context = SpringApplication.run(McpServerApplication.class, args);
        var server = context.getBean(McpServer.class);

        // Register tools
        ToolRegistry.register("add", context.getBean(AddTool.class));
        ToolRegistry.register("multiply", context.getBean(MultiplyTool.class));
        ToolRegistry.register("random", context.getBean(RandomTool.class));

        log.info("[Server] Listening on stdin...");

        // Start server (blocks until EOF on stdin)
        server.start();

        log.info("[Server] Shutdown complete");
    }
}
```

**Lines of code:** ~20 lines

---

### Step 4: Implement McpServerImpl (Message Loop)

**Purpose:** Orchestration layer that glues transport + codec + handler

```java
@Service
public class McpServerImpl implements McpServer {

    private final ServerStdioTransport transport;
    private final JsonRpcCodec codec;
    private final ServerMessageHandler messageHandler;

    private volatile boolean running;

    public McpServerImpl(
        ServerStdioTransport transport,
        JsonRpcCodec codec,
        ServerMessageHandler messageHandler
    ) {
        this.transport = transport;
        this.codec = codec;
        this.messageHandler = messageHandler;
    }

    @Override
    public void start() {
        running = true;
        log.info("MCP Server listening on stdin...");

        // Simple message loop - just glue code
        while (running) {
            try {
                // 1. Receive raw JSON from stdin
                String raw = transport.receive();

                // 2. Decode to domain model
                McpMessage message = codec.decode(raw);

                // 3. If it's a request, handle it
                if (message instanceof McpRequest request) {
                    McpMessage response = messageHandler.handle(request);

                    // 4. Encode and send response
                    String encoded = codec.encode(response);
                    transport.send(encoded);
                }

            } catch (TransportException e) {
                // EOF on stdin = client disconnected
                log.info("Client disconnected, stopping server");
                running = false;
            } catch (Exception e) {
                log.error("Error in message loop", e);
                // Continue serving (resilient to bad requests)
            }
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
```

---

### Step 5: Implement McpClientDemo (Client Main + Demo)

**Purpose:** Spawns server, sends demo requests, displays responses

```java
package ai.mcp.helloworld.client;

@Slf4j
public class McpClientDemo {

    public static void main(String[] args) throws Exception {
        log.info("[Client] Starting MCP Client Demo...");

        // Auto-spawn server
        List<String> serverCommand = List.of(
            "java", "-jar", "target/mcp-hello-world-server.jar"
        );

        try (StdioTransport transport = new StdioTransport(serverCommand)) {
            JsonRpcCodec codec = new JsonRpcCodecImpl();
            McpClient client = new McpClientImpl(transport, codec);

            runDemo(client);
        }

        log.info("[Client] Demo complete");
    }

    private static void runDemo(McpClient client) {
        System.out.println("\n=== MCP Client/Server Demo ===\n");

        // Demo 1: List tools
        System.out.println("1. Listing available tools:");
        var tools = client.listTools();
        System.out.println("   → " + tools + "\n");

        // Demo 2: Add
        System.out.println("2. Calling add(5, 3):");
        var result = client.callTool("add", Map.of("a", 5, "b", 3));
        System.out.println("   → " + result + "\n");

        // Demo 3: Multiply
        System.out.println("3. Calling multiply(4, 7):");
        result = client.callTool("multiply", Map.of("a", 4, "b", 7));
        System.out.println("   → " + result + "\n");

        // Demo 4: Error handling
        System.out.println("4. Testing error: unknown tool");
        try {
            client.callTool("unknown", Map.of());
        } catch (Exception e) {
            System.out.println("   → Error (expected): " + e.getMessage());
        }
    }
}
```

**Lines of code:** ~40 lines

---

### Step 6: Implement McpClientImpl (Synchronous Client)

**Purpose:** Simple request/response wrapper with convenience methods

```java
@Component
public class McpClientImpl implements McpClient {

    private final StdioTransport transport;
    private final JsonRpcCodec codec;

    public McpClientImpl(StdioTransport transport, JsonRpcCodec codec) {
        this.transport = transport;
        this.codec = codec;
    }

    /**
     * Send request and wait for response (synchronous, simple).
     */
    public McpMessage sendRequest(McpRequest request) {
        // Encode request
        String encoded = codec.encode(request);

        // Send via transport
        transport.send(encoded);

        // Wait for response (blocking)
        String raw = transport.receive();

        // Decode and return
        return codec.decode(raw);
    }

    /**
     * Convenience: List all available tools.
     */
    public ToolListResponse listTools() {
        McpRequest request = ToolListRequest.create("1");
        return (ToolListResponse) sendRequest(request);
    }

    /**
     * Convenience: Invoke a tool by name.
     */
    public ToolInvocationResponse callTool(String toolName, Map<String, Object> args) {
        McpRequest request = ToolInvocationRequest.create("2", toolName, args);
        return (ToolInvocationResponse) sendRequest(request);
    }
}
```

---

### Step 7: Configure Maven to Build Two JARs

**Purpose:** Build separate executable JARs for server and client

```xml
<!-- Build TWO executable JARs -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>server</id>
                    <goals><goal>repackage</goal></goals>
                    <configuration>
                        <classifier>server</classifier>
                        <mainClass>ai.mcp.helloworld.server.McpServerApplication</mainClass>
                    </configuration>
                </execution>
                <execution>
                    <id>client</id>
                    <goals><goal>repackage</goal></goals>
                    <configuration>
                        <classifier>client</classifier>
                        <mainClass>ai.mcp.helloworld.client.McpClientDemo</mainClass>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

## Code Summary

### New Code Required

1. ✅ **`ServerStdioTransport`** - System.in/out wrapper (~30 lines)
2. ✅ **`ServerMessageHandler`** - Request dispatcher (~60 lines)
3. ✅ **`McpServerApplication`** - Server main() (~20 lines)
4. ✅ **`McpServerImpl`** - Message loop (~50 lines)
5. ✅ **`McpClientDemo`** - Client main() + demo (~40 lines)
6. ✅ **`McpClientImpl`** - Synchronous client (~30 lines)

**Total new code: ~230 lines**

### Reused Code

- ✅ Existing `StdioTransport` (client-side, already works!)
- ✅ Existing `JsonRpcCodec`
- ✅ Existing domain layer (protocol models, tools, etc.)

**Total reused code: ~1000+ lines**

---

## Running the Demo

```bash
# 1. Build both JARs
mvn clean package

# 2. Run client (auto-spawns server)
java -jar target/mcp-hello-world-client.jar

# Expected Output:
=== MCP Client/Server Demo ===

[Server] Starting MCP Server...
[Server] Listening on stdin...

1. Listing available tools:
[Server] Handling request: tools/list
[Server] Returning 3 tools
   → ToolListResponse(tools=[add, multiply, random])

2. Calling add(5, 3):
[Server] Handling request: tools/call
[Server] Executing tool: add with args: {a=5, b=3}
   → ToolInvocationResponse(result=[TextContent(8)])

3. Calling multiply(4, 7):
[Server] Handling request: tools/call
[Server] Executing tool: multiply with args: {a=4, b=7}
   → ToolInvocationResponse(result=[TextContent(28)])

4. Testing error: unknown tool
[Server] Handling request: tools/call
   → Error (expected): Tool 'unknown' not found
```

---

## Why This Approach Maximizes Learning

1. ✅ **See both sides** - Client logs + Server logs interleaved in same console
2. ✅ **Real MCP protocol** - Actual stdio communication, real JSON-RPC messages
3. ✅ **Easy to debug** - Can manually run server and inspect stdin/stdout
4. ✅ **Production-like** - This is how Claude Desktop talks to MCP servers
5. ✅ **Minimal code** - Only ~230 new lines, rest is reuse
6. ✅ **Future-ready** - Easy to add REST API (just another consumer of `ServerMessageHandler`)
7. ✅ **No mode switching** - Both client and server run automatically
8. ✅ **Clear separation** - Server code vs client code is obvious

---

## Future Extensibility

### Adding REST API (Later)

```java
// NEW: REST Controller (no changes to existing code needed)
@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final ServerMessageHandler messageHandler;  // Reuse!

    @PostMapping("/list")
    public ToolListResponse listTools() {
        McpRequest request = ToolListRequest.create(UUID.randomUUID().toString());
        return (ToolListResponse) messageHandler.handle(request);
    }

    @PostMapping("/call")
    public ToolInvocationResponse callTool(@RequestBody ToolCallRequest dto) {
        McpRequest request = ToolInvocationRequest.create(
            UUID.randomUUID().toString(),
            dto.toolName(),
            dto.arguments()
        );
        return (ToolInvocationResponse) messageHandler.handle(request);
    }
}
```

**Key insight:** `ServerMessageHandler` is **transport-agnostic**. Works with stdio, HTTP, WebSocket, etc.

---

### Adding Interactive Chatbot Client (Later)

```java
// NEW: Interactive CLI (no changes to server needed)
@Component
public class ChatbotClient {

    private final McpClient client;  // Reuse!
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("MCP Chatbot - Type 'help' for commands");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("tools")) {
                var tools = client.listTools();
                System.out.println("Available: " + tools);
            } else if (input.startsWith("add ")) {
                // Parse "add 5 3"
                String[] parts = input.split(" ");
                var result = client.callTool("add",
                    Map.of("a", Integer.parseInt(parts[1]),
                           "b", Integer.parseInt(parts[2])));
                System.out.println("Result: " + result);
            }
        }
    }
}
```

---

## Context (Auto-generated)

**Current Project State:**
- Branch: feature/poc-01-hello-world
- Last commit: c99ac2d mcp server work in progress

**Related Files:**
- `StdioTransport.java` (existing, client-side)
- `JsonRpcCodec.java` (existing, shared)
- Domain models (existing, shared)
- Tools (AddTool, MultiplyTool, RandomTool - existing)

**Next Steps:**
1. Create `ServerStdioTransport.java` (new)
2. Create `ServerMessageHandler.java` (new)
3. Create `McpServerApplication.java` (new)
4. Create `McpClientDemo.java` (new)
5. Configure Maven multi-execution build
6. Test end-to-end

**Estimated Time:** 2-3 hours for full implementation + testing
