FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# 1. Копируем родительский конфиг
COPY pom.xml .

# 2. Копируем POM-файлы ВСЕХ модулей (этот блок одинаковый во всех Dockerfile!)
COPY jwt_authentication/pom.xml ./jwt_authentication/
COPY users/pom.xml ./users/
COPY orders/pom.xml ./orders/
COPY cart/pom.xml ./cart/
COPY products/pom.xml ./products/
COPY shipments/pom.xml ./shipments/

# 3. Копируем исходники (Меняем products на имя текущего сервиса)
COPY jwt_authentication/src ./jwt_authentication/src
COPY orders/src ./orders/src

# 4. Собираем (Меняем products на имя текущего сервиса)
# -am соберет jwt_authentication автоматически
RUN mvn clean install -DskipTests -am -pl orders  # <--- МЕНЯТЬ ТУТ

# ===== RUNTIME STAGE =====
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Не забудь поменять путь к jar файлу
COPY --from=build /build/orders/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]