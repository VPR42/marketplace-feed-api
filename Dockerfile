FROM gradle:8.10.2-jdk21-alpine AS builder
WORKDIR /home/gradle/src

COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradle.properties ./

RUN gradle --version
RUN gradle dependencies > /dev/null 2>&1 || true

COPY . .
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
ENV TZ=Etc/UTC \
    JAVA_OPTS=""

WORKDIR /app
EXPOSE 33303

COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
