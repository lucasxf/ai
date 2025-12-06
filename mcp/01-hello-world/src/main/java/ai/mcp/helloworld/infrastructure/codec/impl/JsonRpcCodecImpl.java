package ai.mcp.helloworld.infrastructure.codec.impl;

import ai.mcp.helloworld.domain.protocol.McpMessage;
import ai.mcp.helloworld.domain.protocol.McpRequest;
import ai.mcp.helloworld.infrastructure.codec.CodecException;
import ai.mcp.helloworld.infrastructure.codec.JsonRpcCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * @author lucas
 * @date 01/12/2025 18:41
 */
@Service
public class JsonRpcCodecImpl implements JsonRpcCodec {

    private final ObjectMapper mapper;

    public JsonRpcCodecImpl(ObjectMapper mapper) { this.mapper = mapper; }

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
            return mapper.readValue(json, McpMessage.class);
        } catch (Exception e) {
            final var message = getDeserializationExceptionMessage(json);
            throw new CodecException(message, e);
        }
    }

    private String getSerializationExceptionMessage(McpRequest request) {
        return STR."Can't serialize request { id=\{request.id()}, method=\{request.method()} to json}";
    }

    private String getDeserializationExceptionMessage(String json) {
        return STR."Can't deserialize json \{json}";
    }

}
