# ── STAGE 1: Build ──────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia prima il pom.xml così Docker mette in cache le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copia il codice sorgente e compila (i test sono già disabilitati nel pom)
COPY src ./src
RUN mvn package -q

# ── STAGE 2: Runtime ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
