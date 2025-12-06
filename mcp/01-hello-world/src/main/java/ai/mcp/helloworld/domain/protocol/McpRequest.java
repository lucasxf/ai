package ai.mcp.helloworld.domain.protocol;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

/**
 * Sealed interface for all MCP requests (client → server).
 * <p>
 * JSON-RPC requests include:
 * - jsonrpc: "2.0"
 * - id: request identifier
 * - method: RPC method name (e.g., "tools/list", "tools/call")
 * - params: method parameters (optional)
 *
 * @author lucas
 * @date 04/11/2025 20:38
 */
public sealed interface McpRequest extends McpMessage
                permits ToolInvocationRequest, ToolListRequest {

        String method();

}
