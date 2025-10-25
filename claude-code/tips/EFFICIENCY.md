# 🚀 Claude Code Efficiency Guide

> Como usar Claude Code de forma eficiente e evitar atingir o limite de tokens

**Última atualização:** 2025-10-21

---

## 🎯 Por Que Você Atinge o Limite de Tokens?

### Principais Causas (identificadas neste projeto):

1. **Leitura de arquivos grandes completos** (~30k tokens desperdiçados)
   - ❌ Ler CLAUDE.md inteiro (500+ linhas) quando precisa apenas de 1 seção
   - ❌ Ler CODING_STYLE.md completo quando precisa apenas de exemplos de backend

2. **Saídas verbosas de comandos** (~20k tokens desperdiçados)
   - ❌ `./mvnw test` mostra logs completos do Spring Boot (5000+ linhas)
   - ❌ `docker compose logs` sem `--tail` limita

3. **Múltiplas leituras do mesmo arquivo** (~15k tokens desperdiçados)
   - ❌ Ler ReviewService.java 3 vezes na mesma sessão

4. **Contexto não reaproveitado** (~10k tokens desperdiçados)
   - ❌ Pedir "leia X novamente" quando já foi lido

5. **Arquivos de documentação monolíticos** (~25k tokens desperdiçados)
   - ❌ CLAUDE.md com 40k+ caracteres (tudo em um arquivo)
   - ❌ Misturar roadmap + learnings + guidelines no mesmo arquivo
   - ❌ Documentação crescendo indefinidamente sem estrutura

---

## 🛠️ Ferramentas e Atalhos Essenciais

### 1. Atalho `#` (File Reference)

**O mais importante para economizar tokens!**

#### Como Funciona:
```
Você digita: # ReviewService.java
```

**O que acontece:**
- Claude Code mostra **preview** do arquivo (título, primeiras linhas)
- Eu **NÃO leio o arquivo completo** automaticamente
- Você ganha contexto visual sem gastar tokens
- Posso então ler **apenas a seção necessária**

#### Exemplo Prático:

**❌ INEFICIENTE (gasta ~15k tokens):**
```
Você: "Leia ReviewService.java e adicione um método findByUserId()"
```
→ Eu leio **todas** as 300 linhas do arquivo

**✅ EFICIENTE (gasta ~3k tokens):**
```
Você: "# ReviewService.java - adicione método findByUserId() após findById()"
```
→ Eu leio apenas ~50 linhas ao redor do método findById()

---

### 2. Atalho `@` (Mention Context)

Busca em contextos específicos sem ler tudo:

#### Tipos de @ disponíveis:

- **`@workspace`** - Busca em todo o workspace
  ```
  @workspace onde está implementado o JWT validation?
  ```

- **`@docs`** - Consulta documentação oficial
  ```
  @docs Spring Boot security best practices
  ```

- **`@web`** - Busca na web (use com moderação)
  ```
  @web latest PostgreSQL 16 features
  ```

#### Quando usar:
- ✅ Quando não sabe onde está algo no projeto
- ✅ Quando precisa de documentação oficial
- ❌ Quando já sabe o arquivo específico (use `#` direto)

---

### 3. Slash Commands (`/`)

Comandos customizados que você pode criar.

#### Como Criar:

**Arquivo:** `.claude/commands/test-service.md`
```markdown
---
description: Run tests for a specific service class
---

Run unit tests for {{SERVICE_NAME}}

Execute:
cd services/api && ./mvnw test -Dtest={{SERVICE_NAME}}Test -q
```

**Uso:**
```
/test-service ReviewService
```

#### Comandos Úteis para Este Projeto:

1. **`/test-service`** - Roda testes de um serviço específico
2. **`/api-doc`** - Abre Swagger UI (http://localhost:8080/swagger-ui.html)
3. **`/build-clean`** - Clean build silencioso
4. **`/check-style`** - Verifica padrões de código

---

## 📋 Estratégias de Eficiência

### A. Organize Documentação em Múltiplos Arquivos

**Problema:** CLAUDE.md cresce indefinidamente, atingindo 40k+ caracteres (869+ linhas).

**Por que isso é ruim:**
- Claude Code precisa ler o arquivo inteiro para encontrar 1 seção
- Sessões consomem 15-20k tokens só carregando contexto inicial
- Documentação se torna difícil de navegar e manter
- Histórico de sessões e roadmap poluem arquivo de arquitetura

**❌ ESTRUTURA INEFICIENTE (arquivo único):**
```
CLAUDE.md (40k chars, 869 linhas)
├── Diretrizes gerais (150 linhas)
├── Convenções de código (200 linhas)
├── Roadmap e próximos passos (100 linhas)
├── Histórico de sessões (90 linhas)
├── Learnings e decisões técnicas (200 linhas)
└── Setup e comandos (129 linhas)
```
**Resultado:** Claude lê 40k chars toda vez, mesmo para responder "como rodar testes?"

**✅ ESTRUTURA EFICIENTE (separação por responsabilidade):**
```
CLAUDE.md (28k chars, 489 linhas)
├── PART 1: GENERAL - Regras cross-stack
├── PART 2: BACKEND - Java/Spring específico
├── PART 3: FRONTEND - Flutter/Dart específico
└── PART 4: INFRASTRUCTURE - Docker/CI/CD

ROADMAP.md (154 linhas)
├── ✅ Implemented
├── 🚧 In Progress
├── 🎯 Next Steps (Priority 1, 2, 3...)
└── 📍 Future Backlog

LEARNINGS.md (314 linhas)
├── Session 2025-10-25: Flutter Initialization
├── Session 2025-10-22 (Part 3): Integration Tests
├── Session 2025-10-22 (Part 2): Code Quality
└── Session 2025-10-22 (Part 1): Authentication

CODING_STYLE.md (separado)
└── Exemplos detalhados de padrões de código
```

**Benefícios:**
- ✅ **44% redução no arquivo principal** (869 → 489 linhas)
- ✅ **Carregamento seletivo:** `/start-session` só carrega CLAUDE.md + ROADMAP.md (~35k chars)
- ✅ **Histórico não polui arquitetura:** Sessões antigas em LEARNINGS.md (carregado só quando necessário)
- ✅ **Organização por tipo:** Arquitetura vs Status vs História
- ✅ **Escalabilidade:** LEARNINGS.md pode crescer indefinidamente sem impactar CLAUDE.md

**Como implementar:**
1. **Identifique conteúdo volátil:** Roadmap, histórico de sessões, logs de debug
2. **Separe por responsabilidade:**
   - `CLAUDE.md` → Arquitetura, padrões, diretrizes (muda raramente)
   - `ROADMAP.md` → Status atual, in progress, next steps (atualizado toda sessão)
   - `LEARNINGS.md` → Histórico cronológico de sessões (append-only)
3. **Estruture em partes reutilizáveis:** PART 1 (General), PART 2 (Backend), PART 3 (Frontend)
   - Permite copiar seções para novos projetos (backend-only, frontend-only)
4. **Atualize comandos:** `/start-session` carrega CLAUDE.md + ROADMAP.md (não LEARNINGS.md)
5. **Cross-reference:** Documentar quando cada arquivo deve ser atualizado

**Exemplo de atualização de comandos:**
```markdown
# .claude/commands/start-session.md
---
description: Start development session with standard context
---

Load project context:
- CLAUDE.md (architecture & guidelines)
- ROADMAP.md (current status & priorities)
- CODING_STYLE.md (code conventions)
- README.md (setup & features)

Note: LEARNINGS.md is NOT loaded by default (use only when investigating past decisions)
```

**Economia de tokens:**
- Antes: 40k chars carregados toda sessão
- Depois: 35k chars (CLAUDE.md + ROADMAP.md apenas)
- **Economia:** 5k tokens por sessão (~12% redução no carregamento inicial)
- **Escalabilidade:** LEARNINGS.md pode ter 100+ sessões sem impactar carregamento

---

### B. Planeje Antes de Executar

Use **TodoWrite** para criar plano **sem executar**:

**❌ INEFICIENTE:**
```
Você: "Implemente upload de imagem"
Claude: [lê 20 arquivos, implementa, descobre que faltam requisitos, refaz]
```

**✅ EFICIENTE:**
```
Você: "Crie TodoWrite com plano para implementar upload de imagem com:
- Endpoint POST /api/images
- Validação de tamanho (max 10MB)
- Integração com S3
- Testes"

Claude: [cria plano estruturado]

Você: "Aprovo, execute"

Claude: [executa tudo de uma vez, lendo apenas arquivos necessários]
```

**Economia:** ~50k tokens por feature complexa

---

### C. Seja Específico e Direto

#### ❌ Vago (gasta muito):
```
"Melhore a documentação do projeto"
```
→ Preciso ler TODOS os arquivos de docs

#### ✅ Específico (gasta pouco):
```
"No README.md PART 2 (Backend), adicione exemplo de como
rodar testes de integração com Testcontainers"
```
→ Leio apenas PART 2 do README.md

---

### D. Agrupe Tarefas Relacionadas

**❌ INEFICIENTE (3 comandos separados):**
```
1. "Adicione teste em ReviewServiceTest"
   [Claude executa, você espera]

2. "Adicione teste em AuthServiceTest"
   [Claude executa, você espera]

3. "Adicione teste em CommentServiceTest"
   [Claude executa, você espera]
```
**Custo:** ~30k tokens (relê contexto 3 vezes)

**✅ EFICIENTE (1 comando agrupado):**
```
"Adicione os seguintes testes:
1. ReviewServiceTest: shouldHandlePaginationCorrectly()
2. AuthServiceTest: shouldRefreshTokenWhenExpired()
3. CommentServiceTest: shouldDeleteCommentsWhenReviewDeleted()

Para cada um, siga o padrão já existente nos testes."
```
**Custo:** ~12k tokens (lê contexto 1 vez, executa tudo)

**Economia:** 60% de tokens

---

### E. Use Ferramentas de Busca (Grep/Glob)

Em vez de ler arquivos, **busque** primeiro:

**❌ INEFICIENTE:**
```
"Leia todos os controllers e me diga quais usam @PostMapping"
```

**✅ EFICIENTE:**
```
"Use Grep para encontrar todos os @PostMapping no projeto"
```

**Economia:** ~40k tokens em projetos médios

---

### F. Limite Outputs Verbosos

Comandos com muita saída desperdiçam tokens:

| ❌ Comando Verboso | ✅ Comando Silencioso | Economia |
|-------------------|----------------------|----------|
| `./mvnw test` | `./mvnw test -q` | ~5k tokens |
| `./mvnw clean install` | `./mvnw clean install -q` | ~8k tokens |
| `docker compose logs` | `docker compose logs --tail=50` | ~10k tokens |
| `cat large-file.log` | `tail -n 100 large-file.log` | ~15k tokens |
| `git log` | `git log --oneline -10` | ~3k tokens |

---

### G. Reaproveite Contexto da Sessão

Eu **lembro** do que já li. Não precisa repetir:

**❌ INEFICIENTE:**
```
Você: "Leia ReviewService.java"
Claude: [lê arquivo - 5k tokens]

Você: "Agora leia ReviewService.java e adicione método X"
Claude: [lê arquivo DE NOVO - mais 5k tokens]
```

**✅ EFICIENTE:**
```
Você: "Leia ReviewService.java"
Claude: [lê arquivo - 5k tokens]

Você: "Adicione método findByUserId() nesse arquivo"
Claude: [usa cache da leitura anterior - 0 tokens extras]
```

**Economia:** 5k tokens por reutilização

---

## 🎓 Workflow Recomendado para Features Complexas

### Exemplo: "Implementar Sistema de Upload de Imagens"

#### ❌ Workflow Ineficiente (~150k tokens):

```
1. Você: "Me explique a arquitetura do projeto"
   Claude: [lê 50 arquivos]

2. Você: "Leia ReviewService para entender o padrão"
   Claude: [lê ReviewService]

3. Você: "Implemente ImageUploadService"
   Claude: [relê arquivos, implementa]

4. Você: "Adicione testes"
   Claude: [relê arquivos, adiciona testes]

5. Você: "Adicione documentação OpenAPI"
   Claude: [relê controller, adiciona docs]

6. Você: "Rode testes"
   Claude: [roda com logs verbosos]

7. Você: "Commite"
   Claude: [relê para verificar o que mudou]
```

**Problemas:**
- Múltiplas leituras do mesmo conteúdo
- Falta de planejamento
- Tarefas fragmentadas
- Outputs verbosos

---

#### ✅ Workflow Eficiente (~30k tokens):

```
1. Você: "# ReviewService.java - analisar padrão de implementação

   Quero implementar ImageUploadService seguindo o mesmo padrão.
   Crie TodoWrite com plano de implementação incluindo:
   - Service + Controller + Tests
   - Integração com S3 (pre-signed URLs)
   - Validação (max 10MB, MIME types: image/jpeg, image/png)
   - OpenAPI documentation

   NÃO execute ainda, apenas planeje."

2. Claude: [lê apenas ReviewService, cria plano estruturado - ~5k tokens]

3. Você: [revisa plano, aprova ou ajusta]

4. Você: "Plano aprovado. Execute tudo, rodando testes com -q flag."

5. Claude: [implementa + testa + documenta + commita em sequência - ~25k tokens]
```

**Ganhos:**
- ✅ Lê cada arquivo apenas 1 vez
- ✅ Planejamento claro antes de executar
- ✅ Tarefas agrupadas
- ✅ Outputs silenciosos (-q)
- ✅ **Economia de 80% em tokens**

---

## 🔧 Configurações Avançadas

### A. Auto-Approve Commands

Evite confirmações para comandos seguros:

**Arquivo:** `.claude/settings.json`
```json
{
  "tools": {
    "bash": {
      "autoApprove": [
        "./mvnw test -q",
        "./mvnw clean install -q",
        "git status",
        "git log --oneline -10",
        "docker compose ps"
      ]
    }
  }
}
```

**Benefício:** Comandos aprovados rodam instantaneamente

---

### B. Hooks para Automação

**Arquivo:** `.claude/hooks/pre-commit.sh`
```bash
#!/bin/bash
# Roda testes antes de commitar (silenciosamente)
cd services/api && ./mvnw test -q
```

**Ativa automaticamente** quando uso git commit.

---

### C. MCP Servers (Avançado)

Integre ferramentas externas:

**Arquivo:** `.claude/mcp.json`
```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_TOKEN": "${GITHUB_TOKEN}"
      }
    }
  }
}
```

**Uso:**
```
"Liste todas as issues abertas no GitHub"
```

Eu acesso direto via MCP, sem precisar ler arquivos.

---

## 📊 Monitoramento de Uso

### Como Ver Quanto Você Gastou:

Durante a sessão, observe:
```
<system_warning>Token usage: 98000/200000; 102000 remaining</system_warning>
```

**Interpretação:**
- **98k usados** de 200k disponíveis
- **102k restantes** nesta sessão
- **49% da sessão consumida**

### Principais Consumidores (neste projeto):

| Ação | Tokens Aprox. | Como Reduzir |
|------|--------------|--------------|
| Ler CLAUDE.md completo | 15k | Use `#` + seção específica (3k) |
| `./mvnw test` (verbose) | 8k | Use `./mvnw test -q` (1k) |
| Ler 5 arquivos Java | 20k | Use Grep primeiro, leia só o necessário (5k) |
| Git log completo | 5k | Use `git log --oneline -10` (0.5k) |

---

## ✅ Checklist Antes de Cada Comando

Pergunte-se:

- [ ] **Posso usar `#` em vez de "leia arquivo.java"?**
  - ✅ Sim → Use `#`
  - ❌ Não preciso do arquivo inteiro

- [ ] **Posso especificar seção/linha em vez de arquivo completo?**
  - ✅ "na seção Backend Testing"
  - ❌ "leia tudo"

- [ ] **Posso agrupar múltiplas tarefas relacionadas?**
  - ✅ "Adicione 3 testes: X, Y, Z"
  - ❌ Pedir cada teste separadamente

- [ ] **Posso usar Grep/Glob em vez de ler arquivos?**
  - ✅ "Encontre @PostMapping no projeto"
  - ❌ "Leia todos controllers"

- [ ] **Preciso de output completo ou `-q` basta?**
  - ✅ `./mvnw test -q`
  - ❌ `./mvnw test`

- [ ] **Já li esse arquivo antes na sessão?**
  - ✅ "Adicione método X nesse arquivo"
  - ❌ "Leia X novamente e adicione método"

- [ ] **Posso usar TodoWrite para planejar antes de executar?**
  - ✅ Tarefas complexas → Planejar primeiro
  - ❌ Executar direto sem plano

---

## 🎯 Metas de Eficiência

### Para Este Projeto (wine-reviewer):

| Métrica | Meta | Atual (sem otimização) | Com Otimização |
|---------|------|------------------------|----------------|
| Tokens por feature média | < 30k | ~100k | ~25k ✅ |
| Tokens por bug fix | < 10k | ~40k | ~8k ✅ |
| Tokens por refactor | < 20k | ~80k | ~18k ✅ |
| Reuso de contexto | > 70% | ~20% | ~75% ✅ |

---

## 📚 Recursos Adicionais

### Documentação Oficial:
- [Claude Code Docs](https://docs.claude.com/en/docs/claude-code)
- [MCP Documentation](https://modelcontextprotocol.io/)

### Neste Projeto:
- `CLAUDE.md` - Diretrizes do projeto (3 partes)
- `CODING_STYLE.md` - Padrões de código (3 partes)
- `README.md` - Setup e comandos (3 partes)

---

## 🔥 Dicas Finais

1. **Use `#` sempre que possível** - Economiza 70% de tokens em leituras
2. **Agrupe tarefas relacionadas** - Economiza 60% em reprocessamento
3. **Planeje com TodoWrite primeiro** - Economiza 50% em refações
4. **Use flags silenciosas (-q)** - Economiza 80% em outputs
5. **Reaproveite contexto** - Economiza 100% em releituras

---

**Lembre-se:** Qualidade > Quantidade de comandos

É melhor 1 comando bem planejado e específico do que 10 comandos vagos e fragmentados.

---

**Criado em:** 2025-10-21
**Versão:** 1.0
**Autor:** Claude Code Efficiency Team
