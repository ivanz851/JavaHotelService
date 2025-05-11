FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY ./booking-service/pom.xml ./booking-service/mvnw ./booking-service/mvnw.cmd ./
COPY ./booking-service/.mvn ./.mvn

RUN mvn dependency:go-offline -B

COPY ./booking-service/src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder ./app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-booking:5432/book_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=password

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]

