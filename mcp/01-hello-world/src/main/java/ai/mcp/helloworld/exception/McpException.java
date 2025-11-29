package ai.mcp.helloworld.exception;

import org.springframework.http.HttpStatus;

/**
 * @author lucas
 * @date 04/11/2025 21:01
 */
public abstract class McpException extends RuntimeException {

    public McpException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();

}
