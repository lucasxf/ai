package ai.mcp.helloworld.infrastructure.codec;

import ai.mcp.helloworld.domain.protocol.*;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;
import ai.mcp.helloworld.infrastructure.codec.impl.JsonRpcCodecImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link JsonRpcCodecImpl}.
 * <p>
 * Tests JSON-RPC 2.0 encoding and decoding for MCP protocol messages.
 *
 * @author lucas
 * @date 16/01/2026
 */
class JsonRpcCodecImplTest {

    private JsonRpcCodecImpl codec;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        codec = new JsonRpcCodecImpl(mapper);
    }

    @Nested
    @DisplayName("encode")
    class EncodeTests {

        @Test
        @DisplayName("should encode ToolListRequest to JSON")
        void shouldEncodeToolListRequestToJson() {
            ToolListRequest request = ToolListRequest.create("1");

            String json = codec.encode(request);

            assertThat(json).contains("\"jsonrpc\":\"2.0\"");
            assertThat(json).contains("\"id\":\"1\"");
            assertThat(json).contains("\"method\":\"tools/list\"");
        }

        @Test
        @DisplayName("should encode ToolInvocationRequest to JSON")
        void shouldEncodeToolInvocationRequestToJson() {
            ToolInvocationRequest request = ToolInvocationRequest.create(
                    "2", "add", Map.of("a", 5, "b", 3));

            String json = codec.encode(request);

            assertThat(json).contains("\"jsonrpc\":\"2.0\"");
            assertThat(json).contains("\"id\":\"2\"");
            assertThat(json).contains("\"method\":\"tools/call\"");
            assertThat(json).contains("\"name\":\"add\"");
        }

        @Test
        @DisplayName("should encode ToolListResponse to JSON")
        void shouldEncodeToolListResponseToJson() {
            var tools = List.of(
                    new ToolDefinition("add", "Adds numbers", "{}"),
                    new ToolDefinition("multiply", "Multiplies numbers", "{}"));
            var result = new ToolListResponse.ToolListResult(tools);
            var response = new ToolListResponse("1", "2.0", "success", result);

            String json = codec.encode(response);

            assertThat(json).contains("\"jsonrpc\":\"2.0\"");
            assertThat(json).contains("\"id\":\"1\"");
            assertThat(json).contains("\"tools\"");
            assertThat(json).contains("\"add\"");
            assertThat(json).contains("\"multiply\"");
        }

        @Test
        @DisplayName("should encode McpErrorResponse to JSON")
        void shouldEncodeMcpErrorResponseToJson() {
            var error = new McpError(-32601, "Method not found", null);
            var response = new McpErrorResponse("1", "2.0", error);

            String json = codec.encode(response);

            assertThat(json).contains("\"jsonrpc\":\"2.0\"");
            assertThat(json).contains("\"id\":\"1\"");
            assertThat(json).contains("\"error\"");
            assertThat(json).contains("-32601");
            assertThat(json).contains("Method not found");
        }

    }

    @Nested
    @DisplayName("decode requests")
    class DecodeRequestTests {

        @Test
        @DisplayName("should decode tools/list request")
        void shouldDecodeToolsListRequest() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "method": "tools/list",
                      "params": {}
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(ToolListRequest.class);
            ToolListRequest request = (ToolListRequest) message;
            assertThat(request.id()).isEqualTo("1");
            assertThat(request.method()).isEqualTo("tools/list");
        }

        @Test
        @DisplayName("should decode tools/call request")
        void shouldDecodeToolsCallRequest() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "2",
                      "method": "tools/call",
                      "params": {
                        "name": "add",
                        "arguments": {"a": 5, "b": 3}
                      }
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(ToolInvocationRequest.class);
            ToolInvocationRequest request = (ToolInvocationRequest) message;
            assertThat(request.id()).isEqualTo("2");
            assertThat(request.method()).isEqualTo("tools/call");
            assertThat(request.params().name()).isEqualTo("add");
            assertThat(request.params().arguments()).containsEntry("a", 5);
            assertThat(request.params().arguments()).containsEntry("b", 3);
        }

        @Test
        @DisplayName("should decode request with numeric ID")
        void shouldDecodeRequestWithNumericId() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": 123,
                      "method": "tools/list",
                      "params": {}
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(ToolListRequest.class);
            // Note: Jackson may deserialize numeric id to string based on record definition
        }

    }

    @Nested
    @DisplayName("decode responses")
    class DecodeResponseTests {

        @Test
        @DisplayName("should decode ToolListResponse")
        void shouldDecodeToolListResponse() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "result": {
                        "tools": [
                          {"name": "add", "description": "Adds numbers", "schema": "{}"}
                        ]
                      }
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(ToolListResponse.class);
            ToolListResponse response = (ToolListResponse) message;
            assertThat(response.result().tools()).hasSize(1);
            assertThat(response.result().tools().get(0).name()).isEqualTo("add");
        }

        @Test
        @DisplayName("should decode ToolInvocationResponse")
        void shouldDecodeToolInvocationResponse() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "2",
                      "result": {
                        "content": [
                          {"text": "8"}
                        ]
                      }
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(ToolInvocationResponse.class);
            ToolInvocationResponse response = (ToolInvocationResponse) message;
            assertThat(response.result().content()).hasSize(1);
            assertThat(response.result().content().get(0))
                    .isInstanceOf(ContentBlock.TextContent.class);
            assertThat(((ContentBlock.TextContent) response.result().content().get(0)).text())
                    .isEqualTo("8");
        }

        @Test
        @DisplayName("should decode McpErrorResponse")
        void shouldDecodeMcpErrorResponse() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "error": {
                        "code": -32601,
                        "message": "Method not found"
                      }
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(McpErrorResponse.class);
            McpErrorResponse response = (McpErrorResponse) message;
            assertThat(response.error().code()).isEqualTo(-32601);
            assertThat(response.error().message()).isEqualTo("Method not found");
        }

        @Test
        @DisplayName("should decode error response with data field")
        void shouldDecodeErrorResponseWithDataField() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "error": {
                        "code": -32602,
                        "message": "Invalid params",
                        "data": {"detail": "Parameter 'a' is required"}
                      }
                    }
                    """;

            McpMessage message = codec.decode(json);

            assertThat(message).isInstanceOf(McpErrorResponse.class);
            McpErrorResponse response = (McpErrorResponse) message;
            assertThat(response.error().code()).isEqualTo(-32602);
            assertThat(response.error().data()).isNotNull();
        }

    }

    @Nested
    @DisplayName("decode error handling")
    class DecodeErrorHandlingTests {

        @Test
        @DisplayName("should throw on missing jsonrpc field")
        void shouldThrowOnMissingJsonrpcField() {
            String json = """
                    {
                      "id": "1",
                      "method": "tools/list"
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("jsonrpc");
        }

        @Test
        @DisplayName("should throw on invalid jsonrpc version")
        void shouldThrowOnInvalidJsonrpcVersion() {
            String json = """
                    {
                      "jsonrpc": "1.0",
                      "id": "1",
                      "method": "tools/list"
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("version");
        }

        @Test
        @DisplayName("should throw on missing id field")
        void shouldThrowOnMissingIdField() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "method": "tools/list"
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("id");
        }

        @Test
        @DisplayName("should throw on unknown message type")
        void shouldThrowOnUnknownMessageType() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1"
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("Unknown message type");
        }

        @Test
        @DisplayName("should throw on unknown method")
        void shouldThrowOnUnknownMethod() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "method": "unknown/method"
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("Unknown method");
        }

        @Test
        @DisplayName("should throw on invalid JSON")
        void shouldThrowOnInvalidJson() {
            String json = "{ invalid json }";

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class);
        }

        @Test
        @DisplayName("should throw on unknown response structure")
        void shouldThrowOnUnknownResponseStructure() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "result": {
                        "unknownField": "value"
                      }
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("Unknown response structure");
        }

        @Test
        @DisplayName("should throw on error response missing code")
        void shouldThrowOnErrorResponseMissingCode() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "error": {
                        "message": "Error occurred"
                      }
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("code");
        }

        @Test
        @DisplayName("should throw on error response missing message")
        void shouldThrowOnErrorResponseMissingMessage() {
            String json = """
                    {
                      "jsonrpc": "2.0",
                      "id": "1",
                      "error": {
                        "code": -32600
                      }
                    }
                    """;

            assertThatThrownBy(() -> codec.decode(json))
                    .isInstanceOf(CodecException.class)
                    .hasMessageContaining("message");
        }

    }

    @Nested
    @DisplayName("round-trip")
    class RoundTripTests {

        @Test
        @DisplayName("should encode and decode ToolListRequest")
        void shouldEncodeAndDecodeToolListRequest() {
            ToolListRequest original = ToolListRequest.create("42");

            String json = codec.encode(original);
            McpMessage decoded = codec.decode(json);

            assertThat(decoded).isInstanceOf(ToolListRequest.class);
            ToolListRequest result = (ToolListRequest) decoded;
            assertThat(result.id()).isEqualTo(original.id());
            assertThat(result.method()).isEqualTo(original.method());
        }

        @Test
        @DisplayName("should encode and decode ToolInvocationRequest")
        void shouldEncodeAndDecodeToolInvocationRequest() {
            ToolInvocationRequest original = ToolInvocationRequest.create(
                    "123", "multiply", Map.of("a", 7, "b", 6));

            String json = codec.encode(original);
            McpMessage decoded = codec.decode(json);

            assertThat(decoded).isInstanceOf(ToolInvocationRequest.class);
            ToolInvocationRequest result = (ToolInvocationRequest) decoded;
            assertThat(result.id()).isEqualTo(original.id());
            assertThat(result.params().name()).isEqualTo(original.params().name());
        }

    }

}
