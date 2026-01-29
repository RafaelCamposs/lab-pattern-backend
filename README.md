# Daily Pattern Backend

API REST desenvolvida em Kotlin com Spring Boot para uma plataforma de aprendizado de Design Patterns através de desafios diários gerados por IA.

## Sobre o Projeto

O Daily Pattern é uma aplicação que ajuda desenvolvedores a aprenderem Design Patterns de forma prática e interativa. A plataforma gera desafios diários personalizados usando IA (OpenAI) e avalia automaticamente as submissões dos usuários, fornecendo feedback detalhado sobre a implementação dos padrões.

### Funcionalidades Principais

- Autenticação e autorização com JWT
- Geração automática de desafios diários baseados em Design Patterns
- Avaliação automática de submissões usando IA
- Sistema de progresso do usuário
- Agendamento de tarefas para geração de desafios
- API documentada com Swagger/OpenAPI

## Arquitetura

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:

- **domain**: Lógica de negócio pura (casos de uso, entidades, interfaces)
- **application**: Camada de infraestrutura (controllers, configurações, persistência)

### Tecnologias Utilizadas

- **Kotlin 2.2.10** + JVM 22
- **Spring Boot 3.5.4**
- **Spring Security** com JWT
- **Spring Data JPA** + Hibernate
- **MySQL** (banco de dados principal)
- **H2** (banco de dados para testes)
- **Flyway** (migração de banco de dados)
- **OpenAI API** (geração e avaliação de desafios)
- **Gradle** com Kotlin DSL
- **JUnit 5** + **MockK** (testes)
- **Testcontainers** (testes de integração)
- **SpringDoc OpenAPI** (documentação da API)
- **Jacoco** (cobertura de código)
- **SonarQube** (qualidade de código)

## Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 22** ou superior
- **Gradle 8+** (ou use o wrapper incluído)
- **MySQL 8.0+**
- **Docker** (opcional, para execução com containers)

## Configuração

### 1. Configuração do Banco de Dados

Crie um banco de dados MySQL:

```sql
CREATE DATABASE daily_pattern;
```

### 2. Variáveis de Ambiente

Configure as seguintes variáveis de ambiente antes de executar a aplicação:

#### Banco de Dados
```bash
export DB_URL="jdbc:mysql://localhost:3306/daily_pattern?useSSL=false&serverTimezone=UTC"
export DB_USER="seu_usuario"
export DB_PASSWORD="sua_senha"
```

#### OpenAI API
```bash
export OPENAI_API_KEY="sua_chave_api"
export OPENAI_PROJECT_ID="seu_project_id"
export OPENAI_ORG_ID="sua_organization_id"
```

#### JWT
```bash
export JWT_SECRET_KEY="sua_chave_secreta_jwt_minimo_256_bits"
```

#### CORS (Opcional)
```bash
export CORS_ALLOWED_ORIGINS="http://localhost:3000,http://localhost:5173"
```

> **Dica**: Crie um arquivo `.env` ou `.env.local` para facilitar o gerenciamento das variáveis de ambiente (não comite este arquivo).

### 3. Exemplo de Arquivo de Configuração Local

Você pode criar um arquivo `application-local.yml` em `application/src/main/resources/`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/daily_pattern?useSSL=false&serverTimezone=UTC
    username: root
    password: password
  jpa:
    show-sql: true

server:
  port: 8080

logging:
  level:
    com.example: DEBUG
```

E executar com o perfil local: `--spring.profiles.active=local`

## Como Executar

### Usando Gradle Wrapper

1. **Build do projeto**:
```bash
./gradlew clean build
```

2. **Executar a aplicação**:
```bash
./gradlew :application:bootRun
```

3. **Executar os testes**:
```bash
./gradlew test
```

4. **Gerar relatório de cobertura**:
```bash
./gradlew jacocoTestReport
```

### Usando JAR

1. **Gerar o JAR**:
```bash
./gradlew :application:bootJar
```

2. **Executar o JAR**:
```bash
java -jar application/build/libs/application-0.0.1-SNAPSHOT.jar
```

### Usando Docker (Futuro)

```bash
docker-compose up -d
```

## Documentação da API

Após iniciar a aplicação, a documentação interativa estará disponível em:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Endpoints Principais

### Autenticação
- `POST /auth/register` - Registrar novo usuário
- `POST /auth/login` - Fazer login

### Desafios
- `GET /challenges/daily` - Obter desafio diário do usuário
- `GET /challenges/{id}` - Obter detalhes de um desafio
- `POST /admin/challenges/generate` - Gerar novo desafio (admin)

### Submissões
- `POST /submissions` - Submeter solução para um desafio
- `GET /submissions/{id}` - Obter detalhes de uma submissão
- `GET /submissions/challenge/{challengeId}` - Listar submissões de um desafio

### Progresso do Usuário
- `GET /user-progress` - Obter progresso do usuário
- `GET /user-progress/completed-patterns` - Listar padrões completados

### Health Check
- `GET /actuator/health` - Verificar saúde da aplicação
- `GET /actuator/info` - Informações da aplicação

## Estrutura do Banco de Dados

As migrações Flyway criam as seguintes tabelas:

- `users` - Usuários da plataforma
- `design_patterns` - Catálogo de Design Patterns
- `challenges` - Desafios gerados
- `submissions` - Submissões dos usuários
- `evaluations` - Avaliações das submissões
- `user_progress` - Progresso dos usuários

## Testes

O projeto possui testes unitários e de integração:

```bash
# Executar todos os testes
./gradlew test

# Executar testes do domínio
./gradlew :domain:test

# Executar testes da aplicação
./gradlew :application:test

# Gerar relatório de cobertura
./gradlew jacocoTestReport
```

Os relatórios de cobertura estarão disponíveis em:
- `domain/build/reports/jacoco/test/html/index.html`
- `application/build/reports/jacoco/test/html/index.html`

## Qualidade de Código

O projeto utiliza SonarQube para análise de qualidade:

```bash
./gradlew sonar
```

## Segurança

- Autenticação via JWT (JSON Web Tokens)
- Senhas criptografadas com BCrypt
- Autorização baseada em roles (USER, ADMIN)
- Proteção CSRF desabilitada (API stateless)
- CORS configurável via variável de ambiente

## Desenvolvimento

### Estrutura de Pacotes

```
daily-pattern-backend/
├── domain/                          # Camada de domínio
│   └── src/main/kotlin/com/example/domain/
│       ├── challenge/              # Casos de uso de desafios
│       ├── evaluation/             # Casos de uso de avaliação
│       ├── pattern/                # Casos de uso de padrões
│       ├── submission/             # Casos de uso de submissões
│       └── user/                   # Casos de uso de usuários
│
└── application/                     # Camada de aplicação
    └── src/main/kotlin/com/example/application/
        ├── config/                 # Configurações (Security, CORS, etc)
        ├── controller/             # Controllers REST
        ├── entity/                 # Entidades JPA
        ├── repository/             # Repositórios
        ├── scheduler/              # Tarefas agendadas
        └── service/                # Serviços e adaptadores
```

### Adicionando Novos Design Patterns

1. Adicione o padrão na tabela `design_patterns` via Flyway migration
2. O scheduler gerará automaticamente desafios para os novos padrões

### Boas Práticas

- Siga os princípios SOLID
- Mantenha a separação entre domínio e infraestrutura
- Escreva testes para novos casos de uso
- Documente endpoints com anotações do Swagger
- Use injeção de dependência do Spring
- Valide dados de entrada com Bean Validation
