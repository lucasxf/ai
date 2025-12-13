🎯 MCP Protocol Overview (JSON-RPC 2.0)
Before coding, let's understand what we're building. MCP uses JSON-RPC 2.0 over stdio:
Request Format (Client → Server)
{
  "jsonrpc": "2.0",
  "id": "1",
  "method": "tools/list",
  "params": {}
}
Response Format (Server → Client)
{
  "jsonrpc": "2.0",
  "id": "1",
  "result": {
    "tools": [
      {
        "name": "add",
        "description": "Add two numbers",
        "inputSchema": { ... }
      }
    ]
  }
}
Error Response
{
  "jsonrpc": "2.0",
  "id": "1",
  "error": {
    "code": -32601,
    "message": "Method not found"
  }
}
---
📐 Protocol Model Design
1. Base Message Interface
File: McpMessage.java
Design rationale:
- Represents JSON-RPC 2.0 envelope (common fields)
- Sealed interface ensures type safety
- All MCP messages are either requests or responses
package ai.mcp.helloworld.domain.protocol;
/**
 * Base sealed interface for all MCP protocol messages (JSON-RPC 2.0).
 * <p>
 * Every MCP message must include:
 * - jsonrpc version ("2.0")
 * - id (for request-response correlation)
 * <p>
 * Permits only McpRequest and McpResponse subtypes.
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public sealed interface McpMessage
    permits McpRequest, McpResponse {
    /**
     * JSON-RPC protocol version (always "2.0").
     */
    String jsonrpc();
    /**
     * Unique identifier for request-response correlation.
     * Can be string or number in JSON-RPC spec, we use String for simplicity.
     */
    String id();
}
---
2. Request Models
File: McpRequest.java
package ai.mcp.helloworld.domain.protocol.request;
import ai.mcp.helloworld.domain.protocol.McpMessage;
/**
 * Sealed interface for all MCP requests (client → server).
 * <p>
 * JSON-RPC requests include:
 * - jsonrpc: "2.0"
 * - id: request identifier
 * - method: RPC method name (e.g., "tools/list", "tools/call")
 * - params: method parameters (optional)
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public sealed interface McpRequest extends McpMessage
    permits ToolListRequest, ToolInvocationRequest {
    /**
     * JSON-RPC method name.
     * MCP standard methods:
     * - "tools/list" - List available tools
     * - "tools/call" - Invoke a specific tool
     */
    String method();
}
File: ToolListRequest.java
package ai.mcp.helloworld.domain.protocol.request;
/**
 * Request to list all available MCP tools.
 * <p>
 * JSON-RPC format:
 * {
 *   "jsonrpc": "2.0",
 *   "id": "1",
 *   "method": "tools/list",
 *   "params": {}
 * }
 * <p>
 * This request has no parameters.
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public record ToolListRequest(
    String jsonrpc,
    String id,
    String method
) implements McpRequest {
    /**
     * Factory method for creating tool list requests.
     */
    public static ToolListRequest create(String id) {
        return new ToolListRequest("2.0", id, "tools/list");
    }
    /**
     * Compact constructor with validation.
     */
    public ToolListRequest {
        if (jsonrpc == null || !jsonrpc.equals("2.0")) {
            throw new IllegalArgumentException("jsonrpc must be '2.0'");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (method == null || !method.equals("tools/list")) {
            throw new IllegalArgumentException("method must be 'tools/list'");
        }
    }
}
File: ToolInvocationRequest.java
package ai.mcp.helloworld.domain.protocol.request;
import java.util.Map;
/**
 * Request to invoke a specific MCP tool with parameters.
 * <p>
 * JSON-RPC format:
 * {
 *   "jsonrpc": "2.0",
 *   "id": "2",
 *   "method": "tools/call",
 *   "params": {
 *     "name": "add",
 *     "arguments": {
 *       "a": 5,
 *       "b": 3
 *     }
 *   }
 * }
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public record ToolInvocationRequest(
    String jsonrpc,
    String id,
    String method,
    ToolCallParams params
) implements McpRequest {
    /**
     * Nested record for tool call parameters.
     */
    public record ToolCallParams(
        String name,
        Map<String, Object> arguments
    ) {
        public ToolCallParams {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Tool name cannot be null or blank");
            }
            if (arguments == null) {
                throw new IllegalArgumentException("Tool arguments cannot be null (use empty map)");
            }
        }
    }
    /**
     * Factory method for creating tool invocation requests.
     */
    public static ToolInvocationRequest create(String id, String toolName, Map<String, Object> arguments) {
        var params = new ToolCallParams(toolName, arguments);
        return new ToolInvocationRequest("2.0", id, "tools/call", params);
    }
    /**
     * Compact constructor with validation.
     */
    public ToolInvocationRequest {
        if (jsonrpc == null || !jsonrpc.equals("2.0")) {
            throw new IllegalArgumentException("jsonrpc must be '2.0'");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (method == null || !method.equals("tools/call")) {
            throw new IllegalArgumentException("method must be 'tools/call'");
        }
        if (params == null) {
            throw new IllegalArgumentException("params cannot be null");
        }
    }
}
---
3. Response Models
File: McpResponse.java
package ai.mcp.helloworld.domain.protocol.response;
import ai.mcp.helloworld.domain.protocol.McpMessage;
/**
 * Sealed interface for all MCP responses (server → client).
 * <p>
 * JSON-RPC responses include:
 * - jsonrpc: "2.0"
 * - id: request identifier (matches request)
 * - result: success result (present if no error)
 * - error: error object (present if failed)
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public sealed interface McpResponse extends McpMessage
    permits ToolListResponse, ToolInvocationResponse {
    /**
     * Indicates if this response represents an error.
     */
    boolean isError();
}
File: ToolListResponse.java
package ai.mcp.helloworld.domain.protocol.response;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import java.util.List;
/**
 * Response containing list of available MCP tools.
 * <p>
 * JSON-RPC format:
 * {
 *   "jsonrpc": "2.0",
 *   "id": "1",
 *   "result": {
 *     "tools": [
 *       {
 *         "name": "add",
 *         "description": "Add two numbers",
 *         "inputSchema": { ... }
 *       }
 *     ]
 *   }
 * }
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public record ToolListResponse(
    String jsonrpc,
    String id,
    ToolListResult result
) implements McpResponse {
    /**
     * Nested record for tool list result.
     */
    public record ToolListResult(
        List<ToolDefinition> tools
    ) {
        public ToolListResult {
            if (tools == null) {
                throw new IllegalArgumentException("Tools list cannot be null (use empty list)");
            }
        }
    }
    /**
     * Factory method for creating successful tool list responses.
     */
    public static ToolListResponse success(String id, List<ToolDefinition> tools) {
        var result = new ToolListResult(tools);
        return new ToolListResponse("2.0", id, result);
    }
    @Override
    public boolean isError() {
        return false;
    }
    /**
     * Compact constructor with validation.
     */
    public ToolListResponse {
        if (jsonrpc == null || !jsonrpc.equals("2.0")) {
            throw new IllegalArgumentException("jsonrpc must be '2.0'");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (result == null) {
            throw new IllegalArgumentException("result cannot be null");
        }
    }
}
File: ToolInvocationResponse.java
package ai.mcp.helloworld.domain.protocol.response;
import java.util.List;
/**
 * Response containing result of tool invocation.
 * <p>
 * JSON-RPC format (success):
 * {
 *   "jsonrpc": "2.0",
 *   "id": "2",
 *   "result": {
 *     "content": [
 *       {
 *         "type": "text",
 *         "text": "8"
 *       }
 *     ]
 *   }
 * }
 * <p>
 * MCP tools return content blocks (text, images, etc.).
 * For this POC, we only support text content.
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
public record ToolInvocationResponse(
    String jsonrpc,
    String id,
    ToolInvocationResult result
) implements McpResponse {
    /**
     * Nested record for tool invocation result.
     */
    public record ToolInvocationResult(
        List<ContentBlock> content
    ) {
        public ToolInvocationResult {
            if (content == null || content.isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }
        }
    }
    /**
     * Content block (text, image, etc.).
     * For POC 1, we only support text.
     */
    public record ContentBlock(
        String type,
        String text
    ) {
        public ContentBlock {
            if (type == null || !type.equals("text")) {
                throw new IllegalArgumentException("Only 'text' type supported in POC 1");
            }
            if (text == null) {
                throw new IllegalArgumentException("Text content cannot be null");
            }
        }
        public static ContentBlock text(String text) {
            return new ContentBlock("text", text);
        }
    }
    /**
     * Factory method for creating successful tool invocation responses.
     */
    public static ToolInvocationResponse success(String id, String textResult) {
        var content = List.of(ContentBlock.text(textResult));
        var result = new ToolInvocationResult(content);
        return new ToolInvocationResponse("2.0", id, result);
    }
    @Override
    public boolean isError() {
        return false;
    }
    /**
     * Compact constructor with validation.
     */
    public ToolInvocationResponse {
        if (jsonrpc == null || !jsonrpc.equals("2.0")) {
            throw new IllegalArgumentException("jsonrpc must be '2.0'");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (result == null) {
            throw new IllegalArgumentException("result cannot be null");
        }
    }
}
---
🎯 Implementation Summary
Key Design Decisions (Aligned with wine-reviewer)
1. Java 21 Records ✅
  - Immutable by default
  - Compact constructors for validation
  - Pattern matching ready
2. Sealed Interfaces ✅
  - Type-safe protocol hierarchy
  - Compiler ensures all cases covered in switch
3. Validation in Constructors ✅
  - Fail-fast approach
  - Clear error messages
  - Follows wine-reviewer's validation pattern
4. Factory Methods ✅
  - create() for convenience
  - success() for responses
  - Hides complexity from callers
5. Nested Records ✅
  - Groups related data (params, results)
  - Follows JSON structure
  - Type-safe, immutable
What You Should Implement
Replace the skeleton files with these implementations. The models are:
- ✅ JSON-RPC 2.0 compliant
- ✅ Immutable (records)
- ✅ Validated (compact constructors)
- ✅ Type-safe (sealed interfaces)
- ✅ Well-documented (Javadoc with examples)
Next After Implementation
Once you implement these protocol models, we'll move to Priority 3: Tool abstraction (Tool, ToolDefinition,ToolRegistry) which these responses
reference.
Ready to implement? Let me know when you're done and I'll review! 🚀