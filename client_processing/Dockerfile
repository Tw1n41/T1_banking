FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY pom.xml .
COPY client_processing/pom.xml ./client_processing/
RUN mkdir -p client_processing && \
    mvn -f client_processing/pom.xml dependency:go-offline
COPY client_processing/ ./client_processing/
RUN mvn -f client_processing/pom.xml clean package -DskipTests
ARG JAR_FILE=client_processing/target/client_processing-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
