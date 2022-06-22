FROM openjdk:17-alpine

EXPOSE 8080

COPY target/CloudStorage-0.0.1-SNAPSHOT.jar app.jar

WORKDIR /var/fileStorage

CMD ["java", "-jar", "/app.jar"]