package ai.mcp.helloworld.domain.protocol;

import ai.mcp.helloworld.domain.tool.ContentBlock;

import java.util.List;

/**
 * @author lucas
 * @date 04/11/2025 20:46
 */
public record ToolInvocationResponse(
        String id,
        String jsonrpc,
        ToolCallResult result) implements McpResponse {

    record ToolCallResult(List<ContentBlock> content) { }

}
