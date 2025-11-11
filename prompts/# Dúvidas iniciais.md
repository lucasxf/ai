# Dúvidas iniciais

1. Você tem acesso ao contexto de outros chats? Ou ainda, algum tipo de memória e aprendizado compartilhado?
2. Você tem acesso à esses links abaixo? Se sim, leia o conteúdo completo dos dois primeiros (AWD Labs e Baeldung Spring AI).
  - https://awslabs.github.io/mcp/servers/cost-explorer-mcp-server
  - https://www.baeldung.com/spring-ai-model-context-protocol-mcp
  - https://github.com/lucasxf/wine-reviewer/tree/develop
  - https://github.com/lucasxf/ai/tree/develop

# Contexto das dúvidas e pedidos

1. No outro chat eu solicitei sua ajuda com a redação de uma série de artigos a respeito da minha experiência com Claude Code, e parte do que estamos preparando neste chat, minhas POCs em MCP, tem correlação direta com aquele chat lá. Não quero me repetir e explicar minha persona, estilo e necessidades novamente.
2. Enquanto Tech Manager, tenho responsabilidades a respeito dos custos dos serviços AWS utilizados pelas contas dos times que gerencio. FinOps é uma pauta bastante importante e recorrente no meu dia-a-dia. Quero adicionar à lista de POCs (talvez imediatamente após o Hello World e antes do buscador de fotos) o consumo do MCP Explorador de Custos para AWS. Quero começar com algo bem simples utilizando Spring Boot, Java (e se precisar, Python).
3. Eu utilizo um S3 para armazenar fotos no meu projeto pessoal `wine-reviewer`. Quero testar o MCP client de consulta de custos AWS utilizando esse projeto como cobaia
4. Preciso de ajuda com o preparo de um prompt brabo (ótimo), topíssimo (excelente) para utilizar no Claude Code e trabalhar com essas POCs que você sugeriu anteriormente.
5. Vou tentar concentrar pedidos de código-fonte no Claude Code, e as questões profissionais, acadêmicas, publicações em fóruns e mídias sociais, organização e planejamento aqui no Claude desktop.
6. Meus experimentos e projetos de IA ficarão concentrados no projetos `ai`.
7. Quero organizar a estrutura de projetos e branches de acordo com o tema.
   - Ex.: a "raiz" do projeto e itens genéricos, ficarão em `/ai` e na branch `develop`.
   - Projetos específicos, como a POC de MCP para  FinOps de AWS, moraria no diretório `/ai/mcp/poc-bla-bla-abc-xyz` 
   - o projeto teria um git flow: main -> develop -> develop-<assunto> -> feature/abc. Ex.: `main` -> `develop` -> `develop-mcp` -> `feature/poc-aws-finops`. Ou algo assim
8. Revise este prompt