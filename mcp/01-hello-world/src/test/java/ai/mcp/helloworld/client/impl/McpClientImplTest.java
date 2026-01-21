package ai.mcp.helloworld.client.impl;

import ai.mcp.helloworld.domain.protocol.*;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import ai.mcp.helloworld.exception.McpClientException;
import ai.mcp.helloworld.exception.TransportException;
import ai.mcp.helloworld.infrastructure.codec.CodecException;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link McpClientImpl}.
 * <p>
 * Tests MCP client operations using mocked transport and codec.
 *
 * @author lucas
 * @date 16/01/2026
 */
@ExtendWith(MockitoExtension.class)
class McpClientImplTest {

    @Mock
    private Transport transport;

    @Mock
    private JsonRpcCodec codec;

    private McpClientImpl client;

    @BeforeEach
    void setUp() {
        client = new McpClientImpl(transport, codec);
    }

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should throw when transport is null")
        void shouldThrowWhenTransportIsNull() {
            assertThatThrownBy(() -> new McpClientImpl(null, codec))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Transport");
        }

        @Test
        @DisplayName("should throw when codec is null")
        void shouldThrowWhenCodecIsNull() {
            assertThatThrownBy(() -> new McpClientImpl(transport, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Codec");
        }

        @Test
        @DisplayName("should create client with valid dependencies")
        void shouldCreateClientWithValidDependencies() {
            McpClientImpl validClient = new McpClientImpl(transport, codec);
            assertThat(validClient).isNotNull();
        }

    }

    @Nested
    @DisplayName("listTools")
    class ListToolsTests {

        @Test
        @DisplayName("should return tool definitions on success")
        void shouldReturnToolDefinitionsOnSuccess() {
            // Arrange
            var tools = List.of(
                    new ToolDefinition("add", "Adds two numbers", "{}"),
                    new ToolDefinition("multiply", "Multiplies two numbers", "{}")
            );
            var result = new ToolListResponse.ToolListResult(tools);
            var response = new ToolListResponse("1", "2.0", "success", result);

            when(codec.encode(any(ToolListRequest.class))).thenReturn("{\"jsonrpc\":\"2.0\"}");
            when(transport.receive()).thenReturn("{\"jsonrpc\":\"2.0\",\"result\":{}}");
            when(codec.decode(anyString())).thenReturn(response);

            // Act
            List<ToolDefinition> actualTools = client.listTools();

            // Assert
            assertThat(actualTools).hasSize(2);
            assertThat(actualTools).extracting(ToolDefinition::name)
                    .containsExactly("add", "multiply");
        }

        @Test
        @DisplayName("should throw McpClientException on error response")
        void shouldThrowMcpClientExceptionOnErrorResponse() {
            // Arrange
            var error = new McpError(-32601, "Method not found", null);
            var errorResponse = new McpErrorResponse("1", "2.0", error);

            when(codec.encode(any(ToolListRequest.class))).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(errorResponse);

            // Act & Assert
            assertThatThrownBy(() -> client.listTools())
                    .isInstanceOf(McpClientException.class)
                    .hasMessageContaining("Method not found");
        }

        @Test
        @DisplayName("should throw McpClientException on unexpected response type")
        void shouldThrowMcpClientExceptionOnUnexpectedResponseType() {
            // Arrange - return a ToolInvocationResponse instead of ToolListResponse
            var unexpectedResponse = new ToolInvocationResponse(
                    "1", "2.0",
                    new ToolInvocationResponse.ToolCallResult(List.of()));

            when(codec.encode(any(ToolListRequest.class))).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(unexpectedResponse);

            // Act & Assert
            assertThatThrownBy(() -> client.listTools())
                    .isInstanceOf(McpClientException.class)
                    .hasMessageContaining("Unexpected response type");
        }

        @Test
        @DisplayName("should send request via transport")
        void shouldSendRequestViaTransport() {
            // Arrange
            var tools = List.of(new ToolDefinition("add", "Adds", "{}"));
            var result = new ToolListResponse.ToolListResult(tools);
            var response = new ToolListResponse("1", "2.0", "success", result);

            when(codec.encode(any(ToolListRequest.class))).thenReturn("{\"encoded\"}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(response);

            // Act
            client.listTools();

            // Assert
            verify(transport).send("{\"encoded\"}");
        }

    }

    @Nested
    @DisplayName("callTool")
    class CallToolTests {

        @Test
        @DisplayName("should return content blocks on success")
        void shouldReturnContentBlocksOnSuccess() {
            // Arrange
            var content = List.<ContentBlock>of(new ContentBlock.TextContent("8"));
            var callResult = new ToolInvocationResponse.ToolCallResult(content);
            var response = new ToolInvocationResponse("1", "2.0", callResult);

            when(codec.encode(any(ToolInvocationRequest.class))).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(response);

            // Act
            List<ContentBlock> result = client.callTool("add", Map.of("a", 5, "b", 3));

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isInstanceOf(ContentBlock.TextContent.class);
            assertThat(((ContentBlock.TextContent) result.get(0)).text()).isEqualTo("8");
        }

        @Test
        @DisplayName("should throw McpClientException on error response")
        void shouldThrowMcpClientExceptionOnErrorResponse() {
            // Arrange
            var error = new McpError(-32602, "Invalid params", null);
            var errorResponse = new McpErrorResponse("1", "2.0", error);

            when(codec.encode(any(ToolInvocationRequest.class))).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(errorResponse);

            // Act & Assert
            assertThatThrownBy(() -> client.callTool("add", Map.of("a", 5)))
                    .isInstanceOf(McpClientException.class)
                    .hasMessageContaining("add")
                    .hasMessageContaining("Invalid params");
        }

        @Test
        @DisplayName("should throw McpClientException on unexpected response type")
        void shouldThrowMcpClientExceptionOnUnexpectedResponseType() {
            // Arrange - return a ToolListResponse instead of ToolInvocationResponse
            var tools = List.of(new ToolDefinition("add", "Adds", "{}"));
            var result = new ToolListResponse.ToolListResult(tools);
            var unexpectedResponse = new ToolListResponse("1", "2.0", "success", result);

            when(codec.encode(any(ToolInvocationRequest.class))).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(unexpectedResponse);

            // Act & Assert
            assertThatThrownBy(() -> client.callTool("add", Map.of("a", 5, "b", 3)))
                    .isInstanceOf(McpClientException.class)
                    .hasMessageContaining("Unexpected response type");
        }

        @Test
        @DisplayName("should encode tool name and arguments in request")
        void shouldEncodeToolNameAndArgumentsInRequest() {
            // Arrange
            var content = List.<ContentBlock>of(new ContentBlock.TextContent("42"));
            var callResult = new ToolInvocationResponse.ToolCallResult(content);
            var response = new ToolInvocationResponse("1", "2.0", callResult);

            ArgumentCaptor<ToolInvocationRequest> requestCaptor =
                    ArgumentCaptor.forClass(ToolInvocationRequest.class);
            when(codec.encode(requestCaptor.capture())).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(response);

            // Act
            client.callTool("multiply", Map.of("a", 6, "b", 7));

            // Assert
            ToolInvocationRequest capturedRequest = requestCaptor.getValue();
            assertThat(capturedRequest.params().name()).isEqualTo("multiply");
            assertThat(capturedRequest.params().arguments())
                    .containsEntry("a", 6)
                    .containsEntry("b", 7);
        }

    }

    @Nested
    @DisplayName("error handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("should propagate TransportException on send failure")
        void shouldPropagateTransportExceptionOnSendFailure() {
            // Arrange
            when(codec.encode(any(ToolListRequest.class))).thenReturn("{}");
            doThrow(new TransportException("Connection lost")).when(transport).send(anyString());

            // Act & Assert
            assertThatThrownBy(() -> client.listTools())
                    .isInstanceOf(TransportException.class)
                    .hasMessageContaining("Transport communication failed");
        }

        @Test
        @DisplayName("should propagate TransportException on receive failure")
        void shouldPropagateTransportExceptionOnReceiveFailure() {
            // Arrange
            when(codec.encode(any(ToolListRequest.class))).thenReturn("{}");
            when(transport.receive()).thenThrow(new TransportException("EOF"));

            // Act & Assert
            assertThatThrownBy(() -> client.listTools())
                    .isInstanceOf(TransportException.class)
                    .hasMessageContaining("Transport communication failed");
        }

        @Test
        @DisplayName("should propagate CodecException on encode failure")
        void shouldPropagateCodecExceptionOnEncodeFailure() {
            // Arrange
            when(codec.encode(any(ToolListRequest.class)))
                    .thenThrow(new CodecException("Encoding failed"));

            // Act & Assert
            assertThatThrownBy(() -> client.listTools())
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("encode/decode");
        }

        @Test
        @DisplayName("should propagate CodecException on decode failure")
        void shouldPropagateCodecExceptionOnDecodeFailure() {
            // Arrange
            when(codec.encode(any(ToolListRequest.class))).thenReturn("{}");
            when(transport.receive()).thenReturn("invalid json");
            when(codec.decode(anyString())).thenThrow(new CodecException("Invalid JSON"));

            // Act & Assert
            assertThatThrownBy(() -> client.listTools())
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("encode/decode");
        }

    }

    @Nested
    @DisplayName("connection management")
    class ConnectionManagementTests {

        @Test
        @DisplayName("should delegate isConnected to transport")
        void shouldDelegateIsConnectedToTransport() {
            // Arrange
            when(transport.isConnected()).thenReturn(true);

            // Act & Assert
            assertThat(client.isConnected()).isTrue();
            verify(transport).isConnected();
        }

        @Test
        @DisplayName("should delegate close to transport")
        void shouldDelegateCloseToTransport() {
            // Act
            client.close();

            // Assert
            verify(transport).close();
        }

    }

    @Nested
    @DisplayName("request ID generation")
    class RequestIdGenerationTests {

        @Test
        @DisplayName("should generate sequential request IDs")
        void shouldGenerateSequentialRequestIds() {
            // Arrange
            var tools = List.of(new ToolDefinition("add", "Adds", "{}"));
            var result = new ToolListResponse.ToolListResult(tools);
            var response = new ToolListResponse("1", "2.0", "success", result);

            ArgumentCaptor<ToolListRequest> requestCaptor =
                    ArgumentCaptor.forClass(ToolListRequest.class);
            when(codec.encode(requestCaptor.capture())).thenReturn("{}");
            when(transport.receive()).thenReturn("{}");
            when(codec.decode(anyString())).thenReturn(response);

            // Act - make multiple requests
            client.listTools();
            client.listTools();
            client.listTools();

            // Assert - IDs should be sequential
            List<ToolListRequest> requests = requestCaptor.getAllValues();
            assertThat(requests).hasSize(3);
            assertThat(requests.get(0).id()).isEqualTo("1");
            assertThat(requests.get(1).id()).isEqualTo("2");
            assertThat(requests.get(2).id()).isEqualTo("3");
        }

    }

}
