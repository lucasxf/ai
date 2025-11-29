package ai.mcp.helloworld.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lucas
 * @date 04/11/2025 20:09
 */
@Getter
@Setter
@ConfigurationProperties("client")
public class McpClientProperties {
}
