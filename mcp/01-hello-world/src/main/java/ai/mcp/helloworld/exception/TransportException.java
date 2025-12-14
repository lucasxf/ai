package ai.mcp.helloworld.exception;

import org.springframework.http.HttpStatus;

/**
 * @author lucas
 * @date 04/11/2025 21:04
 */
public class TransportException extends McpException {

    public TransportException(String message) {
        super(message);
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
