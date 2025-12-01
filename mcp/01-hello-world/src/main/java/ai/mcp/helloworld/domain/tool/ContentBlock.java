package ai.mcp.helloworld.domain.tool;

/**
 * @author lucas
 * @date 29/11/2025 05:40
 */
public sealed interface ContentBlock {

    record TextContent(String text) implements ContentBlock {}

    record ImageContent(String data, String mimeType) implements ContentBlock {}

    record ResourceContent(String uri) implements ContentBlock {}

}
