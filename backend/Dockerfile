####
# Multi-stage Dockerfile for Quarkus application
# Stage 1: Build the application with Maven
# Stage 2: Run the application with JVM
####

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM registry.access.redhat.com/ubi8/openjdk-21:1.18

ENV LANGUAGE='en_US:en'

# Copy the built application from the build stage
COPY --from=build --chown=185 /build/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /build/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /build/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /build/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
