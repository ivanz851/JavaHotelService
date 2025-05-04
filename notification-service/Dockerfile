FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY ./notification-service/pom.xml ./notification-service/mvnw ./notification-service/mvnw.cmd ./
RUN mvn dependency:go-offline

COPY ./notification-service/src ./src
COPY ./notification-service/.mvn ./.mvn

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder ./app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]

