FROM eclipse-temurin:21
EXPOSE 33303
COPY ./build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]