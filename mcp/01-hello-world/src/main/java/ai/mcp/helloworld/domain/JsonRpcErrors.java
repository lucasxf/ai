package ai.mcp.helloworld.domain;

/**
 * @author lucas
 * @date 14/12/2025 11:49
 */
public enum JsonRpcErrors {

    METHOD_NOT_FOUND(32_601),
    INVALID_PARAMS(32_601),
    INTERNAL_ERROR(32_603);

    private final int code;

    JsonRpcErrors(int code) { this.code = code; }

    public int toCode() { return code; }

}
