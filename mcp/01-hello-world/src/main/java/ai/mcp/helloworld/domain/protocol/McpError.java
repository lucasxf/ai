package ai.mcp.helloworld.domain.protocol;

import java.io.Serializable;

/**
 * @author lucas
 * @date 06/12/2025 10:48
 */
public record McpError(int code, String message, Object data) implements Serializable {
}
