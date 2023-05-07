FROM eclipse-temurin:17-jdk-jammy

EXPOSE 8080
ADD target/hust-document-system.jar ./app/hust-document-system.jar
ENTRYPOINT ["java", "-jar","./app/hust-document-system.jar"]