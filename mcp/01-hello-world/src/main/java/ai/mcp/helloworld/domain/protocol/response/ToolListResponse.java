package ai.mcp.helloworld.domain.protocol.response;

import ai.mcp.helloworld.domain.tool.Tool;

import java.util.List;

/**
 * @author lucas
 * @date 04/11/2025 20:41
 */
public record ToolListResponse(
        String message,
        List<Tool> tools) implements McpResponse {
}
