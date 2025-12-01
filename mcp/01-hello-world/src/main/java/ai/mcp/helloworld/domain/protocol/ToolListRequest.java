package ai.mcp.helloworld.domain.protocol;

import ai.mcp.helloworld.exception.InvalidToolParametersException;

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
            throw new InvalidToolParametersException("jsonRpc must be \"2.0\"");
        }
        if (id == null || id.isBlank()) {
            throw new InvalidToolParametersException("Id must not be null or blank");
        }
        if (!methodName.equalsIgnoreCase(method)) {
            throw new InvalidToolParametersException("Invalid request method");
        }
    }

}
