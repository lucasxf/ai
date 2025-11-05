package ai.mcp.helloworld.domain.protocol.request;

import ai.mcp.helloworld.domain.protocol.McpMessage;

/**
 * @author lucas
 * @date 04/11/2025 20:38
 */
public sealed interface McpRequest extends McpMessage
        permits ToolInvocationRequest, ToolListRequest {
}
