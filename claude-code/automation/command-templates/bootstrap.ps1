# Claude Code Command Templates - Bootstrap Script (PowerShell)
# Automatically copies and adapts commands for new projects

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet('generic', 'java-spring', 'flutter', 'nodejs')]
    [string]$StackType,

    [Parameter(Mandatory=$false)]
    [string]$ProjectDir = "."
)

$TemplatesDir = $PSScriptRoot
$CommandsDir = Join-Path $ProjectDir ".claude\commands"

# Display header
Write-Host "Claude Code Command Templates - Bootstrap Script" -ForegroundColor Blue
Write-Host ""

# Create .claude/commands directory
Write-Host "Setting up Claude Code commands for " -NoNewline
Write-Host $StackType -ForegroundColor Yellow -NoNewline
Write-Host "..."
Write-Host ""

New-Item -ItemType Directory -Path $CommandsDir -Force | Out-Null

# Copy generic commands
Write-Host "✓ Copying generic commands..." -ForegroundColor Green
Copy-Item -Path "$TemplatesDir\generic\*.md" -Destination $CommandsDir -Force

# Copy stack-specific commands if not generic
if ($StackType -ne "generic") {
    $StackDir = Join-Path $TemplatesDir $StackType
    if (Test-Path $StackDir) {
        Write-Host "✓ Copying $StackType specific commands..." -ForegroundColor Green
        Copy-Item -Path "$StackDir\*.md" -Destination $CommandsDir -Force
    } else {
        Write-Host "⚠  Stack '$StackType' commands coming soon!" -ForegroundColor Yellow
    }
}

# Count commands copied
$CommandCount = (Get-ChildItem -Path $CommandsDir -Filter "*.md").Count

Write-Host ""
Write-Host "✓ Success! " -ForegroundColor Green -NoNewline
Write-Host "Copied $CommandCount commands to $CommandsDir"
Write-Host ""

# Display next steps
Write-Host "Next Steps:" -ForegroundColor Blue
Write-Host ""
Write-Host "1. Review and adapt commands for your project:"
Write-Host "   Each command has an 'Adaptation Guide' section at the bottom"
Write-Host ""
Write-Host "2. Common adaptations needed:"

switch ($StackType) {
    "java-spring" {
        Write-Host "   - Update directory path: 'services/api' → your backend directory"
        Write-Host "   - Update port numbers: '8080' → your API port"
        Write-Host "   - Update Docker paths: 'infra' → your docker-compose location"
        Write-Host "   - Switch build tool if needed: Maven → Gradle"
    }
    "flutter" {
        Write-Host "   - Update directory path: 'apps/mobile' → your Flutter app directory"
        Write-Host "   - Update app name in commands"
    }
    "nodejs" {
        Write-Host "   - Update directory path to your Node.js project"
        Write-Host "   - Update package manager: npm → yarn/pnpm"
    }
    "generic" {
        Write-Host "   - Update documentation file names (CLAUDE.md, CODING_STYLE.md)"
        Write-Host "   - Update test commands in /finish-session"
    }
}

Write-Host ""
Write-Host "3. Test each command in Claude Code:"
Write-Host "   PS> claude"
Write-Host "   > /start-session"
Write-Host "   > /directive `"Your directive here`""
Write-Host ""
Write-Host "Happy coding with Claude Code! 🚀" -ForegroundColor Blue

# Examples for users
Write-Host ""
Write-Host "Examples:" -ForegroundColor Cyan
Write-Host "  .\bootstrap.ps1 -StackType generic"
Write-Host "  .\bootstrap.ps1 -StackType java-spring -ProjectDir C:\path\to\project"
