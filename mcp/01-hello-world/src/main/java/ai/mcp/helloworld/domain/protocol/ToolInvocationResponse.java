package ai.mcp.helloworld.domain.protocol;

/**
 * @author lucas
 * @date 04/11/2025 20:46
 */
public record ToolInvocationResponse(
        String id,
        String jsonRpc) implements McpResponse {
}
