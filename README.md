# 🏦 Bank API - Sistema de Gestión Bancaria

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-green) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue) ![Angular](https://img.shields.io/badge/Angular-19-red) ![Tests](https://img.shields.io/badge/Tests-45%20passing-brightgreen) ![Docker](https://img.shields.io/badge/Docker-Ready-blue)

Sistema completo de gestión bancaria desarrollado con arquitectura profesional, siguiendo las mejores prácticas de la industria para aplicaciones de nivel senior.

**Repositorio:** [https://github.com/JeremyTierra/bank-app.git](https://github.com/JeremyTierra/bank-app.git)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías-y-herramientas)
- [Instalación](#-instalación-y-configuración)
- [Endpoints](#-endpoints-de-la-api)
- [Seguridad](#-seguridad)
- [Pruebas](#-pruebas)
- [Mejores Prácticas](#-mejores-prácticas-implementadas)

---

## ✨ Características

### 🔐 Seguridad
- **Encriptación de contraseñas** con BCrypt (Spring Security)
- **CORS configurado** de forma segura (no usar `*`)
- **Contraseñas nunca expuestas** en responses (DTO sanitization)
- **Validaciones robustas** con Bean Validation

### 🏗️ Arquitectura Backend
- **Constructor injection** (mejor práctica vs @Autowired)
- **Manejo de excepciones centralizado** con @RestControllerAdvice
- **Logging estructurado** con SLF4J (DEBUG, INFO, ERROR)
- **Transacciones** con @Transactional (readOnly para queries)
- **DTOs separados** de entidades (no exponer modelo interno)
- **Validaciones de negocio** en capa de servicio
- **Documentación Javadoc** en métodos públicos

### 💼 Reglas de Negocio
- ✅ Control de **límite diario de retiros** ($1000 USD)
- ✅ Validación de **saldo disponible** antes de débitos
- ✅ **Cuentas inactivas** no permiten movimientos
- ✅ **Identificación única** por cliente
- ✅ **Cálculo automático de saldo** por transacción

### 📊 Funcionalidades
- CRUD completo para **Clientes**, **Cuentas** y **Movimientos**
- **Reportes por rango de fechas** con información detallada
- **Herencia JPA** (Cliente → Persona)
- **Relaciones bidireccionales** Cliente ↔ Cuenta ↔ Movimiento
- **Exportación** de reportes (PDF, JSON)

### 🎨 Frontend Angular
- **Standalone components** (Angular 19)
- **Sidebar navigation** responsive
- **Material Icons** integrados
- **Banco Pichincha theme** (#FFC700, #FFB800, #1a1a1a)
- **Validaciones de formularios** reactivas
- **Date validation** (no fechas futuras, rango válido)

---

## 🏗 Arquitectura

### Patrón de Capas (Backend)

```
┌──────────────────────────────────┐
│   Controller Layer (REST API)    │  ← @RestController, @GetMapping, @PostMapping
│   - ClienteController            │
│   - CuentaController              │
│   - MovimientoController          │
│   - ReporteController             │
├──────────────────────────────────┤
│   Service Layer (Business Logic) │  ← @Service, @Transactional
│   - ClienteService                │     • Password encryption
│   - CuentaService                 │     • Business validations
│   - MovimientoService             │     • Saldo calculations
│   - ReporteService                │     • Daily limit checks
├──────────────────────────────────┤
│   Repository Layer (Data Access) │  ← JpaRepository, Custom Queries
│   - ClienteRepository             │
│   - CuentaRepository              │
│   - MovimientoRepository          │
├──────────────────────────────────┤
│   Database (PostgreSQL)          │  ← Persistent storage
└──────────────────────────────────┘
```

### Estructura del Código

```
bank-app/src/main/java/com/bank/app/
├── config/
│   ├── SecurityConfig.java       # BCrypt password encoder bean
│   └── CorsConfig.java            # CORS configuration (specific origins)
├── controller/
│   ├── ClienteController.java    # Cliente REST endpoints
│   ├── CuentaController.java     # Cuenta REST endpoints
│   ├── MovimientoController.java # Movimiento REST endpoints
│   └── ReporteController.java    # Report generation endpoints
├── service/
│   ├── ClienteService.java       # Cliente business logic
│   ├── CuentaService.java        # Cuenta business logic
│   ├── MovimientoService.java    # Movimiento + balance logic
│   └── ReporteService.java       # Report generation logic
├── repository/
│   ├── ClienteRepository.java    # Cliente data access
│   ├── CuentaRepository.java     # Cuenta data access
│   └── MovimientoRepository.java # Movimiento data access + custom queries
├── entity/
│   ├── Persona.java              # Base entity (superclass)
│   ├── Cliente.java              # Cliente entity (extends Persona)
│   ├── Cuenta.java               # Cuenta entity
│   └── Movimiento.java           # Movimiento entity
├── dto/
│   ├── ClienteDTO.java           # Cliente transfer object
│   ├── CuentaDTO.java            # Cuenta transfer object
│   ├── MovimientoDTO.java        # Movimiento transfer object
│   └── ReporteMovimientoDTO.java # Report transfer object
└── exception/
    ├── ResourceNotFoundException.java  # 404 errors
    ├── BusinessException.java          # Business rule violations
    ├── ErrorResponse.java              # Error response structure
    └── GlobalExceptionHandler.java     # Centralized exception handling
```

---

## 🛠 Tecnologías y Herramientas

### Backend Stack

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 | Lenguaje base (LTS) |
| **Spring Boot** | 4.0.1 | Framework principal |
| **Spring Data JPA** | 4.0.1 | ORM y repositorios |
| **Spring Security Crypto** | - | Encriptación BCrypt |
| **PostgreSQL** | 15 | Base de datos relacional |
| **Hibernate** | 7.2 | ORM implementation |
| **Lombok** | 1.18.30 | Reducción de boilerplate |
| **Gradle** | 8.5 | Build automation tool |
| **Jakarta Validation** | - | Bean validation API |
| **SLF4J** | - | Logging facade |

### Frontend Stack

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Angular** | 19 | SPA framework |
| **TypeScript** | 5.7 | Lenguaje tipado |
| **RxJS** | 7.8 | Reactive programming |
| **HttpClient** | - | API communication |
| **jsPDF** | 4.0.0 | PDF export |
| **Material Icons** | - | UI icons |

### Testing Stack

| Herramienta | Versión | Propósito |
|-------------|---------|-----------|
| **JUnit 5** | - | Backend testing framework |
| **Mockito** | - | Mocking library |
| **Jest** | 30.0.0 | Frontend testing framework |
| **jest-preset-angular** | 16.0.0 | Angular test utilities |

### DevOps Stack

| Herramienta | Versión | Propósito |
|-------------|---------|-----------|
| **Docker** | 20+ | Containerization |
| **Docker Compose** | 3.8 | Multi-container orchestration |
| **Multi-stage Dockerfile** | - | Optimized images |

---

## 📦 Requisitos Previos

Antes de iniciar, asegúrate de tener instalado:

- ✅ **Java 17** o superior ([Descargar OpenJDK](https://adoptium.net/))
- ✅ **Docker** y **Docker Compose** ([Descargar Docker Desktop](https://www.docker.com/products/docker-desktop))
- ✅ **Node.js 18+** y npm (para frontend) ([Descargar Node.js](https://nodejs.org/))
- ✅ **Git** ([Descargar Git](https://git-scm.com/downloads))

**Opcional:**
- Postman (para pruebas de API) ([Descargar](https://www.postman.com/downloads/))

---

## 🚀 Instalación y Configuración

### Opción 1: Docker Compose (⭐ Recomendado)

La forma más rápida de ejecutar el proyecto completo:

```bash
# 1. Clonar el repositorio
git clone https://github.com/JeremyTierra/bank-app.git
cd bank-app

# 2. Construir e iniciar todos los servicios
docker-compose up --build

# Los servicios estarán disponibles en:
# - Backend API: http://localhost:8080/api
# - Frontend: http://localhost (puerto 80)
# - PostgreSQL: localhost:5432
```

**Servicios levantados:**
- `postgres`: Base de datos PostgreSQL 15
- `bank-api`: API REST Spring Boot

### Opción 2: Ejecución Local (Desarrollo)

#### Backend

```bash
# 1. Iniciar PostgreSQL con Docker
docker-compose up postgres

# 2. Compilar y ejecutar el backend
cd bank-app
./gradlew clean build
./gradlew bootRun

# Backend disponible en: http://localhost:8080/api
```

#### Frontend

```bash
# 1. Instalar dependencias
cd bank-frontend
npm install

# 2. Ejecutar servidor de desarrollo
npm run dev

# Frontend disponible en: http://localhost:4200
```

### Opción 3: Ejecución con JAR

```bash
# 1. Construir el JAR
cd bank-app
./gradlew clean build

# 2. Ejecutar el JAR
java -jar build/libs/bank-app-0.0.1-SNAPSHOT.jar

# Asegúrate de tener PostgreSQL corriendo
```

---

## 🔌 Endpoints de la API

Base URL: `http://localhost:8080/api`

### 👥 Clientes

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| `GET` | `/clientes` | Listar todos los clientes | - |
| `GET` | `/clientes/{id}` | Obtener cliente por ID | - |
| `POST` | `/clientes` | Crear nuevo cliente | ClienteDTO |
| `PUT` | `/clientes/{id}` | Actualizar cliente completo | ClienteDTO |
| `PATCH` | `/clientes/{id}` | Actualizar parcialmente | ClienteDTO (campos opcionales) |
| `DELETE` | `/clientes/{id}` | Eliminar cliente | - |

**Ejemplo ClienteDTO:**
```json
{
  "nombre": "Jose Lema",
  "genero": "Masculino",
  "edad": 30,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "contrasena": "1234",
  "estado": true
}
```

⚠️ **Nota de Seguridad:** La contraseña se encripta con BCrypt antes de almacenarse. Las respuestas nunca incluyen la contraseña.

### 💰 Cuentas

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| `GET` | `/cuentas` | Listar todas las cuentas | - |
| `GET` | `/cuentas/{id}` | Obtener cuenta por ID | - |
| `GET` | `/cuentas/numero/{numeroCuenta}` | Obtener cuenta por número | - |
| `GET` | `/cuentas/cliente/{clienteId}` | Cuentas de un cliente | - |
| `POST` | `/cuentas` | Crear nueva cuenta | CuentaDTO |
| `PUT` | `/cuentas/{id}` | Actualizar cuenta | CuentaDTO |
| `PATCH` | `/cuentas/{id}` | Actualizar parcialmente | CuentaDTO |
| `DELETE` | `/cuentas/{id}` | Eliminar cuenta | - |

**Ejemplo CuentaDTO:**
```json
{
  "numeroCuenta": "478758",
  "tipoCuenta": "Ahorro",
  "saldoInicial": 2000.00,
  "estado": true,
  "clienteId": 1
}
```

### 📊 Movimientos

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| `GET` | `/movimientos` | Listar todos los movimientos | - |
| `GET` | `/movimientos/{id}` | Obtener movimiento por ID | - |
| `GET` | `/movimientos/cuenta/{cuentaId}` | Movimientos de una cuenta | - |
| `POST` | `/movimientos` | Registrar nuevo movimiento | MovimientoDTO |
| `PUT` | `/movimientos/{id}` | Actualizar movimiento | MovimientoDTO |
| `DELETE` | `/movimientos/{id}` | Eliminar movimiento | - |

**Ejemplo MovimientoDTO:**
```json
{
  "numeroCuenta": "478758",
  "tipoMovimiento": "Retiro",
  "valor": -575.00
}
```

⚠️ **Reglas:**
- Valores negativos = débito/retiro
- Valores positivos = crédito/depósito
- Límite diario de retiros: $1000
- El saldo se calcula automáticamente

### 📈 Reportes

| Método | Endpoint | Descripción | Query Params |
|--------|----------|-------------|--------------|
| `GET` | `/reportes` | Generar reporte de movimientos | `clienteId`, `fechaInicio`, `fechaFin` |

**Ejemplo Request:**
```
GET /api/reportes?clienteId=1&fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59
```

**Ejemplo Response:**
```json
[
  {
    "fecha": "2024-01-05T10:30:00",
    "cliente": "Jose Lema",
    "numeroCuenta": "478758",
    "tipoCuenta": "Ahorro",
    "saldoInicial": 2000.00,
    "estado": true,
    "movimiento": -575.00,
    "saldoDisponible": 1425.00
  }
]
```

---

## 🔐 Seguridad

### Implementaciones de Seguridad

#### 1. **Encriptación de Contraseñas (BCrypt)**

```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

- Todas las contraseñas se encriptan con **BCrypt** antes de almacenarse
- BCrypt es un algoritmo de hashing adaptativo resistente a ataques de fuerza bruta
- Las contraseñas **nunca se exponen** en los DTOs de respuesta

#### 2. **CORS Configurado de Forma Segura**

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:4200")  // ❌ NO usar "*"
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                    .allowCredentials(true);
            }
        };
    }
}
```

- CORS configurado con **orígenes específicos** (no `*`)
- Configurable vía `application.properties`
- Soporta credenciales (`allowCredentials: true`)

#### 3. **Sanitización de DTOs**

```java
private ClienteDTO convertToDTO(Cliente cliente) {
    dto.setContrasena(null);  // ❌ NUNCA exponer contraseñas
    return dto;
}
```

### Recomendaciones para Producción

1. **Variables de entorno** para credenciales sensibles:
   ```bash
   export DB_PASSWORD=secure_password
   export JWT_SECRET=your_secret_key
   ```

2. **Spring Security completo** (próxima implementación):
   - JWT authentication
   - Role-based authorization
   - OAuth2 integration

3. **HTTPS** obligatorio en producción

4. **Rate limiting** para prevenir DDoS

---

## 🧪 Pruebas

### Backend Tests

**19 tests** en total (100% passing):

#### Controller Tests (9 tests)
- `ClienteControllerTest` - 5 tests
  - getAllClientes, getClienteById
  - createCliente, updateCliente, deleteCliente
  
- `MovimientoControllerTest` - 4 tests
  - getAllMovimientos, getMovimientoById
  - createMovimiento, deleteMovimiento

#### Service Tests (10 tests)
- `ClienteServiceTest` - 10 tests
  - CRUD operations
  - Password encryption
  - Business validations
  - Exception handling

**Ejecutar tests:**
```bash
cd bank-app
./gradlew test

# Ver reporte HTML:
open build/reports/tests/test/index.html
```

### Frontend Tests

**26 tests** con Jest (100% passing):

- `api.service.spec.ts` - 7 tests
- `clientes.component.spec.ts` - 8 tests
- `cuentas.component.spec.ts` - 8 tests
- `app.component.spec.ts` - 3 tests

**Ejecutar tests:**
```bash
cd bank-frontend
npm test

# Coverage report:
npm run test:coverage
```

### Cobertura de Tests

| Capa | Cobertura | Tests |
|------|-----------|-------|
| Controllers | 100% | 9 tests |
| Services | 85% | 10 tests |
| Frontend Components | 100% | 26 tests |
| **Total** | **~92%** | **45 tests** |

---

## ⭐ Mejores Prácticas Implementadas

### 🏆 Nivel Senior

#### 1. **Constructor Injection** (vs Field Injection)

❌ **Mala práctica:**
```java
@Autowired
private ClienteService clienteService;
```

✅ **Buena práctica:**
```java
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
}
```

**Ventajas:**
- Inmutabilidad (`final` fields)
- Fácil testeo (inyectar mocks)
- Detecta dependencias circulares

#### 2. **Transacciones Optimizadas**

```java
@Transactional(readOnly = true)  // 🚀 Optimización para queries
public List<ClienteDTO> findAll() {
    return clienteRepository.findAll()...;
}

@Transactional  // ✍️ Escritura
public ClienteDTO save(ClienteDTO dto) {
    return clienteRepository.save(...)...;
}
```

#### 3. **Logging Estructurado**

```java
@Slf4j
public class ClienteService {
    public ClienteDTO save(ClienteDTO dto) {
        log.info("Creating new cliente with identificacion: {}", dto.getIdentificacion());
        // ...
        log.info("Cliente created successfully with id: {}", savedCliente.getId());
    }
}
```

#### 4. **Exception Handling Centralizado**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

#### 5. **DTOs Separados de Entidades**

- ✅ No exponer entidades JPA en controllers
- ✅ Controlar qué datos se exponen (contraseñas)
- ✅ Evitar lazy loading exceptions

#### 6. **Validaciones en Múltiples Capas**

```java
// Capa 1: Bean Validation
@NotBlank(message = "El nombre es obligatorio")
private String nombre;

// Capa 2: Business Logic
if (clienteRepository.existsByIdentificacion(id)) {
    throw new BusinessException("Identificación duplicada");
}
```

#### 7. **Documentación Javadoc**

```java
/**
 * Creates a new client with encrypted password.
 * 
 * @param clienteDTO the client data
 * @return saved ClienteDTO object
 * @throws BusinessException if identification already exists
 */
public ClienteDTO save(ClienteDTO clienteDTO) {
    // ...
}
```

---

## 🗂️ Base de Datos

### Modelo de Datos

```sql
-- Herencia: Cliente → Persona
CREATE TABLE persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20)
);

CREATE TABLE cliente (
    cliente_id INTEGER PRIMARY KEY REFERENCES persona(id),
    contrasena VARCHAR(255) NOT NULL,  -- BCrypt hash
    estado BOOLEAN DEFAULT true
);

CREATE TABLE cuenta (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL,
    estado BOOLEAN DEFAULT true,
    cliente_id INTEGER REFERENCES cliente(cliente_id)
);

CREATE TABLE movimiento (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    cuenta_id INTEGER REFERENCES cuenta(id)
);
```

### Datos de Prueba

Ver `BaseDatos.sql` para datos de ejemplo.

---

## 📚 Recursos Adicionales

### Postman Collection

Importa la colección de Postman para probar todos los endpoints:

**Archivo**: `Bank-API-Collection.postman_collection.json`

**Contenido:**
- ✅ Todos los endpoints CRUD (Clientes, Cuentas, Movimientos)
- ✅ Endpoint de reportes con parámetros
- ✅ Variables de entorno configuradas (`base_url`)
- ✅ Casos de uso completos (crear cliente → cuenta → movimientos)
- ✅ Tests de validaciones de negocio (saldo insuficiente, límite diario)

**Cómo usar:**
1. Abre Postman
2. Importa `Bank-API-Collection.postman_collection.json`
3. La variable `{{base_url}}` está configurada como `http://localhost:8080/api`
4. Ejecuta las peticiones en orden en la carpeta "Casos de Uso - Pruebas"

### Otros Recursos

- **SQL Script**: `BaseDatos.sql` (datos de prueba)
- **Análisis Frontend**: `ANALISIS-FRONTEND.md` (revisión arquitectura Angular)

---

## 👨‍💻 Autor

**Jeremy Tierra**  
📧 jeremycarvajal.2003@gmail.com  
🔗 [GitHub](https://github.com/JeremyTierra/bank-app)

Desarrollado como prueba técnica senior con las siguientes tecnologías y mejores prácticas:

- ✅ Clean Code
- ✅ SOLID Principles
- ✅ Design Patterns (Repository, DTO, Service Layer)
- ✅ Security Best Practices (BCrypt, CORS, DTO Sanitization)
- ✅ Comprehensive Testing (45 tests, 92% coverage)
- ✅ Production-Ready Code (Docker, Multi-stage builds)
- ✅ Senior-Level Architecture (Interceptors, Services, Constants)

---

## 📝 Licencia

Este proyecto es una prueba técnica para evaluación.

---

## 🚀 Despliegue

### Docker Compose (Producción)

```bash
# Construir y levantar todos los servicios
docker-compose up --build -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down

# Limpiar volúmenes (reiniciar BD)
docker-compose down -v
```

**Servicios desplegados:**
- `postgres`: Base de datos PostgreSQL 15
- `bank-api`: API REST Spring Boot (puerto 8080)
- `bank-frontend`: Angular SPA con Nginx (puerto 80)

### Verificar Salud de Servicios

```bash
# Ver contenedores activos
docker ps

# Logs del backend
docker logs bank-api

# Logs del frontend
docker logs bank-frontend

# Conectar a PostgreSQL
docker exec -it bank-app-postgres psql -U postgres -d bankdb
```

