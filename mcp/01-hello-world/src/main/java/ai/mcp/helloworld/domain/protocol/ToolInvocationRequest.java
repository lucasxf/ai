package ai.mcp.helloworld.domain.protocol;

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
 * @author lucas
 * @date 04/11/2025 20:45
 */
public record ToolInvocationRequest(
        String id,
        String jsonRpc,
        String method,
        ToolCallParams params) implements McpRequest {

    /**
     * Factory method for creating tool invocation requests.
     */
    public static ToolInvocationRequest create(
            String id,
            String toolName,
            Map<String, Object> arguments) {
        var params = new ToolCallParams(toolName, arguments);
        return new ToolInvocationRequest(id, JSON_RPC_VERSION, "tools/call", params);
    }

    /**
     * Compact constructor with validation.
     */
    public ToolInvocationRequest {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("Method cannot be null or blank");
        }
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("Params cannot be null or empty");
        }
    }

    /**
     * Nested record for tool call parameters.
     */
    public record ToolCallParams(
            String name,
            Map<String, Object> arguments) {

        /**
         * Compact constructor with validation.
         */
        public ToolCallParams {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Invalid tool name");
            }
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Invalid tool arguments");
            }
        }

        boolean isEmpty() {
            return arguments == null || arguments.isEmpty();
        }

    }

}
