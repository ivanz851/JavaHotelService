FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY ./proto /proto
COPY ./hotel-service/pom.xml ./hotel-service/mvnw ./hotel-service/mvnw.cmd ./
COPY ./hotel-service/.mvn ./.mvn

# RUN mvn dependency:go-offline -B

COPY ./hotel-service/src ./src

RUN mvn package -Dmaven.test.skip=true -B

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder ./app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-hotel:5432/hotels_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=password

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
