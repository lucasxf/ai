package ai.mcp.helloworld.server;

import ai.mcp.helloworld.domain.JsonRpcErrors;
import ai.mcp.helloworld.domain.protocol.*;
import ai.mcp.helloworld.domain.protocol.ToolInvocationRequest.ToolCallParams;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolRegistry;
import ai.mcp.helloworld.exception.McpServerException;
import ai.mcp.helloworld.exception.ToolNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Central message handler for MCP (Model Context Protocol) server requests.
 * <p>
 * This handler processes incoming JSON-RPC 2.0 requests following the MCP protocol specification.
 * It supports two primary operations:
 * <ul>
 *     <li><strong>tools/list</strong> - Retrieves all available tools registered in the system</li>
 *     <li><strong>tools/call</strong> - Executes a specific tool with provided parameters</li>
 * </ul>
 * <p>
 * <strong>Request Processing Flow:</strong>
 * <ol>
 *     <li>Validate request structure (null checks, ID validation, JSON-RPC version)</li>
 *     <li>Route to appropriate handler based on method name</li>
 *     <li>Execute handler logic (tool lookup, invocation, result building)</li>
 *     <li>Return success response or error response with appropriate JSON-RPC error code</li>
 * </ol>
 * <p>
 * <strong>Error Handling Strategy:</strong>
 * <ul>
 *     <li>{@link ToolNotFoundException} → METHOD_NOT_FOUND (-32601)</li>
 *     <li>{@link McpServerException} → INTERNAL_ERROR (-32603)</li>
 *     <li>Unknown method → METHOD_NOT_FOUND (-32601)</li>
 *     <li>Unexpected exceptions → INTERNAL_ERROR (-32603)</li>
 * </ul>
 * <p>
 * <strong>Thread Safety:</strong> This handler is stateless and thread-safe. Multiple threads can
 * invoke {@link #handleMessage(McpRequest)} concurrently without synchronization.
 * <p>
 * <strong>Example Usage:</strong>
 * <pre>{@code
 * // Tool list request
 * McpRequest listRequest = new ToolListRequest("1", "2.0", "tools/list");
 * McpMessage response = handler.handleMessage(listRequest);
 * // Returns: ToolListResponse with all registered tools
 *
 * // Tool invocation request
 * ToolCallParams params = new ToolCallParams("calculator", Map.of("operation", "add", "a", 5, "b", 3));
 * McpRequest callRequest = new ToolInvocationRequest("2", "2.0", "tools/call", params);
 * McpMessage response = handler.handleMessage(callRequest);
 * // Returns: ToolInvocationResponse with result [ContentBlock(type="text", text="8")]
 * }</pre>
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025 20:59
 * @see McpRequest
 * @see ToolListResponse
 * @see ToolInvocationResponse
 * @see McpErrorResponse
 * @see ToolRegistry
 */
@Slf4j
@Component
public class ServerMessageHandler {

    /**
     * JSON-RPC protocol version supported by this handler.
     * Per JSON-RPC 2.0 specification, this MUST be exactly "2.0".
     */
    private static final String JSONRPC_VERSION = "2.0";

    /**
     * Handles an incoming MCP request and returns the appropriate response.
     * <p>
     * This is the main entry point for request processing. It validates the request,
     * routes it to the appropriate handler based on the method name, and returns
     * either a success response or an error response.
     * <p>
     * <strong>Supported Methods:</strong>
     * <ul>
     *     <li>{@code tools/list} - Returns all available tools</li>
     *     <li>{@code tools/call} - Invokes a specific tool</li>
     * </ul>
     *
     * @param request the MCP request containing method, parameters, and request ID.
     *                Must not be null. Must have valid ID and JSON-RPC version "2.0".
     * @return {@link McpMessage} containing either:
     *         <ul>
     *             <li>{@link ToolListResponse} - for tools/list requests</li>
     *             <li>{@link ToolInvocationResponse} - for successful tool invocations</li>
     *             <li>{@link McpErrorResponse} - for errors (unknown method, tool not found, etc.)</li>
     *         </ul>
     * @throws McpServerException if request validation fails (null request, null/blank ID, wrong JSON-RPC version)
     */
    public McpMessage handleMessage(McpRequest request) {
        log.info("Handling request: {}", request);
        validateRequest(request);
        try {
            switch (request.method()) {
                case "tools/list" -> {
                    return handleToolListRequest(request);
                }
                case "tools/call" -> {
                    return handleToolCallRequest(request);
                }
                default -> {
                    log.warn("Unknown method: {}", request.method());
                    return handleError(request, JsonRpcErrors.METHOD_NOT_FOUND);
                }
            }
        } catch (ToolNotFoundException e) {
            log.error("Tool not found: {}", e.getMessage());
            return handleError(request, JsonRpcErrors.METHOD_NOT_FOUND);
        } catch (Exception e) {
            log.error("Internal error: {}", e.getMessage(), e);
            return handleError(request, JsonRpcErrors.INTERNAL_ERROR);
        }
    }

    /**
     * Validates the incoming MCP request structure and protocol compliance.
     * <p>
     * Performs the following validations:
     * <ul>
     *     <li>Request object is not null</li>
     *     <li>Request ID is not null and not blank</li>
     *     <li>JSON-RPC version is exactly "2.0" (per JSON-RPC 2.0 specification)</li>
     * </ul>
     * <p>
     * This method is called before any request processing to fail fast on invalid requests.
     *
     * @param request the MCP request to validate
     * @throws McpServerException if any validation fails
     */
    private void validateRequest(McpRequest request) {
        if (request == null) {
            throw new McpServerException("Request must not be null");
        }
        if (request.id() == null || request.id().toString().isBlank()) {
            throw new McpServerException("Request ID must not be null or blank");
        }
        if (!JSONRPC_VERSION.equals(request.jsonrpc())) {
            throw new McpServerException("Invalid JSON-RPC version");
        }
    }

    /**
     * Handles "tools/list" requests by retrieving all registered tools.
     * <p>
     * This method queries the {@link ToolRegistry} for all available tools and returns
     * them in a structured response. The response includes tool metadata such as:
     * <ul>
     *     <li>Tool name</li>
     *     <li>Description</li>
     *     <li>Input schema (JSON schema defining expected parameters)</li>
     * </ul>
     *
     * @param request the tool list request (must be valid, validated by {@link #validateRequest(McpRequest)})
     * @return {@link ToolListResponse} containing all registered tools with their metadata
     */
    private ToolListResponse handleToolListRequest(McpRequest request) {
        final List<Tool> tools = ToolRegistry.getAllTools();
        return new ToolListResponse(
                request.id().toString(),
                JSONRPC_VERSION,
                "Tool list retrieved successfully",
                new ToolListResponse.ToolListResult(tools));
    }

    /**
     * Handles "tools/call" requests by executing the specified tool with provided parameters.
     * <p>
     * This method:
     * <ol>
     *     <li>Validates that the request is a {@link ToolInvocationRequest}</li>
     *     <li>Extracts tool name and parameters</li>
     *     <li>Looks up the tool in {@link ToolRegistry}</li>
     *     <li>Executes the tool with the provided arguments</li>
     *     <li>Returns the tool's result wrapped in a {@link ToolInvocationResponse}</li>
     * </ol>
     * <p>
     * <strong>Type Safety:</strong> Uses pattern matching to ensure type safety. If the request
     * is not a {@link ToolInvocationRequest}, throws {@link McpServerException}.
     * <p>
     * <strong>Error Scenarios:</strong>
     * <ul>
     *     <li>Wrong request type → {@link McpServerException}</li>
     *     <li>Tool not found → {@link ToolNotFoundException} (caught by caller, mapped to METHOD_NOT_FOUND)</li>
     *     <li>Invalid tool parameters → Tool implementation throws exception (caught by caller, mapped to INTERNAL_ERROR)</li>
     * </ul>
     *
     * @param request the tool invocation request (must be {@link ToolInvocationRequest})
     * @return {@link ToolInvocationResponse} containing the tool execution results as {@link ContentBlock}s
     * @throws McpServerException if request is not a ToolInvocationRequest
     * @throws ToolNotFoundException if the requested tool is not registered (caught by caller)
     */
    private ToolInvocationResponse handleToolCallRequest(McpRequest request) {
        if (request instanceof ToolInvocationRequest toolInvocationRequest) {
            ToolCallParams params = toolInvocationRequest.params();
            Tool tool = ToolRegistry.getTool(params.name());
            List<ContentBlock> result = tool.execute(params.arguments());
            return new ToolInvocationResponse(
                    request.id().toString(),
                    JSONRPC_VERSION,
                    new ToolInvocationResponse.ToolCallResult(result));
        }
        throw new McpServerException(STR."Invalid request type for tool call: expected ToolInvocationRequest but got [\{request.getClass().getSimpleName()}]");
    }

    /**
     * Constructs an error response for failed request processing.
     * <p>
     * This method creates a standardized JSON-RPC error response using the provided
     * error type from {@link JsonRpcErrors}. The response includes:
     * <ul>
     *     <li>Original request ID (for client correlation)</li>
     *     <li>JSON-RPC version "2.0"</li>
     *     <li>Error code and message from {@link JsonRpcErrors} enum</li>
     *     <li>Error data containing the original request for debugging</li>
     * </ul>
     * <p>
     * <strong>JSON-RPC 2.0 Error Codes:</strong>
     * <ul>
     *     <li>-32600: Invalid Request - Malformed request structure</li>
     *     <li>-32601: Method not found - Unknown method or tool</li>
     *     <li>-32602: Invalid params - Invalid tool parameters</li>
     *     <li>-32603: Internal error - Server-side execution error</li>
     * </ul>
     *
     * @param request the original request that caused the error (used for ID and debugging context)
     * @param errorType the error type defining the JSON-RPC error code and message
     * @return {@link McpErrorResponse} containing the error details in JSON-RPC format
     */
    private McpErrorResponse handleError(McpRequest request, JsonRpcErrors errorType) {
        return new McpErrorResponse(
                request.id(),
                JSONRPC_VERSION,
                McpError.of(errorType, request));
    }

}
