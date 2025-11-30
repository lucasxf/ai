package ai.mcp.helloworld.domain.protocol.request;

/**
 * Request to list all available MCP tools.
 * <p>
 * JSON-RPC format:
 * {
 * "jsonrpc": "2.0",
 * "id": "1",
 * "method": "tools/list",
 * "params": {}
 * }
 * <p>
 * This request has no parameters.
 *
 * @author lucas
 * @date 04/11/2025 20:39
 */
public record ToolListRequest(
        String id,
        String jsonRpc,
        String method,
        String[] params) implements McpRequest {

    private static final String methodName = "tools/list";

    public static ToolListRequest create(String id) {
        return new ToolListRequest(id, JSON_RPC_VERSION, methodName, null);
    }

    public ToolListRequest {
        if (!JSON_RPC_VERSION.equals(jsonRpc)) {
            throw new IllegalArgumentException("jsonRpc must be \"2.0\"");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank");
        }
        if (methodName.equalsIgnoreCase(method)) {
            throw new IllegalArgumentException("Invalid request method");
        }
    }

}
