package ai.mcp.helloworld.domain.protocol;

/**
 * @author lucas
 * @date 06/12/2025
 */
public record McpErrorResponse(Object id, String jsonrpc, McpError error) implements McpResponse { }
