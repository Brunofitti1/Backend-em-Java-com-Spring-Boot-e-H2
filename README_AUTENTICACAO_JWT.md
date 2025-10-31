# 🔐 Autenticação JWT - Guia Rápido

## 📋 Como Funciona

Esta é uma **autenticação simplificada** para desenvolvimento acadêmico:

- ✅ **Qualquer pessoa pode fazer login**
- ✅ Basta fornecer um **email válido** + **qualquer senha**
- ✅ O sistema gera um **token JWT** válido por 24 horas
- ✅ Use o token para acessar os endpoints protegidos

**Não há cadastro de usuários nem validação de senha real.** É perfeito para desenvolvimento e demonstrações.

---

## 🚀 Como Usar

### 1️⃣ **Fazer Login** (Endpoint Público)

**Endpoint:** `POST /api/auth/login`

**Request Body:**

```json
{
  "email": "seu.email@exemplo.com",
  "password": "qualquer_senha_aqui"
}
```

**Exemplo com cURL:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "bruno@fiap.com",
    "password": "12345"
  }'
```

**Response (Sucesso - 200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImJydW5vQGZpYXAuY29tIiwic3ViIjoiYnJ1bm9AZmlhcC5jb20iLCJpYXQiOjE3MzAzNzYwMDAsImV4cCI6MTczMDQ2MjQwMH0.abc123...",
  "type": "Bearer",
  "email": "bruno@fiap.com",
  "message": "Login realizado com sucesso!"
}
```

**Response (Erro - 400 Bad Request):**

```json
{
  "timestamp": "2024-10-31T10:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email deve ser válido",
  "path": "/api/auth/login"
}
```

---

### 2️⃣ **Usar o Token nos Endpoints Protegidos**

Depois de fazer login, **guarde o token** e envie-o no header `Authorization` de todas as requisições para endpoints protegidos.

**Formato do Header:**

```
Authorization: Bearer SEU_TOKEN_AQUI
```

**Exemplo - Listar Sensores (Protegido):**

```bash
curl http://localhost:8080/api/sensores \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImJydW5vQGZpYXAuY29tIiwic3ViIjoiYnJ1bm9AZmlhcC5jb20iLCJpYXQiOjE3MzAzNzYwMDAsImV4cCI6MTczMDQ2MjQwMH0.abc123..."
```

**Exemplo - Criar Leitura (Protegido):**

```bash
curl -X POST http://localhost:8080/api/readings \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sensorId": "temp-001",
    "value": 23.5,
    "unit": "°C"
  }'
```

---

### 3️⃣ **Validar Token** (Opcional)

**Endpoint:** `GET /api/auth/validate`

Verifica se o token ainda é válido.

**Exemplo:**

```bash
curl http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer SEU_TOKEN"
```

**Response (Token Válido - 200 OK):**

```
Token válido para: bruno@fiap.com
```

**Response (Token Inválido - 401 Unauthorized):**

```
Token inválido ou expirado
```

---

## 🔓 Endpoints Públicos (Não Precisam de Token)

Estes endpoints podem ser acessados **sem autenticação**:

- `GET /` - Página inicial
- `GET /api/health` - Status da API
- `POST /api/auth/login` - Login
- `GET /api/auth/validate` - Validar token
- `GET /swagger-ui.html` - Documentação Swagger
- `GET /api-docs` - OpenAPI JSON

---

## 🔒 Endpoints Protegidos (Precisam de Token)

Todos os endpoints `/api/**` **exceto** `/api/auth/**` requerem autenticação:

- `GET /api/sensores` - Listar sensores
- `GET /api/readings` - Listar leituras
- `GET /api/readings/{id}` - Buscar leitura por ID
- `POST /api/readings` - Criar leitura
- `DELETE /api/readings/{id}` - Deletar leitura
- `POST /api/readings/generate` - Gerar dados de teste

---

## 📱 Integração com Frontend (React Native)

### Passo 1: Login e Armazenar Token

```typescript
// LoginScreen.tsx
import * as SecureStore from "expo-secure-store";

const handleLogin = async (email: string, password: string) => {
  try {
    const response = await fetch("http://SEU_IP:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });

    const data = await response.json();

    if (response.ok) {
      // Salva o token de forma segura
      await SecureStore.setItemAsync("authToken", data.token);
      await SecureStore.setItemAsync("userEmail", data.email);

      // Navega para o Dashboard
      navigation.navigate("Dashboard");
    } else {
      alert("Erro ao fazer login");
    }
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro de conexão");
  }
};
```

### Passo 2: Enviar Token nas Requisições

```typescript
// ApiService.ts
import * as SecureStore from "expo-secure-store";
import axios from "axios";

const api = axios.create({
  baseURL: "http://SEU_IP:8080/api",
  timeout: 10000,
});

// Interceptor para adicionar o token automaticamente
api.interceptors.request.use(async (config) => {
  const token = await SecureStore.getItemAsync("authToken");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// Interceptor para tratar token expirado
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      // Token expirado, redireciona para login
      await SecureStore.deleteItemAsync("authToken");
      await SecureStore.deleteItemAsync("userEmail");
      // navigation.navigate('Login'); // Implementar navegação
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Passo 3: Usar a API

```typescript
// Exemplo de uso no Dashboard
import api from "../services/ApiService";

const fetchSensors = async () => {
  try {
    const response = await api.get("/sensores");
    setSensors(response.data);
  } catch (error) {
    console.error("Erro ao buscar sensores:", error);
  }
};
```

---

## ⚙️ Configuração (application.properties)

```properties
# JWT Configuration
jwt.secret=festo-sensors-jwt-secret-key-super-secreta-para-desenvolvimento-2024-fiap
jwt.expiration=86400000  # 24 horas em millisegundos
```

**⚠️ IMPORTANTE:**

- Em **produção**, use uma chave secreta forte e armazene em variável de ambiente
- Exemplo: `jwt.secret=${JWT_SECRET:default-secret-key}`

---

## 🧪 Testando no Swagger UI

1. Acesse: `http://localhost:8080/swagger-ui.html`
2. Vá até **Autenticação → POST /api/auth/login**
3. Clique em "Try it out"
4. Preencha o body:
   ```json
   {
     "email": "teste@fiap.com",
     "password": "123"
   }
   ```
5. Execute e copie o `token` da resposta
6. Clique no botão **"Authorize"** no topo da página
7. Cole o token no formato: `Bearer SEU_TOKEN_AQUI`
8. Agora você pode testar todos os endpoints protegidos!

---

## ❓ FAQ

**Q: Preciso cadastrar usuários?**  
A: Não. Qualquer email válido funciona.

**Q: A senha é validada?**  
A: Não. Qualquer senha é aceita. Isso é intencional para simplificar o desenvolvimento.

**Q: Por quanto tempo o token é válido?**  
A: 24 horas por padrão (configurável em `jwt.expiration`).

**Q: O que acontece se o token expirar?**  
A: Você receberá um erro 401 (Unauthorized) e precisará fazer login novamente.

**Q: É seguro para produção?**  
A: **NÃO.** Esta é uma implementação simplificada para fins acadêmicos. Para produção, você deve:

- Adicionar banco de dados de usuários
- Validar senhas com bcrypt
- Usar HTTPS
- Armazenar a chave JWT em variável de ambiente
- Implementar refresh tokens
- Adicionar rate limiting

---

## 📚 Próximos Passos

Para tornar a autenticação mais robusta:

1. **Adicionar Entity User** com campos: id, email, passwordHash, createdAt
2. **Usar BCrypt** para hash de senhas
3. **Criar endpoint de registro** (`POST /api/auth/register`)
4. **Implementar Refresh Tokens** para renovar tokens expirados
5. **Adicionar Roles/Permissions** para controle de acesso

---

## 👥 Integrantes do Projeto

- **Bruno Morade** - RM XXXXX
- _(Adicione os outros membros)_

---

## 📄 Licença

Projeto acadêmico - FIAP 2024
