

# build a image
# docker build . -t mail-frame

# run the application on docker
# docker run -d -p 8080:8080 mail-frame


# build #
FROM gradle:6.4.1-jdk11 as builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build

# execution #
FROM adoptopenjdk:11-jdk-openj9
COPY --from=builder /app/build/libs/mail-frame-0.0.1-SNAPSHOT.jar mail-frame.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/mail-frame.jar"]