package ai.mcp.helloworld.domain.tool;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Sealed interface representing different types of content blocks in MCP tool responses.
 * <p>
 * Uses Jackson polymorphic deserialization to automatically detect concrete type
 * based on JSON structure:
 * <ul>
 *     <li>{@code {"text": "..."}} → {@link TextContent}</li>
 *     <li>{@code {"data": "...", "mimeType": "..."}} → {@link ImageContent}</li>
 *     <li>{@code {"uri": "..."}} → {@link ResourceContent}</li>
 * </ul>
 *
 * @author Lucas Xavier Ferreira
 * @date 29/11/2025 05:40
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.DEDUCTION
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContentBlock.TextContent.class, name = "text"),
        @JsonSubTypes.Type(value = ContentBlock.ImageContent.class, name = "image"),
        @JsonSubTypes.Type(value = ContentBlock.ResourceContent.class, name = "resource")
})
public sealed interface ContentBlock {

    record TextContent(String text) implements ContentBlock {}

    record ImageContent(String data, String mimeType) implements ContentBlock {}

    record ResourceContent(String uri) implements ContentBlock {}

}
