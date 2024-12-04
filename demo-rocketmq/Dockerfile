FROM openjdk:8u171-jre-alpine
#LABEL authors="liulili"

COPY ./target/demo-rocketmq-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app.jar"]