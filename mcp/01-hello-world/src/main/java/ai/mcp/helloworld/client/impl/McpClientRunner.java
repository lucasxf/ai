package ai.mcp.helloworld.client.impl;

import ai.mcp.helloworld.client.McpClient;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import ai.mcp.helloworld.infrastructure.transport.impl.StdioTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Spring Boot CommandLineRunner for MCP Client demonstration.
 * <p>
 * This runner is activated only when the {@code mcp.client.demo.enabled=true} property is set,
 * preventing it from running alongside the server in normal operation.
 * <p>
 * <strong>Usage:</strong>
 * <pre>{@code
 * # Package the application
 * mvn clean package -DskipTests
 *
 * # Run the client demo (spawns server as subprocess)
 * java -jar target/mcp-hello-world-0.1.0-SNAPSHOT.jar --mcp.client.demo.enabled=true
 * }</pre>
 * <p>
 * <strong>Architecture:</strong>
 * Unlike the server (which uses Spring-managed Transport reading from System.in),
 * the client creates its own StdioTransport that spawns the server as a subprocess.
 * <p>
 * <strong>Workflow:</strong>
 * <ol>
 *     <li>Spring Boot starts and initializes beans</li>
 *     <li>Client runner spawns server subprocess via StdioTransport</li>
 *     <li>Client lists tools and invokes them</li>
 *     <li>Client closes transport (terminates server process)</li>
 *     <li>Application exits</li>
 * </ol>
 *
 * @author Lucas Xavier Ferreira
 * @date 14/01/2026
 * @see McpClient
 * @see McpClientImpl
 * @see StdioTransport
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "mcp.client.demo.enabled", havingValue = "true")
public class McpClientRunner implements CommandLineRunner {

    private final JsonRpcCodec codec;

    public McpClientRunner(JsonRpcCodec codec) {
        this.codec = codec;
    }

    @Override
    public void run(String... args) {
        log.info("=== MCP Client Demo Starting ===");

        // Determine the server JAR path (same JAR we're running from)
        String jarPath = getJarPath();
        List<String> serverCommand = List.of("java", "--enable-preview", "-jar", jarPath);

        log.info("Spawning MCP server subprocess: {}", serverCommand);

        // Create transport (spawns server subprocess)
        Transport transport = new StdioTransport(serverCommand);

        // Create MCP client
        McpClient client = new McpClientImpl(transport, codec);

        try {
            demonstrateToolListing(client);
            demonstrateToolInvocation(client);
        } finally {
            client.close();
            log.info("=== MCP Client Demo Completed ===");
        }
    }

    /**
     * Demonstrates listing all available tools from the server.
     */
    private void demonstrateToolListing(McpClient client) {
        log.info("--- Demonstrating Tool Listing ---");

        List<ToolDefinition> tools = client.listTools();

        log.info("Available tools ({}): ", tools.size());
        tools.forEach(tool -> {
            log.info("  • {} - {}", tool.name(), tool.description());
        });
    }

    /**
     * Demonstrates invoking a specific tool with arguments.
     */
    private void demonstrateToolInvocation(McpClient client) {
        log.info("--- Demonstrating Tool Invocation ---");

        // Test 1: Add two numbers
        log.info("Test 1: Calling 'add' tool");
        List<ContentBlock> addResult = client.callTool("add", Map.of("a", 5, "b", 3));
        log.info("Result: {}", addResult);

        // Test 2: Multiply two numbers
        log.info("Test 2: Calling 'multiply' tool");
        List<ContentBlock> multiplyResult = client.callTool("multiply", Map.of("a", 4, "b", 7));
        log.info("Result: {}", multiplyResult);

        // Test 3: Generate random number
        log.info("Test 3: Calling 'random' tool");
        List<ContentBlock> randomResult = client.callTool("random", Map.of("min", 1, "max", 100));
        log.info("Result: {}", randomResult);
    }

    /**
     * Determines the JAR path of the currently running application.
     * Falls back to a relative path if detection fails.
     */
    private String getJarPath() {
        try {
            String path = McpClientRunner.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            // Convert Windows path if needed (remove leading slash)
            if (path.startsWith("/") && path.contains(":")) {
                path = path.substring(1);
            }

            return path;
        } catch (Exception e) {
            log.warn("Could not detect JAR path, using relative path", e);
            return "target/mcp-hello-world-0.1.0-SNAPSHOT.jar";
        }
    }

}
