# Sistema de Administración de Eventos y Venues

## Descripción del Proyecto
Este proyecto implementa un sistema de administración de eventos y venues, siguiendo los principios de la Arquitectura Hexagonal (Ports & Adapters) y el Diseño Orientado al Dominio (DDD). Incorpora seguridad robusta con JWT, manejo estandarizado de errores, logging estructurado, monitoreo con Spring Boot Actuator y Micrometer, y está preparado para despliegue con Docker y Docker Compose.

## Objetivo de la Historia de Usuario
Asegurar la calidad técnica, estabilidad y operabilidad del sistema mediante pruebas unitarias y de integración, métricas, y la preparación para el despliegue en entornos reales a través de contenerización.

## Tecnologías Utilizadas
*   **Spring Boot 3.3.1**
*   **Java 17**
*   **Maven**
*   **MySQL 8.0**
*   **JPA / Hibernate**
*   **Spring Security**
*   **JWT (JSON Web Tokens)**
*   **Flyway (Gestión de Migraciones de Base de Datos)**
*   **Lombok**
*   **MapStruct**
*   **JUnit 5 & Mockito (Pruebas Unitarias)**
*   **Spring Boot Test (Pruebas de Integración)**
*   **Testcontainers (Pruebas de Integración con DB real)**
*   **Spring Boot Actuator & Micrometer (Monitoreo y Métricas)**
*   **Docker & Docker Compose (Contenerización y Orquestación)**
*   **Swagger UI / OpenAPI 3 (Documentación de API)**

## Prerrequisitos
Antes de ejecutar el proyecto, asegúrate de tener instalado lo siguiente:
*   **Java Development Kit (JDK) 17**
*   **Maven 3.x**
*   **Docker Desktop** (y asegúrate de que esté en ejecución)
*   Un IDE como IntelliJ IDEA o VS Code (opcional, pero recomendado para desarrollo)

## Configuración del Entorno

### 1. Variables de Entorno / `application.properties`
Asegúrate de que tu archivo `src/main/resources/application.properties` contenga la siguiente configuración de base de datos y JWT. Esta configuración se usa cuando ejecutas la aplicación directamente desde tu IDE, conectándose a la base de datos Docker expuesta en el puerto 3307.


## Ejecución de la Aplicación

### Opción 1: Usando Docker Compose (Recomendado para Entornos Aislados)

1.  Abre una terminal en la raíz del proyecto (donde se encuentran `Dockerfile` y `docker-compose.yml`).
2.  Asegúrate de que **Docker Desktop esté en ejecución**.
3.  Ejecuta el siguiente comando para construir la imagen de la aplicación y levantar ambos servicios (aplicación y base de datos):
4.  Observa los logs. La base de datos (`db`) se iniciará primero, y la aplicación (`app`) esperará a que la base de datos esté saludable antes de arrancar.
5.  Para detener los servicios, presiona `Ctrl + C` en la terminal y luego `docker-compose down`.

### Opción 2: Desde tu IDE (Desarrollo Local)

1.  Abre una terminal en la raíz del proyecto.
2.  Asegúrate de que **Docker Desktop esté en ejecución**.
3.  **Inicia SOLAMENTE el servicio de la base de datos** con Docker Compose:

Una vez que la aplicación esté en ejecución (ya sea con Docker Compose o desde el IDE):
Swagger UI (Documentación de API): http://localhost:8080/swagger-ui/index.html
Flujo de Autenticación en Swagger:
a.
Registra un usuario ADMIN (POST /auth/register).
b.
Inicia sesión (POST /auth/login) y copia el accessToken.
c.
Haz clic en el botón "Authorize" en la parte superior de Swagger, pega el token (ej. Bearer eyJ...) y haz clic en "Authorize".
d.
Ahora puedes probar los endpoints protegidos.
Spring Boot Actuator (Monitoreo):
Estado de Salud: http://localhost:8080/actuator/health
Información de la Aplicación: http://localhost:8080/actuator/info
Métricas Disponibles: http://localhost:8080/actuator/metrics
Métricas para Prometheus: http://localhost:8080/actuator/prometheus
Ejecución de Pruebas
Pruebas Unitarias y de Integración (desde el IDE)
1.
Asegúrate de que Docker Desktop esté en ejecución para las pruebas con Testcontainers.
2.
Abre tu IDE.
3.
Puedes ejecutar todas las pruebas desde la raíz del proyecto o individualmente:
VenueServiceTest.java (Unitarias con Mockito)
VenueControllerTest.java (Integración de Controlador con MockMvc)
VenueControllerIntegrationTest.java (Integración completa con Testcontainers y DB real)