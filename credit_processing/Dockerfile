FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY pom.xml .
COPY credit_processing/pom.xml ./credit_processing/
RUN mkdir -p credit_processing && \
    mvn -f credit_processing/pom.xml dependency:go-offline
COPY credit_processing/ ./credit_processing/
RUN mvn -f credit_processing/pom.xml clean package -DskipTests
ARG JAR_FILE=credit_processing/target/credit_processing-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
