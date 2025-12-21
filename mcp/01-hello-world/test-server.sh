#!/bin/bash
# Manual test script for MCP Server
# Usage: ./test-server.sh

echo "=== MCP Server Manual Test Script ==="
echo ""
echo "This script will guide you through testing the MCP server."
echo ""

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven not found. Please install Maven first."
    exit 1
fi

echo "Step 1: Starting MCP Server..."
echo "-------------------------------"
echo "Run this command in a separate terminal:"
echo ""
echo "  cd /c/repo/ai/mcp/01-hello-world"
echo "  mvn spring-boot:run"
echo ""
echo "Wait for the log message: 'MCP Server started'"
echo ""
read -p "Press Enter when the server is running..."

echo ""
echo "Step 2: Testing tools/list request"
echo "-----------------------------------"
echo "In the server terminal, paste this JSON and press Enter:"
echo ""
cat test-requests/01-tools-list.json
echo ""
echo ""
echo "Expected response should contain:"
echo '  "method": "Tool list retrieved successfully"'
echo '  "tools": [{"name":"add",...}, {"name":"multiply",...}, {"name":"random",...}]'
echo ""
read -p "Did you see the expected response? (y/n): " response1

echo ""
echo "Step 3: Testing tools/call request (add)"
echo "-----------------------------------------"
echo "In the server terminal, paste this JSON and press Enter:"
echo ""
cat test-requests/02-tool-call-add.json
echo ""
echo ""
echo "Expected response:"
echo '  {"jsonrpc":"2.0","id":"2","result":{"content":[{"type":"text","text":"8"}]}}'
echo ""
read -p "Did you see the expected response? (y/n): " response2

echo ""
echo "Step 4: Testing tools/call request (multiply)"
echo "----------------------------------------------"
echo "In the server terminal, paste this JSON and press Enter:"
echo ""
cat test-requests/03-tool-call-multiply.json
echo ""
echo ""
echo "Expected response:"
echo '  {"jsonrpc":"2.0","id":"3","result":{"content":[{"type":"text","text":"42"}]}}'
echo ""
read -p "Did you see the expected response? (y/n): " response3

echo ""
echo "Step 5: Testing error handling (unknown method)"
echo "------------------------------------------------"
echo "In the server terminal, paste this JSON and press Enter:"
echo ""
cat test-requests/04-invalid-json.json
echo ""
echo ""
echo "Expected response should contain:"
echo '  "error": {"code":-32601,"message":"Method not found",...}'
echo ""
read -p "Did you see the expected error response? (y/n): " response4

echo ""
echo "Step 6: Testing graceful shutdown"
echo "----------------------------------"
echo "In the server terminal, press Ctrl+C"
echo ""
echo "Expected logs:"
echo '  INFO  Spring context shutting down - stopping MCP Server...'
echo '  INFO  MCP Server stopped'
echo ""
read -p "Did you see the graceful shutdown logs? (y/n): " response5

echo ""
echo "=== Test Results Summary ==="
echo "tools/list:           $response1"
echo "tools/call (add):     $response2"
echo "tools/call (multiply): $response3"
echo "Error handling:       $response4"
echo "Graceful shutdown:    $response5"
echo ""

if [[ "$response1" == "y" && "$response2" == "y" && "$response3" == "y" && "$response4" == "y" && "$response5" == "y" ]]; then
    echo "✅ ALL TESTS PASSED!"
    echo "The MCP Server is working correctly."
else
    echo "⚠️  Some tests failed. Review the server logs for details."
fi

echo ""
echo "Test files are in: test-requests/"
echo "Server logs show detailed request/response flow."
