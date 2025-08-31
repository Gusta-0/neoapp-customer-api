FROM gradle:7.5-jdk17 AS build
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew build -x test --no-daemon

COPY . .

RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar neoapp-customer-api.jar

EXPOSE 8080

USER 1000:1000

ENTRYPOINT ["java", "-jar", "neoapp-customer-api.jar"]
