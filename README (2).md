
# Backend - Sensor Readings API (Spring Boot + H2)

## 📌 Descrição do Projeto

Este é o backend da **Entrega 2** do projeto Digital Twin. O sistema é uma API REST desenvolvida em Java com Spring Boot e banco de dados **H2 em modo file**, que permite criar, listar e buscar leituras de sensores.

---

## 🚀 Como Rodar o Projeto Localmente

### Pré-requisitos:

- Java 17
- Maven
- Postman ou outro cliente REST (opcional, para testes)

### Passos:

1. **Clone o projeto:**

```bash
git clone https://github.com/SEU_USUARIO/Backend-Entrega2.git
cd Backend-Entrega2
```

2. **Compile e execute:**

```bash
./mvnw spring-boot:run
```
> Ou no Windows:
```bash
mvnw spring-boot:run
```

3. **Banco de dados H2:**

- O arquivo do banco será criado em:

```
./data/readings.mv.db
```

- Para abrir o console web do H2:

Acesse:  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)  

**JDBC URL:**  
```
jdbc:h2:file:./data/readings
```

---

## 🛠️ Endpoints Disponíveis

| Método | Endpoint | Função |
|-------|---------|-----|
| GET | `/api/readings` | Lista todas as leituras |
| GET | `/api/readings/{sensorId}` | Filtra leituras por sensor |
| POST | `/api/readings` | Salva nova leitura |

---

## 📋 Exemplo de Requisição POST (com curl)

```bash
curl -X POST http://localhost:8080/api/readings -H "Content-Type: application/json" -d '{
  "sensorId": "sensor1",
  "value": 55.5,
  "timestamp": "2025-06-17T23:30:00"
}'
```

---

## 🌍 Integração com o Frontend

- Ajuste a URL da API no seu app mobile para:

```
http://SEU_IP_LOCAL:8080/api
```
Exemplo:

```
http://192.168.15.17:8080/api
```

- O app frontend usará:

- GET `/api/readings`  
- POST `/api/readings`  

---

## 📑 Estrutura de Diretórios Importante:

```
src/main/java/com/example/demo/
├── DemoApplication.java
├── Reading.java
├── ReadingController.java
└── ReadingRepository.java

src/main/resources/
└── application.properties
```

---

## 👨‍💻 Integrantes do Grupo

- Pedro Lopes Domingues – RM: 99628  
- Mateus Fairbanks – RM: 98202  
- Felipe Pereira de Assis – RM: 98187  
- Enzo Shoji Teixeira Konishi – RM: 99828  
- Bruno Miziara Fittipaldi Morade – RM: 99911  

---
