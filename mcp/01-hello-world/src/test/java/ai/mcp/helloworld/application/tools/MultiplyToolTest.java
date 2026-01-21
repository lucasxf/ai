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
 * Unit tests for {@link MultiplyTool}.
 *
 * @author lucas
 * @date 16/01/2026
 */
class MultiplyToolTest {

    private MultiplyTool multiplyTool;

    @BeforeEach
    void setUp() {
        multiplyTool = new MultiplyTool();
    }

    @Test
    @DisplayName("should have correct definition")
    void shouldHaveCorrectDefinition() {
        var definition = multiplyTool.getDefinition();

        assertThat(definition.name()).isEqualTo("multiply");
        assertThat(definition.description()).containsIgnoringCase("multipl");
    }

    @Test
    @DisplayName("should multiply two positive numbers")
    void shouldMultiplyTwoPositiveNumbers() {
        Map<String, Object> args = Map.of("a", 7, "b", 6);

        List<ContentBlock> result = multiplyTool.execute(args);

        assertThat(result).hasSize(1);
        assertThat(((TextContent) result.get(0)).text()).isEqualTo("42");
    }

    @Test
    @DisplayName("should multiply negative and positive numbers")
    void shouldMultiplyNegativeAndPositiveNumbers() {
        Map<String, Object> args = Map.of("a", -5, "b", 4);

        List<ContentBlock> result = multiplyTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("-20");
    }

    @Test
    @DisplayName("should multiply two negative numbers")
    void shouldMultiplyTwoNegativeNumbers() {
        Map<String, Object> args = Map.of("a", -3, "b", -4);

        List<ContentBlock> result = multiplyTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("12");
    }

    @Test
    @DisplayName("should handle multiplication by zero")
    void shouldHandleMultiplicationByZero() {
        Map<String, Object> args = Map.of("a", 999, "b", 0);

        List<ContentBlock> result = multiplyTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("0");
    }

    @Test
    @DisplayName("should handle large numbers with Long type")
    void shouldHandleLargeNumbersWithLongType() {
        // Test values that exceed Integer.MAX_VALUE when multiplied
        Map<String, Object> args = Map.of("a", 100_000L, "b", 100_000L);

        List<ContentBlock> result = multiplyTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("10000000000");
    }

    @Test
    @DisplayName("should accept Integer values and convert to long")
    void shouldAcceptIntegerValuesAndConvertToLong() {
        Map<String, Object> args = Map.of("a", 7, "b", 6);

        List<ContentBlock> result = multiplyTool.execute(args);

        assertThat(((TextContent) result.get(0)).text()).isEqualTo("42");
    }

    @Test
    @DisplayName("should throw when parameter a is missing")
    void shouldThrowWhenParameterAIsMissing() {
        Map<String, Object> args = Map.of("b", 6);

        assertThatThrownBy(() -> multiplyTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class);
    }

    @Test
    @DisplayName("should throw when parameter is not a number")
    void shouldThrowWhenParameterIsNotANumber() {
        Map<String, Object> args = Map.of("a", "seven", "b", 6);

        assertThatThrownBy(() -> multiplyTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class);
    }

}
