package ai.mcp.helloworld.exception;

import org.springframework.http.HttpStatus;

/**
 * @author lucas
 * @date 16/12/2025
 */
public class McpServerException extends McpException {
    public McpServerException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
