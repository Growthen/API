# Base API REST con Seguridad Incorporada

> **Boilerplate de API Reusable:** Proyecto base equipado con arquitectura y módulos de seguridad listos para usar, pensado para bootstrapear nuevos servicios sin duplicar código ni rehacer la seguridad en cada proyecto.

Plantilla base para desarrollo de servicios web RESTful en Java utilizando **Spring Boot**, **Spring Security**, **JWT (JSON Web Tokens)** con tokens de acceso y de refresco (Refresh Tokens), **Spring Data JPA**, y documentación con **OpenAPI / Swagger**.

---

## 🛠️ Tecnologías y Herramientas

* **Java 21** - Lenguaje de programación.
* **Spring Boot 3 / 4.x** - Framework principal para desarrollo backend.
* **Spring Security** - Autenticación, autorización y control de acceso basado en roles (`USER`, `ADMIN`).
* **JWT (io.jsonwebtoken)** - Autenticación Stateless con Access Tokens y Refresh Tokens.
* **Spring Data JPA & Hibernate** - ORM y gestión de persistencia de datos.
* **PostgreSQL / H2 Database** - Base de datos relacional (producción y desarrollo/pruebas).
* **MapStruct** - Mapeo eficiente de Objetos (DTOs ↔ Entities).
* **Lombok** - Reducción de código boilerplate.
* **Springdoc OpenAPI (Swagger UI)** - Documentación interactiva de la API.
* **Gradle** - Gestor de dependencias y automatización de construcciones.

---

## 📁 Estructura del Proyecto

```text
api/
├── src/
│   ├── main/
│   │   ├── java/dev/growthen/api/
│   │   │   ├── ApiApplication.java             # Clase principal con anotación @SpringBootApplication
│   │   │   ├── auth/                           # Módulo de Autenticación
│   │   │   │   ├── controller/                 # AuthController (register, login, refresh, logout)
│   │   │   │   ├── dto/                        # DTOs de petición y respuesta (Login, Register, AuthResponse)
│   │   │   │   ├── entity/                     # Entidad RefreshToken
│   │   │   │   ├── repository/                 # RefreshTokenRepository
│   │   │   │   └── service/                    # AuthService
│   │   │   ├── common/                         # Componentes comunes y reutilizables
│   │   │   │   ├── constants/                  # ErrorMessages y SecurityConstants
│   │   │   │   ├── controller/                 # TestController
│   │   │   │   ├── entity/                     # BaseEntity (createdAt, updatedAt)
│   │   │   │   ├── exception/                  # Excepciones personalizadas y GlobalExceptionHandler
│   │   │   │   └── response/                   # Wrappers estandarizados (ApiResponse, ErrorResponse)
│   │   │   ├── config/                         # Configuraciones del sistema
│   │   │   │   ├── custom/                     # CustomUserDetailsService
│   │   │   │   ├── jpa/                        # JpaConfig (Auditoría JPA)
│   │   │   │   ├── jwt/                        # JwtService y JwtAuthenticationFilter
│   │   │   │   ├── security/                   # SecurityConfig (Filter Chain, PasswordEncoder, CORS)
│   │   │   │   ├── CorsConfig.java             # Configuración dinámica de CORS
│   │   │   │   └── OpenApiConfig.java          # Configuración de Swagger / OpenAPI
│   │   │   └── user/                           # Módulo de Usuarios
│   │   │       ├── dto/                        # UserResponse DTO
│   │   │       ├── entity/                     # Entidad User
│   │   │       ├── enums/                      # Enum Role (USER, ADMIN)
│   │   │       ├── mapper/                     # UserMapper (Mapstruct)
│   │   │       └── repository/                 # UserRepository
│   │   └── resources/
│   │       └── application.yaml                # Configuración de Spring Boot y lectura de variables de entorno
├── .env.example                                # Plantilla de variables de entorno
├── build.gradle                                # Script de construcción de Gradle
├── settings.gradle                             # Configuración del proyecto Gradle
├── LICENSE                                     # Licencia MIT
└── README.md                                   # Documentación del proyecto
```

---

## ⚙️ Configuración del Entorno (.env)

El proyecto utiliza variables de entorno cargadas automáticamente a través de `application.yaml`.

Crea un archivo `.env` en la raíz del proyecto basándote en la plantilla `.env.example`:

```bash
cp .env.example .env
```

### Contenido de `.env.example`:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
JPA_HIBERNATE_DDL_AUTO=update
JWT_SECRET=
JWT_REFRESH_SECRET=
APP_DOCS_PUBLIC_ENABLED=true
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:5500
CORS_ALLOWED_METHODS=GET,POST,PUT,PATCH,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*
CORS_EXPOSED_HEADERS=Authorization
CORS_ALLOW_CREDENTIALS=true
CORS_MAX_AGE=3600
```

---

## 🔒 Seguridad y Autenticación

1. **BCrypt Password Encoder**: Cifrado seguro de contraseñas de usuario.
2. **Access Tokens (JWT)**: Tokens firmados con vencimiento configurado para autorizar peticiones protegidas HTTP via el encabezado `Authorization: Bearer <TOKEN>`.
3. **Refresh Tokens**: Persistidos en la base de datos para solicitar un nuevo Access Token sin obligar al usuario a iniciar sesión nuevamente.
4. **Filtro de Autenticación Custom**: `JwtAuthenticationFilter` intercepta cada petición y valida la autenticidad del token.
5. **Manejo Centralizado de Excepciones**: Respuestas JSON estandarizadas para códigos HTTP `401 Unauthorized` y `403 Forbidden`.

---

## 🚀 Endpoints Principales

### Autenticación (`/api/auth`)

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Registro de un nuevo usuario | Público |
| `POST` | `/api/auth/login` | Inicio de sesión y generación de tokens | Público |
| `POST` | `/api/auth/refresh` | Renovación del Access Token con Refresh Token | Público |
| `POST` | `/api/auth/logout` | Cierre de sesión y revocación del Refresh Token | Requiere Autenticación |

### Prueba y Diagnóstico (`/api/test`)

| Método | Endpoint | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/test` | Endpoint de verificación de estado | Público |

---

## 📚 Documentación de la API (Swagger UI)

Una vez ejecutada la aplicación, puedes acceder a la interfaz interactiva de Swagger UI en:

```text
http://localhost:8080/swagger-ui.html
```

---

## 📦 Ejecución del Proyecto

### Requisitos Previos
* JDK 21 o superior instalado y configurado en el `PATH`.
* Instancia de PostgreSQL (o configurar H2 en `.env`).

### Comandos de Ejecución

```bash
# Compilar el proyecto
./gradlew build

# Ejecutar la aplicación en modo desarrollo
./gradlew bootRun

# Ejecutar pruebas unitarias
./gradlew test
```

---

## 🔄 Cómo Personalizar el Proyecto (Grupo, Paquete e Identidad)

Si deseas utilizar esta plantilla para un proyecto propio y adaptar la identidad del paquete y grupo (por ejemplo, cambiar de `dev.growthen.api` a `com.tudominio.tunuevoproyecto` o `com.tuusuario.miapi`), sigue estos sencillos pasos:

### 1. Configuración de Gradle y Spring Boot
* **`build.gradle`**: Cambia el grupo y la descripción del proyecto:
  ```groovy
  group = 'com.tudominio'                 # En lugar de 'dev.growthen'
  description = 'tu-nuevo-proyecto'      # En lugar de 'api'
  ```
* **`settings.gradle`**: Cambia el nombre del proyecto raíz:
  ```groovy
  rootProject.name = 'tu-nuevo-proyecto' # En lugar de 'api'
  ```
* **`src/main/resources/application.yaml`**: Actualiza el nombre de la aplicación Spring:
  ```yaml
  spring:
    application:
      name: tu-nuevo-proyecto
  ```

### 2. Estructura de Directorios y Paquetes Java
* **Directorios de código fuente y tests**:
  - Mueve la estructura de carpetas `src/main/java/dev/growthen/api` a `src/main/java/com/tudominio/<tu-nuevo-proyecto>`.
  - Mueve la estructura de carpetas `src/test/java/dev/growthen/api` a `src/test/java/com/tudominio/<tu-nuevo-proyecto>`.
* **Clase Principal y de Tests**:
  - Renombra `ApiApplication.java` a `<TuNuevoProyecto>Application.java` y actualiza la definición de la clase.
  - Renombra `ApiApplicationTests.java` a `<TuNuevoProyecto>ApplicationTests.java`.
* **Reemplazo Global de Paquetes (`package` e `import`)**:
  - Reemplaza en todos los archivos `.java` la raíz del paquete `dev.growthen.api` por tu nuevo paquete:
    - Cambia `package dev.growthen.api...` por `package com.tudominio.<tu-nuevo-proyecto>...`
    - Cambia `import dev.growthen.api...` por `import com.tudominio.<tu-nuevo-proyecto>...`

---

## 📄 Licencia

Este proyecto está bajo la Licencia **MIT**. Consulta el archivo [LICENSE](file:///c:/Users/global/Desktop/U/Personal/practicar-Api-Rest/api-libreria/LICENSE) para obtener más detalles.

