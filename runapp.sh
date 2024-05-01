#!/bin/bash

#mkdir -p optl && \
#wget -O ./optl/opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar && \
#-Dotel.service.name=projeto-sd -Dotel.metrics.exporter=prometheus -Dotel.exporter.prometheus.port=9090 -Dotel.metric.export.interval=15000
#./gradlew clean build && \
#java -javaagent:"$(pwd)"/optl/opentelemetry-javaagent.jar -jar ./build/libs/projeto-sd-1.0.0.jar

# Change to the directory of the script
cd "$(dirname "$0")" || exit

# Path to the OpenTelemetry Java agent
OPENTELEMETRY_AGENT_PATH="$(pwd)/opentelemetry-javaagent.jar"

# Check if the Java agent jar exists
if [ ! -f "$OPENTELEMETRY_AGENT_PATH" ]; then
    echo "OpenTelemetry Java agent not found!"
    wget -O ./opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
fi

# Build the Spring Boot app
./gradlew clean build

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# Run the Spring Boot app with OpenTelemetry Java agent
#-
java -javaagent:"$OPENTELEMETRY_AGENT_PATH" \
-Dotel.service.name=projeto-sd -Dotel.metrics.exporter=otlp -Dotel.metric.export.interval=15000 -Dotel.logs.exporter=logging -Dotel.traces.exporter=logging \
-jar ./build/libs/projeto-sd-1.0.0.jar
