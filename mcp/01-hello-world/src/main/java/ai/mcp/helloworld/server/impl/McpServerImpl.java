package ai.mcp.helloworld.server.impl;

import ai.mcp.helloworld.domain.protocol.McpMessage;
import ai.mcp.helloworld.domain.protocol.McpRequest;
import ai.mcp.helloworld.exception.TransportException;
import ai.mcp.helloworld.infrastructure.codec.CodecException;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import ai.mcp.helloworld.server.McpServer;
import ai.mcp.helloworld.server.ServerMessageHandler;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author lucas
 * @date 14/12/2025 12:00
 */
@Slf4j
@Service
public class McpServerImpl implements McpServer {

    private final Transport transport;
    private final JsonRpcCodec codec;
    private final ServerMessageHandler serverMessageHandler;

    private boolean isRunning;

    public McpServerImpl(
            @Qualifier("serverStdioTransport") Transport transport,
            JsonRpcCodec codec,
            ServerMessageHandler serverMessageHandler) {
        this.transport = transport;
        this.codec = codec;
        this.serverMessageHandler = serverMessageHandler;
    }

    @Override
    public void start() {
        isRunning = true;
        log.info("MCP Server started");
        while (isRunning) {
            try {
                final String receivedMessage = transport.receive();
                log.info("Received message: {}", receivedMessage);
                final McpMessage decodedMessage = codec.decode(receivedMessage);
                if (decodedMessage instanceof McpRequest request) {
                    handleRequest(request);
                } else {
                    log.warn("Received invalid message type: {}", receivedMessage);
                }
            } catch (TransportException e) {
                log.error("Transport error: {}", e.getMessage(), e);
                stop();
            } catch (CodecException e) {
                log.error("Codec error: {}", e.getMessage(), e);
                // Continue processing - codec errors are recoverable
            } catch (Exception e) {
                log.error("Unexpected error in server loop: {}", e.getMessage(), e);
                // Continue processing - try to handle next message
            }
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        transport.close();
        log.info("MCP Server stopped");
    }

    /**
     * Spring lifecycle callback for graceful shutdown.
     * <p>
     * This method is invoked when the Spring context is destroyed (e.g., application shutdown,
     * SIGTERM, Ctrl+C). It ensures the server stops cleanly and releases resources.
     */
    @PreDestroy
    public void destroy() {
        log.info("Spring context shutting down - stopping MCP Server...");
        if (isRunning) {
            stop();
        }
    }

    @Override
    public void handleRequest(McpRequest mcpRequest) {
        log.info("Handling request: {}", mcpRequest);
        final McpMessage mcpMessage = serverMessageHandler.handleMessage(mcpRequest);
        log.info("Server Response: {}", mcpMessage);
        final String encode = codec.encode(mcpMessage);
        transport.send(encode);
        log.info("Sent response: {}", encode);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
