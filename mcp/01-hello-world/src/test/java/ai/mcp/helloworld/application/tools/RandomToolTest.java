package ai.mcp.helloworld.application.tools;

import ai.mcp.helloworld.domain.tool.ContentBlock;
import ai.mcp.helloworld.domain.tool.ContentBlock.TextContent;
import ai.mcp.helloworld.exception.InvalidToolParametersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link RandomTool}.
 *
 * @author lucas
 * @date 16/01/2026
 */
class RandomToolTest {

    private RandomTool randomTool;

    @BeforeEach
    void setUp() {
        randomTool = new RandomTool();
    }

    @Test
    @DisplayName("should have correct definition")
    void shouldHaveCorrectDefinition() {
        var definition = randomTool.getDefinition();

        assertThat(definition.name()).isEqualTo("random");
        assertThat(definition.description()).containsIgnoringCase("random");
    }

    @Test
    @DisplayName("should return result within bounds")
    void shouldReturnResultWithinBounds() {
        Map<String, Object> args = Map.of("x", 100);

        List<ContentBlock> result = randomTool.execute(args);

        assertThat(result).hasSize(1);
        int value = Integer.parseInt(((TextContent) result.get(0)).text());
        assertThat(value).isGreaterThanOrEqualTo(0);
        assertThat(value).isLessThan(100);
    }

    @RepeatedTest(20)
    @DisplayName("should always return value within bounds (repeated)")
    void shouldAlwaysReturnValueWithinBounds() {
        Map<String, Object> args = Map.of("x", 50);

        List<ContentBlock> result = randomTool.execute(args);

        int value = Integer.parseInt(((TextContent) result.get(0)).text());
        assertThat(value).isGreaterThanOrEqualTo(0);
        assertThat(value).isLessThan(50);
    }

    @Test
    @DisplayName("should return zero when upper bound is 1")
    void shouldReturnZeroWhenUpperBoundIsOne() {
        Map<String, Object> args = Map.of("x", 1);

        List<ContentBlock> result = randomTool.execute(args);

        int value = Integer.parseInt(((TextContent) result.get(0)).text());
        assertThat(value).isEqualTo(0);
    }

    @Test
    @DisplayName("should accept Long values")
    void shouldAcceptLongValues() {
        Map<String, Object> args = Map.of("x", 100L);

        List<ContentBlock> result = randomTool.execute(args);

        int value = Integer.parseInt(((TextContent) result.get(0)).text());
        assertThat(value).isGreaterThanOrEqualTo(0);
        assertThat(value).isLessThan(100);
    }

    @Test
    @DisplayName("should throw when parameter x is missing")
    void shouldThrowWhenParameterXIsMissing() {
        Map<String, Object> args = Map.of("y", 100);

        assertThatThrownBy(() -> randomTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class);
    }

    @Test
    @DisplayName("should throw when parameter is not a number")
    void shouldThrowWhenParameterIsNotANumber() {
        Map<String, Object> args = Map.of("x", "hundred");

        assertThatThrownBy(() -> randomTool.execute(args))
                .isInstanceOf(InvalidToolParametersException.class);
    }

    @Test
    @DisplayName("getRandom should return value in range")
    void getRandomShouldReturnValueInRange() {
        int result = randomTool.getRandom(10);

        assertThat(result).isGreaterThanOrEqualTo(0);
        assertThat(result).isLessThan(10);
    }

}
