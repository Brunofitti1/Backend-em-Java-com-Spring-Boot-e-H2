# 🚀 Backend - Sistema de Monitoramento de Sensores Festo

Backend desenvolvido em **Spring Boot** com **PostgreSQL** para monitoramento de sensores industriais.

---

## 📋 Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **Docker** e **Docker Compose**
- **Git**

---

## 🔧 Como Rodar

### 1. Clone o Repositório

```bash
git clone https://github.com/Brunofitti1/Backend-em-Java-com-Spring-Boot-e-H2.git
cd Backend-em-Java-com-Spring-Boot-e-H2
```

### 2. Inicie o Banco de Dados (PostgreSQL)

```bash
docker-compose up -d
```

Isso irá iniciar:

- **PostgreSQL** na porta `5432`
- **PgAdmin** na porta `5050` (http://localhost:5050)
  - Email: `admin@festo.com`
  - Senha: `admin123`

**Verificar se está rodando:**

```bash
docker ps
```

### 3. Execute a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

---

## 🔐 Autenticação

### Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@festo.com",
    "password": "admin123"
  }'
```

**Resposta:**

```json
{
  "token": "eyJhbGci...",
  "type": "Bearer",
  "email": "admin@festo.com",
  "message": "Login realizado com sucesso!"
}
```

### Usar Token em Requisições

```bash
TOKEN="seu-token-aqui"

curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/readings
```

---

## 📚 Endpoints da API

### Autenticação

| Método | Endpoint             | Descrição                     |
| ------ | -------------------- | ----------------------------- |
| POST   | `/api/auth/login`    | Fazer login e obter token JWT |
| GET    | `/api/auth/validate` | Validar token JWT             |

### Sensores

| Método | Endpoint        | Descrição                |
| ------ | --------------- | ------------------------ |
| GET    | `/api/sensores` | Listar todos os sensores |

### Leituras (Readings)

| Método | Endpoint                          | Descrição                  |
| ------ | --------------------------------- | -------------------------- |
| GET    | `/api/readings`                   | Listar todas as leituras   |
| GET    | `/api/readings/{sensorId}`        | Buscar leituras por sensor |
| GET    | `/api/readings/id/{id}`           | Buscar leitura por ID      |
| POST   | `/api/readings`                   | Criar nova leitura         |
| POST   | `/api/readings/generate?count=10` | Gerar dados de teste       |
| DELETE | `/api/readings/{id}`              | Deletar leitura            |

---

## 🧪 Testar a API

### 1. Health Check

```bash
curl http://localhost:8080/actuator/health
```

### 2. Login e Obter Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@festo.com","password":"admin123"}'
```

### 3. Listar Leituras (com autenticação)

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@festo.com","password":"admin123"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/readings
```

### 4. Criar Nova Leitura

```bash
curl -X POST http://localhost:8080/api/readings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sensorId": "1",
    "value": 75.5,
    "timestamp": "2025-10-31T10:00:00"
  }'
```

---

## 🗄️ Banco de Dados

### Credenciais PostgreSQL

- **Host**: localhost
- **Porta**: 5432
- **Database**: festo_sensors
- **Usuário**: festo_user
- **Senha**: festo_pass_2024

### Conectar via CLI

```bash
docker exec -it festo-postgres psql -U festo_user -d festo_sensors
```

### Comandos SQL Úteis

```sql
-- Ver tabelas
\dt

-- Ver leituras
SELECT * FROM readings LIMIT 10;

-- Contar leituras por sensor
SELECT sensor_id, COUNT(*) as total
FROM readings
GROUP BY sensor_id;
```

---

## 🔧 Configuração

### Arquivo: `application.properties`

```properties
# Servidor
server.port=8080

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/festo_sensors
spring.datasource.username=festo_user
spring.datasource.password=festo_pass_2024

# JWT
jwt.secret=festo-sensors-jwt-secret-key-super-secreta-para-desenvolvimento-2024-fiap
jwt.expiration=86400000  # 24 horas

# CORS
app.cors.allowed-origins=http://localhost:19006,http://localhost:8081,http://192.168.*.*:19006
```

---

## 🐳 Comandos Docker

### Iniciar containers

```bash
docker-compose up -d
```

### Parar containers

```bash
docker-compose down
```

### Ver logs

```bash
docker-compose logs -f postgres
```

### Resetar banco de dados

```bash
docker-compose down -v
docker-compose up -d
```

---

## 🛠️ Desenvolvimento

### Compilar

```bash
mvn clean install
```

### Executar testes

```bash
mvn test
```

### Gerar JAR

```bash
mvn clean package -DskipTests
```

### Executar JAR

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## 🐛 Troubleshooting

### Porta 8080 já está em uso

```bash
# Descobrir processo
sudo lsof -i :8080

# Matar processo
sudo kill -9 [PID]
```

### PostgreSQL não está respondendo

```bash
# Verificar status
docker ps

# Reiniciar container
docker-compose restart postgres

# Ver logs
docker-compose logs postgres
```

### Erro de conexão com banco

1. Verifique se Docker está rodando: `docker ps`
2. Verifique se PostgreSQL está saudável: `docker ps | grep healthy`
3. Teste conexão: `telnet localhost 5432`

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

**Status dos Sensores:**

- 🟢 **OK**: Valor ≤ 60
- 🟡 **Aviso**: 60 < Valor ≤ 80
- 🔴 **Alerta**: Valor > 80

---

## 📁 Estrutura do Projeto

```
src/main/java/com/example/demo/
├── config/              # Configurações (Security, CORS, OpenAPI)
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects
│   ├── request/
│   └── response/
├── entity/              # Entidades JPA
├── exception/           # Exceções e handlers
├── mapper/              # Mappers (Entity ↔ DTO)
├── repository/          # Repositories JPA
├── service/             # Lógica de negócio
└── tools/               # Classes utilitárias
```

---

## 🚀 Deploy

### Variáveis de Ambiente (Produção)

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://seu-host:5432/festo_sensors
export SPRING_DATASOURCE_USERNAME=seu_usuario
export SPRING_DATASOURCE_PASSWORD=sua_senha
export JWT_SECRET=sua_chave_secreta_forte
```

### Executar em Produção

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

---

## 📄 Licença

Projeto acadêmico - FIAP 2025

---

## 👥 Equipe

**FIAP - Análise e Desenvolvimento de Sistemas**

---

## 📞 Suporte

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **GitHub Issues**: https://github.com/Brunofitti1/Backend-em-Java-com-Spring-Boot-e-H2/issues
