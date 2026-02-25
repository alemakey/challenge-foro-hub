# 🗣️ Foro Hub – API REST con Spring Boot

Challenge de Alura Latam (ONE) – Back-end con Spring Framework.

## 📋 Descripción

API REST para gestionar un foro de discusión. Permite crear, listar, actualizar y eliminar tópicos, con autenticación JWT y autorización de endpoints.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3.2.3
- Spring Security + JWT (Auth0)
- Spring Data JPA + H2 (in-memory)
- Bean Validation (Jakarta)
- Maven

## ▶️ Cómo ejecutar

### Requisitos
- Java 17+
- Maven (o usar el `mvnw` incluido)

### Variables de entorno
```bash
# Secreto para firmar tokens JWT (mínimo 32 caracteres recomendado)
JWT_SECRET=mi-secreto-super-seguro-de-32-chars
```

### Ejecutar en desarrollo
```bash
cd foro-hub
./mvnw spring-boot:run
```
La API quedará disponible en `http://localhost:8080`

---

## 🔐 Autenticación

Todos los endpoints (excepto `/login`) requieren un token JWT en el header:

```
Authorization: Bearer <token>
```

### Obtener token
```http
POST /login
Content-Type: application/json

{
  "login": "usuario@email.com",
  "clave": "contraseña"
}
```
**Respuesta:**
```json
{ "token": "eyJhbGciOiJI..." }
```

---

## 📡 Endpoints

| Método | Ruta | Descripción | Auth requerida |
|--------|------|-------------|----------------|
| `POST` | `/login` | Autenticar usuario y obtener JWT | ❌ |
| `GET` | `/topicos` | Listar tópicos activos (paginado) | ✅ |
| `GET` | `/topicos/{id}` | Detalle de un tópico | ✅ |
| `POST` | `/topicos` | Crear nuevo tópico | ✅ |
| `PUT` | `/topicos/{id}` | Actualizar tópico | ✅ |
| `DELETE` | `/topicos/{id}` | Desactivar tópico (soft delete) | ✅ |

### Crear tópico
```http
POST /topicos
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "Duda sobre Streams en Java",
  "mensaje": "¿Cómo funciona el método flatMap?",
  "curso": "Java con Spring"
}
```

### Paginación
```
GET /topicos?page=0&size=10&sort=fechaCreacion,asc
```

---

## 🗂️ Estructura del proyecto

```
src/main/java/com/foro/foro_hub/
├── controller/
│   ├── AuthController.java        # Login y generación de JWT
│   └── TopicoController.java      # CRUD de tópicos
├── domain/
│   ├── topico/
│   │   ├── Topico.java            # Entidad JPA
│   │   ├── TopicoRepository.java  # Repositorio + consultas custom
│   │   ├── DatosRegistroTopico.java
│   │   ├── DatosActualizarTopico.java
│   │   ├── DatosRespuestaTopico.java
│   │   └── DatosListadoTopico.java
│   └── usuario/
│       ├── Usuario.java           # Entidad + UserDetails
│       ├── UsuarioRepository.java
│       └── DatosAutenticacion.java
└── infra/
    ├── errores/
    │   ├── TratadorDeErrores.java # Handler global de excepciones
    │   └── ValidacionException.java
    └── security/
        ├── SecurityConfig.java
        ├── SecurityFilter.java
        ├── TokenService.java
        └── DatosTokenJWT.java
```
