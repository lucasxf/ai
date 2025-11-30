package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.AbstractTool;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author lucas
 * @date 04/11/2025 20:59
 */
public class AddTool extends AbstractTool {

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition(
                "add",
                "The Add Tool adds 2 numbers and returns their sum",
                "{}");
    }

    @Override
    public List<ContentBlock> execute(Map<String, Object> arguments) {
        final int a = extractInt(arguments, "a");
        final int b = extractInt(arguments, "b");
        final int result = add(a, b);
        return textResult(result);
    }

    private int add(int a, int b) {
        return a + b;
    }

}
