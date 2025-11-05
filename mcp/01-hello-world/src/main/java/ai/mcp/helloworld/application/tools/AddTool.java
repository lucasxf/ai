package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

/**
 * @author lucas
 * @date 04/11/2025 20:59
 */
public class AddTool implements Tool {

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition(
                "Addition",
                "Adds 2 numbers and returns their sum",
                "schema");
    }

    public int add(int a, int b) {
        return a + b;
    }

}
