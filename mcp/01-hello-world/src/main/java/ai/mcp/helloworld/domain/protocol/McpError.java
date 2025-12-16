package ai.mcp.helloworld.domain.protocol;

import ai.mcp.helloworld.domain.JsonRpcErrors;

import java.io.Serializable;

/**
 * @author lucas
 * @date 06/12/2025 10:48
 */
public record McpError(int code, String message, Object data) implements Serializable {

    public static McpError of(JsonRpcErrors error, Object data) {
        return of(error.code(), error.message(), data);
    }

    public static McpError of(int code, String message, Object data) {
        return new McpError(code, message, data);
    }

}
