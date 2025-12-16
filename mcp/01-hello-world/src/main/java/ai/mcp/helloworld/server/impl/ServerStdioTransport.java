package ai.mcp.helloworld.server.impl;

import ai.mcp.helloworld.exception.TransportException;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Lucas Xavier Ferreira
 * @date 16/12/2025
 */
@Component
public class ServerStdioTransport implements Transport {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerStdioTransport() {
        // Server reads from System.in (parent sends requests here)
        this.reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );

        // Server writes to System.out (parent reads responses here)
        this.writer = new BufferedWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
        );
    }

    @Override
    public void send(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            throw new TransportException("Failed to send message via STDIO transport", e);
        }
    }

    @Override
    public String receive() {
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new TransportException("End of stream reached while reading from STDIO transport");
            }
            return line;
        } catch (IOException e) {
            throw new TransportException("Failed to receive message via STDIO transport", e);
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void close() {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            throw new TransportException("Failed to close STDIO transport", e);
        }
    }

}
