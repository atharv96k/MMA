# ── Base image — Java 21 JRE (lighter than JDK)
# FROM eclipse-temurin:21-jre

# ── Set working directory inside container
# WORKDIR /app

# ── Copy the built jar (rename for clean versioning)
# COPY target/moneymanager-0.0.1-SNAPSHOT.jar moneymanager-v1.0.jar

# ── Document the port Spring Boot listens on
# EXPOSE 9090

# ── Start the application
#ENTRYPOINT ["java", "-jar", "moneymanager-v1.0.jar"]
 
#________________________________________________________________________________#
# Module 6 - Optimize Image

# ── STAGE 1: Extract layers from the fat jar
FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /builder
COPY target/moneymanager-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ── STAGE 2: Build the final lean image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy layers in order of how often they change (least → most)
COPY --from=builder /builder/dependencies/ ./
COPY --from=builder /builder/spring-boot-loader/ ./
COPY --from=builder /builder/snapshot-dependencies/ ./
COPY --from=builder /builder/application/ ./

EXPOSE 9090
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]