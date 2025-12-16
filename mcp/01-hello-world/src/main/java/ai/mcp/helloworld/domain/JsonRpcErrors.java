package ai.mcp.helloworld.domain;

/**
 * @author lucas
 * @date 14/12/2025 11:49
 */
public enum JsonRpcErrors {

    INVALID_REQUEST(-32_600, "Invalid Request"),
    METHOD_NOT_FOUND(-32_601, "Method not found"),
    INVALID_PARAMS(-32_602, "Invalid parameters"),
    INTERNAL_ERROR(-32_603,"Internal error");

    private final int code;
    private final String message;

    JsonRpcErrors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
