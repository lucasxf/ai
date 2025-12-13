package ai.mcp.helloworld.integration;

import ai.mcp.helloworld.application.client.McpClient;
import ai.mcp.helloworld.application.server.McpServer;
import ai.mcp.helloworld.domain.tool.ToolRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lucas
 * @date 04/11/2025 21:11
 */
public class AbstractIntegrationTest {


    @Autowired
    protected McpClient client;

    @Autowired
    protected McpServer server;

    @Autowired
    protected ToolRegistry toolRegistry;

    @BeforeEach
    void setup() {
        // Common test setup
    }

    @AfterEach
    void cleanup() {
        // Cleanup resources
    }

}
