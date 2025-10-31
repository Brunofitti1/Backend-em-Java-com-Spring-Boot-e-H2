# ✅ Autenticação JWT Implementada - Teste Rápido

## 🎯 O Que Foi Feito

Implementamos uma **autenticação JWT super simples** onde:

✅ **Qualquer pessoa pode fazer login** usando:

- Email válido (ex: `bruno@fiap.com`)
- Qualquer senha (ex: `123`, `abc`, qualquer coisa)

✅ **Sistema gera token JWT** válido por 24 horas

✅ **Endpoints protegidos** requerem o token no header

---

## 🚀 Como Testar (Quando o Servidor Estiver Rodando)

### 1️⃣ Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "bruno@fiap.com",
    "password": "123"
  }'
```

**Resposta esperada:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "email": "bruno@fiap.com",
  "message": "Login realizado com sucesso!"
}
```

### 2️⃣ Testar Endpoint Protegido SEM Token (Deve Dar Erro)

```bash
curl http://localhost:8080/api/sensores
```

**Resposta esperada:** `401 Unauthorized` (sem permissão)

### 3️⃣ Testar Endpoint Protegido COM Token (Deve Funcionar)

```bash
curl http://localhost:8080/api/sensores \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta esperada:** Lista de sensores (200 OK)

---

## 📝 Endpoints Criados

### Público (Não Precisa Token)

- `POST /api/auth/login` - Fazer login
- `GET /api/auth/validate` - Validar token

### Protegidos (Precisa Token)

- `GET /api/sensores`
- `GET /api/readings`
- `POST /api/readings`
- `DELETE /api/readings/{id}`
- etc.

---

## ⚠️ Problema Atual

O Maven não conseguiu baixar as dependências JWT devido ao proxy:

```
io.jsonwebtoken:jjwt-api:jar:0.11.5
io.jsonwebtoken:jjwt-impl:jar:0.11.5
io.jsonwebtoken:jjwt-jackson:jar:0.11.5
```

### Soluções:

**Opção 1: Conectar em rede sem proxy**

```bash
mvn clean install
mvn spring-boot:run
```

**Opção 2: Configurar proxy do Maven**

Edite `~/.m2/settings.xml`:

```xml
<settings>
  <proxies>
    <proxy>
      <id>corporate-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>19.12.80.237</host>
      <port>83</port>
    </proxy>
  </proxies>
</settings>
```

**Opção 3: Baixar JARs manualmente**

Download dos JARs:

- https://repo1.maven.org/maven2/io/jsonwebtoken/jjwt-api/0.11.5/jjwt-api-0.11.5.jar
- https://repo1.maven.org/maven2/io/jsonwebtoken/jjwt-impl/0.11.5/jjwt-impl-0.11.5.jar
- https://repo1.maven.org/maven2/io/jsonwebtoken/jjwt-jackson/0.11.5/jjwt-jackson-0.11.5.jar

---

## 🧪 Testar no Swagger UI

Quando o servidor rodar:

1. Acesse: `http://localhost:8080/swagger-ui.html`
2. Vá em **Autenticação → POST /api/auth/login**
3. Teste com:
   ```json
   {
     "email": "teste@exemplo.com",
     "password": "qualquer_coisa"
   }
   ```
4. Copie o `token` da resposta
5. Clique no botão **"Authorize"** (cadeado no topo)
6. Cole: `Bearer SEU_TOKEN`
7. Agora teste os outros endpoints protegidos!

---

## 📱 Integrar com o Frontend

No `ApiService.ts` do React Native:

```typescript
// Interceptor para adicionar token automaticamente
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
      // Token expirado, volta pro login
      await SecureStore.deleteItemAsync("authToken");
      navigation.navigate("Login");
    }
    return Promise.reject(error);
  }
);
```

---

## ✅ Checklist de Implementação

- [x] Adicionar dependências JWT no pom.xml
- [x] Criar DTOs (LoginRequest, AuthResponse)
- [x] Criar JwtUtil (gerar e validar tokens)
- [x] Criar AuthService (lógica de login)
- [x] Criar AuthController (endpoint /api/auth/login)
- [x] Criar JwtAuthenticationFilter (interceptar requisições)
- [x] Atualizar SecurityConfig (proteger endpoints)
- [x] Configurar application.properties (jwt.secret)
- [x] Criar documentação (README_AUTENTICACAO_JWT.md)
- [x] Commit das mudanças
- [ ] Baixar dependências Maven (pendente - proxy)
- [ ] Testar login no Swagger UI
- [ ] Testar endpoints protegidos
- [ ] Integrar com frontend React Native

---

## 🎓 Resumo para Apresentação

**"Implementamos autenticação JWT simplificada onde qualquer pessoa pode fazer login usando apenas um email válido e qualquer senha. O sistema gera um token que é usado para acessar os endpoints protegidos da API. Esta abordagem simplificada é ideal para desenvolvimento e demonstrações acadêmicas."**

---

## 📚 Arquivos Criados

```
Backend-em-Java-com-Spring-Boot-e-H2/
├── pom.xml (atualizado com JWT)
├── README_AUTENTICACAO_JWT.md
├── TESTE_AUTENTICACAO.md (este arquivo)
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java (atualizado)
│   │   │   └── JwtAuthenticationFilter.java (novo)
│   │   ├── controller/
│   │   │   └── AuthController.java (novo)
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   └── LoginRequest.java (novo)
│   │   │   └── response/
│   │   │       └── AuthResponse.java (novo)
│   │   ├── service/
│   │   │   └── AuthService.java (novo)
│   │   └── tools/
│   │       └── JwtUtil.java (novo)
│   └── resources/
│       └── application.properties (atualizado com jwt.*)
```

---

## 💡 Dica Final

Quando conseguir rodar o servidor, faça este teste completo:

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"bruno@fiap.com","password":"123"}' \
  | jq -r '.token')

echo "Token obtido: $TOKEN"

# 2. Usar token
curl http://localhost:8080/api/sensores \
  -H "Authorization: Bearer $TOKEN"
```

Se funcionar, a autenticação está perfeita! 🎉
