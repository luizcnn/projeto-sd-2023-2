FROM openjdk:17-alpine as build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/
COPY src/ src/

RUN chmod +x ./gradlew && \
    ./gradlew clean build --parallel -x test

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/build/libs/projeto-sd-1.0.0.jar"]

FROM openjdk:17-alpine

WORKDIR /app

ENV JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -javaagent:/opt/opentelemetry/opentelemetry-javaagent.jar"
ENV OTEL_SERVICE_NAME=projeto-sd
#ENV OTEL_TRACES_EXPORTER=logging
ENV OTEL_METRICS_EXPORTER=prometheus
ENV OTEL_EXPORTER_PROMETHEUS_PORT=9090
#ENV OTEL_LOGS_EXPORTER=logging
ENV OTEL_METRIC_EXPORT_INTERVAL=15000


# Download the OpenTelemetry Java agent
RUN  mkdir -p /opt/opentelemetry \
    && wget -O /opt/opentelemetry/opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

COPY --from=build /app/build/libs/*.jar projeto-sd.jar

EXPOSE 8080

CMD ["java", "-javaagent:/opt/opentelemetry/opentelemetry-javaagent.jar", "-jar", "projeto-sd.jar"]