FROM eclipse-temurin:11-jre-alpine

ARG JAR_FILE

WORKDIR /app

COPY target/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]