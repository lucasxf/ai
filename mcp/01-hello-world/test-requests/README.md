# MCP Server Manual Test Requests

## Test Files

Each JSON file contains a JSON-RPC 2.0 request for testing the MCP server.

### 1. Tools List (`01-tools-list.json`)
```json
{"jsonrpc":"2.0","id":"1","method":"tools/list"}
```
**Expected Response**: List of all available tools (add, multiply, random)

### 2. Tool Call - Add (`02-tool-call-add.json`)
```json
{"jsonrpc":"2.0","id":"2","method":"tools/call","params":{"name":"add","arguments":{"a":5,"b":3}}}
```
**Expected Response**: `{"jsonrpc":"2.0","id":"2","result":{"content":[{"type":"text","text":"8"}]}}`

### 3. Tool Call - Multiply (`03-tool-call-multiply.json`)
```json
{"jsonrpc":"2.0","id":"3","method":"tools/call","params":{"name":"multiply","arguments":{"a":7,"b":6}}}
```
**Expected Response**: `{"jsonrpc":"2.0","id":"3","result":{"content":[{"type":"text","text":"42"}]}}`

### 4. Invalid Method (`04-invalid-json.json`)
```json
{"jsonrpc":"2.0","id":"4","method":"unknown-method"}
```
**Expected Response**: Error response with code -32601 (METHOD_NOT_FOUND)

## How to Test

### Option 1: Interactive Testing (Recommended)
```bash
# Start the server
cd /c/repo/ai/mcp/01-hello-world
mvn spring-boot:run

# In another terminal, send requests one by one:
cat test-requests/01-tools-list.json

# Copy the JSON and paste it into the running server terminal
# Press Enter to send
# Observe the response
```

### Option 2: Piped Testing
```bash
# Start server in background
mvn spring-boot:run &

# Send request via pipe
cat test-requests/01-tools-list.json | nc localhost 8080
```

### Option 3: File-based Testing
```bash
# Run server with input redirection
mvn spring-boot:run < test-requests/01-tools-list.json
```

## Test Execution Order

1. **tools/list** - Verify server can list all registered tools
2. **tools/call (add)** - Verify basic arithmetic tool execution
3. **tools/call (multiply)** - Verify another tool execution
4. **Invalid method** - Verify error handling

## Expected Server Logs

You should see:
```
INFO  MCP Server started
INFO  Received message: {"jsonrpc":"2.0","id":"1","method":"tools/list"}
INFO  Handling request: ToolListRequest[...]
INFO  Server Response: ToolListResponse[...]
INFO  Sent response: {"jsonrpc":"2.0",...}
```

## Graceful Shutdown Test

Press `Ctrl+C` and verify:
```
INFO  Spring context shutting down - stopping MCP Server...
INFO  MCP Server stopped
```
