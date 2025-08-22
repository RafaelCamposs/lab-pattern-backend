FROM gradle:8.10.0-jdk22-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew

RUN ./gradlew :application:bootJar --no-daemon
RUN ls -R

FROM amazoncorretto:22-alpine

WORKDIR /app
COPY --from=build /app/application/build/libs/*.jar application.jar
EXPOSE 8080
CMD ["java", "-jar", "application.jar"]

