package ai.mcp.helloworld.server.impl;

import ai.mcp.helloworld.server.McpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Spring Boot CommandLineRunner that starts the MCP server on application startup.
 * <p>
 * This runner is executed after the Spring context is fully initialized, ensuring
 * all dependencies (Transport, Codec, ServerMessageHandler) are properly wired.
 * <p>
 * <strong>Execution Flow:</strong>
 * <ol>
 *     <li>Spring Boot application starts</li>
 *     <li>All beans are created and injected</li>
 *     <li>CommandLineRunner.run() is invoked</li>
 *     <li>McpServer.start() begins listening on stdin</li>
 *     <li>Server blocks waiting for JSON-RPC requests</li>
 * </ol>
 * <p>
 * <strong>Shutdown Behavior:</strong>
 * When the application receives a shutdown signal (SIGTERM, Ctrl+C), Spring Boot
 * will trigger context destruction, which will call the {@code @PreDestroy} method
 * on McpServerImpl if configured.
 * <p>
 * <strong>Thread Safety:</strong>
 * This runner starts the server in the main thread, which blocks on stdio reading.
 * To run the server in the background, consider using {@code @Async} or a separate
 * thread executor.
 *
 * @author Lucas Xavier Ferreira
 * @date 20/12/2025
 * @see McpServer
 * @see McpServerImpl
 */
@Slf4j
@Component
public class McpServerRunner implements CommandLineRunner {

    private final McpServer mcpServer;

    public McpServerRunner(McpServer mcpServer) {
        this.mcpServer = mcpServer;
    }

    @Override
    public void run(String... args) {
        log.info("Starting MCP Server via CommandLineRunner...");
        mcpServer.start();
    }

}
