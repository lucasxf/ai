package ai.mcp.helloworld.domain.protocol.response;

import ai.mcp.helloworld.domain.tool.Tool;

/**
 * @author lucas
 * @date 04/11/2025 20:46
 */
public record ToolInvocationResponse(
        String message,
        Tool tool) implements McpResponse {
}
