package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.AbstractTool;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author lucas
 * @date 04/11/2025 21:00
 */
public class MultiplyTool extends AbstractTool {

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition(
                "multiply",
                "Multiplies 2 numbers and returns their product",
                "schema");
    }

    @Override
    public List<ContentBlock> execute(Map<String, Object> arguments) {
        final long a = extractLong(arguments, "a");
        final long b = extractLong(arguments, "b");
        final long result = multiply(a, b);
        return textResult(result);
    }

    private long multiply(long a, long b) {
        return a * b;
    }

}
