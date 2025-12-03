# --- Etapa 1: Construcción (Build Stage) ---
# Usamos una imagen oficial de Maven con una versión de Java compatible (Eclipse Temurin es una buena opción)
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar el cache de capas de Docker
# Si las dependencias no cambian, Docker no las descargará de nuevo
COPY pom.xml .

# Descargamos todas las dependencias del proyecto
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente de la aplicación
COPY src ./src

# Compilamos la aplicación y empaquetamos en un .jar
# -DskipTests para no ejecutar las pruebas durante la construcción de la imagen
RUN mvn package -DskipTests

# --- Etapa 2: Ejecución (Runtime Stage) ---
# Usamos una imagen de JRE mucho más ligera para la ejecución
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos únicamente el .jar generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que correrá la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
