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

    String JSON_RPC_VERSION = "2.0";

    /**
     * Unique identifier for request-response correlation.
     * Can be string or number in JSON-RPC spec, we use String for simplicity.
     *
     * @return this message ID.
     */
    String id();

    /**
     * JSON-RPC protocol version (always "2.0").
     *
     * @return "2.0"
     */
    String jsonRpc();

}
