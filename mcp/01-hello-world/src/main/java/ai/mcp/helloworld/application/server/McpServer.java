package ai.mcp.helloworld.application.server;

import ai.mcp.helloworld.domain.protocol.McpRequest;

/**
 * @author lucas
 * @date 04/11/2025 20:58
 */
public interface McpServer {

    void start();

    void stop();

    void handleRequest(McpRequest mcpRequest);

    boolean isRunning();

}
