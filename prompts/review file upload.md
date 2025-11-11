/start-session 
# pom.xml
# WineReviewerApiApplication.java
# FileUploadResponse.java
# AwsConfig.java
# AwsProperties.java
# JpaConfig.java
# AuthController.java
# FileUploadController.java
# CommentRepository.java
# ReviewRepository.java
# UserRepository.java
# WineRepository.java
# CustomUserDetailsService.java
# JwtUtil.java
# FileUploadService.java
# impl/S3Service.java
# application-dev.yml
# application.yml
# docker-compose.yml

Com TodoWrite considere o contexto e instruções à seguir para criar um plano de:\
1. Construção dos testes unitários e de integração para o fluxo de upload de arquivos.\
2. Documentação do código desenvolvido para o fluxo de upload de arquivos.\
3. Revisão completa do código novo, contemplando:\
        3.1. Melhores práticas de codificação, segurança, qualidade, system design, legibilidade e semântica.\
        3.2. Limpeza de código comentado e desnecessário.\
        3.3. Remoção de mocks e hard coding\
4. Atualização dos scripts de bancos de dados para inclusão de usuário mock no startup da aplicação\
   de modo que eu não vou precisar ficar comentando ou fazendo hard coding toda vez que for testar via Postman\
    4.1. Dados sugeridos do usuário: validar método "mockUser()" no FileUploadController e CustomUserDetailsService\

Contexto:\
Para preservar o limite semanal de consumo de tokens e praticar mais\
vou concentrar por hora o uso do claude com a parte de frontend e tentar me virar sozinho con o backend.\
Comecei a desenvolver por conta própria o fluxo de upload de arquivos,\
quando tentei executar a aplicação localmente, encontrei diversos erros de configuração.\
especialmente relacionados à integração com a base de dados (PostgreSQL // Flyway),\
ao conflito de portas (8080 para docker e spring boot), e as credenciais do DB.\
Fiz os ajustes nos arquivos de propriedads e configurações, bem como, docker, schema, etc.\
Configurei mais cedo um bucket S3 que já está ativo e funcional. Não mexa nas propriedades do S3,\
a não ser que vá sugerir melhorias semânticas nos nomes dos campos, mas os valores devem permanecer os mesmos.\
Construí e testei via postman e o upload está funcionando.\
Precisei comentar trechos relacionados à autenticação, pois,\
o usuário que estou usando ainda não foi criado na base de dados.\

Ao final deste plano:
1. Crie uma diretriz nova (como em /directive) para sempre testar a subida da aplicação\
 quando houver mudanças relevantes em arquivos de configuração, estrutura da aplicação,\
  dependências, segurança ou criação de fluxos e integrações novos, etc.
1. Exibir alterações com explicações detalhadas
2. Revisar este prompt

Ainda não é necessário commitar o código, tampouco, atualizar arquivos como CLAUDE, CODING_STYLE, LEARNINGS,\
 etc., pois, isso será feito ao final dessa sessão quando eu rodar o /finish-session
