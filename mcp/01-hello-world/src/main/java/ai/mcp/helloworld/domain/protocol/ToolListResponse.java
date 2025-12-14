package ai.mcp.helloworld.domain.protocol;

import ai.mcp.helloworld.domain.tool.Tool;

import java.util.List;

/**
 * @author lucas
 * @date 04/11/2025 20:41
 */
public record ToolListResponse(
        String id,
        String jsonrpc,
        String message,
        ToolListResult result) implements McpResponse {

    record ToolListResult(List<Tool> tools) { }

}
