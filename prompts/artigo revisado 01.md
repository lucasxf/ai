# Minha Experiência com Claude Code

## Introdução

Há algumas semanas, por pura curiosidade, eu comecei a utilizar o Claude Code (nas palavras da Anthropic: A Highly Agentic Coding Assistant), uma ferramenta de IA para auxiliar na codificação de projetos de software.
Neste artigo, vou compartilhar minhas impressões, algumas dicas e aprendizados e enfim, minha experiência. (:

De largada já adianto: até agora está minha experiência sendo incrível!

Talvez por viés “herdado” do hábito de utilizar o ChatGPT e o GitHub Copilot como auxiliares nos meus projetos, comecei usando o Claude Code de forma parecida: como uma espécie de “gerador de código”, na falta de expressão melhor. Esta abordagem foi de certa forma um engano, um erro feliz. Claude é isso tudo, mas é MUITO mais também, como vou descrever abaixo.

Uma curiosidade: conheci primeiro o Claude Code (através de posts de pessoas engenheiras de software no LinkedIn), depois Claude e finalmente a Anthropic, nesta ordem rs.

## Combinados

Gosto de acreditar que um bom engenheiro não confia em soluções milagrosas ou "balas-de-prata" (aprendi com Uncle Bob em Clean Code - ou será que foi em Clean Architecture?). Pelo contrário, somos capazes de avaliar situações e tomar decisões pesando vantagens x desvantagens.
Portanto, vou apresentar minhas "descobertas" também com as oportunidades de melhoria (não exaustivas, mas identificadas na MINHA realidade) envolvidas.

- Sendo assim, vou tentar (mas não prometo nada rs) estruturar esse artigo da seguinte forma:
  1. Desafio: situação com uma oportunidade, problema ou desafio a solucionar
  2. Minha abordagem para atacar o desafio
      1. Vantagens na minha abordagem
      2. Desavantagens (tradeoffs) na minha abordagem
- A ordem das soluções apresentadas aqui não é exatamente óbvia ou intuitiva: eu literalmente fui criando as ferramentas na ordem em que minhas dores se apresentaram. o artigo segue a ordem de criação delas.
- Para manter o artigo conciso, eu não dei tantos detalhes de como/onde utilizar cada ferramenta. Todavia, apresentarei links das fontes e repositórios referenciados ao final.

## Primeiras impressões:

Me considero pragmático e tento balancear sempre entre a parte acadêmica (documentação oficial e treinamentos) e uma abordagem mais "mãos na massa". Dessa vez não foi diferente: conheci a ferramenta, li guias de introdução no site oficial e comecei um treinamento muito bom na DeepLearningAI.

Depois - já ansioso para testar a ferramenta - criei minha conta, rodei os comandos de setup e comecei "linkando" um projeto meu recém-criado do GitHub ao Claude Code.
Nesse vínculo inicial, vi que o CC já fez uma varredura para conhecer a estrutura do repositório, stack tecnológica, meu estilo de trabalho, etc., e gerou insights valiosos. Lembro de ter pensado "legal, mas até aí, nada demais...".

A seguir, solicitei ao Claude o início da criação de uma feature nova. Tal feature, embora simples, percorria um fluxo de ponta-a-ponta do projeto (frontend, backend, base de dados e infra).
Nesse foi o meu primeiro momento "mind blown": Claude criou um plano detalhado em etapas para a construção da feature e todas alterações necessárias. Eu não sabia na hora, mas ele estava utilizando o `TodoWrite`, uma das suas muitas ferramentas embarcadas, além de algumas outras.

O plano passava por todas as camadas da aplicação e compreendendo:
- arquitetura sugerida (respeitando o padrão observado no projeto e sugerindo melhorias)
- estrutura de diretórios e arquivos de código-fonte
- propriedades externalizadas e personalizáveis
- comandos de build e execução
- documentação
- gestão de dependências
- git flow (milestones de commits e PRs)
- N outras coisas

A riqueza de detalhes e a qualidade da resposta me impressionou. Pedi que implementasse o primeiro passo do plano.
Nessa hora minha expectativa, por conta das ferramentas com as quais eu estava acostumado, era que o Claude gerasse código-fonte e eu precisasse manualmente:
- criar os diretórios destino do código
- criar os arquivos destino do código
- alterar os arquivos já existentes com refactors propostos
- enfim: fazer uma tour de copy & paste que nós devs conhecemos tão bem

Minha expectativa foi BASTANTE excedida: Claude me perguntou se podia modificar os arquivos e criar os diretórios, selecionei "Yes" no terminal e dei enter. Nessa hora ele saiu criando e reorganizando diretórios e arquivos de fonte, gerando código e documentação...
Aqui foi quando comecei a sentir uma grande diferença em relação aos assistentes de código que citei. Ele sanou essa dor de manualidade no processo.
Não pensei duas vezes: fui ao site e assinei um plano (até então, estava utilizando o free trial) rs.

---

## Experiência e Produndidade

Como bom engenheiro, eu gosto muito de fuçar rs. Conforme fui utilizando o Claude Code praticamente todos os dias da semana, identifiquei algumas oportunidades de melhoria no meu uso da ferramenta, a começar pelo meu estilo de codificação.

## Sobre Convenções e estilo de codificação (coding style)

Meu projeto utiliza Java com Spring Boot no backend. Java, como muitos bem sabem, é uma linguagem de convenções fortes e muito conhecidas.
Ainda assim, há espaço para cada engenheiro imprimir seu próprio estilo no código-fonte.
Javeiro que sou rs (javeiro = engenheiro de software em java), naturalmente fui personalizando o `CLAUDE.md` com meu estilo a cada sessão nova.

Tá, e?

O ponto é que o `CLAUDE.md` ficou MUITO grande (*bloated) e com informações e diretrizes que desviavam parcial ou totalmente de seu propósito.
Acabou que a cada início de sessão, Claude estava precisando de muito mais tempo que o necessário e consumindo rios de tokens para carregar o contexto.

### Solução

Criei um `CODING_STYLE.md` e migrei todas as diretrizes de estilo de codificação do `CLAUDE.md` para ele. 

### As vantagens

- Meu início de sessão ficou mais rápido (menos contexto para o Claude carregar)
- Meu início de sessão ficou mais eficiente (menos tokens processados no contexto do Claude)
- Eu eu poderia carregar o estilo sob demanda via `@file_name`. Diferente de antes que era carregado em todas sessões, mesmo quando eu não iria alterar código-fonte, por exemplo.
- Com o arquivo novo, eu poderia reutilizar noutros projetos da mesma stack (diferente do CLAUDE.md que é bastante acoplado ao contexto do projeto em questão).

### Os tradeoffs

- Em boa parte das sessões eu preciso "rodar" pelo menos 1 comando ou carga de arquivo adicional.

## Intro à Eficiência (Efficiency 101)

Beleza, sigo utilizando Claude e pós-separação das convenções para o `CODING_STYLE.md` vi ganhos imediatos de eficiência.
Todavia, por vezes ainda estava consumindo mais processamento com tarefas que não estavam tão próximas da geração de valor.
Fui à [documentação oficial](https://support.claude.com/en/articles/9797557-usage-limit-best-practices) e aprendi algumas práticas que me ajudaram (por brevidade, não irei repeti-las aqui).

### Solução

Criei diretrizes e comandos (opa, falaremos sobre comandos mais tarde... calma aí rs) para execução de testes e build em modo "silencioso/não-verboso".

#### As Vantagens

- O output das execuções de build e testes bem sucedidos ficou muito menos verboso, consumindo menos tokens de sessão e tempo de processamento do Claude.

#### Os tradeoffs

- Em casos de erros de build ou em testes unitários e de integração, se faz necessária nova execução de tais comandos, agora em modo verboso, para enriquecer o contexto e facilitar o troubleshooting.

---

## Toda hora esse negócio de diretriz

Talvez vocês tenham notado um padrão: a palavra "diretriz" apareceu um tanto de vezes até agora. Não foi por esquecimento, falta de revisão editorial, ou vocabulário limitado deste que vos fala rs.
Foi pela experiência mesmo. Toda hora eu precisava - e preciso - de uma diretriz nova. E vez ou outra, editar uma antiga.
O fato é que a gente interage com o Claude (e muitas outras ferramentas) por prompts, e um item fundamental da engenharia de prompt é o estabelecimento de constraints e diretrizes claras.

- [Leitor/Reader] "Ah, mas é só atualizar o `CLAUDE.md`, Lucas"
- [Lucas/Author] "Olha, nem toda diretriz é ampla. Algumas são de contexto, outras são de estilo de código-fonte ou convenções, por exemplo"
- [Reader] "Hmm, mas você pode atualizar o `CODING_STYLE.md`"
- [Lucas] "Verdade, mas eu tenho pressa (ou preguiça) de ficar especificando a categoria e o destino da diretriz toda vez."
- [Reader] "Então você pode  utilizar `# memory` keyword"
- [Lucas] "Mais ou menos. Ele pede para atualizar um arquivo de memória, fora que tem questões de duplicidade envolvidas aí também"

Pois é, pensando nisso eu criei um [slash-command](https://docs.claude.com/en/docs/claude-code/slash-commands) customizado, o `/directive`.

### Solução

O `/directive <diretriz>` recebe uma diretriz e a encaminha para o lugar certo.
Quando já existe, ele atualiza apropriadamente. Quando não, cria uma nova.

#### As vantagens

- Valida duplicidade
- Seleciona o arquivo apropriado a partir do input
- Atualiza a área correta do arquivo selecionado (não só "joga" as diretrizes aleatoriamente no início/fim do arquivo de memória)


#### Os tradeoffs

- Cresce acoplado à novos arquivos de memória, isto é, novos destinos para decidir onde a diretriz vai "morar"

### Comparativo com o `# memory`:

| Feature           | # (built-in)                 | /directive (Lucas)                                    |
|-------------------|------------------------------|-------------------------------------------------------|
| Input             | Manual text entry            | One-line command                                      |
| Deduplication     | ❌ No - can create duplicates | ✅ Yes - searches for similar directives               |
| File Selection    | Manual prompt                | ✅ Automatic (detects Backend vs Frontend vs General)  |
| Section Detection | ❌ No - appends to end        | ✅ Yes - finds correct section (Part 2: Backend, etc.) |
| Formatting        | Raw text                     | ✅ Bullet point + timestamp                            |
| Git Integration   | ❌ No                         | ✅ Shows diff + commit prompt                          |
| Smart Routing     | ❌ No                         | ✅ CLAUDE.md vs CODING_STYLE.md detection              |
| Prevents Errors   | ❌ Can duplicate/misplace     | ✅ Validates before adding                             |

---

## Agora ele só pensa em comandos

Depois do primeiro `slash-command` personalizado, a gente se acostuma não para mais.
Nas sessões mais "divertidas" (naquelas onde a eu podia "codar" mais rs), eu percebi um padrão repetitivo nas minhas ações.

## Início de sessão

Em boa parte dos inícios de sessão eu precisava carregar ou referenciar os mesmos arquivos de memória e alguns outros de código-fonte da feature em andamento.

### Solução

Adivinha? 
Isso mesmo: criei o `/start-session`. 

#### As vantagens

- Além de carregar arquivos, o **roadmap** (falaremos mais dele já já) e aprender sobre o contexto, esse comando também sabe qual agente (vem aí) acionar para apoiar nas tarefas de começo de sessão.
- Corta a manualidade de alguns prompts iniciais repetitivos (ou mesmo aquele copy & paste daquele seu "prompts-inicias.txt" rs)

#### Os tradeoffs

- Mesmo sessões parecidas tem suas particularidades, então existe um balanço delicado entre:
  - padronizar demais e carregar automaticamente arquivos que podem não ser necessários naquela sessão
  - padronizar de menos e precisar referenciar arquivos e agentes "manualmente"

---

## Fim de sessão

Similarmente, nos finais de sessão eu precisava carregar ou referenciar arquivos (as vezes os mesmos do início da sessão), mas ainda, executar comandos, revisar código, rodar testes, etc.

### Solução

É óbvio: `/finish-session`

#### As vantagens

- Valida o que rodar (se houve ou não alterações em build/testes/etc.)
- Roda as execuções padrão em modo silencioso (ver [Eficiência](###-Intro-à-Eficiência-(Efficiency-101)))
- Atualiza documentação, roadmap, etc.

#### Os tradeoffs

- Tem um bom número de passos, as vezes em sessões muito curtas compensa mais simplesmente "encerrar"
- Balanço de sub/super padronização parecido com o do `/start-session`

---

## Eficiência Moderada

Mas gosta de uma palavra, hein?

As vezes no meio de uma sessão, eu queria atualizar o roadmap com os próximos passos e precisava revisar código específico à uma classe, pacote, contexto ou commit.
O `/review` embarcado do Claude Code revisa uma pr inteira, não atende tão bem a necessidade de revisar código "não commitado".

### As soluções

Se tratando de comandos mais simples, abreviei numa seção só: `/update-roadmap` e `/review-code`.

#### As vantagens

- Focados em trabalho no MEIO de uma sessão, não nas extremidades
- corte de prompts repetitivos do tipo "agora que conclui o `CustomerController` atualize o roadmap no @CLAUDE.md e revise o código novo"
- Comandos bem especializados são mais facilmente reutilizáveis

#### Os tradeoffs

- Nem todo contexto terá um roadmap documentado dessa forma
- Não é TÃO trabalhoso assim solicitar uma revisão específica

## Meio de Sessão

É muito precisarmos parar no meio do desenvolvimento de uma tarefa, user story ou feature, e nem sempre essa pausa ocorre num milestone "commitável".
Me deparei diversas vezes com esse cenário onde:
1. eu avancei no plano de execução
2. não conclui todas as etapas daquele milestone
3. preciso documentar esse avanço para "lembrar" onde parei e partir daí na próxima sessão
4. não quero "poluir" o roadmap do `CLAUDE.md` com avanços "pequenos"
5. preciso iniciar a próxima sessão com contexto um pouco mais personalizado


Para atender as necessidades acima,  criei o `/resume-session` e o `/save-response`.

### A solução: `/resume-session`

> Trabalhando em apoio ao `/resume-session`, dias mais tarde, 
> separei o roadmap do `CLAUDE.MD` e criei o `ROADMAP.md`, tal qual fiz com o `CODING_STYLE.md`.

#### As vantagens

- Eficiência: Carga mais personalizada de contexto em relação ao `/start-session`
- Produtividade: ajuda o dev a "lembrar" de onde parou e pular mais rápido para a parte "mãos na massa"

#### Os tradeoffs

- Carece de adaptação ao contexto (é menos genérico que outros comandos)
- Não substitui totalmente a carga manual de arquivos relevantes para a sessão

### Comparativo com o `resume`:

| Feature              | /resume (built-in)      | /resume-session (Lucas)                                                              |
|----------------------|-------------------------|--------------------------------------------------------------------------------------|
| Chat History         | ✅ Restores conversation | ❌ Starts fresh (context from files)                                                  |
| Project Context      | ❌ No automatic loading  | ✅ Auto-loads CLAUDE.md, CODING_STYLE.md, ROADMAP.md, README.md                       |
| Session Context      | ❌ No                    | ✅ Loads saved context from prompts/responses/                                        |
| Git Awareness        | ❌ No                    | ✅ Reviews recent commits, checks status                                              |
| Progress Tracking    | ❌ No                    | ✅ Identifies current step/milestone from commits + context file                      |
| Smart File Selection | ❌ Manual                | ✅ Auto-discovery, fuzzy matching, date sorting                                       |
| Summary Report       | ❌ No                    | ✅ Provides: last session context, current progress, uncommitted changes, next action |


### A solução: `/save-response`

Como usuário assíduo do `TodoWrite`, eu muitas vezes precisava de apoio do CLaude Code para redação plano detalhado para a próxima entrega.
Dado que o `ROADMAP.md` armazena listas de atividades e features de alto nível, eu ainda tinha esse gap de precisar salvar planos de entregas menores, mais especializadas.
O `/save-response` faz exatamente isso. No nível de tarefa, ele pega o último plano exibido no terminal (ou trata ambiguidades) e salva num arquivo a parte para apoiar a execução do `/resume-session` na sessão seguinte.


#### As vantagens

- Não polui `CLAUDE.md`, `LEARNINGS.md` ou `ROADMAP.md` com tecnicalidades desnecessárias
- Ajuda o engenheiro a seguir trabalhando mesmo à nível de tarefas
- Otimiza a carga de contexto

#### Os tradeoffs

- A geração adicional de arquivos de memória carece maior atenção aos commits (para não subir ao remote detalhes desnecessários) e pode "pesar" o projeto

---

## Tá, mas quando vamos falar de agentes?

Ainda focado em automação e eficiência, o próximo passo natural da minha jornada com Claude Code foi o uso de agentes especializados.

O projeto "cobaia" que escolhi para explorar o Claude é full stack, tem:
- frontend em flutter/dart
- backend em Java c/ Spring Boot
- bd com Postgres e Flyway migrations
- Conteinerização c/ Docker
- Um pouquinho de nuvem com AWS

Como backend de formação, não tenho profundidade em 100% desses temas, então esses agentes foram meio óbvios (no brainers):
- `backend-code-reviewer` (não compete com o `/review-code`, falarei deles noutro artigo/momento/material)
- `flutter-implementation-coach`
- `frontend-ux-specialist`

Por isso, não vou aprofundar neles agora.
Esses aqui são MUITO mais interessantes:
- `tech-writer`
- `session-optimizer`
- `automation-sentinel`

---

## Eficiência Avançada

### A solução: Tech Writer

Num projeto "full stack", há diversos tipos de documentação:
- na gestão de dependências
- nos arquivos de propriedades e configuração
- no código-fonte (ex.: Java Doc)
- nas APIs REST (ex.: OpenAPI/Swagger)
- infra, etc.

O `tech-writer` surge para apoiar o engenheiro (Me, Myself, and I), bem como, comandos como `/finish-session` e `/update-roadmap`.

#### As vantagens

- "Alivia" o trabalho de comandos e prompts de documentação
- Maior qualidade e eficiência nas tarefas de documentação

#### Os tradeoffs

- Honestamente: ainda não encontrei

---

### A solução: Session Optimizer

Alguém aqui falou em "eficiência"? Rs

#### As vantagens

- O nome é semi-autoexplicativo, mas vamos lá: o nosso otimizador de sessões auxilia o engenheiro, outros agentes e comandos a executar suas tarefas de forma mais eficiente possível.
- focado em maximizar produtividade e em carga inteligente de contexto.
- corta solicitações explícitas do engenheiro para execução de tarefas de forma eficiente/otimizada
- ajuda claude a carregar somente o context necessário para realização das tarefas

#### Os tradeoffs

- É um agente muito recente. Ainda não sei dizer.

### A  solução: Automation Sentinel

Guardei o melhor para o final rs (last but not least).
Não, sério. Brincadeiras à parte, esse agente me ajudou a aprender ainda mais sobre Agentic AI.
Se trata de um meta-agente que monitora, analisa e otimiza todo ecossistema de automação (agentes, comandos, hooks e workflows de CI/CD).
Ele coleta métricas e ajuda a garantir saúde, eficiência e entrega de valor.

#### As vantagens

- ele é "auto consciente" (self-aware), então analisa todos os agentes e a si próprio
- ajuda a manter o ciclo de vida dos agentes (quando chega a hora de arquivar/depreciar ou mesmo, melhorar um agente)
- apresenta dados: traz métricas de uso para apoiar a tomada de decisão

#### Os tradeoffs

- Ele "roda" automaticamente praticamente toda hora (isso deve ter algum custo de processamento e tokens). Ainda não mensurei.

---

## Entrega de Features

Concentrar commits e preparar a mensagem com as entregas da PR é meio "chatinho". E, por mais que seja fácil e rápido solicitar ao Claude que prepare uma PR,
identifiquei que ainda havia um gap no fluxo: eu queria disparar ações e agentes como o `automation-sentinel` ao final de cada ciclo, isto é, na entrega de uma feature,
com a abertura - e fechamento - de uma pull request.


### A solução

Para complementar meu workflow e encaixar nos demais comandos e agentes, criei o `/create-pr`.
Ele analisa todos os commits da PR e prepara uma mensagem descritiva e concisa daquela entrega.
Adicioalmente, dispara triggers de ações necessárias em fechamento de ciclos.

#### As vantagens

- Automação completa do workflow
- Qualidade: corta manualidades e garante que processos secundários executem (sem o dev precisar lembrar)
- Sinergia com demais comandos e agentes do workflow

#### Os tradeoffs

- Não é um comando sempre necessário
- Como valida um pacote grande de commits, pode custar muitos tokens de sessão

---

## TLDR - Resumo das ferramentas

| Type | Name | Description | Usage |
|------|------|-------------|-------|
| Agent | automation-sentinel | Analyzes automation health, workflow metrics, and optimization opportunities | Auto-triggered on PR creation (`/create-pr`) |
| Agent | backend-code-reviewer | Expert Java/Spring Boot code review with educational feedback and best practices | Manual trigger after implementing backend features |
| Agent | flutter-implementation-coach | Flutter/Dart implementation guidance, Riverpod state management, API integration | Manual trigger for Flutter coding assistance and debugging |
| Agent | frontend-ux-specialist | UI/UX design, screen layouts, Material Design 3, accessibility patterns | Auto-triggered when designing screens or discussing UI |
| Agent | session-optimizer | Token efficiency monitoring, context optimization, time-saving recommendations | Auto-triggered at `/start-session` and `/finish-session` |
| Agent | tech-writer | Creates/updates documentation (OpenAPI, README, LEARNINGS.md, ADRs, Javadoc) | Auto-triggered on new REST endpoints and during `/finish-session` |
| Command | /create-pr | Create pull request with gh CLI and auto-trigger automation-sentinel for workflow analysis | `/create-pr [optional-title]` when ready to merge feature |
| Command | /directive | Add coding convention to CLAUDE.md or CODING_STYLE.md with smart deduplication | `/directive "Always use constructor injection"` |
| Command | /finish-session | Run tests, update docs (LEARNINGS.md, ROADMAP.md), create commit, optionally create PR | `/finish-session "feature description"` at end of session |
| Command | /review-code | Breadth-first code quality audit (conventions, tests, docs, security, performance) | `/review-code [optional-path]` before committing |
| Command | /save-response | Save Claude's last response (plan, specification, or structured output) to prompts/responses/ | `/save-response [optional-filename]` after receiving plans |
| Command | /start-session | Load project context (CLAUDE.md, CODING_STYLE.md, ROADMAP.md, README.md) | `/start-session [optional-context]` at session start |
| Command | /update-roadmap | Update ROADMAP.md by moving completed items to "Implemented" and reprioritizing next steps | `/update-roadmap "what was completed"` during development |
| File | CODING_STYLE.md | Code style conventions organized in 3 parts (General/Backend/Frontend/Infrastructure) | Referenced automatically by Claude; updated via `/directive` |
| File | ROADMAP.md | Current implementation status, priorities, backlog, and next steps | Updated via `/update-roadmap` or `/finish-session` |

---

## Exemplo de Workflow completo com as ferramentas citadas
  ---

  ## Updated Command Flow Diagram

  ┌─────────────────────────────────────────────────────────────────────────────┐
  │   REPEATING DEVELOPMENT CYCLE                                               │
  └─────────────────────────────────────────────────────────────────────────────┘

    ┌────────────────────────────────────────────────────┐
    │  Session Start                                     │
    │  ─────────────                                     │
    │  /start-session   or   /resume-session             │
    │       │                      │                     │
    │       ▼                      ▼                     │
    │  session-optimizer    session-optimizer            │
    │  (optimize load)      (restore + analyze)          │
    │                       (loads saved plans)          │
    └────────┬───────────────────────┬───────────────────┘
             │                       │
             └───────────┬───────────┘
                         ▼
    ┌────────────────────────────────────────────────────┐
    │  Planning Phase (NEW!)                             │
    │  ──────────────────                                │
    │  User: "Create implementation plan"                │
    │  Claude: [Generates structured plan]               │
    │       │                                            │
    │       ▼                                            │
    │  /save-response {plan-name}                        │
    │       │                                            │
    │       ▼                                            │
    │  ✓ Plan saved to prompts/responses/                │
    │  ✓ Can be loaded later with /resume-session        │
    └────────┬───────────────────────────────────────────┘
             │
             ▼
    ┌────────────────────────────────────────────────────┐
    │  Development Loop                                  │
    │  ────────────────                                  │
    │  ┌──────────────────────┐                          │
    │  │ Code Implementation  │◄──────┐                  │
    │  └──────┬───────────────┘       │                  │
    │         │                       │                  │
    │         ├─► Endpoint detected   │                  │
    │         │       │               │                  │
    │         │       ▼               │                  │
    │         │   tech-writer         │                  │
    │         │   (OpenAPI docs)      │                  │
    │         │                       │                  │
    │         ├─► /directive          │                  │
    │         │   (add convention)    │                  │
    │         │                       │                  │
    │         ├─► /review-code        │                  │
    │         │   (quick audit)       │                  │
    │         │       │               │                  │
    │         │       ├─► Issues? ────┤                  │
    │         │       │               │                  │
    │         │       ▼               │                  │
    │         │   backend-code-reviewer                  │
    │         │   (deep analysis)     │                  │
    │         │                       │                  │
    │         └─► Fix issues ─────────┘                  │
    │                                                    │
    │  /update-roadmap (track progress)                  │
    └────────┬───────────────────────────────────────────┘
             │
             ▼
    ┌────────────────────────────────────────────────────┐
    │  Session End                                       │
    │  ───────────                                       │
    │  /finish-session                                   │
    │       │                                            │
    │       ▼                                            │
    │  session-optimizer (efficiency report)             │
    │       │                                            │
    │       ▼                                            │
    │  Run tests (./mvnw verify -q)                      │
    │       │                                            │
    │       ▼                                            │
    │  tech-writer (update LEARNINGS.md)                 │
    │       │                                            │
    │       ▼                                            │
    │  Git commit created                                │
    │       │                                            │
    │       ├─► Option 1: Manual PR creation             │
    │       │             (gh pr create)                 │
    │       │                                            │
    │       └─► Option 2: /create-pr (RECOMMENDED!)      │
    │                   │                                │
    │                   ▼                                │
    │           ┌───────────────────────┐                │
    │           │ /create-pr workflow   │                │
    │           ├───────────────────────┤                │
    │           │ • Validate branch     │                │
    │           │ • Generate PR details │                │
    │           │ • Create with gh CLI  │                │
    │           │ • Auto-trigger:       │                │
    │           │   automation-sentinel │                │
    │           └───────┬───────────────┘                │
    │                   │                                │
    │                   ▼                                │
    │       automation-sentinel (workflow analysis)      │
    │       │                                            │
    │       ▼                                            │
    │  ✅ PR created + analyzed                          │
    └────────┬───────────────────────────────────────────┘
             │
             └──────► Next session: /resume-session {saved-plan}
                      (loads plan from /save-response)

---

## Dicas gerais

- O `TodoWrite` salva vidas: essa ferramenta do Claude Code auxilia no planejamento e execução de planos longos. Ex.: "Crie uma feature de cadastro de clientes" (esse prompt é ruim, mas só por exemplo). Ao invés, tente "Utilizando TodoWrite, crie um plano com milestones para a feature de cadastro de clientes". Tenta lá e depois me conta o que achou ;)
- Nada substitui a experiência e conhecimento do engenheiro: a gente tem o conhecimento, o contexto e o poder da tomada de decisão. Façamos bom uso disso tudo! (:
- Confie mas confira: é óbvio, mas precisamos sempre conferir o output de quaisquer ferramentas dessa natureza
- Engenharia de Prompt é o caminho: quanto melhor o prompt, melhor o resultado. O Claude é incrível, e fica ainda melhor com prompts bem elaborados. Se ainda não estudou, corre atrás! Rs

---

## Insights Chave

### 1. Compound Learning                                                      
   
   Each `/directive` improves Claude's behavior for ALL future sessions     

### 2. Context Preservation                                                   
   
   `/finish-session` saves state → `/resume-session` restores it              

### 3. Git Integration                                                        
   
   Workflow aware of branches, commits, uncommitted changes               

### 4. Automation Layering                                                    
   
   Commands chain together (finish → PR → `automation-sentinel`)

### 5. Documentation as Code       

   Directives versioned in git, evolve with codebase                      

### 6. Agent Orchestration     

   Auto-triggered agents handle repetitive tasks (docs, reviews, analysis)

### 7. Token Efficiency

   `session-optimizer` minimizes waste, maximizes relevant context
                                                  
### 8. Exponential ROI
   Time invested in automation compounds across all future sessions

---

## Conclusão

Se você chegou até aqui, talvez seja um curioso, engenheiro, profissional tech, entusiasta de tech ou todas as anteriores como eu sou (:

Então, primeiramente: muito obrigado pelo seu tempo e atenção.

Este é meu primeiro artigo na Medium - de muitos, eu espero -. Espero ter tenha ajudado, sanado dúvidas e provocado novas dúvidas e reflexões também.
A proposta aqui não era ser exaustivo, mas compartilhar conhecimento e convidar para um debate enriquecedor de ideias.

Conta para mim o que achou?
Você também tem comandos e agentes úteis que gostaria de compartilhar?

Segue links e referências importantes:
- Meu repo pessoal de projetos com AI (para consulta dos agentes e comandos citados aqui): [lucasxf/ai](https://github.com/lucasxf/ai)
- Claude
  - [Claude AI](https://claude.ai/new)
  - [Claude Code](https://www.claude.com/product/claude-code)
    - [Agents](https://docs.claude.com/en/docs/claude-code/sub-agents)
    - [Best practices](https://www.anthropic.com/engineering/claude-code-best-practices)
    - [Memory](https://docs.claude.com/en/docs/claude-code/memory)
    - [Slash Commands](https://docs.claude.com/en/docs/claude-code/slash-commands)
    - [Web](https://www.anthropic.com/news/claude-code-on-the-web)
  - Usage and limits
    - [Understanding](https://support.claude.com/en/articles/11647753-understanding-usage-and-length-limits)
    - [Best practices](https://support.claude.com/en/articles/9797557-usage-limit-best-practices)
  
- DeepLearning.AI
  - [Claude Code: A Highly Agentic Coding Assistant](https://learn.deeplearning.ai/courses/claude-code-a-highly-agentic-coding-assistant/lesson/66b35/introduction)

Um abraço,

Lucas.

> Footnote: com exceção das seções "Insights chave" e "Exemplo de Workflow", este arquivo foi gerado 100% manualmente e serviu de base nos prompts que apoiaram a redação do artigo. ;)