package ai.mcp.helloworld.exception;

public class McpServerException extends RuntimeException {
  public McpServerException(String message) {
    super(message);
  }
}
