package ai.mcp.helloworld.domain.protocol.response;

import ai.mcp.helloworld.domain.protocol.McpMessage;

/**
 * @author lucas
 * @date 04/11/2025 20:39
 */
public sealed interface McpResponse extends McpMessage
        permits ToolInvocationResponse, ToolListResponse {
}
