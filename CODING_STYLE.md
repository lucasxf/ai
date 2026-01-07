# Coding Style Guide - Lucas Xavier Ferreira

> Este documento define o estilo de programação do desenvolvedor para manter consistência em todos os projetos.
> Baseado na análise do repositório: https://github.com/lucasxf/estudos
>
> **Organizado em 3 partes:** GENERAL (cross-stack), BACKEND (Java/Spring Boot), FRONTEND (Flutter/Dart)

---

# 🌍 PART 1: GENERAL STANDARDS (Cross-stack)

> **Use this section for:** Universal coding standards applicable to any language/framework.
> **Reusable in:** All projects (backend, frontend, fullstack).

## 🎯 Princípios Gerais

- **Qualidade sobre velocidade** - Tomar o tempo necessário para fazer certo
- **Código em inglês** - Nomes de classes, métodos, variáveis sempre em inglês
- **Comentários e logs podem ser em português** - Documentação e mensagens de log
- **Separation of Concerns** - Clara divisão entre camadas
- **Test-After-Implementation** - Sempre criar testes imediatamente após implementar classe testável
- **DRY (Don't Repeat Yourself)** - Evitar duplicação de conhecimento e intenção; cada peça de conhecimento/lógica deve ter uma única representação autoritativa no sistema (Added 2026-01-07)
- **ETC (Easier to Change)** - Priorizar design e decisões que facilitam mudanças futuras; código deve ser flexível e adaptável ao invés de otimizado prematuramente (Added 2026-01-07)
- **SHY (Shy Code)** - Minimizar acoplamento entre módulos; cada módulo deve expor apenas o necessário e não revelar detalhes internos de implementação (Added 2026-01-07)
- **Orthogonality** - Componentes devem ser independentes e auto-contidos; mudanças em um módulo não devem exigir mudanças em outros módulos não relacionados (Added 2026-01-07)

## 📋 Nomenclatura Universal

- **Classes:** PascalCase - `AccountAggregate`, `CreateAccountHandler`, `ReviewService`
- **Métodos:** camelCase - `createAccount()`, `validateCommand()`, `getUserById()`
- **Variáveis:** camelCase - `correlationId`, `accountStream`, `userId`
- **Constantes:** UPPER_SNAKE_CASE - `EMAIL_PATTERN`, `MAX_RETRY_ATTEMPTS`, `ONE_HOUR_MS`
- **Pacotes (Java) / Módulos (Dart):** lowercase - `subscriptions_billing`, `domain.account`, `features/auth`
- **Números grandes:** SEMPRE usar underscore para separar milhares - `3_600_000` (não `3600000`)

### Exemplo de Números com Agrupamento

```java
// ✅ CORRETO - Legível
private static final long ONE_HOUR_MS = 3_600_000L;
private static final long ONE_DAY_MS = 86_400_000L;
private static final int MAX_FILE_SIZE = 10_000_000;  // 10 MB

// ❌ INCORRETO - Difícil de ler
private static final long ONE_HOUR_MS = 3600000L;
private static final long ONE_DAY_MS = 86400000L;
private static final int MAX_FILE_SIZE = 10000000;
```

## 📚 Documentação Viva

### Princípio de Documentação Contínua

**REGRA:** A documentação deve ser atualizada ao final de cada sessão de desenvolvimento.

**REGRA CRÍTICA: Organização de Documentação (Estrutura 3 Partes)**

Todos os arquivos principais de documentação (`CLAUDE.md`, `CODING_STYLE.md`, `README.md`) **devem** ser organizados em 3 partes:
1. **PART 1: GENERAL** - Guidelines cross-stack, visão geral, regras universais
2. **PART 2: BACKEND** - Específico de backend (Java/Spring Boot): setup, convenções, testes
3. **PART 3: FRONTEND** - Específico de frontend (Flutter/Dart): setup, convenções, testes

**Benefícios:**
- ✅ **Reutilizável**: Copiar apenas seções relevantes para novos projetos (backend-only, frontend-only, fullstack)
- ✅ **Organizado**: Sem mistura de guidelines de stacks diferentes
- ✅ **Escalável**: Fácil adicionar novas seções (PART 4: BFF, PART 5: Infraestrutura, etc.)
- ✅ **Claro**: Cada seção tem delimitadores claros e instruções de uso

**Arquivos a atualizar após mudanças significativas:**
1. **`CLAUDE.md`** - Sempre atualizar com novas diretrizes, decisões arquiteturais e aprendizados
   - **CRITICAL:** Atualizar seção "Next Steps (Roadmap)" - mover itens completos para "Implemented", adicionar novos próximos passos
   - **Estrutura:** 3 partes (General/Backend/Frontend)
2. **`CODING_STYLE.md`** (este arquivo) - Sempre atualizar com novos padrões de código identificados
   - **Estrutura:** 3 partes (General/Backend/Frontend)
3. **`README.md`** - Atualizar quando o estado da aplicação mudar (novas features, endpoints, configurações)
   - **Estrutura:** 3 partes (General/Backend/Frontend)
4. **OpenAPI/Swagger (Backend)** - Atualizar anotações nos controllers sempre que criar/modificar endpoints REST

**O que caracteriza mudança significativa:**
- Novas features implementadas
- Novos endpoints REST criados/modificados
- Mudanças arquiteturais (novos padrões, exceções, estruturas)
- Novas convenções de código identificadas
- Atualizações de dependências importantes

**Formato de atualização:**
- Sempre incluir data da atualização
- Descrever brevemente o que foi adicionado/modificado
- Manter histórico de mudanças relevantes
- **Atualizar "Next Steps (Roadmap)" em CLAUDE.md:**
  - Mover tasks completadas para "Current Implementation Status"
  - Adicionar novos próximos passos baseados no progresso
  - Manter priorização clara (1, 2, 3, 4...)
  - Ajuda na carga de contexto ao início de cada nova sessão

---

# ☕ PART 2: BACKEND STANDARDS (Java/Spring Boot)

> **Use this section for:** Backend-only projects, Spring Boot microservices, REST APIs.
> **Copy from here when creating:** Java backend projects, Spring Boot applications.

## 📦 Estrutura de Pacotes

### Padrão Preferido (DDD/CQRS)
```
com.winereviewer.api/
├── application/
│   ├── api/              # Controllers (REST endpoints)
│   │   └── dto/          # Request/Response DTOs
│   ├── commands/         # Command handlers
│   │   └── handlers/     # Implementações de handlers
│   └── process/          # Sagas e processos
├── domain/               # Domain layer
│   ├── [aggregate]/      # Por agregado (ex: account, subscription)
│   │   ├── command/      # Commands do agregado
│   │   └── event/        # Events do agregado
│   └── exception/        # Domain exceptions
├── infrastructure/       # Infrastructure layer
│   ├── repository/       # Implementações de repositórios
│   ├── messaging/        # Event bus, publishers
│   ├── context/          # Context management (correlation/causation)
│   └── serialization/    # Mappers e serializers
└── config/               # Spring configuration
```

### Para Projetos CRUD Simples
```
com.winereviewer.api/
├── controller/           # REST endpoints
├── service/              # Business logic
├── repository/           # Data access
├── domain/               # Entities
├── application/dto/      # Data Transfer Objects
├── exception/            # Custom exceptions
├── config/               # Configuration classes (@Configuration, @ConfigurationProperties)
└── security/             # Security filters, utils (não configs)
```

**Regra importante - Organização de configs:**
- Classes `@Configuration` e `@ConfigurationProperties` → `/config`
- Classes de segurança (filters, utils) → `/security`
- Exemplo: `JwtProperties` fica em `/config`, `JwtUtil` fica em `/security`

## 🏗️ Padrões Arquiteturais (Backend)

### 1. Event Sourcing / CQRS (quando aplicável)

**Aggregates:**
- Imutáveis com estado reconstruído a partir de eventos
- Método `decide()` para comandos → retorna eventos
- Método privado `apply()` para aplicar eventos ao estado
- Método `replay()` para reconstruir estado do histórico

**Exemplo:**
```java
public class AccountAggregate {
    private final UUID id;
    private final List<AccountEvent> history;
    private long version = -1L;
    @Getter
    private AccountStatus status;

    public static AccountAggregate from(UUID id, List<AccountEvent> history, long lastVersion) {
        final var aggregate = new AccountAggregate(id, history, lastVersion);
        aggregate.version = lastVersion;
        return aggregate;
    }

    public AccountCreated decide(CreateAccount command) {
        validateCommand(command);
        // Business rules
        return new AccountCreated(...);
    }

    private void replay() {
        for (AccountEvent event : history) {
            switch (event) {
                case AccountCreated e -> apply(e);
                // ...
            }
        }
    }

}
```

### 2. Command Handlers

- Interface `CommandHandler<T extends Command>`
- Implementações separadas por comando
- Fluxo: Load → Decide → Append → Publish
- Sempre usar `ContextScope` para correlation/causation IDs

**Exemplo:**
```java
@Slf4j
@Component
public class CreateAccountHandler implements CommandHandler<CreateAccount> {

    @Override
    public void handle(CreateAccount command, UUID correlationId) {
        try (final var scope = ContextScope.open(correlationId, command.commandId())) {
            log.info("Handling CreateAccount command for username: {}", command.username());

            // 1. Load current state
            final var stream = repository.load(accountId);
            final var aggregate = AccountAggregate.from(...);

            // 2. Decide new events
            final var event = aggregate.decide(command);

            // 3. Persist new events
            final var events = repository.append(...);

            // 4. Publish new events
            eventBus.publishAll(events, correlationId, commandId);
        }
    }

}
```

### 3. Rich Domain Models (Tell, Don't Ask)

**REGRA:** Preferir rich domain models com comportamento encapsulado ao invés de anemic models com getters/setters.

**Princípio "Tell, Don't Ask":**
- Diga ao objeto o que fazer (comando) ao invés de perguntar seu estado e decidir externamente
- Comportamento deve estar **dentro** do domínio, não em serviços que manipulam dados
- Evitar expor estado interno através de getters desnecessários

**Exemplo INCORRETO (Anemic Model):**
```java
// ❌ Modelo anêmico - apenas dados, sem comportamento
public class Order {
    private OrderStatus status;
    private BigDecimal totalAmount;

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal amount) { this.totalAmount = amount; }
}

// ❌ Lógica de negócio no serviço (Tell, Don't Ask violation)
public class OrderService {
    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PENDING) {  // ← Perguntando
            order.setStatus(OrderStatus.CANCELLED);       // ← Manipulando
            order.setTotalAmount(BigDecimal.ZERO);        // ← Manipulando
        }
    }
}
```

**Exemplo CORRETO (Rich Domain):**
```java
// ✅ Rich domain model - comportamento encapsulado
public class Order {
    private OrderStatus status;
    private BigDecimal totalAmount;

    public void cancel() {  // ← Tell (comando), não Ask
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Só é possível cancelar pedidos pendentes");
        }
        this.status = OrderStatus.CANCELLED;
        this.totalAmount = BigDecimal.ZERO;
        // Validações e regras de negócio ficam aqui
    }

    public boolean isPending() {  // ← Query methods são OK para leitura
        return status == OrderStatus.PENDING;
    }

    // Getters apenas para leitura, sem setters que expõem mutabilidade
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}

// ✅ Serviço apenas coordena, não contém lógica de domínio
public class OrderService {
    public void cancelOrder(UUID orderId) {
        var order = repository.findById(orderId);
        order.cancel();  // ← Tell: diz o que fazer, não manipula estado
        repository.save(order);
    }
}
```

**Benefícios:**
- Lógica de negócio centralizada no domínio (single source of truth)
- Invariantes protegidas (validações dentro do modelo)
- Testes mais simples (testar domínio sem dependências externas)
- Menor acoplamento entre serviços e modelos

(Added 2026-01-07)

## 📝 Convenções de Código (Backend)

### Ordenação de Métodos em Classes

**Regra:** Métodos devem ser ordenados de **público para privado**, do **topo para baixo**.

**Princípios:**
1. **Visibilidade decrescente:** public → protected → package-private → private
2. **Nenhum método privado pode aparecer acima de um método público**
3. **Métodos privados são ordenados pela ordem de invocação:**
   - Métodos privados chamados primeiro pelos públicos aparecem primeiro
   - Métodos privados chamados depois aparecem depois
   - Isso cria um "fluxo de leitura" natural: top-down

**Exemplo correto:**
```java
@Service
public class ReviewServiceImpl implements ReviewService {

    // 1. Campos (private final)
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WineRepository wineRepository;

    // 2. Construtor
    public ReviewServiceImpl(ReviewRepository reviewRepository, ...) {
        this.reviewRepository = reviewRepository;
        // ...
    }

    // 3. Métodos públicos (interface implementation)
    @Override
    public ReviewResponse createReview(CreateReviewRequest request, UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(...);
        var wine = wineRepository.findById(wineId).orElseThrow(...);
        var review = toReview(request, user, wine);  // ← chama privado
        reviewRepository.save(review);
        return toReviewResponse(review, ...);  // ← chama privado
    }

    @Override
    public ReviewResponse updateReview(UUID reviewId, ...) {
        // ...
    }

    // 4. Métodos privados (ordenados pela ordem de chamada acima)
    private Review toReview(CreateReviewRequest request, User user, Wine wine) {
        // Chamado primeiro no createReview
    }

    private ReviewResponse toReviewResponse(Review review, ...) {
        // Chamado depois no createReview
    }

    private UserSummaryResponse toUserSummary(User user) {
        // Chamado por toReviewResponse
    }

    private WineSummaryResponse toWineSummary(Wine wine) {
        // Chamado por toReviewResponse
    }

}
```

**Benefícios:**
- Leitura natural top-down (public API → implementação)
- Facilita navegação no código
- Entendimento progressivo: vê-se primeiro "o que" a classe faz, depois "como"

### Visibilidade de Campos e Constantes

**Regra:** A menos que sejam usados em várias classes diferentes, campos e constantes em uma determinada classe **não devem** ser declarados como `public`.

**Princípios:**
1. **Encapsulamento:** Campos e constantes devem ter a menor visibilidade possível
2. **Single Responsibility:** Se uma constante é usada em múltiplas classes, considere criar uma classe de constantes compartilhadas
3. **Evitar acoplamento:** Constantes públicas em classes de serviço/repository acoplam clientes desnecessariamente

**Exemplos:**

```java
// ❌ INCORRETO - Constantes públicas desnecessárias
@Service
public class JsonRpcCodecImpl implements JsonRpcCodec {
    public static final String CODE = "code";      // ❌ Só usado nesta classe
    public static final String MESSAGE = "message"; // ❌ Só usado nesta classe
    public static final String DATA = "data";       // ❌ Só usado nesta classe

    private final ObjectMapper mapper;
    // ...
}

// ✅ CORRETO - Constantes privadas (uso interno)
@Service
public class JsonRpcCodecImpl implements JsonRpcCodec {
    private static final String CODE = "code";      // ✅ Encapsulada
    private static final String MESSAGE = "message"; // ✅ Encapsulada
    private static final String DATA = "data";       // ✅ Encapsulada

    private final ObjectMapper mapper;
    // ...
}

// ✅ CORRETO - Constantes compartilhadas em classe dedicada
public final class JsonRpcErrorCodes {
    private JsonRpcErrorCodes() {} // Previne instanciação

    public static final int PARSE_ERROR = -32700;       // ✅ Usado em múltiplas classes
    public static final int INVALID_REQUEST = -32600;   // ✅ Usado em múltiplas classes
    public static final int METHOD_NOT_FOUND = -32601;  // ✅ Usado em múltiplas classes
}
```

**Quando usar `public`:**
- Constantes de configuração compartilhadas (em classes de constantes dedicadas)
- Códigos de erro/status usados por múltiplos componentes
- Enums de domínio usados em várias camadas

**Quando usar `private`:**
- Constantes usadas apenas internamente na classe
- Valores de configuração específicos da implementação
- Strings de validação, mensagens de erro, chaves JSON internas

(Adicionado 2025-12-08)

### Java Moderno (Java 21)

- ✅ **`var`** para inferência de tipo (quando o tipo é óbvio)
- ✅ **Records** para DTOs e Commands/Events imutáveis
- ✅ **Sealed classes** para hierarquias fechadas (Commands, Events)
- ✅ **Pattern matching** com `switch` e `instanceof`
- ✅ **Text blocks** para strings multilinha

**Exemplo:**
```java
// Records para Commands
public sealed interface AccountCommand extends Command {
    record CreateAccount(
        UUID commandId,
        Instant timestamp,
        UUID accountId,
        String username,
        String usernameKey
    ) implements AccountCommand {}
}

// Pattern matching em switch
private void replay() {
    for (AccountEvent event : history) {
        switch (event) {
            case AccountCreated e -> apply(e);
            case AccountDeleted e -> apply(e);
            default -> throw new DomainException("Unexpected: " + event);
        }
    }
}

// var para clareza
final var accountStream = repository.load(accountId);
final var accountAggregate = AccountAggregate.from(...);
```

### Anotações Lombok

- ✅ `@Slf4j` - Logging automático
- ✅ `@Getter` - Getters seletivos (não usar `@Data` indiscriminadamente)
- ❌ Evitar `@Data` em entidades de domínio (preferir imutabilidade)

### Anotações Jackson (JSON Serialization)

**REGRA:** Evite `@JsonProperty` quando os nomes dos campos Java correspondem exatamente aos campos JSON.

Jackson ObjectMapper faz mapeamento automático quando os nomes coincidem (case-sensitive).

**Quando NÃO usar `@JsonProperty`:**

```java
// ✅ CORRETO - Sem anotação (nomes coincidem)
public record Person(String name, long id) {}

// JSON correspondente
{
    "name": "Lucas",
    "id": 123
}
```

**Quando USAR `@JsonProperty`:**

```java
// ✅ CORRETO - Nomes diferentes (snake_case ↔ camelCase)
public record User(
    @JsonProperty("user_name") String userName,
    @JsonProperty("created_at") Instant createdAt
) {}

// JSON correspondente
{
    "user_name": "lucas",
    "created_at": "2025-12-01T10:00:00Z"
}
```

**Outras anotações úteis:**
- `@JsonInclude(NON_NULL)` - Omite campos nulos na serialização
- `@JsonIgnore` - Exclui campo da serialização/desserialização
- `@JsonAlias` - Aceita múltiplos nomes na desserialização

(Added 2025-12-01)

### Formatação de Classes

**REGRA CRÍTICA - Linha em Branco Antes do Closing Bracket:**

Sempre deixar **uma linha em branco** antes do closing bracket (`}`) de **qualquer classe**.

**Exceção:** Records **não** precisam da linha em branco (são estruturas compactas).

**Exemplos:**

```java
// ✅ CORRETO - Classe regular com linha em branco
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    public Review createReview(CreateReviewRequest request) {
        // implementação
    }

}  // ← Linha em branco antes do closing bracket

// ✅ CORRETO - Record sem linha em branco (exceção)
public record ReviewResponse(
    String id,
    Integer rating,
    String notes
) {}  // ← SEM linha em branco (records são compactos)

// ✅ CORRETO - Exception com linha em branco
public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}  // ← Linha em branco antes do closing bracket

// ❌ INCORRETO - Faltando linha em branco
public class ReviewService {
    public void doSomething() {
        // ...
    }
}  // ← SEM linha em branco (errado!)
```

**Justificativa:**
- Melhora legibilidade visual do código
- Facilita navegação em arquivos grandes
- Consistência no codebase
- Preferência pessoal do desenvolvedor

### Formatação de Lambdas e Method Chaining

**REGRA CRÍTICA - Closing Parenthesis na Mesma Linha (Updated 2025-10-26):**

Sempre colocar o **closing parenthesis `)`** na **mesma linha** do último elemento, seja em:
- **Lambda expressions** com **method chaining** (builders como Spring Security, JPA Criteria, etc.)
- **Chamadas de construtores** com múltiplos parâmetros
- **Chamadas de métodos** com múltiplos parâmetros

**Princípio:** Evitar parênteses "soltos" em linhas isoladas.

**Exemplos:**

```java
// ✅ CORRETO - Closing parenthesis na mesma linha
@Bean
public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll());
    return http.build();
}

// ✅ CORRETO - Múltiplos níveis de nesting
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    .anyRequest().authenticated());
    return http.build();
}

// ❌ INCORRETO - Parênteses isolados em linha separada
http
        .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );  // ← Parêntese isolado (evitar!)

// ✅ CORRETO - Constructor calls (closing parenthesis na mesma linha)
final var request = new CreateReviewRequest(
        testWine.getId().toString(),
        5,
        "Excelente vinho!",
        null);

// ✅ CORRETO - Method calls (closing parenthesis na mesma linha)
final var result = someService.processData(
        param1,
        param2,
        param3,
        param4);

// ❌ INCORRETO - Closing parenthesis isolado
final var request = new CreateReviewRequest(
        testWine.getId().toString(),
        5,
        "Excelente vinho!",
        null
);  // ← Parêntese isolado (evitar!)
```

**Justificativa:**
- Reduz poluição visual (menos linhas "vazias" apenas com `)` ou `);`)
- Mantém o código mais compacto e legível
- Segue convenção comum em projetos Spring Boot modernos
- Facilita leitura top-down (cada linha tem conteúdo significativo)

**Onde aplicar:**
- ✅ Spring Security `HttpSecurity` builders
- ✅ JPA Criteria API
- ✅ Stream API com lambdas longos
- ✅ Builders fluentes com lambdas
- ✅ Qualquer method chaining com lambda expressions
- ✅ Chamadas de construtores com múltiplos parâmetros (DTOs, entities, test objects)
- ✅ Chamadas de métodos com múltiplos parâmetros

### Tratamento de Exceções

**Regras:**
- **Domain exceptions** específicas por tipo de erro
- Hierarquia de exceções: `DomainException` → exceções específicas
- Validações com mensagens claras em português

**Hierarquia padrão de exceções de domínio:**
```java
DomainException (abstrata)
├── ResourceNotFoundException (404 NOT FOUND)
├── InvalidRatingException (400 BAD REQUEST)
├── UnauthorizedAccessException (403 FORBIDDEN)
├── BusinessRuleViolationException (422 UNPROCESSABLE ENTITY)
└── InvalidTokenException (401 UNAUTHORIZED)
```

**Exemplo:**
```java
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();

}

public class InvalidAccountException extends DomainException {
    public InvalidAccountException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}

// Uso em serviços
if (status != AccountStatus.NEW) {
    throw new AccountCreationException(
        "Uma conta já com este nome de usuário já foi criada " +
        "ou está em um estado inválido: " + command.username()
    );
}
```

**GlobalExceptionHandler:**
- Handler unificado para `DomainException` usando polimorfismo
- Status HTTP determinado por `getHttpStatus()` de cada exceção
- Handlers legados (`IllegalArgumentException`, `SecurityException`) marcados como `@Deprecated`

## 🎨 Estilo de Controllers

### REST Controllers

**REGRA CRÍTICA - Documentação OpenAPI Obrigatória:**

**SEMPRE** adicionar anotações OpenAPI/Swagger ao criar **novos endpoints REST**.

**Anotações obrigatórias:**
- `@Tag` - No nível da classe para agrupar endpoints
- `@Operation` - Em cada método endpoint (summary + description)
- `@ApiResponses` - Documentar todos os status HTTP possíveis
- `@Parameter` - Para path variables e query params

**Convenções:**
- Anotação `@RestController` + `@RequestMapping`
- Injeção via construtor (não `@Autowired` em fields)
- Validação no controller, business logic no handler/service
- Headers opcionais: `X-Correlation-Id` para rastreamento
- Logs informativos em português
- **Documentação OpenAPI completa**

**Exemplo completo:**
```java
@Slf4j
@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "API de gerenciamento de avaliações de vinhos")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @Operation(
        summary = "Criar avaliação de vinho",
        description = "Cria uma nova avaliação para um vinho específico. Requer autenticação (JWT)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Avaliação criada com sucesso",
            content = @Content(schema = @Schema(implementation = ReviewResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos (rating fora do range 1-5)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Vinho ou usuário não encontrado"
        )
    })
    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @RequestBody @Valid CreateReviewRequest request) {
        log.info("Recebida requisição para criar review do vinho: {}", request.wineId());
        var review = service.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @Operation(
        summary = "Buscar avaliação por ID",
        description = "Retorna os detalhes de uma avaliação específica pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Avaliação encontrada",
            content = @Content(schema = @Schema(implementation = ReviewResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Avaliação não encontrada"
        )
    })
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getById(
            @Parameter(description = "ID da avaliação", required = true)
            @PathVariable UUID reviewId) {
        log.info("Recebida requisição para buscar review: {}", reviewId);
        var review = service.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

}
```

**Workflow ao criar novos endpoints:**
1. Implementar método do controller
2. Adicionar `@Operation` com summary e description
3. Adicionar `@ApiResponses` para **todos** os status HTTP possíveis
4. Adicionar `@Parameter` para path/query params
5. Testar endpoint no Swagger UI (`/swagger-ui.html`)
6. Atualizar README.md com novo endpoint

**Status HTTP a documentar:**
- `200 OK` - GET/PUT bem-sucedido
- `201 Created` - POST bem-sucedido
- `204 No Content` - DELETE bem-sucedido
- `400 Bad Request` - Validação falhou
- `401 Unauthorized` - Token inválido/expirado
- `403 Forbidden` - Sem permissão (ownership)
- `404 Not Found` - Recurso não encontrado
- `422 Unprocessable Entity` - Regra de negócio violada
- `501 Not Implemented` - Endpoint planejado mas não implementado

## 📋 Javadoc e Comentários

### Javadoc

- **Obrigatório** para classes públicas e interfaces
- **Obrigatório** para métodos públicos (incluir `@param`, `@return`, `@throws` quando aplicável)
- **Não necessário** para métodos privados (código deve ser autoexplicativo)
- Incluir `@author` e `@date` em classes
- Descrição concisa em português

**(Atualizado 2025-12-16: Regra de métodos privados)**

**Exemplo - Classe:**
```java
/**
 * Controller para gerenciamento de contas de usuário.
 *
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@RestController
public class AccountController {

}
```

**Exemplo - Método Público:**
```java
/**
 * Cria uma nova conta de usuário.
 *
 * @param command dados da conta a ser criada
 * @return a conta criada com ID gerado
 * @throws InvalidAccountException se os dados forem inválidos
 */
public Account createAccount(CreateAccountCommand command) {
    // ...
}
```

### Comentários inline

- Usar quando a lógica não é óbvia
- Preferir código autoexplicativo a comentários excessivos
- Podem ser em português

## 🧪 Testes (Backend)

### Estrutura de Testes

- JUnit 5
- Cobertura de caminhos de sucesso e falha
- Testes de integração com contexto completo
- Mock apenas quando necessário (preferir testes reais)

### Nomenclatura

- `should[ExpectedBehavior]When[StateUnderTest]`
- Exemplo: `shouldCreateAccountWhenValidCommand()`

## 🔧 Logging (Backend)

- `@Slf4j` do Lombok
- Mensagens informativas em português
- Incluir contexto relevante (IDs, usernames, etc.)
- Níveis apropriados: INFO para fluxo, DEBUG para detalhes, ERROR para exceções

**Onde aplicar logging:**
- ✅ **Service layer** - Business logic, orchestration (CreateAccountHandler, ReviewService)
- ✅ **Controllers** - REST endpoints entrada/saída (ReviewController, AuthController)
- ✅ **Infrastructure layer** - Transport, codec, external I/O (ServerStdioTransport, JsonRpcCodec)
- ✅ **Application layer** - Client/server implementations (McpServerImpl, McpClientImpl)
- ❌ **Domain POJOs** - Records, entities, value objects (apenas lógica de domínio, sem side effects)
- ❌ **Configuration classes** - @ConfigurationProperties (apenas dados de config)
- ❌ **DTOs** - Request/Response records (apenas transferência de dados)

**Exemplo:**
```java
log.info("Handling CreateAccount command for username: {}", command.username());
log.info("Account {} created successfully for username: {}", accountId, command.username());
```

(Updated 2025-12-20: Added guidance on where to apply logging)

## 📦 Maven / Gerenciamento de Dependências

### Versões de Dependências em Properties

**Regra:** Sempre declarar versões de dependências em `<properties>` e referenciar via placeholders.

**Por quê:**
- Centraliza versões em um único lugar
- Facilita upgrades (atualiza em um lugar só)
- Evita inconsistências de versões entre módulos
- Melhora legibilidade do pom.xml

**Exemplo:**
```xml
<properties>
    <!-- Java -->
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>

    <!-- Spring -->
    <spring-boot.version>3.2.0</spring-boot.version>

    <!-- Security -->
    <jwt.version>0.12.6</jwt.version>

    <!-- Database -->
    <postgresql.version>42.7.1</postgresql.version>
    <flyway.version>10.4.1</flyway.version>

    <!-- Testing -->
    <testcontainers.version>1.19.3</testcontainers.version>

    <!-- Utilities -->
    <lombok.version>1.18.30</lombok.version>
</properties>

<dependencies>
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>${jwt.version}</version>  <!-- ← Placeholder -->
    </dependency>

    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>${postgresql.version}</version>  <!-- ← Placeholder -->
    </dependency>
</dependencies>
```

**Nomenclatura de propriedades:**
- Usar kebab-case ou dot notation: `jwt.version` ou `jwt-version`
- Preferir sufixo `.version` para clareza
- Agrupar por categoria (Spring, Database, Security, etc.)

## 🔧 Injeção de Dependências e Configurações

### Injeção de Propriedades (Configuration Properties)

**Regra:** NUNCA usar `@Value` ou field injection. SEMPRE usar `@ConfigurationProperties` com POJOs dedicados e injeção via construtor.

**Por quê:**
- Type-safe: validação em tempo de compilação
- Testável: fácil criar instâncias para testes
- Centralizado: todas as configs de um módulo em uma classe
- Imutável: usar `final` nos campos para segurança
- Sem reflexão: injeção via construtor é explícita

**Exemplo CORRETO (✅):**
```java
// 1. POJO de configuração
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private final String secret;
    private final Long expiration;

    public JwtProperties(String secret, Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }

}

// 2. Habilitar no config
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {
    // ...

}

// 3. Usar via constructor injection
@Component
public class JwtUtil {
    private final String secret;
    private final Long expirationMs;

    public JwtUtil(JwtProperties properties) {
        this.secret = properties.getSecret();
        this.expirationMs = properties.getExpiration();
    }

    public String generateToken(UUID userId) {
        // Usa this.secret e this.expirationMs
    }

}
```

**Exemplo INCORRETO (❌):**
```java
// ❌ NUNCA FAZER ISSO
@Component
public class JwtUtil {
    @Value("${jwt.secret}")  // ← Field injection!
    private String secret;

    @Value("${jwt.expiration}")
    private Long expirationMs;
}
```

**application.yml correspondente:**
```yaml
jwt:
  secret: your-secret-key-min-256-bits-32chars
  expiration: 3600000  # 1 hora em milissegundos
```

## 🗄️ JPA / Hibernate

### Callbacks de Entidade

**REGRA CRÍTICA:** Apenas **UM método por tipo de callback** por entidade.

Jakarta Persistence permite apenas um método anotado com cada tipo de callback:
- `@PrePersist` - UM método apenas
- `@PreUpdate` - UM método apenas
- `@PostPersist`, `@PostUpdate`, `@PreRemove`, `@PostRemove`, `@PostLoad` - UM de cada

**❌ INCORRETO - Múltiplos callbacks do mesmo tipo:**
```java
@Entity
public class Review {

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @PrePersist
    @PreUpdate  // ❌ ERRO: segundo @PreUpdate
    protected void validate() {
        // validações
    }

    @PrePersist
    @PreUpdate  // ❌ ERRO: terceiro @PreUpdate
    protected void normalize() {
        // normalizações
    }
}
// Resultado: PersistenceException ao inicializar EntityManagerFactory
```

**✅ CORRETO - Um callback chama método privado:**
```java
@Entity
public class Review {

    @PrePersist
    protected void onCreate() {
        validateAndNormalize();
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        validateAndNormalize();
        updatedAt = Instant.now();
    }

    private void validateAndNormalize() {
        // Normaliza campos
        if (notes != null) {
            notes = notes.trim();
        }

        // Valida regras de negócio
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating deve estar entre 1 e 5");
        }
    }

}
```

**Por que essa limitação?**
- JPA precisa saber exatamente qual método executar em cada fase do ciclo de vida
- Múltiplos callbacks causam ambiguidade sobre ordem de execução
- Especificação Jakarta Persistence proíbe explicitamente

### Palavras Reservadas SQL

**Atenção:** Alguns nomes de colunas são palavras reservadas em bancos de dados.

**Exemplos comuns:**
- `year`, `month`, `day`, `hour` (temporais)
- `user`, `group`, `order` (entidades comuns)
- `key`, `value`, `index` (estruturas)

**Solução:** Escapar com backticks na anotação `@Column`:

```java
@Entity
public class Wine {

    @Column(name = "`year`")  // ✅ Escapado - funciona em H2, PostgreSQL, MySQL
    private Integer year;

    @Column(name = "`order`")  // ✅ Escapado
    private Integer order;

}
```

**Nota:** Backticks funcionam na maioria dos bancos. PostgreSQL também aceita aspas duplas (`"year"`), mas backticks são mais portáveis.

## 🔒 Spring Security

### Filtros de Segurança como @Bean

**REGRA:** Filtros customizados devem ser declarados como `@Bean` em classes de configuração, **NÃO** como `@Component`.

**❌ INCORRETO - Filtro como @Component:**
```java
@Component  // ❌ Registro implícito, dificulta testes
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // ...
}

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter filter;  // Injetado

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http.addFilterBefore(filter, ...).build();
    }

}
```

**Problemas:**
- Filtro é registrado automaticamente pelo Spring
- Dificulta testes (precisa carregar todo contexto de segurança)
- Não fica claro que é parte da configuração de segurança

**✅ CORRETO - Filtro como @Bean:**
```java
// SEM @Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Classe POJO, sem anotações Spring

}

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter filter) {  // Injetado como parâmetro
        return http.addFilterBefore(filter, ...).build();
    }

}
```

**Vantagens:**
- **Controle explícito:** Fica claro que filtro é parte da configuração de segurança
- **Facilita testes:** Pode ser facilmente mockado em `@WebMvcTest` ou `@SpringBootTest`
- **Segue padrão Spring Security:** Configurações devem declarar seus beans
- **Evita registro duplo:** Filtro só é criado quando necessário

## 🚫 Anti-Padrões a Evitar (Backend)

- ❌ `@Autowired` em fields (usar injeção via construtor)
- ❌ **`@Value` para propriedades** (usar `@ConfigurationProperties` com POJOs)
- ❌ **Field injection** (sempre usar constructor injection)
- ❌ Getters/setters desnecessários (usar Lombok seletivamente)
- ❌ Lógica de negócio em controllers
- ❌ Exceptions genéricas (`throw new Exception()`)
- ❌ Magic numbers/strings (usar constantes)
- ❌ Null checks excessivos (usar `Optional` ou validação antecipada)
- ❌ **Versões hardcoded em dependencies** (usar `<properties>`)

## ✅ Checklist de Code Review (Backend)

- [ ] Código em inglês, comentários podem ser em português
- [ ] Injeção de dependências via construtor (NUNCA field injection)
- [ ] **Propriedades via `@ConfigurationProperties` (NUNCA `@Value`)**
- [ ] Exceptions específicas do domínio
- [ ] Logs informativos nos pontos-chave
- [ ] Javadoc em classes públicas com @author e @date
- [ ] Uso apropriado de Java 21 features (var, records, pattern matching)
- [ ] Separação clara entre camadas (domain/application/infrastructure)
- [ ] Testes cobrindo casos de sucesso e falha
- [ ] Validações com mensagens claras
- [ ] Sem magic numbers ou strings hardcoded
- [ ] **Versões de dependências em `<properties>` com placeholders**
- [ ] **Métodos ordenados: public → private (invocation flow)**
- [ ] **Linha em branco antes de closing bracket (exceto records)**
- [ ] **OpenAPI/Swagger annotations em todos os endpoints REST**

---

# 📱 PART 3: FRONTEND STANDARDS (Flutter/Dart)

> **Use this section for:** Mobile app development, Flutter projects, cross-platform apps.
> **Copy from here when creating:** Flutter applications, mobile projects.

## 📦 Estrutura de Projeto (Flutter)

### Feature-based Folder Structure
```
lib/
├── features/              # Features do app
│   ├── auth/             # Feature de autenticação
│   │   ├── data/         # Data sources, repositories
│   │   ├── domain/       # Entities, use cases
│   │   └── presentation/ # Screens, widgets, providers
│   ├── reviews/          # Feature de reviews
│   └── wines/            # Feature de vinhos
├── core/                  # Código compartilhado
│   ├── router/           # Configuração de rotas (go_router)
│   ├── theme/            # Tema do app
│   ├── utils/            # Utilitários
│   └── constants/        # Constantes
└── common/                # Widgets e código reutilizável
    ├── widgets/          # Widgets comuns
    └── models/           # Models compartilhados
```

## 📝 Convenções de Código (Flutter/Dart)

### Nomenclatura

- **Classes:** PascalCase - `ReviewCard`, `AuthProvider`, `WineList`
- **Arquivos:** snake_case - `review_card.dart`, `auth_provider.dart`, `wine_list.dart`
- **Variáveis/Métodos:** camelCase - `getUserById`, `isLoading`, `reviewList`
- **Constantes:** lowerCamelCase (Dart convention) - `maxFileSize`, `apiBaseUrl`

### Models e DTOs

- Usar **freezed** para models imutáveis
- Usar **json_serializable** para serialização
- Gerar código com `flutter pub run build_runner build --delete-conflicting-outputs`

**Exemplo:**
```dart
import 'package:freezed_annotation/freezed_annotation.dart';

part 'review.freezed.dart';
part 'review.g.dart';

@freezed
class Review with _$Review {
  const factory Review({
    required String id,
    required int rating,
    required String notes,
    String? imageUrl,
  }) = _Review;

  factory Review.fromJson(Map<String, dynamic> json) => _$ReviewFromJson(json);
}
```

### State Management (Riverpod)

- Usar Riverpod para state management
- Providers no mesmo arquivo da feature ou em `providers/`
- Naming: `reviewProvider`, `authStateProvider`

**Exemplo:**
```dart
@riverpod
class ReviewList extends _$ReviewList {
  @override
  Future<List<Review>> build() async {
    final repository = ref.read(reviewRepositoryProvider);
    return repository.getReviews();
  }

  Future<void> addReview(Review review) async {
    final repository = ref.read(reviewRepositoryProvider);
    await repository.createReview(review);
    ref.invalidateSelf();
  }
}
```

### Error Handling

- Usar dio interceptors para retry logic
- Tratar erros de rede gracefully
- Mostrar mensagens de erro amigáveis ao usuário

**Exemplo:**
```dart
class DioClient {
  final Dio _dio = Dio()
    ..interceptors.add(
      RetryInterceptor(
        dio: _dio,
        maxRetries: 3,
        retryDelays: const [
          Duration(seconds: 1),
          Duration(seconds: 2),
          Duration(seconds: 3),
        ],
      ),
    );
}
```

### Widgets

- Preferir StatelessWidget quando possível
- Extrair widgets complexos em componentes separados
- Usar `const` constructors sempre que possível para performance

### Formatação

- Usar `dart format .` para formatar código
- Seguir Effective Dart style guide
- Limite de 80 caracteres por linha (configurável)

## 🧪 Testes (Flutter)

### Tipos de Testes

1. **Unit Tests:** Lógica de negócio, providers, repositories
2. **Widget Tests:** Widgets individuais e telas
3. **Golden Tests:** Testes de regressão visual

### Nomenclatura

- Arquivos de teste: `_test.dart` suffix
- Exemplo: `review_card_test.dart`, `auth_provider_test.dart`

**Exemplo de Widget Test:**
```dart
void main() {
  testWidgets('ReviewCard displays review information', (tester) async {
    final review = Review(
      id: '1',
      rating: 5,
      notes: 'Excellent wine!',
    );

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: ReviewCard(review: review),
        ),
      ),
    );

    expect(find.text('Excellent wine!'), findsOneWidget);
    expect(find.text('5'), findsOneWidget);
  });
}
```

## 🎨 UI/UX

- Material Design como base
- Tema customizado em `core/theme/`
- Responsividade: testar em diferentes tamanhos de tela
- Acessibilidade: labels para screen readers

## ✅ Checklist de Code Review (Flutter)

- [ ] Código segue Effective Dart guidelines
- [ ] Models usam freezed + json_serializable
- [ ] State management com Riverpod
- [ ] Widgets extraídos quando complexos
- [ ] Uso de `const` constructors
- [ ] Error handling apropriado
- [ ] Testes (unit/widget) para lógica crítica
- [ ] Formatação com `dart format`
- [ ] Sem warnings no `flutter analyze`

---

# 🐳 PART 4: INFRASTRUCTURE STANDARDS (Docker, Testing, CI/CD)

> **Use this section for:** DevOps standards, testing infrastructure, containerization best practices.
> **Reusable in:** Microservices, containerized applications, CI/CD pipelines.

## 🧪 Integration Testing with Testcontainers

### Test Structure Standards

**Naming Conventions:**
- **Unit Tests:** `*Test.java` (e.g., `ReviewServiceTest`)
- **Integration Tests:** `*IT.java` (e.g., `ReviewControllerIT`, `AuthControllerIT`)
- **Location:** Mirror production package structure under `src/test/java/`

**Base Class Pattern:**
```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Testcontainers
@Transactional
public abstract class AbstractIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("appname_test")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);  // Performance optimization

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

### Integration Test Best Practices

**✅ DO:**
- Use `@Transactional` for automatic rollback (test isolation)
- Use static `@Container` for shared PostgreSQL container (performance)
- Mock external APIs (Google OAuth, payment gateways, etc.)
- Test database constraints (FK, CHECK, CASCADE)
- Test pagination, sorting, filtering
- Test all HTTP status codes (200, 201, 400, 403, 404, 422, 500)

**❌ DON'T:**
- Don't create new container per test class (slow)
- Don't use H2 for integration tests (not production-like)
- Don't test implementation details (focus on API contract)
- Don't skip testing error scenarios

### Test Data Setup Pattern

```java
@BeforeEach
void setupTestData() {
    // Create test entities directly via repositories
    testUser = userRepository.save(createUser());
    testWine = wineRepository.save(createWine());
}

private User createUser() {
    var user = new User();
    user.setEmail("test@example.com");
    user.setDisplayName("Test User");
    return user;
}
```

### Integration Test Documentation

**Always include class-level Javadoc:**
```java
/**
 * Integration tests for ReviewController with Testcontainers PostgreSQL.
 * <p>
 * Tests all CRUD endpoints against a real PostgreSQL database to verify:
 * - REST API behavior
 * - Database constraints (rating 1-5, cascade delete, foreign keys)
 * - Exception handling (404, 403, 400, 422)
 * - Pagination and sorting
 * <p>
 * <strong>Test strategy:</strong>
 * - Uses real PostgreSQL via Testcontainers (production-like environment)
 * - Each test is @Transactional (auto-rollback for isolation)
 * - Test data inserted directly via repositories for speed
 * - MockMvc for HTTP request/response testing
 *
 * @author lucas
 * @date 22/10/2025
 */
class ReviewControllerIT extends AbstractIntegrationTest {
    // tests...
}
```

## 🐳 Docker Standards

### Dockerfile Best Practices

**Multi-stage builds:**
```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Docker Compose for local development:**
- Use `docker-compose.yml` in `infra/` directory
- Define health checks for dependent services
- Use named volumes for data persistence
- Use environment variables for configuration
- Document all services in README

## 🚀 CI/CD Standards

### GitHub Actions Workflows

**Path-based triggers (monorepo):**
```yaml
on:
  push:
    branches: [main, develop]
    paths:
      - 'services/api/**'
      - '.github/workflows/ci-api.yml'
```

**Caching strategy:**
```yaml
- name: Cache Maven packages
  uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
```

**Test execution in CI:**
- Run unit tests first (fast feedback)
- Run integration tests with Testcontainers
- Generate coverage reports
- Fail build if tests fail

---

# 🔄 Histórico de Atualizações

- **2025-10-22 (v6)** - Adicionada PART 4: INFRASTRUCTURE com padrões de Testcontainers, Docker e CI/CD. Implementados 37 testes de integração (ReviewControllerIT, AuthControllerIT) com PostgreSQL real
- **2025-10-21 (v5)** - Adicionada regra crítica de organização de documentação (estrutura 3 partes: General/Backend/Frontend) para CLAUDE.md, CODING_STYLE.md e README.md
- **2025-10-21 (v4)** - Reestruturado em 3 partes (GENERAL/BACKEND/FRONTEND) para facilitar reuso em diferentes tipos de projetos
- **2025-10-21 (v3)** - Adicionada diretriz de "Next Steps (Roadmap)" no CLAUDE.md para tracking de próximos passos
- **2025-10-21 (v2)** - Corrigida regra de formatação: linha em branco antes de closing bracket para **todas as classes** (exceto records). Adicionada regra obrigatória de documentação OpenAPI/Swagger
- **2025-10-21 (v1)** - Adicionadas regras de exceções de domínio e formatação inicial
- **2025-10-20** - Adicionadas regras de JPA callbacks e Spring Security filters
- **Versão inicial** - Estabelecidos padrões base do projeto
