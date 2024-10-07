FROM openjdk:11-jdk-slim

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
COPY src src

RUN chmod +x gradlew
RUN ./gradlew clean
RUN ./gradlew installDist

EXPOSE 8080

CMD ["./build/install/ktor-sample/bin/ktor-sample"]
