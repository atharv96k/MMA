# ── STAGE 1: Build the jar from source using Maven ──
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy only the pom first so dependency downloads are cached
# across builds when only source code changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── STAGE 2: Extract layers from the fat jar ──
FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /builder
COPY --from=build /build/target/moneymanager-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ── STAGE 3: Build the final lean runtime image ──
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy layers in order of how often they change (least → most)
COPY --from=builder /builder/dependencies/ ./
COPY --from=builder /builder/spring-boot-loader/ ./
COPY --from=builder /builder/snapshot-dependencies/ ./
COPY --from=builder /builder/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]