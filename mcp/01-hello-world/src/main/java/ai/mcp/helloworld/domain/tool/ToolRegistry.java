package ai.mcp.helloworld.domain.tool;

import ai.mcp.helloworld.exception.InvalidToolParametersException;
import ai.mcp.helloworld.exception.ToolNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author lucas
 * @date 04/11/2025 20:50
 */
@Component
public class ToolRegistry {

    private static final Map<String, Tool> REGISTRY = new HashMap<>();

    public static Tool getTool(String name) {
        final var tool = REGISTRY.get(name);
        if (tool == null) {
            throw new ToolNotFoundException("Tool not found: " + name);
        }
        return tool;
    }

    public static List<Tool> getAllTools() {
        return new ArrayList<>(REGISTRY.values());
    }

    public static void register(String name, Tool tool) {
        if (name == null || name.isBlank()) {
            throw new InvalidToolParametersException("Tool name is null or blank");
        }
        if (tool == null) {
            throw new InvalidToolParametersException("Tool must not be null");
        }
        if (REGISTRY.containsKey(name)) {
            throw new InvalidToolParametersException("Tool already exists: " + name);
        }
        REGISTRY.put(name, tool);
    }

}
