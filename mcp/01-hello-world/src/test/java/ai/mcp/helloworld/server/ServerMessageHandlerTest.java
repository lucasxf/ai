package ai.mcp.helloworld.server;

import ai.mcp.helloworld.domain.JsonRpcErrors;
import ai.mcp.helloworld.domain.protocol.*;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import ai.mcp.helloworld.domain.tool.ToolRegistry;
import ai.mcp.helloworld.exception.McpServerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ServerMessageHandler}.
 * <p>
 * Tests MCP request processing including tool list, tool invocation,
 * request validation, and error handling.
 *
 * @author lucas
 * @date 16/01/2026
 */
class ServerMessageHandlerTest {

    private ServerMessageHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ServerMessageHandler();
        ToolRegistry.clear();
    }

    @AfterEach
    void tearDown() {
        ToolRegistry.clear();
    }

    @Nested
    @DisplayName("handleMessage - tools/list")
    class ToolListTests {

        @Test
        @DisplayName("should return empty tool list when no tools registered")
        void shouldReturnEmptyToolListWhenNoToolsRegistered() {
            ToolListRequest request = ToolListRequest.create("1");

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(ToolListResponse.class);
            ToolListResponse toolListResponse = (ToolListResponse) response;
            assertThat(toolListResponse.id()).isEqualTo("1");
            assertThat(toolListResponse.jsonrpc()).isEqualTo("2.0");
            assertThat(toolListResponse.result().tools()).isEmpty();
        }

        @Test
        @DisplayName("should return all registered tools")
        void shouldReturnAllRegisteredTools() {
            registerTestTool("tool1", "Tool One Description");
            registerTestTool("tool2", "Tool Two Description");
            ToolListRequest request = ToolListRequest.create("123");

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(ToolListResponse.class);
            ToolListResponse toolListResponse = (ToolListResponse) response;
            assertThat(toolListResponse.result().tools()).hasSize(2);
            assertThat(toolListResponse.result().tools())
                    .extracting(ToolDefinition::name)
                    .containsExactlyInAnyOrder("tool1", "tool2");
        }

        @Test
        @DisplayName("should preserve request ID in response")
        void shouldPreserveRequestIdInResponse() {
            ToolListRequest request = ToolListRequest.create("unique-id-456");

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(ToolListResponse.class);
            ToolListResponse toolListResponse = (ToolListResponse) response;
            assertThat(toolListResponse.id()).isEqualTo("unique-id-456");
        }

    }

    @Nested
    @DisplayName("handleMessage - tools/call")
    class ToolCallTests {

        @Test
        @DisplayName("should execute tool and return result")
        void shouldExecuteToolAndReturnResult() {
            registerTestTool("add", "Adds two numbers");
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "42", "add", Map.of("a", 5, "b", 3));

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(ToolInvocationResponse.class);
            ToolInvocationResponse invocationResponse = (ToolInvocationResponse) response;
            assertThat(invocationResponse.id()).isEqualTo("42");
            assertThat(invocationResponse.jsonrpc()).isEqualTo("2.0");
            assertThat(invocationResponse.result().content()).hasSize(1);
            assertThat(invocationResponse.result().content().get(0))
                    .isInstanceOf(ContentBlock.TextContent.class);
        }

        @Test
        @DisplayName("should return METHOD_NOT_FOUND error when tool not registered")
        void shouldReturnMethodNotFoundErrorWhenToolNotRegistered() {
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "99", "nonexistent", Map.of("x", 1));

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(McpErrorResponse.class);
            McpErrorResponse errorResponse = (McpErrorResponse) response;
            assertThat(errorResponse.error().code()).isEqualTo(JsonRpcErrors.METHOD_NOT_FOUND.code());
        }

        @Test
        @DisplayName("should preserve request ID in response")
        void shouldPreserveRequestIdInResponse() {
            registerTestTool("multiply", "Multiplies");
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "request-789", "multiply", Map.of("a", 2, "b", 3));

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(ToolInvocationResponse.class);
            ToolInvocationResponse invocationResponse = (ToolInvocationResponse) response;
            assertThat(invocationResponse.id()).isEqualTo("request-789");
        }

    }

    @Nested
    @DisplayName("handleMessage - request validation")
    class RequestValidationTests {

        @Test
        @DisplayName("should throw when request is null")
        void shouldThrowWhenRequestIsNull() {
            assertThatThrownBy(() -> handler.handleMessage(null))
                    .isInstanceOf(McpServerException.class)
                    .hasMessageContaining("null");
        }

        // Note: Tests for null/blank ID and wrong jsonrpc version are not included here
        // because McpRequest is a sealed interface, and both permitted implementations
        // (ToolListRequest, ToolInvocationRequest) validate these in their compact
        // constructors. The validateRequest() method in ServerMessageHandler acts as
        // a defensive layer for edge cases.

    }

    @Nested
    @DisplayName("handleMessage - error handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("should return METHOD_NOT_FOUND when tool not found")
        void shouldReturnMethodNotFoundWhenToolNotFound() {
            // Request for a tool that doesn't exist
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "1", "nonexistent-tool", Map.of("x", 1));

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(McpErrorResponse.class);
            McpErrorResponse errorResponse = (McpErrorResponse) response;
            assertThat(errorResponse.error().code()).isEqualTo(JsonRpcErrors.METHOD_NOT_FOUND.code());
            assertThat(errorResponse.id()).isEqualTo("1");
        }

        @Test
        @DisplayName("should return INTERNAL_ERROR when tool execution throws unexpected exception")
        void shouldReturnInternalErrorWhenToolExecutionThrowsUnexpectedException() {
            registerFailingTool("failing-tool", new RuntimeException("Unexpected error"));
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "1", "failing-tool", Map.of("x", 1));

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(McpErrorResponse.class);
            McpErrorResponse errorResponse = (McpErrorResponse) response;
            assertThat(errorResponse.error().code()).isEqualTo(JsonRpcErrors.INTERNAL_ERROR.code());
        }

        @Test
        @DisplayName("should include request ID in error response")
        void shouldIncludeRequestIdInErrorResponse() {
            // Use a request that will trigger an error (tool not found)
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "error-request-id-123", "missing-tool", Map.of("x", 1));

            McpMessage response = handler.handleMessage(request);

            assertThat(response).isInstanceOf(McpErrorResponse.class);
            McpErrorResponse errorResponse = (McpErrorResponse) response;
            assertThat(errorResponse.id()).isEqualTo("error-request-id-123");
        }

        // Note: "unknown method" error path cannot be directly tested because McpRequest
        // is a sealed interface, and both permitted implementations validate their method
        // names in compact constructors. The switch default case is a defensive layer.

    }

    // ========== Helper Methods ==========

    /**
     * Registers a simple test tool that returns a fixed response.
     */
    private void registerTestTool(String name, String description) {
        Tool tool = new Tool() {
            @Override
            public ToolDefinition getDefinition() {
                return new ToolDefinition(name, description, "{}");
            }

            @Override
            public List<ContentBlock> execute(Map<String, Object> arguments) {
                return List.of(new ContentBlock.TextContent("test-result"));
            }
        };
        ToolRegistry.register(name, tool);
    }

    /**
     * Registers a tool that throws the specified exception when executed.
     */
    private void registerFailingTool(String name, RuntimeException exception) {
        Tool tool = new Tool() {
            @Override
            public ToolDefinition getDefinition() {
                return new ToolDefinition(name, "Failing tool", "{}");
            }

            @Override
            public List<ContentBlock> execute(Map<String, Object> arguments) {
                throw exception;
            }
        };
        ToolRegistry.register(name, tool);
    }

}
