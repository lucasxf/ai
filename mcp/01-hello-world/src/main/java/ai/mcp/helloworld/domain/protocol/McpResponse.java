package ai.mcp.helloworld.domain.protocol;

/**
 * Sealed interface for all MCP responses (server → client).
 * <p>
 * JSON-RPC responses include:
 * - jsonrpc: "2.0"
 * - id: request identifier (matches request)
 * - result: success result (present if no error)
 * - error: error object (present if failed)
 *
 * @author lucas
 * @date 04/11/2025 20:39
 */
public sealed interface McpResponse extends McpMessage
        permits ToolInvocationResponse, ToolListResponse {

    record ErrorResponse(int code, String message) { }

}
