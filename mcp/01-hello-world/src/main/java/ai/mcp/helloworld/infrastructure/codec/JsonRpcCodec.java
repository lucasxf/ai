package ai.mcp.helloworld.infrastructure.codec;

import ai.mcp.helloworld.domain.protocol.McpMessage;
import ai.mcp.helloworld.domain.protocol.McpRequest;

/**
 * @author lucas
 * @date 04/11/2025 20:55
 */
public interface JsonRpcCodec {

    /**
     * Encodes an MCP request to JSON-RPC 2.0 format.
     *
     * @param request the MCP request (domain object)
     * @return JSON-RPC 2.0 string
     * @throws CodecException if JSON serialization fails
     */
    String encode(McpRequest request);

    McpMessage decode(String json);

}
