# 🚀 Backend - Sistema de Monitoramento de Sensores Festo

## Sprint 4 - Escalabilidade e Experiência Completa

### 👥 Integrantes

- **Nome:** [SEU NOME COMPLETO]
- **RM:** [SEU RM]
- **Nome:** [NOME DO PARCEIRO]
- **RM:** [RM DO PARCEIRO]

---

## 📋 Descrição

Backend escalável desenvolvido em **Spring Boot** para o sistema de monitoramento de sensores industriais Festo. Este projeto implementa uma arquitetura robusta em camadas com persistência em **PostgreSQL**, autenticação **JWT** via **Microsoft Entra ID** e documentação automática com **Swagger**.

---

## 🏗️ Arquitetura

### Estrutura de Pacotes

```
src/main/java/com/example/demo/
├── config/              # Configurações (Security, CORS, OpenAPI, DataInitializer)
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects
│   ├── request/         # DTOs de requisição
│   └── response/        # DTOs de resposta
├── entity/              # Entidades JPA
├── exception/           # Exceções customizadas e handlers
├── mapper/              # Mappers (Entity ↔ DTO)
├── repository/          # Repositories JPA
├── service/             # Lógica de negócio
└── tools/               # Classes utilitárias
```

### Camadas da Aplicação

1. **Controller**: Recebe requisições HTTP e retorna respostas
2. **Service**: Contém toda a lógica de negócio
3. **Repository**: Acesso aos dados (JPA/Hibernate)
4. **Entity**: Representação das tabelas do banco
5. **DTO**: Objetos de transferência de dados
6. **Mapper**: Conversão entre Entity e DTO

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia        | Versão | Descrição                  |
| ----------------- | ------ | -------------------------- |
| Java              | 17     | Linguagem de programação   |
| Spring Boot       | 3.5.0  | Framework principal        |
| Spring Security   | 3.5.0  | Autenticação e autorização |
| Spring Data JPA   | 3.5.0  | Persistência de dados      |
| PostgreSQL        | 16     | Banco de dados relacional  |
| Lombok            | Latest | Redução de boilerplate     |
| MapStruct         | 1.5.5  | Mapeamento de objetos      |
| SpringDoc OpenAPI | 2.3.0  | Documentação Swagger       |
| Docker Compose    | 3.8    | Orquestração de containers |

---

## 🚀 Início Rápido

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **Docker** e **Docker Compose**
- Conta **Microsoft Azure** (para autenticação Entra ID)

### 1. Clone o Repositório

```bash
git clone https://github.com/[SEU-USUARIO]/Backend-em-Java-com-Spring-Boot-e-H2
cd Backend-em-Java-com-Spring-Boot-e-H2
```

### 2. Configure as Variáveis de Ambiente

```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite o arquivo .env com suas configurações
nano .env
```

Preencha com as credenciais do **Azure AD**:

```properties
AZURE_TENANT_ID=seu-tenant-id
AZURE_CLIENT_ID=seu-client-id
```

### 3. Inicie o Banco de Dados

```bash
# Inicia PostgreSQL e PgAdmin
docker-compose up -d

# Verifique se os containers estão rodando
docker-compose ps
```

**Acessos:**

- **PostgreSQL**: `localhost:5432`
- **PgAdmin**: `http://localhost:5050`
  - Email: `admin@festo.com`
  - Senha: `admin123`

### 4. Compile e Execute a Aplicação

```bash
# Compile o projeto
mvn clean install -DskipTests

# Execute a aplicação
mvn spring-boot:run
```

### 5. Acesse a Aplicação

- **API Base**: `http://localhost:8080/api`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`
- **Health Check**: `http://localhost:8080/api/health`

---

## 📚 Documentação da API

### Endpoints Principais

#### 🔍 Sensores

```http
GET /api/sensores
```

**Resposta:**

```json
[
  {
    "id": "1",
    "nome": "Reed Switch",
    "status": "ok",
    "valor": 68.86,
    "tipo": "Digital",
    "unidade": "ciclos"
  }
]
```

#### 📊 Leituras

##### Listar Todas

```http
GET /api/readings
```

##### Buscar por Sensor

```http
GET /api/readings/{sensorId}
```

##### Buscar por ID

```http
GET /api/readings/id/{id}
```

##### Criar Nova Leitura

```http
POST /api/readings
Content-Type: application/json

{
  "sensorId": "1",
  "value": 75.5,
  "timestamp": "2025-09-30T10:00:00"
}
```

##### Gerar Dados de Teste

```http
POST /api/readings/generate?count=10
```

##### Deletar Leitura

```http
DELETE /api/readings/{id}
```

### Códigos de Status HTTP

| Código | Descrição                |
| ------ | ------------------------ |
| 200    | Sucesso                  |
| 201    | Criado com sucesso       |
| 204    | Deletado com sucesso     |
| 400    | Requisição inválida      |
| 404    | Recurso não encontrado   |
| 500    | Erro interno do servidor |

---

## 🔐 Autenticação com Microsoft Entra ID

### Configuração no Azure Portal

1. Acesse [Azure Portal](https://portal.azure.com)
2. Vá em **Azure Active Directory** > **App Registrations**
3. Crie novo registro:
   - Nome: `Festo Sensors API`
   - Tipo de conta: `Contas neste diretório organizacional`
   - URI de Redirecionamento: `http://localhost:8080`
4. Copie:
   - **Application (client) ID** → `AZURE_CLIENT_ID`
   - **Directory (tenant) ID** → `AZURE_TENANT_ID`

### Uso da Autenticação

```bash
# Obter token (exemplo com curl)
curl -X POST https://login.microsoftonline.com/{TENANT_ID}/oauth2/v2.0/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id={CLIENT_ID}" \
  -d "scope=api://{CLIENT_ID}/.default" \
  -d "grant_type=client_credentials"

# Usar token nas requisições
curl http://localhost:8080/api/readings \
  -H "Authorization: Bearer {TOKEN}"
```

---

## 🗄️ Banco de Dados

### Modelo de Dados

**Tabela: `readings`**

| Campo        | Tipo        | Descrição            |
| ------------ | ----------- | -------------------- |
| id           | BIGSERIAL   | Chave primária       |
| sensor_id    | VARCHAR(50) | ID do sensor         |
| sensor_value | DOUBLE      | Valor da leitura     |
| timestamp    | TIMESTAMP   | Data/hora da leitura |
| created_at   | TIMESTAMP   | Data de criação      |
| updated_at   | TIMESTAMP   | Data de atualização  |

### Gerenciamento

#### Via PgAdmin

1. Acesse `http://localhost:5050`
2. Login: `admin@festo.com` / `admin123`
3. Adicione servidor:
   - Host: `postgres`
   - Port: `5432`
   - Database: `festo_sensors`
   - Username: `festo_user`
   - Password: `festo_pass_2024`

#### Via CLI

```bash
# Conectar ao PostgreSQL
docker exec -it festo-postgres psql -U festo_user -d festo_sensors

# Listar tabelas
\dt

# Ver dados
SELECT * FROM readings LIMIT 10;
```

---

## 🧪 Testes

### Executar Testes

```bash
mvn test
```

### Testar API com curl

```bash
# Health Check
curl http://localhost:8080/api/health

# Listar sensores
curl http://localhost:8080/api/sensores

# Criar leitura
curl -X POST http://localhost:8080/api/readings \
  -H "Content-Type: application/json" \
  -d '{"sensorId":"1","value":80.5}'

# Gerar dados de teste
curl -X POST "http://localhost:8080/api/readings/generate?count=5"
```

---

## 📊 Sensores Disponíveis

| ID  | Nome                | Tipo      | Unidade |
| --- | ------------------- | --------- | ------- |
| 1   | Reed Switch         | Digital   | ciclos  |
| 2   | Pressão Absoluta    | Analógico | bar     |
| 3   | Pressão Diferencial | Analógico | bar     |
| 4   | Acelerômetro        | Analógico | g       |
| 5   | Temperatura         | Analógico | °C      |
| 6   | Strain Gauge        | Analógico | με      |
| 7   | Contador de Ciclos  | Digital   | ciclos  |
| 8   | Qualidade do Ar     | Analógico | ppm     |

### Status dos Sensores

- **OK** (verde): Valor ≤ 60
- **Aviso** (amarelo): 60 < Valor ≤ 80
- **Alerta** (vermelho): Valor > 80

---

## 🐳 Docker

### Comandos Úteis

```bash
# Iniciar containers
docker-compose up -d

# Parar containers
docker-compose down

# Ver logs
docker-compose logs -f postgres

# Resetar banco de dados
docker-compose down -v
docker-compose up -d
```

---

## 🔧 Configuração Avançada

### Perfis do Spring

```bash
# Desenvolvimento (padrão)
mvn spring-boot:run

# Produção
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Customizar CORS

Edite `application.properties`:

```properties
app.cors.allowed-origins=http://localhost:3000,https://meuapp.com
app.cors.allowed-methods=GET,POST,PUT,DELETE
```

---

## 🐛 Troubleshooting

### Porta 8080 em Uso

```bash
sudo lsof -i :8080
sudo kill -9 [PID]
```

### Problemas com PostgreSQL

```bash
# Verificar logs
docker-compose logs postgres

# Reiniciar container
docker-compose restart postgres

# Recriar containers
docker-compose down
docker-compose up -d
```

### Problemas com Maven

```bash
mvn clean install -U
```

### Erro de Conexão com Banco

- Verifique se o Docker está rodando
- Confirme que o PostgreSQL está acessível: `docker-compose ps`
- Teste conexão: `telnet localhost 5432`

---

## 📦 Build para Produção

```bash
# Gerar JAR
mvn clean package -DskipTests

# Executar JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar

# Com perfil de produção
java -jar -Dspring.profiles.active=prod target/demo-0.0.1-SNAPSHOT.jar
```

---

## 📈 Próximos Passos

- [ ] Implementar cache com Redis
- [ ] Adicionar testes de integração
- [ ] Configurar CI/CD
- [ ] Deploy em Azure App Service
- [ ] Implementar rate limiting
- [ ] Adicionar métricas com Prometheus
- [ ] Implementar WebSocket para dados em tempo real

---

## 📄 Licença

Este projeto é parte de um trabalho acadêmico da FIAP.

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'feat: Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📞 Suporte

Para dúvidas ou problemas, abra uma [issue](https://github.com/[SEU-USUARIO]/[SEU-REPO]/issues).

---

## ✅ Status do Projeto

🟢 **Sprint 4 Concluída!**

### Funcionalidades Implementadas

- ✅ Arquitetura em camadas
- ✅ Persistência em PostgreSQL
- ✅ Autenticação JWT com Microsoft Entra ID
- ✅ Documentação Swagger completa
- ✅ DTOs com validação
- ✅ Tratamento global de exceções
- ✅ Docker Compose
- ✅ Mappers automáticos
- ✅ Logging estruturado
- ✅ CORS configurável

---

**Desenvolvido com ❤️ pela equipe Festo - FIAP 2025**
