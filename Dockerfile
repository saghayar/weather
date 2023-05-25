# First stage: build the project
FROM gradle:7.6.1-jdk17 as builder
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

# Second stage: run the application
FROM openjdk:17-oracle
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 9094
ENTRYPOINT ["java","--enable-preview", "-jar", "app.jar"]



