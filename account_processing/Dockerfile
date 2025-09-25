FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY pom.xml .
COPY account_processing/pom.xml ./account_processing/
RUN mkdir -p account_processing && \
    mvn -f account_processing/pom.xml dependency:go-offline
COPY account_processing/ ./account_processing/
RUN mvn -f account_processing/pom.xml clean package -DskipTests
ARG JAR_FILE=account_processing/target/account_processing-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
