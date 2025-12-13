package ai.mcp.helloworld.infrastructure.transport;

import java.io.Closeable;

/**
 * Abstração de transporte para comunicação MCP (Model Context Protocol).
 * <p>
 * Gerencia troca bidirecional de mensagens JSON-RPC entre cliente e servidor MCP.
 * Implementações devem garantir:
 * <ul>
 *     <li>Mensagens delimitadas por newline (\n) - protocolo line-delimited JSON</li>
 *     <li>Encoding UTF-8 para todas as mensagens</li>
 *     <li>Limpeza adequada de recursos via {@link #close()}</li>
 *     <li>Thread-safety para chamadas concorrentes (se aplicável)</li>
 * </ul>
 * <p>
 * <strong>Padrão de uso:</strong>
 * <pre>{@code
 * try (Transport transport = new StdioTransport(command)) {
 *     transport.send("{\"jsonrpc\":\"2.0\",\"method\":\"tools/list\"}");
 *     String response = transport.receive();
 * }
 * }</pre>
 *
 * @author Lucas Xavier Ferreira
 * @date 09/12/2025
 * @see StdioTransport
 */
public interface Transport extends Closeable {

    /**
     * Envia uma mensagem JSON-RPC para o servidor remoto.
     * <p>
     * A mensagem deve ser JSON válido (já serializado). O transporte adiciona
     * automaticamente o delimitador de linha (\n) e força o envio imediato (flush).
     * <p>
     * <strong>Thread-safety:</strong> Implementações devem garantir que mensagens
     * de threads diferentes não sejam intercaladas (usar sincronização se necessário).
     *
     * @param message mensagem JSON-RPC (string JSON válida, sem \n no final)
     * @throws TransportException se o envio falhar (I/O error, processo morto, etc.)
     */
    void send(String message);

    /**
     * Recebe uma mensagem JSON-RPC do servidor remoto.
     * <p>
     * Bloqueia até que uma mensagem completa esteja disponível (linha terminada por \n).
     * Se o processo remoto terminar inesperadamente (EOF), lança exceção.
     * <p>
     * <strong>Comportamento de bloqueio:</strong> Este método bloqueia a thread chamadora
     * até que dados estejam disponíveis. Para I/O assíncrono, considere usar Virtual Threads.
     *
     * @return mensagem JSON-RPC recebida (string JSON válida, sem \n no final)
     * @throws TransportException se a recepção falhar ou o processo terminar (EOF)
     */
    String receive();

    /**
     * Verifica se a conexão com o servidor remoto ainda está ativa.
     * <p>
     * Útil para health checks e detecção de falhas antes de tentar enviar/receber.
     *
     * @return {@code true} se o transporte está conectado e operacional,
     *         {@code false} se o processo morreu ou nunca foi iniciado
     */
    boolean isConnected();

    /**
     * Fecha o transporte e libera todos os recursos associados.
     * <p>
     * Implementações devem:
     * <ul>
     *     <li>Sinalizar threads auxiliares para parar (ex: stderr reader)</li>
     *     <li>Tentar shutdown gracioso do processo (SIGTERM, esperar timeout)</li>
     *     <li>Forçar terminação se timeout excedido (SIGKILL)</li>
     *     <li>Fechar todos os streams (stdin, stdout, stderr)</li>
     *     <li>Ser idempotente (seguro chamar múltiplas vezes)</li>
     * </ul>
     * <p>
     * Este método não deve lançar exceções checked (segue contrato de {@link Closeable}).
     * Erros durante fechamento devem ser logados mas não propagados.
     *
     * @see AutoCloseable#close()
     */
    @Override
    void close();

}
