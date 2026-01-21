package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ContentBlock.TextContent;
import ai.mcp.helloworld.exception.InvalidToolParametersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link AddTool}.
 *
 * @author lucas
 * @date 16/01/2026
 */
class AddToolTest {

    private AddTool addTool;

    @BeforeEach
    void setUp() {
        addTool = new AddTool();
    }

    @Test
    @DisplayName("should have correct definition")
    void shouldHaveCorrectDefinition() {
        var definition = addTool.getDefinition();

        assertThat(definition.name()).isEqualTo("add");
        assertThat(definition.description()).contains("add", "sum");
    }

    @Test
    @DisplayName("should add two positive integers")
    void shouldAddTwoPositiveIntegers() {
        Map<String, Object> args = Map.of("a", 5, "b", 3);

        List<ContentBlock> result = addTool.execute(args);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isInstanceOf(TextContent.class);
        assertThat(((TextContent) result.get(0)).text()).isEqualTo("8");
    }

    @Test
    @DisplayName("should add negative and positive integers")
    void shouldAddNegativeAndPositiveIntegers() {
        Map<String, Object> args = Map.of("a", -10, "b", 25);

        List<ContentBlock> result = addTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("15");
    }

    @Test
    @DisplayName("should add two negative integers")
    void shouldAddTwoNegativeIntegers() {
        Map<String, Object> args = Map.of("a", -5, "b", -7);

        List<ContentBlock> result = addTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("-12");
    }

    @Test
    @DisplayName("should handle zero")
    void shouldHandleZero() {
        Map<String, Object> args = Map.of("a", 0, "b", 42);

        List<ContentBlock> result = addTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("42");
    }

    @Test
    @DisplayName("should accept Long values and convert to int")
    void shouldAcceptLongValuesAndConvertToInt() {
        Map<String, Object> args = Map.of("a", 5L, "b", 3L);

        List<ContentBlock> result = addTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("8");
    }

    @Test
    @DisplayName("should throw when parameter a is missing")
    void shouldThrowWhenParameterAIsMissing() {
        Map<String, Object> args = Map.of("b", 3);

        assertThatThrownBy(() -> addTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class);
    }

    @Test
    @DisplayName("should throw when parameter b is missing")
    void shouldThrowWhenParameterBIsMissing() {
        Map<String, Object> args = Map.of("a", 5);

        assertThatThrownBy(() -> addTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class);
    }

    @Test
    @DisplayName("should throw when parameter is not a number")
    void shouldThrowWhenParameterIsNotANumber() {
        Map<String, Object> args = Map.of("a", "five", "b", 3);

        assertThatThrownBy(() -> addTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class)
                .hasMessageContaining("must be integer");
    }

}
