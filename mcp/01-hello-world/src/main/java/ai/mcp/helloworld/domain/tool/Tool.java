package ai.mcp.helloworld.domain.tool;

import java.util.List;
import java.util.Map;

/**
 * @author lucas
 * @date 04/11/2025 20:42
 */
public interface Tool {

    ToolDefinition getDefinition();

    List<ContentBlock> execute(Map<String, Object> arguments);

}
