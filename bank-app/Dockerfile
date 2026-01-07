# Etapa 1: Build
FROM gradle:8.14-jdk17 AS build
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Copiar código fuente
COPY src ./src

# Construir la aplicación
RUN gradle clean build -x test

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR construido desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Variables de entorno (pueden ser sobrescritas)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/bankdb
ENV SPRING_DATASOURCE_USERNAME=bankuser
ENV SPRING_DATASOURCE_PASSWORD=bankpass

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
