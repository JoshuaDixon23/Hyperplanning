# ==========================
# Stage 1 : build avec Maven
# ==========================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copie le pom et les sources
COPY pom.xml .
COPY src ./src

# Build du projet et création d'un "uber-jar" avec les dépendances
RUN mvn clean package -DskipTests

RUN mvn clean package -DskipTests && ls -l target/

# ==========================
# Stage 2 : image finale
# ==========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copie du JAR avec dépendances
COPY --from=builder /app/target/hyperplanning.jar ./hyperplanning.jar

# Commande par défaut
CMD ["java", "-jar", "hyperplanning.jar"]