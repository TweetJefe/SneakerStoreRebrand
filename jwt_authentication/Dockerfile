FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -pl jwt_authentication -am -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/jwt_authentication/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]