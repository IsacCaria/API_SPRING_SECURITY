# API REST com Spring Security e JWT

Projeto didático em Java 17 e Spring Boot 3, criado para demonstrar autenticação stateless com JWT e autorização por papéis de usuário.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Security
- Spring Data JPA
- H2 Database
- Auth0 Java JWT
- Maven

## Como executar

No terminal, dentro da pasta do projeto, execute:

```bash
mvn spring-boot:run
```

A aplicação será iniciada em:

```text
http://localhost:8080
```

O console do H2 estará disponível em:

```text
http://localhost:8080/h2-console
```

Dados de acesso do H2:

```text
JDBC URL: jdbc:h2:mem:securitydb
User: sa
Password: deixe em branco
```

## Endpoints principais

### Criar usuário

```http
POST /users
```

Exemplo de corpo da requisição:

```json
{
  "email": "maria@email.com",
  "password": "123456",
  "role": "ROLE_CUSTOMER"
}
```

Também é possível criar um administrador:

```json
{
  "email": "admin@email.com",
  "password": "123456",
  "role": "ROLE_ADMINISTRATOR"
}
```

Resposta esperada:

```text
201 Created
```

### Login

```http
POST /users/login
```

Exemplo:

```json
{
  "email": "maria@email.com",
  "password": "123456"
}
```

Resposta esperada:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Testar autenticação

```http
GET /users/test
```

Adicionar o header:

```text
Authorization: Bearer SEU_TOKEN
```

### Testar rota de CUSTOMER

```http
GET /users/test/customer
```

Apenas usuários com `ROLE_CUSTOMER` conseguem acessar.

### Testar rota de ADMINISTRATOR

```http
GET /users/test/administrator
```

Apenas usuários com `ROLE_ADMINISTRATOR` conseguem acessar.

### Consultar dados do usuário logado

```http
GET /users/me
```

Adicionar o header:

```text
Authorization: Bearer SEU_TOKEN
```

Resposta esperada:

```json
{
  "id": 1,
  "email": "maria@email.com",
  "roles": [
    "ROLE_CUSTOMER"
  ]
}
```

