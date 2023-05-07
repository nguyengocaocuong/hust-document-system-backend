FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve

COPY src ./src
COPY security ./security
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql"]