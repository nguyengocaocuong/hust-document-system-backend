# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS development

EXPOSE 8080
ADD target/hust-document-system.jar hust-document-system.jar
ENTRYPOINT ["java", "-jar","/hust-document-system.jar"]

