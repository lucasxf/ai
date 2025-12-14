package ai.mcp.helloworld.domain.tool;

import ai.mcp.helloworld.domain.tool.ContentBlock.TextContent;
import ai.mcp.helloworld.exception.InvalidToolParametersException;

import java.util.List;
import java.util.Map;

/**
 * @author lucas
 * @date 29/11/2025 05:38
 */
public abstract class AbstractTool implements Tool {

    protected int extractInt(Map<String, Object> arguments, String key) {
        Object value = validateValue(arguments, key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new InvalidToolParametersException("Parameter " + key + " must be integer");
    }

    protected long extractLong(Map<String, Object> arguments, String key) {
        Object value = validateValue(arguments, key);
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new InvalidToolParametersException("Parameter " + key + " must be integer");
    }

    protected double extractDouble(Map<String, Object> arguments, String key) {
        Object value = validateValue(arguments, key);
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new InvalidToolParametersException("Parameter " + key + " must be double");
    }

    protected String extractString(Map<String, Object> arguments, String key) {
        Object value = validateValue(arguments, key);
        if (value instanceof String) {
            return (String) value;
        }
        throw new InvalidToolParametersException("Parameter " + key + " must be string");
    }

    protected List<ContentBlock> textResult(Object result) {
        return List.of(new TextContent(result.toString()));
    }

    private Object validateValue(Map<String, Object> arguments, String key) {
        if (arguments == null) {
            throw new InvalidToolParametersException("Arguments can't be null");
        }
        if (key == null) {
            throw new InvalidToolParametersException("Key can't be null");
        }
        Object value = arguments.get(key);
        if (value == null) {
            throw new InvalidToolParametersException("Value can't be null");
        }
        return value;
    }

}
