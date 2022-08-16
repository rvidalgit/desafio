# Build
FROM gradle:jdk17-alpine as build
WORKDIR /app
COPY . .
RUN gradle build -x test

# Run
FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=build ./app/build/libs .
RUN rm *-plain.jar
RUN mv *.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]