package ai.mcp.helloworld.domain.protocol;

/**
 *
 * Base sealed interface for all MCP protocol messages (JSON-RPC 2.0).
 * <p>
 * Every MCP message must include:
 * - jsonrpc version ("2.0")
 * - id (for request-response correlation)
 * <p>
 * Permits only McpRequest and McpResponse subtypes.
 *
 * @author lucas
 * @date 04/11/2025 20:37
 */
public sealed interface McpMessage permits McpRequest, McpResponse {

    /**
     * Unique identifier for request-response correlation.
     *
     * @return this message ID.
     */
    Object id();

    String jsonRpc();

}
