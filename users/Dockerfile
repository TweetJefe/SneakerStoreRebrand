# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# 1. Копируем родительский POM
COPY pom.xml .

# 2. Копируем POM-файлы ВСЕХ модулей, чтобы Maven видел структуру (Reactor)
# Если какой-то из этих модулей отсутствует на диске, закомментируй строку
COPY jwt_authentication/pom.xml ./jwt_authentication/
COPY users/pom.xml ./users/
COPY orders/pom.xml ./orders/
COPY cart/pom.xml ./cart/
COPY products/pom.xml ./products/
COPY shipments/pom.xml ./shipments/

# 3. Копируем ИСХОДНИКИ только тех модулей, которые нужны для сборки этого сервиса
COPY jwt_authentication/src ./jwt_authentication/src
COPY users/src ./users/src

# 4. Собираем проект.
# -pl users : собираем только модуль users
# -am (also-make) : автоматически соберет зависимости (jwt_authentication)
RUN mvn clean install -DskipTests -am -pl users

# ===== RUNTIME STAGE =====
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Путь к jar теперь будет таким, так как мы собирали из корня /build
COPY --from=build /build/users/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]