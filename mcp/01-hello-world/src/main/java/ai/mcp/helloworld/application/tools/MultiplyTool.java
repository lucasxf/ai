package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

/**
 * @author lucas
 * @date 04/11/2025 21:00
 */
public class MultiplyTool implements Tool {

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition(
                "Multiplication",
                "Multiplies 2 numbers and returns their product",
                "schema");
    }

    public long multiply(int a, int b) {
        return (long) a * b;
    }

}
