FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/case.jar /app/case.jar

CMD ["java", "-jar", "case.jar"]
