package ai.mcp.helloworld.domain.protocol.request;

/**
 * @author lucas
 * @date 04/11/2025 20:45
 */
public record ToolInvocationRequest(String message) implements McpRequest {
}
