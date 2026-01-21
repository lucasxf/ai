package ai.mcp.helloworld.client.impl;

import ai.mcp.helloworld.client.McpClient;
import ai.mcp.helloworld.domain.protocol.*;
import ai.mcp.helloworld.domain.protocol.ToolInvocationRequest.ToolCallParams;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import ai.mcp.helloworld.exception.McpClientException;
import ai.mcp.helloworld.exception.TransportException;
import ai.mcp.helloworld.infrastructure.codec.CodecException;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Client implementation for MCP (Model Context Protocol) communication.
 * <p>
 * This client provides a simplified request-response pattern for interacting with MCP servers.
 * Unlike the server (which runs in a continuous loop), the client sends individual requests
 * and waits for corresponding responses.
 * <p>
 * <strong>Architecture:</strong>
 * <ul>
 *     <li>Uses {@link Transport} for I/O (spawns server subprocess via {@code StdioTransport})</li>
 *     <li>Uses {@link JsonRpcCodec} for JSON-RPC 2.0 serialization/deserialization</li>
 *     <li>Generates unique request IDs using {@link AtomicLong} (thread-safe)</li>
 *     <li>Parses responses inline (no separate handler delegation for simplicity)</li>
 * </ul>
 * <p>
 * <strong>Request-Response Flow:</strong>
 * <ol>
 *     <li>Create request with unique ID</li>
 *     <li>Encode request to JSON via codec</li>
 *     <li>Send JSON to server via transport</li>
 *     <li>Block and wait for response from transport</li>
 *     <li>Decode response from JSON via codec</li>
 *     <li>Parse response (success or error)</li>
 *     <li>Return result or throw exception</li>
 * </ol>
 * <p>
 * <strong>Error Handling:</strong>
 * <ul>
 *     <li>{@link McpErrorResponse} from server → throws {@link McpException} with error details</li>
 *     <li>{@link TransportException} → I/O failures (process died, stdin/stdout closed)</li>
 *     <li>{@link CodecException} → JSON parsing failures (malformed response)</li>
 *     <li>Unexpected response type → throws {@link McpException}</li>
 * </ul>
 * <p>
 * <strong>Thread Safety:</strong> This client is thread-safe for concurrent method invocations.
 * Request ID generation is atomic, and transport send/receive operations are synchronized internally.
 * <p>
 * <strong>Usage Example:</strong>
 * <pre>{@code
 * // Create transport (spawns server subprocess)
 * Transport transport = new StdioTransport(List.of("java", "-jar", "mcp-server.jar"));
 * JsonRpcCodec codec = new JsonRpcCodecImpl();
 * McpClient client = new McpClientImpl(transport, codec);
 *
 * try {
 *     // List available tools
 *     List<ToolDefinition> tools = client.listTools();
 *     tools.forEach(tool -> System.out.println("Tool: " + tool.name()));
 *
 *     // Call a tool
 *     List<ContentBlock> result = client.callTool("add", Map.of("a", 5, "b", 3));
 *     System.out.println("Result: " + result);
 * } finally {
 *     client.close();
 * }
 * }</pre>
 *
 * @author Lucas Xavier Ferreira
 * @date 14/01/2026
 * @see McpClient
 * @see Transport
 * @see JsonRpcCodec
 */
@Slf4j
public class McpClientImpl implements McpClient {

    private static final String JSONRPC_VERSION = "2.0";

    private final Transport transport;
    private final JsonRpcCodec codec;
    private final AtomicLong requestIdGenerator;

    /**
     * Creates a new MCP client with the specified transport and codec.
     * <p>
     * The transport is responsible for I/O (typically {@code StdioTransport} which spawns
     * a server subprocess and communicates via stdin/stdout).
     * <p>
     * The codec handles JSON-RPC 2.0 serialization/deserialization.
     *
     * @param transport the transport layer for communication (must be connected)
     * @param codec the JSON-RPC codec for encoding/decoding messages
     * @throws IllegalArgumentException if transport or codec is null
     */
    public McpClientImpl(Transport transport, JsonRpcCodec codec) {
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }
        if (codec == null) {
            throw new IllegalArgumentException("Codec must not be null");
        }
        this.transport = transport;
        this.codec = codec;
        this.requestIdGenerator = new AtomicLong(1);
    }

    @Override
    public List<ToolDefinition> listTools() {
        log.info("Listing available tools from MCP server");

        ToolListRequest request = ToolListRequest.create(generateRequestId());

        McpMessage response = sendAndReceive(request);

        if (response instanceof ToolListResponse toolListResponse) {
            List<ToolDefinition> tools = toolListResponse.result().tools();
            log.info("Received {} tools from server", tools.size());
            return tools;
        } else if (response instanceof McpErrorResponse errorResponse) {
            String errorMessage = STR."Tool list request failed: \{errorResponse.error().message()}";
            log.error(errorMessage);
            throw new McpClientException(errorMessage);
        }

        throw new McpClientException(STR."Unexpected response type for tools/list: \{response.getClass().getSimpleName()}");
    }

    @Override
    public List<ContentBlock> callTool(String toolName, Map<String, Object> arguments) {
        log.info("Calling tool '{}' with arguments: {}", toolName, arguments);

        ToolCallParams params = new ToolCallParams(toolName, arguments);
        ToolInvocationRequest request = new ToolInvocationRequest(
                generateRequestId(),
                JSONRPC_VERSION,
                "tools/call",
                params
        );

        McpMessage response = sendAndReceive(request);

        if (response instanceof ToolInvocationResponse toolResponse) {
            List<ContentBlock> result = toolResponse.result().content();
            log.info("Tool '{}' returned {} content blocks", toolName, result.size());
            return result;
        } else if (response instanceof McpErrorResponse errorResponse) {
            String errorMessage = STR."Tool invocation failed for '\{toolName}': \{errorResponse.error().message()}";
            log.error(errorMessage);
            throw new McpClientException(errorMessage);
        }

        throw new McpClientException(STR."Unexpected response type for tools/call: \{response.getClass().getSimpleName()}");
    }

    @Override
    public boolean isConnected() {
        return transport.isConnected();
    }

    @Override
    public void close() {
        log.info("Closing MCP client and terminating server process");
        transport.close();
    }

    /**
     * Sends a request to the server and waits for a response.
     * <p>
     * This method encapsulates the full request-response cycle:
     * <ol>
     *     <li>Encode request to JSON</li>
     *     <li>Send JSON to server</li>
     *     <li>Block waiting for response</li>
     *     <li>Decode response from JSON</li>
     * </ol>
     * <p>
     * <strong>Blocking Behavior:</strong> This method blocks until the server sends a response.
     * If the server crashes or the transport is closed, {@link TransportException} is thrown.
     * <p>
     * <strong>Error Scenarios:</strong>
     * <ul>
     *     <li>Encoding failure → {@link CodecException}</li>
     *     <li>Transport I/O failure → {@link TransportException}</li>
     *     <li>Decoding failure → {@link CodecException}</li>
     * </ul>
     *
     * @param request the MCP request to send (must be valid JSON-RPC 2.0 request)
     * @return the decoded response from the server ({@link ToolListResponse}, {@link ToolInvocationResponse}, or {@link McpErrorResponse})
     * @throws TransportException if I/O fails (server process died, stdin/stdout closed)
     * @throws CodecException if JSON encoding/decoding fails
     */
    private McpMessage sendAndReceive(McpRequest request) {
        try {
            String requestJson = codec.encode(request);
            log.debug("Sending request: {}", requestJson);

            transport.send(requestJson);

            String responseJson = transport.receive();
            log.debug("Received response: {}", responseJson);

            return codec.decode(responseJson);

        } catch (TransportException e) {
            String errorMessage = STR."Transport communication failed: \{e.getMessage()}";
            log.error(errorMessage, e);
            throw new TransportException(errorMessage, e);
        } catch (CodecException e) {
            String errorMessage = STR."Failed to encode/decode message: \{e.getMessage()}";
            log.error(errorMessage, e);
            throw new CodecException(errorMessage, e);
        }
    }

    /**
     * Generates a unique request ID for JSON-RPC correlation.
     * <p>
     * Uses an {@link AtomicLong} to ensure thread-safe ID generation.
     * IDs are sequential integers starting from 1.
     * <p>
     * JSON-RPC 2.0 allows IDs to be strings or numbers. We use string representation
     * for consistency with the protocol models.
     *
     * @return a unique request ID as a string (e.g., "1", "2", "3", ...)
     */
    private String generateRequestId() {
        return String.valueOf(requestIdGenerator.getAndIncrement());
    }

}
