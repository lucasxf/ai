package ai.mcp.helloworld.config;

import ai.mcp.helloworld.application.tools.AddTool;
import ai.mcp.helloworld.application.tools.MultiplyTool;
import ai.mcp.helloworld.application.tools.RandomTool;
import ai.mcp.helloworld.domain.tool.Tool;
import ai.mcp.helloworld.domain.tool.ToolRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for registering all available MCP tools.
 * <p>
 * This class initializes the {@link ToolRegistry} with all tool implementations
 * during Spring Boot application startup. Tools must be registered before the
 * MCP server starts accepting requests.
 * <p>
 * <strong>Registration Flow:</strong>
 * <ol>
 *     <li>Spring Boot initializes this @Configuration class</li>
 *     <li>@PostConstruct method executes after bean creation</li>
 *     <li>Each tool is instantiated and registered with its name</li>
 *     <li>ToolRegistry becomes populated and ready for requests</li>
 * </ol>
 * <p>
 * <strong>Adding New Tools:</strong>
 * To add a new tool:
 * <ol>
 *     <li>Create tool class extending {@link ai.mcp.helloworld.domain.tool.AbstractTool}</li>
 *     <li>Implement {@code getDefinition()} and {@code execute()} methods</li>
 *     <li>Add registration call in {@link #registerTools()}</li>
 * </ol>
 *
 * @author Lucas Xavier Ferreira
 * @date 20/12/2025
 * @see ToolRegistry
 * @see Tool
 */
@Slf4j
@Configuration
public class ToolRegistryConfig {

    /**
     * Registers all available MCP tools during application startup.
     * <p>
     * This method is invoked automatically by Spring after the bean is constructed.
     * It populates the {@link ToolRegistry} with all tool implementations.
     */
    @PostConstruct
    public void registerTools() {
        log.info("Registering MCP tools...");

        registerTool(new AddTool());
        registerTool(new MultiplyTool());
        registerTool(new RandomTool());

        log.info("Registered {} tools: {}",
                ToolRegistry.getAllTools().size(),
                ToolRegistry.getAllTools().stream()
                        .map(tool -> tool.getDefinition().name())
                        .toList());
    }

    private void registerTool(Tool tool) {
        final String toolName = tool.getDefinition().name();
        ToolRegistry.register(toolName, tool);
        log.debug("Registered tool: {}", toolName);
    }

}
