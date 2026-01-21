package ai.mcp.helloworld.domain.tool;

import ai.mcp.helloworld.exception.InvalidToolParametersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link AbstractTool} parameter extraction methods.
 * <p>
 * Uses a concrete test implementation to verify protected method behavior.
 *
 * @author lucas
 * @date 16/01/2026
 */
class AbstractToolTest {

    private TestTool testTool;

    @BeforeEach
    void setUp() {
        testTool = new TestTool();
    }

    @Nested
    @DisplayName("extractInt")
    class ExtractIntTests {

        @Test
        @DisplayName("should extract Integer value directly")
        void shouldExtractIntegerValueDirectly() {
            Map<String, Object> args = Map.of("value", 42);

            int result = testTool.testExtractInt(args, "value");

            assertThat(result).isEqualTo(42);
        }

        @Test
        @DisplayName("should extract int from Long value")
        void shouldExtractIntFromLongValue() {
            Map<String, Object> args = Map.of("value", 42L);

            int result = testTool.testExtractInt(args, "value");

            assertThat(result).isEqualTo(42);
        }

        @Test
        @DisplayName("should extract int from Double value")
        void shouldExtractIntFromDoubleValue() {
            Map<String, Object> args = Map.of("value", 42.9);

            int result = testTool.testExtractInt(args, "value");

            assertThat(result).isEqualTo(42); // truncates decimal
        }

        @Test
        @DisplayName("should throw when value is String")
        void shouldThrowWhenValueIsString() {
            Map<String, Object> args = Map.of("value", "not a number");

            assertThatThrownBy(() -> testTool.testExtractInt(args, "value"))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("must be integer");
        }

        @Test
        @DisplayName("should throw when key is missing")
        void shouldThrowWhenKeyIsMissing() {
            Map<String, Object> args = Map.of("other", 42);

            assertThatThrownBy(() -> testTool.testExtractInt(args, "value"))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("can't be null");
        }

        @Test
        @DisplayName("should throw when arguments map is null")
        void shouldThrowWhenArgumentsMapIsNull() {
            assertThatThrownBy(() -> testTool.testExtractInt(null, "value"))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("Arguments can't be null");
        }

        @Test
        @DisplayName("should throw when key is null")
        void shouldThrowWhenKeyIsNull() {
            Map<String, Object> args = Map.of("value", 42);

            assertThatThrownBy(() -> testTool.testExtractInt(args, null))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("Key can't be null");
        }

    }

    @Nested
    @DisplayName("extractLong")
    class ExtractLongTests {

        @Test
        @DisplayName("should extract Long value directly")
        void shouldExtractLongValueDirectly() {
            Map<String, Object> args = Map.of("value", 9_999_999_999L);

            long result = testTool.testExtractLong(args, "value");

            assertThat(result).isEqualTo(9_999_999_999L);
        }

        @Test
        @DisplayName("should extract long from Integer value")
        void shouldExtractLongFromIntegerValue() {
            Map<String, Object> args = Map.of("value", 42);

            long result = testTool.testExtractLong(args, "value");

            assertThat(result).isEqualTo(42L);
        }

        @Test
        @DisplayName("should throw when value is String")
        void shouldThrowWhenValueIsString() {
            Map<String, Object> args = Map.of("value", "not a number");

            assertThatThrownBy(() -> testTool.testExtractLong(args, "value"))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("must be integer");
        }

    }

    @Nested
    @DisplayName("extractDouble")
    class ExtractDoubleTests {

        @Test
        @DisplayName("should extract Double value directly")
        void shouldExtractDoubleValueDirectly() {
            Map<String, Object> args = Map.of("value", 3.14159);

            double result = testTool.testExtractDouble(args, "value");

            assertThat(result).isEqualTo(3.14159);
        }

        @Test
        @DisplayName("should extract double from Integer value")
        void shouldExtractDoubleFromIntegerValue() {
            Map<String, Object> args = Map.of("value", 42);

            double result = testTool.testExtractDouble(args, "value");

            assertThat(result).isEqualTo(42.0);
        }

        @Test
        @DisplayName("should extract double from Long value")
        void shouldExtractDoubleFromLongValue() {
            Map<String, Object> args = Map.of("value", 42L);

            double result = testTool.testExtractDouble(args, "value");

            assertThat(result).isEqualTo(42.0);
        }

        @Test
        @DisplayName("should throw when value is String")
        void shouldThrowWhenValueIsString() {
            Map<String, Object> args = Map.of("value", "not a number");

            assertThatThrownBy(() -> testTool.testExtractDouble(args, "value"))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("must be double");
        }

    }

    @Nested
    @DisplayName("extractString")
    class ExtractStringTests {

        @Test
        @DisplayName("should extract String value directly")
        void shouldExtractStringValueDirectly() {
            Map<String, Object> args = Map.of("value", "hello world");

            String result = testTool.testExtractString(args, "value");

            assertThat(result).isEqualTo("hello world");
        }

        @Test
        @DisplayName("should throw when value is Integer")
        void shouldThrowWhenValueIsInteger() {
            Map<String, Object> args = Map.of("value", 42);

            assertThatThrownBy(() -> testTool.testExtractString(args, "value"))
                    .isInstanceOf(InvalidToolParametersException.class)
                    .hasMessageContaining("must be string");
        }

    }

    @Nested
    @DisplayName("textResult")
    class TextResultTests {

        @Test
        @DisplayName("should create TextContent from integer result")
        void shouldCreateTextContentFromIntegerResult() {
            List<ContentBlock> result = testTool.testTextResult(42);

            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isInstanceOf(ContentBlock.TextContent.class);
            assertThat(((ContentBlock.TextContent) result.get(0)).text()).isEqualTo("42");
        }

        @Test
        @DisplayName("should create TextContent from String result")
        void shouldCreateTextContentFromStringResult() {
            List<ContentBlock> result = testTool.testTextResult("hello");

            assertThat(result).hasSize(1);
            assertThat(((ContentBlock.TextContent) result.get(0)).text()).isEqualTo("hello");
        }

    }

    /**
     * Concrete implementation of AbstractTool for testing protected methods.
     */
    private static class TestTool extends AbstractTool {

        @Override
        public ToolDefinition getDefinition() {
            return new ToolDefinition("test", "Test tool for unit testing", "{}");
        }

        @Override
        public List<ContentBlock> execute(Map<String, Object> arguments) {
            return List.of();
        }

        // Expose protected methods for testing
        int testExtractInt(Map<String, Object> args, String key) {
            return extractInt(args, key);
        }

        long testExtractLong(Map<String, Object> args, String key) {
            return extractLong(args, key);
        }

        double testExtractDouble(Map<String, Object> args, String key) {
            return extractDouble(args, key);
        }

        String testExtractString(Map<String, Object> args, String key) {
            return extractString(args, key);
        }

        List<ContentBlock> testTextResult(Object result) {
            return textResult(result);
        }

    }

}
