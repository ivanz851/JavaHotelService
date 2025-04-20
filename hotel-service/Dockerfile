FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY ./hotel-service/pom.xml ./hotel-service/mvnw ./hotel-service/mvnw.cmd ./
RUN mvn dependency:go-offline

COPY ./hotel-service/src ./src
COPY ./hotel-service/.mvn ./.mvn

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder ./app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-hotel:5432/hotels_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=password

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
