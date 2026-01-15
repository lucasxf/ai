package ai.mcp.helloworld.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when MCP client operations fail.
 * <p>
 * This exception is thrown when:
 * <ul>
 *     <li>Server returns an error response ({@link ai.mcp.helloworld.domain.protocol.McpErrorResponse})</li>
 *     <li>Unexpected response type is received (not {@link ai.mcp.helloworld.domain.protocol.ToolListResponse} or {@link ai.mcp.helloworld.domain.protocol.ToolInvocationResponse})</li>
 *     <li>Client-side validation fails</li>
 * </ul>
 * <p>
 * For transport-level failures (I/O errors, process crashed), {@link TransportException} is used instead.
 * For JSON encoding/decoding failures, {@link ai.mcp.helloworld.infrastructure.codec.CodecException} is used.
 *
 * @author Lucas Xavier Ferreira
 * @date 14/01/2026
 * @see TransportException
 * @see ai.mcp.helloworld.infrastructure.codec.CodecException
 */
public class McpClientException extends McpException {

    public McpClientException(String message) {
        super(message);
    }

    public McpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
