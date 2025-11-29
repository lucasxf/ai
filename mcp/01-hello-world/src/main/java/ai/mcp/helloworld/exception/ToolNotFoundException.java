package ai.mcp.helloworld.exception;

import org.springframework.http.HttpStatus;

/**
 * @author lucas
 * @date 04/11/2025 21:02
 */
public class ToolNotFoundException extends McpException {

    public ToolNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
