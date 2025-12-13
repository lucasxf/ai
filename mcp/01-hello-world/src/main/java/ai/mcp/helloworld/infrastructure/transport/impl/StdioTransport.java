package ai.mcp.helloworld.infrastructure.transport.impl;

import ai.mcp.helloworld.exception.TransportException;
import ai.mcp.helloworld.infrastructure.transport.Transport;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implementação de transporte baseada em stdio (stdin/stdout) para comunicação MCP.
 * <p>
 * Esta implementação spawna um processo filho (servidor MCP) e comunica-se através
 * das streams padrão do processo:
 * <ul>
 *     <li><strong>stdin</strong> - Envia mensagens JSON-RPC ao servidor (write + flush)</li>
 *     <li><strong>stdout</strong> - Recebe respostas JSON-RPC do servidor (read line-by-line)</li>
 *     <li><strong>stderr</strong> - Captura logs de erro do servidor (Virtual Thread assíncrono)</li>
 * </ul>
 * <p>
 * <strong>Características de implementação:</strong>
 * <ul>
 *     <li>Encoding UTF-8 explícito em todas as streams</li>
 *     <li>Buffered I/O para eficiência (BufferedWriter/BufferedReader)</li>
 *     <li>Flush automático após cada mensagem (garante envio imediato)</li>
 *     <li>Leitura assíncrona de stderr via Virtual Thread (previne deadlock de buffer)</li>
 *     <li>Shutdown gracioso com timeout (destroy → wait → destroyForcibly)</li>
 *     <li>Thread-safe: método send() sincronizado para prevenir interleaving</li>
 * </ul>
 * <p>
 * <strong>Exemplo de uso:</strong>
 * <pre>{@code
 * List<String> command = List.of("java", "-jar", "mcp-server.jar");
 * try (StdioTransport transport = new StdioTransport(command)) {
 *     transport.send("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/list\"}");
 *     String response = transport.receive();
 *     System.out.println("Server response: " + response);
 * }
 * }</pre>
 * <p>
 * <strong>Tratamento de erros:</strong>
 * <ul>
 *     <li>Se o processo filho morrer (EOF em stdout), {@link #receive()} lança {@link TransportException}</li>
 *     <li>Se o buffer de stderr encher, a Virtual Thread continua lendo (previne deadlock)</li>
 *     <li>Erros de I/O durante send/receive são wrapeados em {@link TransportException}</li>
 * </ul>
 *
 * @author Lucas Xavier Ferreira
 * @date 04/11/2025 20:55
 * @see Transport
 * @see ProcessBuilder
 */
@Slf4j
public class StdioTransport implements Transport {

    private final Process process;
    private final BufferedWriter writer;
    private final BufferedReader reader;
    private final BufferedReader errorReader;

    private volatile boolean running = true;

    /**
     * Cria o transporte stdio spawneando um processo servidor MCP.
     * <p>
     * Este construtor:
     * <ol>
     *     <li>Spawna o processo usando {@link ProcessBuilder}</li>
     *     <li>Configura streams buffered com encoding UTF-8</li>
     *     <li>Inicia Virtual Thread para leitura assíncrona de stderr</li>
     * </ol>
     * <p>
     * O processo filho herda o environment do processo pai (variáveis de ambiente).
     * O working directory padrão é o diretório atual do processo pai.
     * <p>
     * <strong>Exemplo de comando:</strong>
     * <pre>{@code
     * // Java server
     * List.of("java", "-jar", "mcp-server.jar")
     *
     * // Python server
     * List.of("python", "server.py")
     *
     * // Node.js server
     * List.of("node", "server.js")
     * }</pre>
     *
     * @param command comando e argumentos do servidor MCP (primeiro elemento é o executável)
     * @throws TransportException se falhar ao spawnar o processo (comando inválido, arquivo não encontrado, etc.)
     */
    public StdioTransport(List<String> command) throws TransportException {
        try {
            log.info("Spawning MCP server process: {}", command);

            ProcessBuilder pb = new ProcessBuilder(command);
            this.process = pb.start();

            // Setup I/O streams
            this.writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8)
            );

            this.reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );

            this.errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)
            );

            log.info("MCP server process spawned successfully");
            startStderrReader();
        } catch (IOException e) {
            throw new TransportException("Failed to spawn server process", e);
        }
    }

    @Override
    public synchronized void send(String message) {
        try {
            writer.write(message);
            /*
            MCP protocol uses newline-delimited JSON.
            Each message must end with \n so the receiver knows
            where one message ends and the next begins.
             */
            writer.newLine();
            // immediate send
            writer.flush();
        } catch (IOException e) {
            var errorMessage = STR."Failed to send message '\{message}' to server";
            throw new TransportException(errorMessage, e);
        }
    }

    @Override
    public String receive() {
        String message;
        try {
            message = reader.readLine();
            if (message != null) {
                return message;
            } else {
                throw new TransportException("No message received from server");
            }
        } catch (IOException e) {
            throw new TransportException("Failed to read message from server", e);
        }
    }

    @Override
    public boolean isConnected() {
        return process != null && process.isAlive();
    }

    @Override
    public void close() {
        running = false;
        if (isConnected()) {
            try {
                log.info("Closing MCP server process");
                process.destroy();

                boolean exited = process.waitFor(5, TimeUnit.SECONDS);
                if (!exited) {
                    log.warn("MCP server hasn't shutdown gracefully, forcing termination");
                    process.destroyForcibly();
                    process.waitFor();
                }
                log.info("MCP server process closed successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                process.destroyForcibly();
            }
        }
        closeQuietly(errorReader, reader, writer);
    }

    /**
     * Inicia Virtual Thread para leitura assíncrona de stderr do processo.
     * <p>
     * <strong>Por que isso é necessário?</strong>
     * <br>
     * As streams stdin/stdout/stderr do processo têm buffers finitos (4KB-64KB).
     * Se o servidor escrever em stderr e ninguém ler, o buffer enche e o servidor
     * bloqueia na próxima tentativa de escrever em stderr → <strong>deadlock!</strong>
     * <p>
     * <strong>Solução:</strong> Virtual Thread dedicada lê stderr continuamente,
     * prevenindo que o buffer encha. Mensagens de stderr são logadas como ERROR.
     * <p>
     * <strong>Virtual Thread vs Platform Thread:</strong>
     * <ul>
     *     <li>Virtual Thread bloqueia em readLine() sem consumir thread do OS</li>
     *     <li>Pode criar milhares de Virtual Threads (uma por transporte)</li>
     *     <li>Custo de memória mínimo (~1KB vs ~1MB de platform thread)</li>
     * </ul>
     * <p>
     * A thread é interrompida quando {@code running = false} (no método {@link #close()}).
     */
    private void startStderrReader() {
        Thread.ofVirtual()
                .name("mcp-stderr-reader")
                .start(() -> {
                    try {
                        String line;
                        while (running && (line = errorReader.readLine()) != null) {
                            log.error("[MCP Server stderr] {}", line);
                        }
                    } catch (IOException e) {
                        if (running) {
                            log.error("Error reading stderr", e);
                        }
                    }
                });
    }

    /**
     * Fecha recursos (streams, connections, etc.) de forma silenciosa, suprimindo exceções.
     * <p>
     * Útil para cleanup em finally blocks ou método {@link #close()}, onde exceções
     * de fechamento não devem interromper o shutdown de outros recursos.
     * <p>
     * <strong>Comportamento:</strong>
     * <ul>
     *     <li>Aceita varargs - pode fechar múltiplos recursos em uma chamada</li>
     *     <li>Null-safe - ignora recursos null</li>
     *     <li>Exceptions suprimidas - apenas logadas como WARN</li>
     *     <li>Continua fechando outros recursos mesmo se um falhar</li>
     * </ul>
     * <p>
     * <strong>Exemplo de uso:</strong>
     * <pre>{@code
     * closeQuietly(writer, reader, errorReader);
     * }</pre>
     * <p>
     * <strong>Padrão similar:</strong> Apache Commons IOUtils.closeQuietly()
     *
     * @param resources recursos para fechar (BufferedReader, BufferedWriter, etc.)
     */
    private void closeQuietly(AutoCloseable... resources) {
        try {
            for (AutoCloseable closeable : resources) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (Exception e) {
            log.warn("Error closing resource", e);
        }
    }

}

