package ai.mcp.helloworld.client;

import ai.mcp.helloworld.client.impl.McpClientImpl;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import ai.mcp.helloworld.infrastructure.codec.impl.JsonRpcCodecImpl;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import ai.mcp.helloworld.infrastructure.transport.impl.StdioTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Demo application for testing MCP Client implementation.
 * <p>
 * This standalone runner demonstrates the complete client workflow:
 * <ol>
 *     <li>Spawn MCP server process via {@link StdioTransport}</li>
 *     <li>Create client with codec and transport</li>
 *     <li>List available tools from server</li>
 *     <li>Invoke tools with arguments</li>
 *     <li>Gracefully shutdown client (terminates server)</li>
 * </ol>
 * <p>
 * <strong>Usage:</strong>
 * <pre>{@code
 * # First, package the server JAR
 * mvn clean package -DskipTests
 *
 * # Run the client demo
 * java -cp target/mcp-hello-world-0.1.0-SNAPSHOT.jar ai.mcp.helloworld.client.McpClientDemo
 * }</pre>
 * <p>
 * <strong>Expected Output:</strong>
 * <pre>
 * INFO  - Spawning MCP server process...
 * INFO  - Listing available tools from MCP server
 * INFO  - Received 3 tools from server
 * Tool: add - Adds two numbers together
 * Tool: multiply - Multiplies two numbers
 * Tool: random - Generates a random number within a range
 *
 * INFO  - Calling tool 'add' with arguments: {a=5, b=3}
 * INFO  - Tool 'add' returned 1 content blocks
 * Result: [TextContent[text=8]]
 *
 * INFO  - Closing MCP client and terminating server process
 * INFO  - MCP server process closed successfully
 * </pre>
 *
 * @author Lucas Xavier Ferreira
 * @date 14/01/2026
 * @see McpClient
 * @see McpClientImpl
 * @see StdioTransport
 */
@Slf4j
public class McpClientDemo {

    public static void main(String[] args) {
        log.info("=== MCP Client Demo Starting ===");

        // Determine the server JAR path (adjust if needed)
        String jarPath = "target/mcp-hello-world-0.1.0-SNAPSHOT.jar";
        List<String> serverCommand = List.of("java", "-jar", jarPath);

        // Create transport (spawns server subprocess)
        Transport transport = new StdioTransport(serverCommand);

        // Create codec with ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        JsonRpcCodec codec = new JsonRpcCodecImpl(objectMapper);

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
    private static void demonstrateToolListing(McpClient client) {
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
    private static void demonstrateToolInvocation(McpClient client) {
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

}
