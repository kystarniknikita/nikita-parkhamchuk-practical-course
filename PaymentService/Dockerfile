FROM eclipse-temurin:17-jdk as build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x ./gradlew

COPY src src

RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

RUN groupadd -r paymentuser && useradd -r -g paymentuser paymentuser

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown paymentuser:paymentuser app.jar

USER paymentuser

EXPOSE 8084

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8084/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]