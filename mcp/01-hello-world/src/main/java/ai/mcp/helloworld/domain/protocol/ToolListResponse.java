package ai.mcp.helloworld.domain.protocol;

import ai.mcp.helloworld.domain.tool.ToolDefinition;

import java.util.List;

/**
 * Response message for tools/list JSON-RPC request.
 * <p>
 * Contains list of tool definitions (name, description, schema) that the server provides.
 * Clients use this to discover available tools before invoking them.
 * <p>
 * <strong>JSON Structure:</strong>
 * <pre>{@code
 * {
 *   "id": "1",
 *   "jsonrpc": "2.0",
 *   "message": "Tool list retrieved successfully",
 *   "result": {
 *     "tools": [
 *       {
 *         "name": "add",
 *         "description": "Adds two numbers",
 *         "schema": "{\"type\":\"object\",\"properties\":{\"a\":{\"type\":\"number\"},\"b\":{\"type\":\"number\"}}}"
 *       }
 *     ]
 *   }
 * }
 * }</pre>
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025 20:41
 */
public record ToolListResponse(
        String id,
        String jsonrpc,
        String message,
        ToolListResult result) implements McpResponse {

    /**
     * Result payload containing list of tool definitions.
     *
     * @param tools list of tool definitions (never null, may be empty)
     */
    public record ToolListResult(List<ToolDefinition> tools) { }

}
