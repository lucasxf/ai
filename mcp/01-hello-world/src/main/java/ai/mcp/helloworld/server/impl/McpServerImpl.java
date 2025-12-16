package ai.mcp.helloworld.server.impl;

import ai.mcp.helloworld.server.McpServer;
import ai.mcp.helloworld.domain.protocol.McpMessage;
import ai.mcp.helloworld.domain.protocol.McpRequest;
import ai.mcp.helloworld.domain.tool.ToolRegistry;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import org.springframework.stereotype.Service;

/**
 * @author lucas
 * @date 14/12/2025 12:00
 */
@Service
public class McpServerImpl implements McpServer {

    private final Transport transport;
    private final JsonRpcCodec codec;
    private final ToolRegistry toolRegistry;

    private boolean isRunning;

    public McpServerImpl(Transport transport, JsonRpcCodec codec, ToolRegistry toolRegistry) {
        this.transport = transport;
        this.codec = codec;
        this.toolRegistry = toolRegistry;
    }

    @Override
    public void start() {
        while (isRunning()) {
            final String receivedMessage = transport.receive();
            final McpMessage decodedMessage = codec.decode(receivedMessage);
        }
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void handleRequest(McpRequest mcpRequest) {

    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
