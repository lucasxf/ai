package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.AbstractTool;
import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author lucas
 * @date 04/11/2025 21:00
 */
public class RandomTool extends AbstractTool {

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition(
                "random",
                "Generates a random number between 0 and the upper bound (not inclusive)",
                "schema");
    }

    @Override
    public List<ContentBlock> execute(Map<String, Object> arguments) {
        final int x = extractInt(arguments, "x");
        final int result = getRandom(x);
        return textResult(result);
    }

    public int getRandom(int upperBound) {
        return (int) (Math.random() * upperBound);
    }

}
