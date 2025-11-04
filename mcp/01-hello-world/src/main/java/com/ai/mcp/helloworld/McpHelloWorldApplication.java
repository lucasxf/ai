package com.ai.mcp.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for MCP Hello World POC.
 * <p>
 * This POC demonstrates basic MCP protocol implementation with:
 * - Java MCP Client (stdio transport)
 * - Java MCP Server (stdio transport)
 * - Simple calculator tools (add, multiply, random)
 * <p>
 * Goal: Understand MCP protocol basics and Spring Boot integration.
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025
 */
@SpringBootApplication
public class McpHelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpHelloWorldApplication.class, args);
    }

}
