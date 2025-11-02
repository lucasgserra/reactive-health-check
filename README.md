# Reactive HealthCheck - Siterelic API

Este é um projeto simples de estudo de **Spring Reactive** que realiza verificações de saúde (healthcheck) em sites utilizando a API do Siterelic.

## Endpoints

- **GET /health**  
  Retorna a saúde de um site específico.

- **GET /health/links**  
  Retorna a saúde de todos os endpoints registrados.

## Requisitos

- Java 17+
- Spring Boot
- Spring WebFlux
- Variável de ambiente `API_KEY` configurada com sua chave de autenticação do Siterelic

## Configuração

Antes de rodar a aplicação, configure sua chave do Siterelic na sua máquina:

```bash
export API_KEY=sua_chave_aqui   # Linux / Mac
setx API_KEY "sua_chave_aqui"  # Windows
