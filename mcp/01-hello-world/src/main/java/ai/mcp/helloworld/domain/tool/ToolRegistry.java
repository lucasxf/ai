package ai.mcp.helloworld.domain.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucas
 * @date 04/11/2025 20:50
 */
public class ToolRegistry {

    private static final Map<String, Tool> REGISTRY = new HashMap<>();

    public static Tool getTool(String name) {
        Tool tool = REGISTRY.get(name);
        if (tool == null) {
            throw new RuntimeException("Tool not found: " + name);
        }
        return tool;
    }

    public static void register(String name, Tool tool) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Tool name is null or blank");
        }
        if (tool == null) {
            throw new RuntimeException("Tool must not be null");
        }
        if (REGISTRY.containsKey(name)) {
            throw new RuntimeException("Tool already exists: " + name);
        }
        REGISTRY.put(name, tool);
    }

}
