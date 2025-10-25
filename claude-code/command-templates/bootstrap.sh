#!/bin/bash

# Claude Code Command Templates - Bootstrap Script
# Automatically copies and adapts commands for new projects

set -e

TEMPLATES_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
STACK_TYPE="$1"
PROJECT_DIR="${2:-.}"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Display usage
usage() {
    echo -e "${BLUE}Claude Code Command Templates - Bootstrap Script${NC}"
    echo ""
    echo "Usage: $0 <stack-type> [project-directory]"
    echo ""
    echo "Stack types:"
    echo "  generic        - Generic commands only (works with any stack)"
    echo "  java-spring    - Generic + Java/Spring Boot commands"
    echo "  flutter        - Generic + Flutter/Dart commands (coming soon)"
    echo "  nodejs         - Generic + Node.js commands (coming soon)"
    echo ""
    echo "Examples:"
    echo "  $0 generic                    # Copy generic commands to current directory"
    echo "  $0 java-spring /path/to/proj  # Copy Java/Spring commands to specified project"
    echo ""
    exit 1
}

# Validate arguments
if [ -z "$STACK_TYPE" ]; then
    usage
fi

# Validate stack type
case "$STACK_TYPE" in
    generic|java-spring|flutter|nodejs)
        ;;
    *)
        echo -e "${RED}Error: Invalid stack type '$STACK_TYPE'${NC}"
        usage
        ;;
esac

# Create .claude/commands directory
echo -e "${BLUE}Setting up Claude Code commands for ${YELLOW}$STACK_TYPE${BLUE}...${NC}"
echo ""

COMMANDS_DIR="$PROJECT_DIR/.claude/commands"
mkdir -p "$COMMANDS_DIR"

# Copy generic commands
echo -e "${GREEN}✓${NC} Copying generic commands..."
cp "$TEMPLATES_DIR/generic/"*.md "$COMMANDS_DIR/"

# Copy stack-specific commands if not generic
if [ "$STACK_TYPE" != "generic" ]; then
    if [ -d "$TEMPLATES_DIR/$STACK_TYPE" ]; then
        echo -e "${GREEN}✓${NC} Copying $STACK_TYPE specific commands..."
        cp "$TEMPLATES_DIR/$STACK_TYPE/"*.md "$COMMANDS_DIR/"
    else
        echo -e "${YELLOW}⚠${NC}  Stack '$STACK_TYPE' commands coming soon!"
    fi
fi

# Count commands copied
COMMAND_COUNT=$(ls -1 "$COMMANDS_DIR"/*.md 2>/dev/null | wc -l)

echo ""
echo -e "${GREEN}✓ Success!${NC} Copied $COMMAND_COUNT commands to $COMMANDS_DIR"
echo ""

# Display next steps
echo -e "${BLUE}Next Steps:${NC}"
echo ""
echo "1. Review and adapt commands for your project:"
echo "   Each command has an 'Adaptation Guide' section at the bottom"
echo ""
echo "2. Common adaptations needed:"

case "$STACK_TYPE" in
    java-spring)
        echo "   - Update directory path: 'services/api' → your backend directory"
        echo "   - Update port numbers: '8080' → your API port"
        echo "   - Update Docker paths: 'infra' → your docker-compose location"
        echo "   - Switch build tool if needed: Maven → Gradle"
        ;;
    flutter)
        echo "   - Update directory path: 'apps/mobile' → your Flutter app directory"
        echo "   - Update app name in commands"
        ;;
    nodejs)
        echo "   - Update directory path to your Node.js project"
        echo "   - Update package manager: npm → yarn/pnpm"
        ;;
    generic)
        echo "   - Update documentation file names (CLAUDE.md, CODING_STYLE.md)"
        echo "   - Update test commands in /finish-session"
        ;;
esac

echo ""
echo "3. Test each command in Claude Code:"
echo "   $ claude"
echo "   > /start-session"
echo "   > /directive \"Your directive here\""
echo ""
echo -e "${BLUE}Happy coding with Claude Code! 🚀${NC}"
