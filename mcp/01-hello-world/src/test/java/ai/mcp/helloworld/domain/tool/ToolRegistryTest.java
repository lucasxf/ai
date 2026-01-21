package ai.mcp.helloworld.domain.tool;

import ai.mcp.helloworld.exception.InvalidToolParametersException;
import ai.mcp.helloworld.exception.ToolNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ToolRegistry}.
 * <p>
 * Tests tool registration, lookup, and validation behavior.
 *
 * @author lucas
 * @date 16/01/2026
 */
class ToolRegistryTest {

    private ToolRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ToolRegistry();
        // Clear static registry before each test (test isolation)
        ToolRegistry.clear();
    }

    @Nested
    @DisplayName("register")
    class RegisterTests {

        @Test
        @DisplayName("should register tool successfully")
        void shouldRegisterToolSuccessfully() {
            Tool tool = createTestTool("calculator");

            ToolRegistry.register("calculator", tool);

            Tool retrieved = ToolRegistry.getTool("calculator");
            assertThat(retrieved).isSameAs(tool);
        }

        @Test
        @DisplayName("should throw when name is null")
        void shouldThrowWhenNameIsNull() {
            Tool tool = createTestTool("test");

            assertThatThrownBy(() -> ToolRegistry.register(null, tool))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("null or blank");
        }

        @Test
        @DisplayName("should throw when name is blank")
        void shouldThrowWhenNameIsBlank() {
            Tool tool = createTestTool("test");

            assertThatThrownBy(() -> ToolRegistry.register("   ", tool))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("null or blank");
        }

        @Test
        @DisplayName("should throw when tool is null")
        void shouldThrowWhenToolIsNull() {
            assertThatThrownBy(() -> ToolRegistry.register("test", null))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("must not be null");
        }

        @Test
        @DisplayName("should throw when registering duplicate tool name")
        void shouldThrowWhenRegisteringDuplicateToolName() {
            Tool tool1 = createTestTool("add");
            Tool tool2 = createTestTool("add");

            ToolRegistry.register("add", tool1);

            assertThatThrownBy(() -> ToolRegistry.register("add", tool2))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("already exists");
        }

    }

    @Nested
    @DisplayName("getTool")
    class GetToolTests {

        @Test
        @DisplayName("should return registered tool")
        void shouldReturnRegisteredTool() {
            Tool tool = createTestTool("multiply");
            ToolRegistry.register("multiply", tool);

            Tool result = ToolRegistry.getTool("multiply");

            assertThat(result).isSameAs(tool);
        }

        @Test
        @DisplayName("should throw when tool not found")
        void shouldThrowWhenToolNotFound() {
            assertThatThrownBy(() -> ToolRegistry.getTool("nonexistent"))
                    .isInstanceOf(ToolNotFoundException.class)
                    .hasMessageContaining("not found")
                    .hasMessageContaining("nonexistent");
        }

    }

    @Nested
    @DisplayName("getAllTools")
    class GetAllToolsTests {

        @Test
        @DisplayName("should return empty list when no tools registered")
        void shouldReturnEmptyListWhenNoToolsRegistered() {
            List<Tool> tools = ToolRegistry.getAllTools();

            assertThat(tools).isEmpty();
        }

        @Test
        @DisplayName("should return all registered tools")
        void shouldReturnAllRegisteredTools() {
            Tool add = createTestTool("add");
            Tool multiply = createTestTool("multiply");
            Tool random = createTestTool("random");

            ToolRegistry.register("add", add);
            ToolRegistry.register("multiply", multiply);
            ToolRegistry.register("random", random);

            List<Tool> tools = ToolRegistry.getAllTools();

            assertThat(tools).hasSize(3);
            assertThat(tools).containsExactlyInAnyOrder(add, multiply, random);
        }

        @Test
        @DisplayName("should return defensive copy")
        void shouldReturnDefensiveCopy() {
            Tool tool = createTestTool("test");
            ToolRegistry.register("test", tool);

            List<Tool> tools = ToolRegistry.getAllTools();
            tools.clear(); // Modify the returned list

            // Registry should still have the tool
            assertThat(ToolRegistry.getAllTools()).hasSize(1);
        }

    }

    /**
     * Creates a simple test tool with the given name.
     */
    private Tool createTestTool(String name) {
        return new Tool() {
            @Override
            public ToolDefinition getDefinition() {
                return new ToolDefinition(name, "Test tool: " + name, "{}");
            }

            @Override
            public List<ContentBlock> execute(Map<String, Object> arguments) {
                return List.of(new ContentBlock.TextContent("result"));
            }
        };
    }

}
