package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolDefinition;

/**
 * @author lucas
 * @date 04/11/2025 21:00
 */
public class RandomTool implements Tool {

    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition(
                "Random",
                "Generates a random number between 0 and the upper bount (not inclusive)",
                "schema");
    }

    public int getRandom(int upperBound) {
        return (int) (Math.random() * upperBound);
    }

}
