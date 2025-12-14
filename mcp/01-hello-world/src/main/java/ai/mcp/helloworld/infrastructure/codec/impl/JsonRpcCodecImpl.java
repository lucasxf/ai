package ai.mcp.helloworld.infrastructure.codec.impl;

import ai.mcp.helloworld.domain.protocol.*;
import ai.mcp.helloworld.infrastructure.codec.CodecException;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Jackson-based implementation of JSON-RPC 2.0 codec for MCP protocol.
 * <p>
 * Handles encoding and decoding of MCP messages to/from JSON-RPC 2.0 format.
 * Uses JsonNode for flexible parsing and type detection.
 *
 * @author lucas
 * @date 01/12/2025 18:41
 */
@Service
public class JsonRpcCodecImpl implements JsonRpcCodec {

    // JSON-RPC 2.0
    private static final String CODE = "code";
    private static final String CONTENT = "content";
    private static final String DATA = "data";
    private static final String ERROR = "error";
    private static final String ID = "id";
    private static final String JSONRPC = "jsonrpc";
    private static final String JSONRPC_VERSION = "2.0";
    private static final String MESSAGE = "message";
    private static final String METHOD = "method";
    private static final String RESULT = "result";
    private static final String TOOLS = "tools";

    // Tooling
    private static final String TOOLS_LIST = "tools/list";
    private static final String TOOLS_CALL = "tools/call";

    private final ObjectMapper mapper;

    public JsonRpcCodecImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String encode(McpRequest request) {
        try {
            return mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            final var message = getSerializationExceptionMessage(request);
            throw new CodecException(message, e);
        }
    }

    @Override
    public McpMessage decode(String json) {
        try {
            return parseMessage(json);
        } catch (JsonProcessingException e) {
            throw new CodecException(getDeserializationExceptionMessage(json), e);
        }
    }

    private McpMessage parseMessage(String json) throws JsonProcessingException {
        final JsonNode node = mapper.readTree(json);

        // Validate JSON-RPC version
        validateJsonMessage(node);

        // Detect message type and deserialize
        if (node.has(METHOD)) {
            return decodeRequest(node);
        } else if (node.has(RESULT)) {
            return decodeResponse(node);
        } else if (node.has(ERROR)) {
            return decodeErrorResponse(node);
        } else {
            throw new CodecException("Unknown message type: missing 'method', 'result', or 'error'");
        }
    }

    private void validateJsonMessage(JsonNode node) {
        if (!node.has(JSONRPC) || node.get(JSONRPC).isNull()) {
            throw new CodecException("Missing required field or value: 'jsonrpc'");
        }
        final JsonNode versionNode = node.get(JSONRPC);
        if (!JSONRPC_VERSION.equals(versionNode.asText())) {
            throw new CodecException("Invalid JSON-RPC version");
        }
        if (!node.has(ID) || node.get(ID).isNull()) {
            throw new CodecException("Missing required field: 'id'");
        }
        if (node.get(ID) == null) {
            throw new CodecException("Message ID must not be null");
        }
    }

    private McpRequest decodeRequest(JsonNode node) throws JsonProcessingException {
        final JsonNode methodNode = node.get(METHOD);
        if  (methodNode == null) {
            throw new CodecException("Missing method field value");
        }
        final var method = methodNode.asText();
        return switch (method) {
            case TOOLS_LIST -> mapper.treeToValue(node, ToolListRequest.class);
            case TOOLS_CALL -> mapper.treeToValue(node, ToolInvocationRequest.class);
            default -> throw new CodecException(STR."Unknown method: \{method}");
        };
    }

    private McpResponse decodeResponse(JsonNode node) throws JsonProcessingException {
        var resultNode = node.get(RESULT);
        if  (resultNode == null || resultNode.isNull()) {
            throw new CodecException("Missing required field: 'result'");
        }
        if (resultNode.has(TOOLS)) {
            return mapper.treeToValue(node, ToolListResponse.class);
        } else if (resultNode.has(CONTENT)) {
            return mapper.treeToValue(node, ToolInvocationResponse.class);
        } else {
            List<String> actualFields = new ArrayList<>();
            resultNode.fieldNames().forEachRemaining(actualFields::add);
            throw new CodecException(
                    "Unknown response structure. " +
                            "Expected 'tools' or 'content' in result, " +
                            "but found fields: " + actualFields);
        }
    }

    private McpResponse decodeErrorResponse(JsonNode node) {
        JsonNode errorNode = node.get(ERROR);
        validateErrorMessage(errorNode);
        var id = extractId(node);
        var code = errorNode.get(CODE).asInt();
        var message = errorNode.get(MESSAGE).asText();
        Object data = null;
        if (errorNode.has(DATA)) {
            data = errorNode.get(DATA);
        }
        return getErrorResponse(id, code, message, data);
    }

    private void validateErrorMessage(JsonNode node) {
        if (!node.has(CODE)) {
            throw new CodecException("Missing required field: 'code'");
        }
        if (!node.has(MESSAGE)) {
            throw new CodecException("Missing required field: 'message'");
        }
    }

    private McpErrorResponse getErrorResponse(Object id, int errorCode, String message, Object data) {
        final McpError error = new McpError(errorCode, message, data);
        return new McpErrorResponse(id, JSONRPC_VERSION, error);
    }

    private Object extractId(JsonNode node) {
        final JsonNode idNode = node.get(ID);
        if (idNode.isNumber()) {
            return idNode.longValue();
        } else {
            return idNode.asText();
        }
    }

    private String getSerializationExceptionMessage(McpRequest request) {
        return STR."Can't serialize request { id=\{request.id()}, method=\{request.method()} to json}";
    }

    private String getDeserializationExceptionMessage(String json) {
        return STR."Can't deserialize json \{json}";
    }

}
