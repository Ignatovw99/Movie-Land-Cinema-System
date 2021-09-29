# multi-stage builds
FROM maven:3.6.3 AS maven
LABEL MAINTAINER="lyuboslavw@gmail.com"

WORKDIR /app/src
COPY pom.xml /app/src/
RUN mvn dependency:go-offline
COPY . /app/src
# Compile and package the application to an executable JAR
RUN mvn clean package -Dmaven.test.skip

FROM openjdk:11.0.12-jre

ARG JAR_FILE=movie-land-cinema-system.jar
WORKDIR /opt/app
# Copy the movie-land-cinema-system.jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=maven /app/src/target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java", "-jar", "movie-land-cinema-system.jar"]