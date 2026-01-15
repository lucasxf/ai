package ai.mcp.helloworld.client;

import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

import java.util.List;
import java.util.Map;

/**
 * MCP Client interface for communicating with Model Context Protocol servers.
 * <p>
 * Provides operations for tool discovery ({@link #listTools()}) and invocation
 * ({@link #callTool(String, Map)}). Communicates with server via JSON-RPC 2.0
 * over stdio transport.
 * <p>
 * <strong>Example:</strong>
 * <pre>{@code
 * McpClient client = new McpClientImpl(transport, codec);
 * try {
 *     List<ToolDefinition> tools = client.listTools();
 *     List<ContentBlock> result = client.callTool("add", Map.of("a", 5, "b", 3));
 * } finally {
 *     client.close();
 * }
 * }</pre>
 *
 * @author Lucas Xavier Ferreira
 * @date 09/01/2026
 * @see ai.mcp.helloworld.server.McpServer
 */
public interface McpClient {

    /**
     * Lists all tools available on the MCP server.
     * Sends JSON-RPC {@code tools/list} request.
     *
     * @return list of tool definitions. Never null, may be empty.
     */
    List<ToolDefinition> listTools();

    /**
     * Invokes a specific tool on the MCP server.
     * Sends JSON-RPC {@code tools/call} request with tool name and arguments.
     *
     * @param toolName the name of the tool to invoke
     * @param arguments the tool parameters as key-value pairs
     * @return list of content blocks representing the tool result. Never null.
     */
    List<ContentBlock> callTool(String toolName, Map<String, Object> arguments);

    /**
     * Checks if the client is connected to the server.
     *
     * @return {@code true} if transport is open and server process is alive
     */
    boolean isConnected();

    /**
     * Closes the client and terminates the server process.
     * Idempotent - safe to call multiple times.
     */
    void close();

}
